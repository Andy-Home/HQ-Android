package com.andy.function.new_record;


import android.view.View;

import com.andy.function.new_record.entity.CatalogItem;

import java.util.List;

/**
 * Created by Andy on 2018/9/7.
 * Modify time 2018/9/7
 */
public class NewRecordContract {

    public interface Present {

        void getCatalog(View view, int id);
    }

    public interface Views {

        void displayCatalogWindow(View view, List<CatalogItem> data);
    }
}
