Android_NetWork
===============
使用方法:

1、首先创建自己的NetWork类，继承本库中的NetWork<T>；
  
public class MyNetWork<T> extends NetWork<T> {

	public static final String TAG = "MyNetWork";
	
	public MyNetWork(String server, Class<T> mClass) {
		super(server, mClass);
		// TODO Auto-generated constructor stub
	}

	public MyNetWork(String server, Class<T> mClass, az.mxl.network.NetWork4Base.OnDataSourceListener<T> listener) {
		super(server, mClass, listener);
		// TODO Auto-generated constructor stub
	}

	public MyNetWork(String server, Class<T> mClass, az.mxl.network.NetWork4Base.OnDataSourceSuccessListener<T> listener) {
		super(server, mClass, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getHost() {
		// TODO Auto-generated method stub
		return "http://az.com/mxl/";
	}

	@Override
	public void debugApi(String api) {
		// TODO Auto-generated method stub
		Log.e(TAG, "api如下:");
		Log.e(TAG, api);// 方面在log中查看完整的api
	}

}
