package com.maxin.im.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.maxin.im.model.bean.UserInfo;
import com.maxin.im.model.db.AccountDB;
import com.maxin.im.model.table.AccountTable;

/**
 * Created by shkstart on 2017/7/1.
 */

public class AccountDAO {
    private AccountDB accountDB;
    public AccountDAO(Context context){
        accountDB=new AccountDB(context);
    }
    //添加用户
    public void addAccount(UserInfo userInfo){
        if(userInfo!=null) {
            SQLiteDatabase database = accountDB.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(AccountTable.COL_HXID,userInfo.getHxid());
            contentValues.put(AccountTable.COL_NICK,userInfo.getNick());
            contentValues.put(AccountTable.COL_PHOTO,userInfo.getPhoto());
            contentValues.put(AccountTable.COL_USERNAME,userInfo.getUsername());
            database.replace(AccountTable.TABLE_NAME,null,contentValues);
        }
    }
    //根据hxid,搜索用户
    public UserInfo getUserInfo(String hxid){
        UserInfo userInfo = new UserInfo();
        if(!TextUtils.isEmpty(hxid)) {
            SQLiteDatabase database = accountDB.getWritableDatabase();
            String sql = "select * from "+AccountTable.TABLE_NAME
                    +" where "+AccountTable.COL_HXID+"=?";
            Cursor cursor = database.rawQuery(sql, new String[]{hxid});

            if(cursor.moveToNext()) {
                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(AccountTable.COL_HXID)));
                userInfo.setNick(cursor.getString(cursor.getColumnIndex(AccountTable.COL_NICK)));
                userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(AccountTable.COL_PHOTO)));
                userInfo.setUsername(cursor.getString(cursor.getColumnIndex(AccountTable.COL_USERNAME)));
            }
            cursor.close();//关闭游标
        }
        return userInfo;
    }
}
