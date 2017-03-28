package cn.ineweyer.onlinechessgame.net;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ineweyer.onlinechessgame.Config;

/**
 * 下棋房间的网络操作类
 * @author LQ
 *
 */
public class RoomNet {
	
	/**
	 * 获取房间网络请求
	 * @param username   用户名
	 * @param success    获取房间成功接口
	 * @param fail       获取房间失败接口
	 * @return
	 */
	public static String getRoom(final String username, final SeccuessCallBack success, final FailCallBack fail) {
		new NetConnection(Config.SEND_MESSAGE_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				if(result != null) {
					try {
						System.out.println(result);
						JSONObject json = new JSONObject(result);
						switch(json.getInt(Config.PARA_STATUS)) {
						case Config.NET_RESULT_SECUESS :
							if(success != null) {
								json = (JSONObject) json.getJSONArray(Config.KEY_ROOM).get(0);
								success.onSeccuess(json);
							}
							break;
							default: 
								if(fail != null) {
									fail.onFail(json.getString(Config.KEY_TOKEN));
								}
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						if(fail != null) {
							fail.onFail("网络传输出错");
						}
					}
				}
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail(String result) {
				if(fail != null) {
					fail.onFail("请检查网络");
				}
			}
		}, Config.KEY_ACTION,Config.ACTION_GET_ROOM,
		Config.PARA_USER_NAME, username);
		
		return null;
	}
	
	/**
	 * 房主检查房间状态网络请求
	 * @param username   房主
	 * @param success    请求成功接口
	 * @param fail       请求失败接口
	 * @return
	 */
	public static String checkRoom(final String username,
			final SeccuessCallBack success, final FailCallBack fail) {
		new NetConnection(Config.SEND_MESSAGE_URL, HttpMethod.GET,
				new NetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						if (result != null) {
							try {
								System.out.println(result);
								JSONObject json = new JSONObject(result);
								switch (json.getInt(Config.PARA_STATUS)) {
								case Config.NET_RESULT_SECUESS:
									if (success != null) {
										success.onSeccuess((JSONObject)(json.getJSONArray(Config.KEY_ROOM).get(0)));
									}
									break;
								default:
									if (fail != null) {
										fail.onFail(json
												.getString(Config.KEY_TOKEN));
									}
									break;
								}
							} catch (JSONException e) {
								e.printStackTrace();
								if (fail != null) {
									fail.onFail("网络传输出错");
								}
							}
						}
					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail(String result) {
						if (fail != null) {
							fail.onFail("请检查网络");
						}
					}
				}, Config.KEY_ACTION, Config.ACTION_CHECK_ROOM,
				Config.PARA_USER_NAME, username);

		return null;
	}
	
	/**
	 * 离开房间网络请求
	 * @param username  房主
	 * @param success   请求成功接口
	 * @param fail      请求失败接口
	 * @return
	 */
	public static String leaveRoom(final String owner, final SeccuessCallBack success, final FailCallBack fail) {
		new NetConnection(Config.SEND_MESSAGE_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				if(result != null) {
					try {
						System.out.println(result);
						JSONObject json = new JSONObject(result);
						switch(json.getInt(Config.PARA_STATUS)) {
						case Config.NET_RESULT_SECUESS :
							if(success != null) {
								success.onSeccuess(null);
							}
							break;
							default: 
								if(fail != null) {
									fail.onFail(json.getString(Config.KEY_TOKEN));
								}
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						if(fail != null) {
							fail.onFail("网络传输出错");
						}
					}
				}
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail(String result) {
				if(fail != null) {
					fail.onFail("请检查网络");
				}
			}
		}, Config.KEY_ACTION,Config.ACTION_LEAVE_ROOM,
		Config.PARA_USER_NAME, owner);
		
		return null;
	}
	public static interface SeccuessCallBack {
		public void onSeccuess(JSONObject json);
	}
	public static interface FailCallBack {
		public void onFail(String result);
	}
	
}
