package com.andy.function.main.person;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.BaseFragment;
import com.andy.R;
import com.andy.function.catalog.CatalogActivity;
import com.andy.function.login.LoginActivity;
import com.andy.utils.ToastUtils;

/**
 * Created by Andy on 2018/8/29.
 * Modify time 2018/8/29
 */
public class PersonFragment extends BaseFragment implements PersonContract.View {
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_person;
    }

    private RelativeLayout vItem1,vItem2,vItem3;

    @Override
    protected void initView(View view) {
        TextView vTitle = view.findViewById(R.id.title);
        vTitle.setText(R.string.main_person);

        vItem1 = view.findViewById(R.id.item1);
        vItem2 = view.findViewById(R.id.item2);
        vItem3 = view.findViewById(R.id.item3);
    }

    private PersonPresent mPresent;

    @Override
    public void onResume() {
        super.onResume();
        mPresent = new PersonPresent(this, getContext());
    }

    @Override
    protected void setListener() {

        vItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogActivity.start(getActivity(), 0, "");
            }
        });

        vItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //退出登录
        vItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresent.logout();
            }
        });
    }

    @Override
    public void logoutSuccess() {
        LoginActivity.start(getActivity());
    }

    @Override
    public void onError(String msg) {
        ToastUtils.shortShow(getContext(), msg);
    }
}
