package com.andy.function.catalog;

import com.andy.dao.db.entity.Catalog;

import java.util.ArrayList;

/**
 * Created by Andy on 2018/8/31.
 * Modify time 2018/8/31
 */
public class CatalogContract {

    interface Present {
        void getCatalogs(int parentId, int userId);
    }

    public interface View{
        void displayCatalogs(ArrayList<Catalog> list);

        void onError(String msg);
    }
}
