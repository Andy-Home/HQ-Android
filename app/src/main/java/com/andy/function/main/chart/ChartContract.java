package com.andy.function.main.chart;

import com.andy.dao.db.entity.RecordStatistics;

import java.util.List;

/**
 * Created by Andy on 2018/9/10.
 * Modify time 2018/9/10
 */
public class ChartContract {

    public interface View {
        void displayChart(List<RecordStatistics> data);
    }

    public interface Present {
        void getCatalog(long start, long end, int id);
    }
}
