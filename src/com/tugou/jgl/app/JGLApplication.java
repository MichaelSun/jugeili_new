package com.tugou.jgl.app;

import android.app.Application;
import com.plugin.common.utils.UtilsConfig;
import com.plugin.common.utils.crash.CrashHandler;
import com.plugin.common.utils.files.FileInfo;

import java.util.HashMap;
import java.util.LinkedList;

public class JGLApplication extends Application implements CrashHandler.CrashHandlerListener {

    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler.getInstance().init(getApplicationContext());
        CrashHandler.getInstance().setCrashHandlerListener(this);
        UtilsConfig.init(getApplicationContext());
    }

    @Override
    public void beforeExitProcess() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HashMap<String, String> onCollectAppInfos() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAsyncUploadCrashLog(LinkedList<FileInfo> logs) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onCrashLogReady(String crashString) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
