package com.maxin.im.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.maxin.im.R;
import com.maxin.im.common.Modle;
import com.maxin.im.controller.activity.LoginActivity;
import com.maxin.im.utils.UiUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shkstart on 2017/7/3.
 */

public class SettingsFragment extends Fragment {
    @Bind(R.id.setting_btn_exit)
    Button settingBtnExit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_setttings, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String currentUser = EMClient.getInstance().getCurrentUser();
        // 更新button显示
        settingBtnExit.setText("退出登录(" + currentUser.toLowerCase() + ")");

        settingBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                //清除个人数据
                                UiUtils.showToast("退出成功");
                                //跳转到登录界面
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                //把当前界面finsh
                                getActivity().finish();
                            }

                            @Override
                            public void onError(int i, String s) {
                                UiUtils.showToast(s);
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
