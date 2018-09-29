package com.andy.function.main.person;

import com.andy.function.BaseView;

/**
 * Created by Andy on 2018/9/29.
 * Modify time 2018/9/29
 */
public class PersonContract {

    public interface View extends BaseView {
        void logoutSuccess();
    }

    public interface Present {
        void logout();
    }
}
