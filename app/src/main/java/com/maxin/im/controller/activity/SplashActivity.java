package com.maxin.im.controller.activity;

import android.content.Intent;
import android.os.CountDownTimer;

import com.hyphenate.chat.EMClient;
import com.maxin.im.R;
import com.maxin.im.base.BaseActivity;
import com.maxin.im.common.Modle;
import com.maxin.im.model.bean.UserInfo;

public class SplashActivity extends BaseActivity {

    private CountDownTimer countDownTimer;
    @Override
    public void initListener() {
        countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //倒计时结束
                selectChageActivity();
            }
        }.start();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }
    //选择进入哪个界面
    private void selectChageActivity() {

        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                //是否登录过环信服务器
                boolean loggedInBefore = EMClient.getInstance().isLoggedInBefore();
                if (loggedInBefore){
                    //登录过
                    //初始化登录成功后的操作
                    String currentUser = EMClient.getInstance().getCurrentUser();
                    Modle.getInstance().loginSuccess(new UserInfo(currentUser,currentUser));
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }else{
                    //没有登录过
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            }
        });
    }

}
