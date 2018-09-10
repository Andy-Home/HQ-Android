package com.andy.function.main.chart;

/**
 * Created by Andy on 2018/9/10.
 * Modify time 2018/9/10
 */
public class ChartContract {

    public interface View {
        void displayChart();
    }

    public interface Present {
        void getCatalog(long start, long end, int id);
    }
}
