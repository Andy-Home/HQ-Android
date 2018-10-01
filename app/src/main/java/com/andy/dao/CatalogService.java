package com.andy.dao;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.andy.dao.db.CatalogDao;
import com.andy.dao.db.DBManage;
import com.andy.dao.db.entity.Catalog;
import com.andy.dao.db.entity.RecordStatistics;
import com.andy.dao.net.CatalogRequest;
import com.andy.dao.net.NetRequestManager;
import com.andy.dao.net.Response;
import com.andy.utils.SharedPreferencesUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
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
    private SharedPreferencesUtils utils = null;

    private void init() {
        mCatalogDao = DBManage.getInstance().getCatalogDao();
        mCatalogRequest = NetRequestManager.getInstance().getCatalogRequest();
        utils = SharedPreferencesUtils.getInstance();
    }

    public void getCatalogList(int parentId, final BaseListener<ArrayList<Catalog>> listener) {
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

                                    utils.putCatalogModifyTime(System.currentTimeMillis());
                                    listener.onSuccess(catalogs);
                                    onComplete();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getCause()));
                                }
                            } else {
                                onError(new Throwable(response.getMsg()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "getCatalogList", e);
                            listener.onError(e.getMessage());
                            d.dispose();
                        }

                        @Override
                        public void onComplete() {
                            d.dispose();
                        }
                    });
        }

    }

    public void newCatalog(final Catalog catalog, final BaseListener listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("parentId", catalog.parentId);
            jsonObject.put("userId", catalog.userId);
            jsonObject.put("name", catalog.name);
            jsonObject.put("type", catalog.style);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError("can not cast to json");
        }
        Log.d(TAG, "net newCatalog");
        mCatalogRequest.newCatalog(jsonObject.toString())
                .subscribeOn(Schedulers.io())
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        Log.d(TAG, "db newCatalog");
                        mCatalogDao.insert(catalog);
                    }
                })
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
                            int id = (int) response.getResult();
                            catalog.id = id;
                            listener.onSuccess(id);
                            onComplete();
                        } else {
                            onError(new Throwable(response.getMsg()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "newCatalog", e);
                        listener.onError(e.getMessage());
                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }

    public void deleteCatalog(final Catalog catalog, final BaseListener<Object> listener) {
        Log.d(TAG, "net deleteCatalog");
        mCatalogRequest.deleteCatalog(catalog.id, utils.getUserId())
                .subscribeOn(Schedulers.io())
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        Log.d(TAG, "db deleteCatalog");
                        mCatalogDao.deleteCatalog(catalog);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.getCode() == 0) {
                            listener.onSuccess(null);
                        } else {
                            onError(new Throwable(response.getMsg()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "deleteCatalog", e);
                        listener.onError(e.getMessage());
                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }

    public void getCatalog(int id, final BaseListener<Catalog> listener) {
        Log.d(TAG, "db getCatalog");
        mCatalogDao.queryCatalog(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Catalog>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onSuccess(Catalog catalog) {
                        listener.onSuccess(catalog);
                        onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getCatalog", e);
                        listener.onError(e.getMessage());
                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }

    public void getCatalog(final long start, final long end, int id, final BaseListener<List<RecordStatistics>> listener) {

        final List<RecordStatistics> data = new ArrayList<>();
        final int[] size = {0};
        mCatalogDao.queryCatalogList(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(new Function<List<Catalog>, Publisher<Catalog>>() {
                    @Override
                    public Publisher<Catalog> apply(List<Catalog> catalogs) {
                        size[0] = catalogs.size();
                        return Flowable.fromIterable(catalogs);
                    }
                }).map(new Function<Catalog, Map<String, Object>>() {

            @Override
            public Map<String, Object> apply(final Catalog catalog) {
                return mCatalogDao.queryCatalogAll(catalog.id)
                        .observeOn(Schedulers.io())
                        .map(new Function<List<Catalog>, Map<String, Object>>() {

                            @Override
                            public Map<String, Object> apply(List<Catalog> catalogs) {
                                Integer[] data = new Integer[catalogs.size() + 1];
                                data[0] = catalog.id;
                                for (int i = 0; i < catalogs.size(); i++) {
                                    data[i + 1] = catalogs.get(i).id;
                                }
                                Map<String, Object> map = new HashMap<>();
                                map.put("data", data);
                                map.put("catalog", catalog);
                                return map;
                            }
                        }).blockingFirst();
            }
        }).map(new Function<Map<String, Object>, RecordStatistics>() {

            @Override
            public RecordStatistics apply(Map<String, Object> stringObjectMap) {
                Integer[] data = (Integer[]) stringObjectMap.get("data");
                final Catalog catalog = (Catalog) stringObjectMap.get("catalog");
                return DBManage.getInstance().getRecordDao().queryRecordTotal(start, end, data)
                        .observeOn(Schedulers.io())
                        .map(new Function<Double, RecordStatistics>() {

                            @Override
                            public RecordStatistics apply(Double aDouble) {
                                RecordStatistics recordStatistics = new RecordStatistics();
                                recordStatistics.catalog = catalog.id;
                                recordStatistics.catalogName = catalog.name;
                                recordStatistics.type = catalog.style;
                                recordStatistics.num = aDouble;
                                return recordStatistics;
                            }
                        }).blockingGet();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<RecordStatistics>() {
                    Subscription s;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(RecordStatistics recordStatistics) {
                        data.add(recordStatistics);
                        if (data.size() == size[0]) {
                            listener.onSuccess(data);
                            onComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "getCatalog", t);
                        listener.onError(t.getMessage());
                        s.cancel();
                    }

                    @Override
                    public void onComplete() {
                        s.cancel();
                    }
                });
    }

    public void sync(final BaseListener<Object> listener) {
        long updateTime = utils.getCatalogModifyTime();
        final long currentTime = System.currentTimeMillis();
        final Handler mainHandler = new Handler(Looper.getMainLooper());

        mCatalogRequest.sync(utils.getUserId(), updateTime)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Function<Response, Map<String, List<Catalog>>>() {

                    @Override
                    public Map<String, List<Catalog>> apply(final Response response) {
                        if (response.getCode() == 0) {

                            if (response.getResult().toString().equals("")) {
                                return new HashMap<>();
                            }

                            List<Catalog> data = new ArrayList<>();
                            List<Catalog> delete = new ArrayList<>();

                            Log.d(TAG, "sync response:" + response.getResult());
                            List<Map<String, Object>> list = (List<Map<String, Object>>) response.getResult();

                            for (Map<String, Object> map : list) {
                                Catalog catalog = new Catalog();
                                int id = (int) map.get("id");
                                int parentId = (int) map.get("parentId");
                                int userId = (int) map.get("userId");
                                String name = (String) map.get("name");
                                int type = (int) map.get("type");
                                int status = (int) map.get("status");

                                catalog.id = id;
                                catalog.parentId = parentId;
                                catalog.userId = userId;
                                catalog.name = name;
                                catalog.style = type;

                                if (status == 0) {
                                    delete.add(catalog);
                                } else {
                                    data.add(catalog);
                                }
                            }

                            Map<String, List<Catalog>> map = new HashMap<>();
                            map.put("data", data);
                            map.put("delete", delete);
                            return map;
                        } else {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onError("sync catalog: " + response.getMsg());
                                }
                            });
                            return new HashMap<>();
                        }
                    }
                }).observeOn(Schedulers.io())
                .subscribe(new Observer<Map<String, List<Catalog>>>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onNext(Map<String, List<Catalog>> map) {
                        if (map == null) {
                            onError(new Throwable());
                        } else {
                            List<Catalog> data = map.get("data");

                            if (data != null && data.size() > 0) {
                                Catalog[] catalogs = new Catalog[data.size()];
                                for (int i = 0; i < data.size(); i++) {
                                    catalogs[i] = data.get(i);
                                }
                                mCatalogDao.insert(catalogs);
                            }

                            List<Catalog> delete = map.get("delete");
                            if (delete != null && delete.size() > 0) {
                                for (Catalog catalog : delete) {
                                    mCatalogDao.deleteCatalog(catalog);
                                }
                            }
                            utils.syncUpdateTime(currentTime);
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSuccess(null);
                                }
                            });

                            onComplete();
                        }
                    }

                    @Override
                    public void onError(final Throwable e) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onError(e.getMessage());
                            }
                        });

                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }
}
