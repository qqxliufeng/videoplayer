package com.android.ql.lf.baselibaray.ui.activity;

import android.content.pm.ActivityInfo;
import android.support.annotation.CallSuper;

import com.android.ql.lf.baselibaray.R;
import com.android.ql.lf.baselibaray.application.BaseApplication;
import com.android.ql.lf.baselibaray.component.ApiServerModule;
import com.android.ql.lf.baselibaray.component.DaggerApiServerComponent;
import com.android.ql.lf.baselibaray.present.GetDataFromNetPresent;
import com.android.ql.lf.baselibaray.utils.BaseConfig;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.Set;

import javax.inject.Inject;

public abstract class BaseSplashActivity extends BaseActivity {

    @Inject
    GetDataFromNetPresent getDataFromNetPresent;

    @CallSuper
    @Override
    public void initView() {
        DaggerApiServerComponent.builder().appComponent(BaseApplication.getInstance().getAppComponent()).apiServerModule(new ApiServerModule()).build().inject(this);
    }

    public GetDataFromNetPresent getGetDataFromNetPresent() {
        return getDataFromNetPresent;
    }

    protected void openImageChoose(Set<MimeType> mimeTypes, int maxSelectable) {
        Matisse.from(this)
                .choose(mimeTypes)
                .imageEngine(new GlideEngine())
                .maxSelectable(maxSelectable)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, BaseConfig.getFileProvidePath(this)))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .theme(R.style.my_matisse_style)
                .forResult(0);
    }
}
