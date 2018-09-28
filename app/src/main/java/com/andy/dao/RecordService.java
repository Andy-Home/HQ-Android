package com.andy.dao;

import android.util.Log;

import com.andy.dao.db.entity.Record;
import com.andy.dao.db.entity.RecordContent;
import com.andy.dao.net.NetRequestManager;
import com.andy.dao.net.RecordRequest;
import com.andy.dao.net.Response;
import com.andy.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy on 2018/9/27.
 * Modify time 2018/9/27
 */
public class RecordService {
    private final static String TAG = CatalogService.class.getSimpleName();

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
    private SharedPreferencesUtils utils = null;

    private void init() {
        mRecordRequest = NetRequestManager.getInstance().getRecordRequest();
        utils = SharedPreferencesUtils.getInstance();
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

    public void getRecordList(long startTime, final BaseListener<List<RecordContent>> listener) {
        mRecordRequest.getRecordList(utils.getUserId(), startTime)
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


                            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getResult();
                            List<RecordContent> recordContents = new ArrayList<>();
                            if (data != null && data.size() > 0) {

                                Calendar calendar = null;
                                for (Map<String, Object> map : data) {
                                    RecordContent content = new RecordContent();
                                    content.id = (int) map.get("id");
                                    content.userId = (int) map.get("userId");
                                    content.num = (double) map.get("amount");
                                    content.catalog = (int) map.get("catalogId");
                                    content.type = (int) map.get("type");
                                    content.time = (long) map.get("recordTime");
                                    content.headUrl = (String) map.get("headUrl");
                                    content.catalogName = (String) map.get("catalogName");

                                    Calendar temp = Calendar.getInstance();
                                    temp.setTimeInMillis(content.time);
                                    if (calendar == null || calendar.get(Calendar.DAY_OF_MONTH) != temp.get(Calendar.DAY_OF_MONTH)) {
                                        calendar = temp;
                                        content.status = 1;
                                    }
                                    recordContents.add(content);
                                }
                            }

                            listener.onSuccess(recordContents);
                            onComplete();

                        } else {
                            onError(new Throwable(response.getMsg()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getRecordList", e);
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
