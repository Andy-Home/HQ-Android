package com.andy.dao;

import android.util.Log;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.entity.Catalog;
import com.andy.dao.net.CatalogRequest;
import com.andy.dao.net.NetRequestManager;
import com.andy.dao.net.Response;
import com.andy.utils.SharedPreferencesUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CatalogService {
    private final static String TAG = CatalogService.class.getSimpleName();
    private CatalogService() {
        init();
    }

    protected static CatalogService getInstance() {
        return SingleHolder.mInstance;
    }

    private static class SingleHolder {
        final static CatalogService mInstance = new CatalogService();
    }

    private CatalogDao mCatalogDao;
    private CatalogRequest mCatalogRequest;

    private void init() {
        mCatalogDao = DBManage.getInstance().getCatalogDao();
        mCatalogRequest = NetRequestManager.getInstance().getCatalogRequest();
    }

    public void getCatalogList(int parentId, final GetListListener listener) {
        final SharedPreferencesUtils utils = SharedPreferencesUtils.getInstance();
        long time = utils.getCatalogModifyTime();
        if (time + 86400000 > System.currentTimeMillis()) {
            Log.d(TAG, "db getCatalogList");
            mCatalogDao.queryCatalogList(parentId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Catalog>>() {
                        Subscription s;
                        ArrayList<Catalog> data = new ArrayList<>();
                        @Override
                        public void onSubscribe(Subscription s) {
                            s.request(Long.MAX_VALUE);
                            this.s = s;
                        }

                        @Override
                        public void onNext(List<Catalog> catalogs) {
                            data.addAll(catalogs);
                            onComplete();
                        }

                        @Override
                        public void onError(Throwable t) {
                            listener.onError("db error");
                            s.cancel();
                        }

                        @Override
                        public void onComplete() {
                            listener.onSuccess(data);
                            s.cancel();
                        }
                    });
        } else {
            Log.d(TAG, "net getCatalogList");
            mCatalogRequest.getCatalogs(parentId, utils.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response>() {
                        Disposable d;
                        ArrayList<Catalog> catalogs = new ArrayList<>();

                        @Override
                        public void onSubscribe(Disposable d) {
                            this.d = d;
                        }

                        @Override
                        public void onNext(Response response) {
                            if (response.getCode() == 0) {
                                Log.d(TAG, "response:" + response.getResult().toString());
                                ObjectMapper mapper = new ObjectMapper();
                                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Map.class);
                                try {
                                    List<Map<String, Object>> data = mapper.readValue(response.getResult().toString(), javaType);

                                    if (data != null && data.size() > 0) {

                                        for (Map<String, Object> map : data) {
                                            Catalog catalog = new Catalog();
                                            catalog.id = (int) map.get("id");
                                            catalog.name = (String) map.get("name");
                                            catalog.parentId = (int) map.get("parentId");
                                            catalog.userId = (int) map.get("userId");
                                            catalog.style = (int) map.get("type");

                                            catalogs.add(catalog);
                                            mCatalogDao.insert(catalog);
                                        }
                                    }
                                    onComplete();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    listener.onError("parse error");
                                }
                            } else {
                                String msg = response.getMsg();
                                listener.onError(msg);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "getCatalogList", e);
                            listener.onError("request error");
                            d.dispose();
                        }

                        @Override
                        public void onComplete() {
                            utils.putCatalogModifyTime(System.currentTimeMillis());
                            listener.onSuccess(catalogs);
                            d.dispose();
                        }
                    });
        }

    }

    public interface GetListListener {
        void onSuccess(ArrayList<Catalog> data);

        void onError(String msg);
    }
}
