package com.andy.function.catalog_edit;

import com.andy.dao.db.entity.Catalog;
import com.andy.function.BaseView;

/**
 * Created by Andy on 2018/8/31.
 * Modify time 2018/8/31
 */
public class CatalogEditContract {

    public interface Present{
        void getCatalog(int id);

        void save(Catalog catalog);

        void delete(Catalog catalog);
    }

    public interface View extends BaseView {
        void saveSuccess();

        void deleteSuccess();

        void displayCatalog(Catalog catalog);
    }
}
