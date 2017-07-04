package com.maxin.im.controller.activity;

import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.maxin.im.R;
import com.maxin.im.base.BaseActivity;
import com.maxin.im.common.Modle;
import com.maxin.im.controller.adapter.InviteAdapter;
import com.maxin.im.model.bean.InvitationInfo;
import com.maxin.im.utils.SPUtils;
import com.maxin.im.utils.UiUtils;

import java.util.List;

import butterknife.Bind;

public class InviteActivity extends BaseActivity {


    @Bind(R.id.lv_invite)
    ListView lvInvite;
    private InviteAdapter adapter;


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        //设置小红点的状态
        SPUtils.getSPUtils().save(SPUtils.NEW_INVITE,false);
        adapter = new InviteAdapter(this,onInviteListener);
        lvInvite.setAdapter(adapter);
        refreshData(); //给适配器设置数据
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite;
    }
    public void refreshData(){
        //获取数据 从数据库
        List<InvitationInfo> invitations = Modle.getInstance().getHelperManager().getInvitationDAO().getInvitations();
        if (invitations != null){
            adapter.refresh(invitations);
        }
    }

    private InviteAdapter.OnInviteListener onInviteListener=new InviteAdapter.OnInviteListener() {
        @Override
        public void invitedSuccess(final InvitationInfo info) {
            Modle.getInstance().getGlobalThread().execute(new Runnable() {
                @Override
                public void run() {
                    String hxid = info.getUserInfo().getHxid();
                    try {
                        //网络
                        EMClient.getInstance().contactManager().acceptInvitation(hxid);
                        //本地
                        Modle.getInstance().getHelperManager()
                                .getInvitationDAO().updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT,hxid);
                        //内存和网页
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshData();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void invitedReject(final InvitationInfo info) {
            Modle.getInstance().getGlobalThread().execute(new Runnable() {
                @Override
                public void run() {
                    String hxid = info.getUserInfo().getHxid();

                    try {
                        //网络
                        EMClient.getInstance().contactManager().declineInvitation(hxid);
                        //本地
                        Modle.getInstance().getHelperManager().getInvitationDAO().removeInvitation(hxid);
                        //内存和网页
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UiUtils.showToast("已拒绝");
                                refreshData();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        UiUtils.showToast(e.getMessage());
                    }
                }
            });
        }
    };
}
