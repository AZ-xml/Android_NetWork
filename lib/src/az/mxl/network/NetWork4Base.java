package az.mxl.network;




public class NetWork4Base<T> {

	OnDataSourceListener<T> listener;
	OnDataSourceSuccessListener<T> sucListener;
	
	/**
	 * 数据加载成功监听
	 *
	 * @param <T>
	 */
	public interface OnDataSourceSuccessListener<T>{
		/**
		 * 数据加载成功(code=OK)
		 * 
		 * @param response
		 * @param tag
		 */
		public void onSuccessData(T response, Object tag);
		
		/**
		 * 查询参数
		 * 
		 * @return
		 */
		public QueryParameter getQueryParameter();
	}
	
	/**
	 * 数据加载监听
	 *
	 * @param <T>
	 */
	public interface OnDataSourceListener<T> extends OnDataSourceSuccessListener<T>{
		
		/**
		 * 数据马上加载
		 */
		public void onPerData();
		
		/**
		 * 数据加载完毕
		 */
		public void onFinishData();

		/**
		 * 数据加载失败
		 * 
		 * @param err
		 * @param tag
		 */
		public void onFailData(ErrorInfo err, Object tag);

	}
	
	public interface DataSourceListener2<T> extends OnDataSourceListener<T>{
		/**
		 * 查询参数--加载更多
		 * 
		 * @return
		 */
		public QueryParameter getQueryParameter4More();
	}


	/**
	 * 加载数据
	 * 
	 * @param tag
	 */
	public void start(Object tag){
		
	}

	/**
	 * 加载数据
	 */
	public void start(){
		
	}
	
	/**
	 * 加载更多数据
	 */
	public void startMore() {

	}
	
	/**
	 * 设置数据加载监听
	 * 
	 * @param listener
	 */
	public void setOnDataSourceListener(OnDataSourceListener<T> listener) {
		this.listener = listener;
	}
	
	/**
	 * 设置数据加载监听
	 * 
	 * @param listener
	 */
	public void setOnDataSourceSuccessListener(OnDataSourceSuccessListener<T> listener) {
		this.sucListener = listener;
	}

}
