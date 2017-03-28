package cn.ineweyer.onlinechessgame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.ineweyer.onlinechessgame.Config;
import cn.ineweyer.onlinechessgame.R;
import cn.ineweyer.onlinechessgame.net.Login;

/**
 * 浏览笔记论坛界面类
 * 
 * @author LQ
 * 
 */
public class WatchBlogAct extends Activity {
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blog_layout);
		
		  webView = (WebView) findViewById(R.id.bolg);  
	      webView.getSettings().setJavaScriptEnabled(true); 
	      
	      // 设置可以支持缩放 
	      webView.getSettings().setUseWideViewPort(true);
	      webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
	      webView.getSettings().setSupportZoom(true); 
	      
	      // 设置出现缩放工具
	      webView.getSettings().setBuiltInZoomControls(true);
	      webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	      webView.getSettings().setLoadWithOverviewMode(true);
	      webView.setInitialScale(80);
	      
	      //定义网址
	      StringBuilder str = new StringBuilder(Config.LOGIN_URL);
	      str.append("?").append(Config.PARA_USER_NAME).append("=").append(Config.getCachedUserName(WatchBlogAct.this));
	      str.append("&").append(Config.PARA_PASSWORD).append("=").append(Config.getCachedPassword(WatchBlogAct.this));
	      
	      webView.setWebViewClient(new MyWebViewClient());
	      
	      findViewById(R.id.blogToGame).setOnClickListener(new View.OnClickListener() {
			
	      //返回游戏界面
			@Override
			public void onClick(View v) {
				startActivity(new Intent(WatchBlogAct.this, IndexUIAct.class));
				finish();
			}
		});
	      
	     //登陆
	    Login.login(Config.getCachedUserName(WatchBlogAct.this), 
	    		Config.getCachedPassword(WatchBlogAct.this), new Login.SeccuessCallBack() {
					
					@Override
					public void onSeccuess(String token) {
						webView.loadUrl(Config.BLOG_URL);
					}
				}, new Login.FailCallBack() {
					
					@Override
					public void onFail(String result) {
						webView.loadUrl(Config.BLOG_URL);
					}
				});
	}
	
	//重写后退键当回退时，页面回退
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return false;
	}
	
	//定义WebViewClient，当在页面中点击页面是不发生跳转，还在原来的窗口中
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
