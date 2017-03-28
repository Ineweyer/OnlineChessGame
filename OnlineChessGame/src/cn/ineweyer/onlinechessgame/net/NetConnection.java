package cn.ineweyer.onlinechessgame.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.util.Log;
import cn.ineweyer.onlinechessgame.Config;

/**
 * http请求处理基类
 * @author LQ
 *
 */
public class NetConnection {
	
	/**
	 * 发送Http请求
	 * @param url               请求地址
	 * @param method            请求方式
	 * @param successCallback   请求成功接口
	 * @param failCallback      请求失败接口
	 * @param kvs               请求参数
	 */
	public NetConnection(final String url, final HttpMethod method,
			final SuccessCallback successCallback,
			final FailCallback failCallback, final String...kvs) {
		new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				StringBuffer strBuffer = new StringBuffer();
				//获取所有的参数
				for (int i = 0; i < kvs.length; i++) {
					strBuffer.append(kvs[i]).append("=").append(kvs[++i]).append("&");
				}
				try {
					//定义连接
					URLConnection connect = null;
					switch (method) {
					case GET:  //get方式连接
						connect = new URL(url + "?" + strBuffer.toString())
								.openConnection();
						break;
					case POST:  //post方式连接
						connect = new URL(url).openConnection();
						//获取到输出流输出参数信息
						OutputStreamWriter or = new OutputStreamWriter(
								connect.getOutputStream());
						BufferedWriter bw = new BufferedWriter(or);
						bw.write(strBuffer.toString());
						bw.flush();
						break;
					}
					//读取服务器发回的数据
					BufferedReader br = new BufferedReader(
							new InputStreamReader(connect.getInputStream(),
									Config.CHARSET));
					String line = null;
					StringBuffer sb = new StringBuffer();
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					Log.i("NET Connection Result", "Recieve data size: " + sb.length());
					return sb.toString();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					if (successCallback != null) {
						successCallback.onSuccess(result);
					}
				} else {
					if (failCallback != null) {
						failCallback.onFail(result);
					}
				}
				super.onPostExecute(result);
			}
		}.execute();
	}

	public static interface SuccessCallback {
		void onSuccess(String result);
	}

	public static interface FailCallback {
		void onFail(String result);
	}
}
