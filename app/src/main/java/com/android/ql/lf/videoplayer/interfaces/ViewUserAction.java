package com.android.ql.lf.videoplayer.interfaces;


import android.text.TextUtils;


import com.android.ql.lf.baselibaray.utils.PreferenceUtils;
import com.android.ql.lf.redpacketmonkey.interfaces.IViewUserAction;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Created by lf on 18.2.8.
 *
 * @author lf on 18.2.8
 */

public class ViewUserAction implements IViewUserAction {

    @Override
    public boolean onLogin(@NotNull JSONObject result) {
        try {
//            PreferenceUtils.setPrefString(MyApplication.application, UserInfo.USER_ID_FLAG, UserInfo.getInstance().getUser_id());
//            UserInfoLiveData.INSTANCE.postUserInfo();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean onLogout() {
//        UserInfo.getInstance().loginOut();
        return true;
    }

    @Override
    public void onRegister(@NotNull JSONObject result) {
    }

    @Override
    public void onForgetPassword(@NotNull JSONObject result) {
    }

    @Override
    public void onResetPassword(@NotNull JSONObject result) {
    }

    @Override
    public boolean modifyInfoForName(@NotNull String name) {
        try {
//            UserInfo.getInstance().setUser_nickname(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean modifyInfoForPic(@NotNull String result) {
        try {
//            UserInfo.getInstance().setUser_pic(result);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
