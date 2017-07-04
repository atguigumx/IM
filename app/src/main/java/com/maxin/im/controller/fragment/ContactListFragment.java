package com.maxin.im.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.maxin.im.R;
import com.maxin.im.common.Constant;
import com.maxin.im.common.Modle;
import com.maxin.im.controller.activity.AddContactActivity;
import com.maxin.im.controller.activity.InviteActivity;
import com.maxin.im.model.bean.UserInfo;
import com.maxin.im.utils.SPUtils;
import com.maxin.im.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<UserInfo> contacts;
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
        contacts = Modle.getInstance().getHelperManager().getContactDAO().getContacts();
        if (contacts != null){
            //添加数据
            Map<String, EaseUser> map = new HashMap<>();
            //数据类型转换
            for (UserInfo info: contacts) {
                map.put(info.getHxid(),new EaseUser(info.getUsername()));
            }
            setContactsMap(map);
            //获取数据
            getContactList();
            //刷新数据
            contactListLayout.refresh();
        }
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

        //显示联系人
        showContact();

        //删除联系人
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    return false;
                }
                //弹警告
                showDialog(position);
                return true;
            }
        });
    }

    private void showDialog(final int position) {
        new AlertDialog.Builder(getActivity())
                .setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Modle.getInstance().getGlobalThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                //网络
                                UserInfo userInfo = contacts.get(position - 1);
                                try {
                                    EMClient.getInstance().contactManager()
                                            .deleteContact(userInfo.getHxid());
                                    //本地
                                    Modle.getInstance().getHelperManager().getContactDAO()
                                            .deleteContactByHxId(userInfo.getHxid());
                                    //内存和页面
                                    if (getActivity() != null){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                refreshData();
                                            }
                                        });
                                    }
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                    UiUtils.showToast(e.getMessage());
                                }

                            }
                        });

                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    private void showContact() {
        //判断是否是第一次进入应用 第一次需要从服务器获取联系人 否则直接从数据库
        refreshServer();
    }

    private void refreshServer() {
        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String>contacts =EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //本地
                    //数据转换
                    List<UserInfo> userinfos = new ArrayList<UserInfo>();
                    for (String contacs:contacts) {
                        UserInfo userinfo = new UserInfo(contacs,contacs);
                        userinfos.add(userinfo);
                    }
                    //保存从服务器获取的联系人
                    Modle.getInstance().getHelperManager()
                            .getContactDAO().saveContacts(userinfos,true);

                    //内存和页面
                    if (getActivity() == null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
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
