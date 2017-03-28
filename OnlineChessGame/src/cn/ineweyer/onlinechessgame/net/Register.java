package cn.ineweyer.onlinechessgame.net;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ineweyer.onlinechessgame.Config;

/**
 * 用户注册网络操作类
 * @author LQ
 *
 */
public class Register {
	
	/**
	 * 用户注册网络请求
	 * @param username  用户名
	 * @param password  密码
	 * @param email     邮箱地址
	 * @param success   注册请求成功接口
	 * @param fail      注册请求失败接口
	 */
	public static void register(final String username, final String password,final String email, final SuccessCallBack success, final FailCallBack fail) {
		new NetConnection(Config.REGISTER_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				if(success != null) {
					try {
						JSONObject json = new JSONObject(result);
						switch(json.getInt(Config.PARA_STATUS)) {
						case Config.NET_RESULT_SECUESS :
							success.onSuccess(json.getString(Config.KEY_TOKEN));
							break;
						default :
							if(fail != null) {
								fail.onFail(json.getString(Config.KEY_TOKEN));
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						if(fail != null) {
							fail.onFail("数据解析错误");
						}
					}
					
				}
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail(String result) {
				if(fail != null) {
					fail.onFail("请检查网络连接");
				}
			}
		},Config.KEY_ACTION, Config.ACTION_REGISTER,
		Config.PARA_USER_NAME, username,
		Config.PARA_PASSWORD, password,
		Config.PARA_EMAIL, email);
	}
	
	public static interface SuccessCallBack {
		public void onSuccess(String token);
	}
	
	public static interface FailCallBack {
		public void onFail(String result);
	}
}
