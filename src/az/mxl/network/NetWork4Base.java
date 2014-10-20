package az.mxl.network;




public class NetWork4Base<T> {

	OnDataSourceListener<T> listener;
	OnDataSourceSuccessListener<T> sucListener;
	
	/**
	 * ���ݼ��سɹ�����
	 *
	 * @param <T>
	 */
	public interface OnDataSourceSuccessListener<T>{
		/**
		 * ���ݼ��سɹ�(code=OK)
		 * 
		 * @param response
		 * @param tag
		 */
		public void onSuccessData(T response, Object tag);
		
		/**
		 * ��ѯ����
		 * 
		 * @return
		 */
		public QueryParameter getQueryParameter();
	}
	
	/**
	 * ���ݼ��ؼ���
	 *
	 * @param <T>
	 */
	public interface OnDataSourceListener<T> extends OnDataSourceSuccessListener<T>{
		
		/**
		 * �������ϼ���
		 */
		public void onPerData();
		
		/**
		 * ���ݼ������
		 */
		public void onFinishData();

		/**
		 * ���ݼ���ʧ��
		 * 
		 * @param err
		 * @param tag
		 */
		public void onFailData(ErrorInfo err, Object tag);

	}
	
	public interface DataSourceListener2<T> extends OnDataSourceListener<T>{
		/**
		 * ��ѯ����--���ظ���
		 * 
		 * @return
		 */
		public QueryParameter getQueryParameter4More();
	}


	/**
	 * ��������
	 * 
	 * @param tag
	 */
	public void start(Object tag){
		
	}

	/**
	 * ��������
	 */
	public void start(){
		
	}
	
	/**
	 * ���ظ�������
	 */
	public void startMore() {

	}
	
	/**
	 * �������ݼ��ؼ���
	 * 
	 * @param listener
	 */
	public void setOnDataSourceListener(OnDataSourceListener<T> listener) {
		this.listener = listener;
	}
	
	/**
	 * �������ݼ��ؼ���
	 * 
	 * @param listener
	 */
	public void setOnDataSourceSuccessListener(OnDataSourceSuccessListener<T> listener) {
		this.sucListener = listener;
	}

}
