package com.andy.function.main.order;

import com.andy.dao.db.entity.RecordContent;
import com.andy.function.BaseView;

import java.util.List;

public class OrderContract {

    public interface Present {

        void getRecords(long startTime, long endTime, int num);
    }

    public interface View extends BaseView {

        void displayRecords(List<RecordContent> data);
    }
}
