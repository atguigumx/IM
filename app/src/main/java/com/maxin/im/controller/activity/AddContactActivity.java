package com.maxin.im.controller.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.maxin.im.R;
import com.maxin.im.base.BaseActivity;
import com.maxin.im.common.Modle;
import com.maxin.im.utils.UiUtils;

import butterknife.Bind;

public class AddContactActivity extends BaseActivity {

    @Bind(R.id.invite_btn_save)
    Button inviteBtnSave;
    @Bind(R.id.invite_et_search)
    EditText inviteEtSearch;
    @Bind(R.id.invite_tv_username)
    TextView inviteTvUsername;
    @Bind(R.id.invite_btn_add)
    Button inviteBtnAdd;
    @Bind(R.id.invite_ll_item)
    LinearLayout inviteLlItem;
    @Bind(R.id.activity_invite_acitivity)
    LinearLayout activityInviteAcitivity;
    private String name;
    @Override
    public void initListener() {
        inviteBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = inviteEtSearch.getText().toString().trim();
                if(TextUtils.isEmpty(name)) {
                    UiUtils.showToast("搜索用户名称不能为空");
                    return;
                }
                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(getUser()) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    inviteLlItem.setVisibility(View.VISIBLE);
                                    inviteTvUsername.setText(name);
                                }
                            });
                        }else {
                            UiUtils.showToast("没有搜索到此用户");
                        }
                    }
                });
            }
        });

        inviteBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EMClient.getInstance().contactManager().addContact(name,"....");
                    UiUtils.showToast("添加联系人成功");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    UiUtils.showToast(e.getMessage());
                }
            }
        });

    }

    private boolean getUser() {
        return true;
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_contact;
    }

}
