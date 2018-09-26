package com.andy.dao;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.entity.Catalog;
import com.andy.dao.net.CatalogRequest;
import com.andy.dao.net.NetRequestManager;
import com.andy.dao.net.Response;
import com.andy.utils.SharedPreferencesUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CatalogService {
    private CatalogService() {
        init();
    }

    public static CatalogService getInstance() {
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

    public List<Catalog> getCatalogList(int parentId, final GetListListener listener) {
        SharedPreferencesUtils utils = SharedPreferencesUtils.getInstance();
        long time = utils.getCatalogModifyTime();
        if (time + 86400000 > System.currentTimeMillis()) {
            mCatalogDao.queryCatalogList(parentId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Catalog>>() {
                        Subscription s;

                        @Override
                        public void onSubscribe(Subscription s) {
                            this.s = s;
                        }

                        @Override
                        public void onNext(List<Catalog> catalogs) {
                            listener.onSuccess(new ArrayList<>(catalogs));
                        }

                        @Override
                        public void onError(Throwable t) {
                            listener.onError("db error");
                            s.cancel();
                        }

                        @Override
                        public void onComplete() {
                            s.cancel();
                        }
                    });
        } else {
            mCatalogRequest.getCatalogs(parentId, utils.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response>() {
                        Disposable d;

                        @Override
                        public void onSubscribe(Disposable d) {
                            this.d = d;
                        }

                        @Override
                        public void onNext(Response response) {
                            if (response.getCode() == 0) {
                                ObjectMapper mapper = new ObjectMapper();

                            } else {
                                listener.onError(response.getMsg());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onError("request error");
                            d.dispose();
                        }

                        @Override
                        public void onComplete() {
                            d.dispose();
                        }
                    });
        }

        return null;
    }

    public interface GetListListener {
        void onSuccess(ArrayList<Catalog> data);

        void onError(String msg);
    }
}
