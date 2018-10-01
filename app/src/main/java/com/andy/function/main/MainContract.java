package com.andy.function.main;

import com.andy.function.BaseView;

public class MainContract {
    public interface View extends BaseView {
        void syncCatalogSuccess();

        void syncRecordSuccess();
    }

    public interface Present {
        void syncCatalog();

        void syncRecord();
    }
}
