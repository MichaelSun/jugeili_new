package com.tugou.jgl.utils;
import android.util.Log;

public class Debug {

	private static final String TAG = "jugeili >>>>>>>>>>";
	public static final boolean DEBUG = true;
	
    public static void LOGD(String msg) {
    	if (DEBUG) {
    	    Log.d(TAG, msg);
    	}
    }
    
    public static void LOGD(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }
}
