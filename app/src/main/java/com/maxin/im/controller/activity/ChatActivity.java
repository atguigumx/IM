package com.maxin.im.controller.activity;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.maxin.im.R;
import com.maxin.im.base.BaseActivity;

public class ChatActivity extends BaseActivity {

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        EaseChatFragment chatFragment = new EaseChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.chat_fl,chatFragment).commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }
}
