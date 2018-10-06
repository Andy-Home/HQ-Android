package com.andy.dao;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.andy.dao.db.DBManage;
import com.andy.dao.db.RecordDao;
import com.andy.dao.db.entity.Record;
import com.andy.dao.db.entity.RecordContent;
import com.andy.dao.net.NetRequestManager;
import com.andy.dao.net.RecordRequest;
import com.andy.dao.net.Response;
import com.andy.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy on 2018/9/27.
 * Modify time 2018/9/27
 */
public class RecordService {
    private final static String TAG = RecordService.class.getSimpleName();

    private RecordService() {
        init();
    }

    protected static RecordService getInstance() {
        return SingleHolder.mInstance;
    }

    private static class SingleHolder {
        final static RecordService mInstance = new RecordService();
    }

    private RecordRequest mRecordRequest;
    private RecordDao mRecordDao;
    private SharedPreferencesUtils utils = null;

    private void init() {
        mRecordRequest = NetRequestManager.getInstance().getRecordRequest();
        utils = SharedPreferencesUtils.getInstance();
        mRecordDao = DBManage.getInstance().getRecordDao();
    }

    public void saveRecord(final Record record, final BaseListener<Object> listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", record.userId);
            jsonObject.put("amount", record.num);
            jsonObject.put("catalogId", record.typeId);
            jsonObject.put("recordTime", record.time);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError("can not cast to json");
        }

        mRecordRequest.newRecord(jsonObject.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        mRecordDao.insert(record);
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
                            record.id = (int) response.getResult();
                            listener.onSuccess(null);
                            onComplete();
                        } else {
                            onError(new Throwable(response.getMsg()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "saveRecord", e);
                        listener.onError(e.getMessage());
                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }


    public void getRecordList(final long startTime, final long endTime, final BaseListener<List<RecordContent>> listener) {
        if (endTime > utils.getEndTime()) {
            Log.d(TAG, "getRecordList db");
            getRecord(startTime, endTime, listener);
        } else {
            Log.d(TAG, "getRecordList sync");
            syncOld(new BaseListener<Object>() {

                @Override
                public void onSuccess(Object o) {
                    getRecord(startTime, endTime, listener);
                }

                @Override
                public void onError(String msg) {
                    listener.onError(msg);
                }
            });
        }
    }

    private void getRecord(long startTime, long endTime, final BaseListener<List<RecordContent>> listener) {
        mRecordDao.queryRecordContents(startTime, endTime, utils.getHomeUsers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<List<RecordContent>>() {
                    Subscription s;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(List<RecordContent> recordContents) {
                        listener.onSuccess(recordContents);
                        onComplete();
                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onError(t.getMessage());
                        s.cancel();
                    }

                    @Override
                    public void onComplete() {
                        s.cancel();
                    }
                });
    }


    private static final long period = 3456000000l;

    public void sync(BaseListener<Object> listener) {
        long startTime = utils.getStartTime();
        long endTime = utils.getEndTime();

        if (startTime == 0 && endTime == 0) {
            endTime = System.currentTimeMillis();
            startTime = endTime - period;
        } else {
            startTime = endTime - (1000 * 60 * 60 * 48);
            endTime = System.currentTimeMillis();
        }

        Log.d(TAG, "syncRecord start:" + startTime + " end:" + endTime);
        sync(startTime, endTime, listener);
    }

    public void syncOld(BaseListener<Object> listener) {
        long startTime = utils.getStartTime();
        long endTime = utils.getEndTime();

        if (startTime == 0 && endTime == 0) {
            endTime = System.currentTimeMillis();
            startTime = endTime - period;
        } else {
            endTime = startTime + (1000 * 60 * 60 * 48);
            startTime = endTime - period;
        }

        Log.d(TAG, "syncRecord Old start:" + startTime + " end:" + endTime);
        sync(startTime, endTime, listener);
    }

    private void sync(final long startTime, final long endTime, final BaseListener<Object> listener) {
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        mRecordRequest.sync(utils.getUserId(), startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Function<Response, Map<String, List<Record>>>() {
                    @Override
                    public Map<String, List<Record>> apply(final Response response) {
                        if (response.getCode() == 0) {

                            if (response.getResult().toString().equals("")) {
                                return new HashMap<>();
                            }

                            List<Record> data = new ArrayList<>();
                            List<Record> delete = new ArrayList<>();

                            Log.d(TAG, "sync response:" + response.getResult());
                            List<Map<String, Object>> list = (List<Map<String, Object>>) response.getResult();

                            for (Map<String, Object> map : list) {
                                Record record = new Record();
                                int id = (int) map.get("id");
                                int userId = (int) map.get("userId");
                                double num = (double) map.get("amount");
                                int typeId = (int) map.get("catalogId");
                                int status = (int) map.get("status");
                                long time = (long) map.get("time");

                                record.id = id;
                                record.userId = userId;
                                record.typeId = typeId;
                                record.num = num;
                                record.time = time;

                                if (status == 0) {
                                    delete.add(record);
                                } else {
                                    data.add(record);
                                }
                            }

                            Map<String, List<Record>> map = new HashMap<>();
                            map.put("data", data);
                            map.put("delete", delete);
                            return map;
                        } else {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onError("sync record: " + response.getMsg());
                                }
                            });

                            return new HashMap<>();
                        }
                    }
                }).observeOn(Schedulers.io())
                .subscribe(new Observer<Map<String, List<Record>>>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onNext(Map<String, List<Record>> stringListMap) {
                        if (stringListMap == null) {
                            onError(new Throwable());
                        } else {
                            List<Record> data = stringListMap.get("data");

                            if (data != null && data.size() > 0) {
                                Record[] records = new Record[data.size()];
                                for (int i = 0; i < data.size(); i++) {
                                    records[i] = data.get(i);
                                }
                                mRecordDao.insert(records);
                            }

                            List<Record> delete = stringListMap.get("delete");
                            if (delete != null && delete.size() > 0) {
                                for (Record record : delete) {
                                    mRecordDao.delete(record);
                                }
                            }

                            if (endTime > utils.getEndTime()) {
                                utils.syncEndTime(endTime);
                            }

                            if (startTime < utils.getStartTime()) {
                                utils.syncStartTime(startTime);
                            }
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
