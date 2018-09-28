package com.andy.function.new_record;


import android.view.View;

import com.andy.dao.db.entity.Record;
import com.andy.function.BaseView;
import com.andy.function.new_record.entity.CatalogItem;

import java.util.List;

/**
 * Created by Andy on 2018/9/7.
 * Modify time 2018/9/7
 */
public class NewRecordContract {

    public interface Present {

        void getCatalog(View view, int id);

        void saveRecord(Record record);
    }

    public interface Views extends BaseView {

        void showCatalogWindow(View view, List<CatalogItem> data);

        void saveRecordSuccess();
    }
}
