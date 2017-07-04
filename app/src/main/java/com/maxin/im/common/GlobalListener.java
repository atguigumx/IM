package com.maxin.im.common;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.maxin.im.model.bean.InvitationInfo;
import com.maxin.im.model.bean.UserInfo;
import com.maxin.im.utils.SPUtils;

/**
 * Created by shkstart on 2017/7/3.
 */

public class GlobalListener {
    private final LocalBroadcastManager manager;

    public GlobalListener(Context context){
        EMClient.getInstance().contactManager().setContactListener(emContactListener);

        manager = LocalBroadcastManager.getInstance(context);
    }

    /*
       设置全局监听
    * */
    EMContactListener emContactListener = new EMContactListener() {

        //收到好友邀请  别人加你
        @Override
        public void onContactInvited(String username, String reason) {

            InvitationInfo invitationinfo=new InvitationInfo();
            invitationinfo.setReason(reason);
            invitationinfo.setUserInfo(new UserInfo(username,username));
            invitationinfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);
            //添加邀请信息
            Modle.getInstance().getHelperManager()
                    .getInvitationDAO()
                    .addInvitation(invitationinfo);

            //保存小红点状态
            SPUtils.getSPUtils().save(SPUtils.NEW_INVITE,true);
            //发送广播
            manager.sendBroadcast(new Intent(Constant.NEW_INVITE_CHANGE));
        }

        //好友请求被同意  你加别人的时候 别人同意了
        @Override
        public void onContactAgreed(String username) {
            InvitationInfo invitationinfo=new InvitationInfo();
            invitationinfo.setUserInfo(new UserInfo(username,username));
            invitationinfo.setReason("邀请被接受");
            invitationinfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);
            //添加邀请信息
            Modle.getInstance().getHelperManager().getInvitationDAO().addInvitation(invitationinfo);

            //保存小红点状态
            SPUtils.getSPUtils().save(SPUtils.NEW_INVITE,true);
            //发送广播
            manager.sendBroadcast(new Intent(Constant.NEW_INVITE_CHANGE));
        }



        //被删除时回调此方法
        @Override
        public void onContactDeleted(String username) {
            //删除邀请信息
            Modle.getInstance().getHelperManager().getInvitationDAO().removeInvitation(username);
            //删除联系人
            Modle.getInstance().getHelperManager().getContactDAO().deleteContactByHxId(username);
            //发送广播
            manager.sendBroadcast(new Intent(Constant.CONTACT_CHANGE));
        }


        //增加了联系人时回调此方法  当你同意添加好友
        @Override
        public void onContactAdded(String username) {
            Modle.getInstance().getHelperManager().getContactDAO()
                    .saveContact(new UserInfo(username,username),true);
            //发送广播
            manager.sendBroadcast(new Intent(Constant.CONTACT_CHANGE));
        }

        //好友请求被拒绝  你加别人 别人拒绝了
        @Override
        public void onContactRefused(String username) {
            //保存小红点
            SPUtils.getSPUtils().save(SPUtils.NEW_INVITE,true);

            //发送广播
            manager.sendBroadcast(new Intent(Constant.NEW_INVITE_CHANGE));
        }
    };
}
