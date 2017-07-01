package com.maxin.im.common;

import android.content.Context;

import com.maxin.im.model.bean.UserInfo;
import com.maxin.im.model.dao.AccountDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shkstart on 2017/7/1.
 */

public class Modle {


    private Context context;
    private AccountDAO accountDAO;
    private Modle(){}
    private static Modle modle=new Modle();

    public static Modle getInstance(){
        return modle;
    }
    public void init(Context context){
        this.context=context;
        accountDAO=new AccountDAO(context);
    }
    private ExecutorService service=Executors.newCachedThreadPool();
    public ExecutorService getGlobalThread(){
        return service;
    }
    //登录成功以后保存用户数据
    public void loginSuccess(UserInfo userInfo) {
        //添加用户
        accountDAO.addAccount(userInfo);
    }

    public AccountDAO getAccountDAO(){
        return accountDAO;
    }
}
