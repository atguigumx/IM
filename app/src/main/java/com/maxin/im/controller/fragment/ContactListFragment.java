package com.maxin.im.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.maxin.im.R;
import com.maxin.im.common.Constant;
import com.maxin.im.controller.activity.AddContactActivity;
import com.maxin.im.controller.activity.InviteActivity;
import com.maxin.im.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shkstart on 2017/7/3.
 */

public class ContactListFragment extends EaseContactListFragment {
    @Bind(R.id.contanct_iv_invite)
    ImageView contanctIvInvite;
    @Bind(R.id.ll_new_friends)
    LinearLayout llNewFriends;
    @Bind(R.id.ll_groups)
    LinearLayout llGroups;
    private BroadcastReceiver invitereceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isShowRed();
        }
    };
    private BroadcastReceiver contactreceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshData();
        }
    };

    private void refreshData() {

    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void setUpView() {
        super.setUpView();

        //初始化listView头布局
        initHeadView();

        isShowRed();

        titleBar.setRightImageResource(R.drawable.ease_blue_add);
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddContactActivity.class));
            }
        });

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        //邀请信息发生改变
        manager.registerReceiver(invitereceiver,new IntentFilter(Constant.NEW_INVITE_CHANGE));
        //联系人发生改变
        manager.registerReceiver(contactreceiver,new IntentFilter(Constant.CONTACT_CHANGE));
    }

    private void initHeadView() {
        View view = View.inflate(getActivity(), R.layout.head_view, null);
        ButterKnife.bind(this, view);
        listView.addHeaderView(view);


        llGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        llNewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),InviteActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    private void isShowRed(){
        Boolean bolValue = SPUtils.getSPUtils().getBolValue(SPUtils.NEW_INVITE);
        //是否显示小红点
        contanctIvInvite.setVisibility(bolValue?View.VISIBLE:View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        isShowRed();
    }
}
