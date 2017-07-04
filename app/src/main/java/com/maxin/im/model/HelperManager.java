package com.maxin.im.model;

import android.content.Context;

import com.maxin.im.model.dao.ContactDAO;
import com.maxin.im.model.dao.InvitationDAO;
import com.maxin.im.model.db.DBHelper;

/**
 * Created by shkstart on 2017/7/3.
 */

public class HelperManager {

    private final DBHelper dbHelper;
    private final ContactDAO contactDAO;
    private final InvitationDAO invitationDAO;


    public HelperManager(Context context, String name){
        dbHelper = new DBHelper(context, name);
        contactDAO = new ContactDAO(dbHelper);
        invitationDAO = new InvitationDAO(dbHelper);
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public ContactDAO getContactDAO() {
        return contactDAO;
    }

    public InvitationDAO getInvitationDAO() {
        return invitationDAO;
    }
    public void close(){
        if (dbHelper != null){
            dbHelper.close();
        }
    }
}
