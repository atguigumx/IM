package com.maxin.im.controller.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.maxin.im.R;
import com.maxin.im.base.BaseActivity;
import com.maxin.im.controller.fragment.ContactListFragment;
import com.maxin.im.controller.fragment.ConversationFragment;
import com.maxin.im.controller.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity {


    @Bind(R.id.main_fl)
    FrameLayout mainFl;
    @Bind(R.id.rb_main_conversation)
    RadioButton rbMainConversation;
    @Bind(R.id.rb_main_contact)
    RadioButton rbMainContact;
    @Bind(R.id.rb_main_setting)
    RadioButton rbMainSetting;
    @Bind(R.id.rg_main)
    RadioGroup rgMain;
    private int position;
    private List<Fragment> fragments;
    private Fragment tempFragment;

    @Override
    public void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_main_conversation :
                        position=0;
                        break;
                    case R.id.rb_main_contact :
                        position=1;
                        break;
                    case R.id.rb_main_setting:
                        position=2;
                        break;
                }
                Fragment fragment = fragments.get(position);
                switchFragment(fragment);
            }
        });
    }

    private void switchFragment(Fragment currentFragment) {
        if (currentFragment != tempFragment) {

            if (currentFragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                //如果没有添加就添加
                if (!currentFragment.isAdded()) {
                    //隐藏之前的
                    if (tempFragment != null) {
                        ft.hide(tempFragment);
                    }

                    //添加Fragment
                    ft.add(R.id.main_fl, currentFragment);

                }
                //如果添加了就隐藏
                else {
                    //隐藏上次显示的
                    if (tempFragment != null) {
                        ft.hide(tempFragment);
                    }

                    //显示
                    ft.show(currentFragment);
                }

                //最后统一提交
                ft.commit();
                //重新赋值
                tempFragment = currentFragment;
            }

        }
    }

    @Override
    public void initData() {
        fragments=new ArrayList<>();
        fragments.add(new ConversationFragment());
        fragments.add(new ContactListFragment());
        fragments.add(new SettingsFragment());
        switchFragment(fragments.get(position));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

}
