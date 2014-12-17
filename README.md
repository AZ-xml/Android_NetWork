#Android_NetWork
===============
本库说明：整合Volley+Gson两个开源库，实现网络请求、数据解析一步到位！

##使用方法:

注：

1. APP中所有接口前面的“域名”都是一样的，所有本库把完整api分为三块(“域名”，api名称，参数)
2. 服务器返回json数据：{"code":"ok","alertMsg":"获取用户信息成功","msg":{"name":"az","age":"25","email":"maxinliang_no1@163.com"}}
3. json最外层的code和alertMsg假设是服务器规定的格式，当访问成功时code=ok，不成功时code!=ok，alertMsg是对于本次访问的说明。对于这俩字段的解析在本库内部[GsonRequest][gsonRequest]进行了处理，所以msg才是主要信息(当写解析类时只写msg中的对应字段就可)。

下面我们以查询用户信息api(<http://az.com/mxl/getUserInfo?id=110>)进行介绍怎样使用<br>

* 1、首先创建自己的NetWork类，继承本库中的NetWork<T>；

```java
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
	public String getOfenParameter() {
		// TODO Auto-generated method stub
		return super.getOfenParameter();
	}
	
	@Override
	public void debugApi(String api) {
		// TODO Auto-generated method stub
		Log.e(TAG, "api如下:");
		Log.e(TAG, api);// 方面在log中查看完整的api
	}
}
```
* 2、根据后台回传的json格式写好解析类
```java
public class UserInfoBean {

	private String name;   // az
	private String age;    // 25
	private String email;  // maxinliang_no1@163.com

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
```

* 3、在用户信息页面执行NetWork

1)、先生成相应的NetWork对象：
```java
private EJiaJieNetWork<UserInfoBean> getUserInfoNetWork = new EJiaJieNetWork<UserInfoBean>(ApiConstantData.USER_INFO, UserInfoBean.class,
			new OnDataSourceListener<UserInfoBean>() {
				@Override
				public void onSuccessData(UserInfoBean response, Object tag) {
					// 数据使用
				}

				@Override
				public QueryParameter getQueryParameter() {
					return QueryParameter.Builder().put("id", "110");
				}

				@Override
				public void onPerData() {
					showProgress();
				}

				@Override
				public void onFinishData() {
					hideProgress();
				}

				@Override
				public void onFailData(ErrorInfo err, Object tag) {
					// showAlterToast(err.getMsg());
				}
			});

```
2)、开始网络请求：getUserInfoNetWork.start();


*******************

[gsonRequest]:https://github.com/AZ-xml/Android_NetWork/blob/master/lib/src/az/mxl/network/GsonRequest.java
