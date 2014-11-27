package az.mxl.network;

import android.util.Log;

public class NetLog {

	private static final String TAG = "NetWork";

	public static void w4defualtTag(String msg) {
		w4defualtTag(TAG, msg);
	}

	protected static void w4defualtTag(String tag, String msg) {
		if (NetWorkManager.isDebug())
			Log.w(tag, msg);
	}

	public static void e4defualtTag(String msg) {
		w4defualtTag(TAG, msg);
	}
	
	protected static void e4defualtTag(String tag, String msg) {
		if (NetWorkManager.isDebug())
			Log.e(tag, msg);
	}
	

}

