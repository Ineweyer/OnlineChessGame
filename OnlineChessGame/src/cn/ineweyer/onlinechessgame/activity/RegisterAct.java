package cn.ineweyer.onlinechessgame.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.ineweyer.onlinechessgame.R;
import cn.ineweyer.onlinechessgame.net.Register;

/**
 * 用户注册界面
 * @author LQ
 *
 */
public class RegisterAct extends Activity{
	
	private EditText userName, password, surePassword, email;
	private Button registerBtn;
	private TextView registerLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		userName = (EditText) findViewById(R.id.register_username);
		password = (EditText) findViewById(R.id.register_passwrod);
		surePassword = (EditText) findViewById(R.id.register_passwrod_again);
		email = (EditText) findViewById(R.id.register_email);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		registerLogin = (TextView) findViewById(R.id.register_login);
		
		// 点击注册按钮，提交注册
		registerBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(userName.getText().toString())) {
					Toast.makeText(RegisterAct.this, R.string.username_empty, Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(password.getText().toString())) {
					Toast.makeText(RegisterAct.this, R.string.password_empty, Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(surePassword.getText().toString())) {
					Toast.makeText(RegisterAct.this, R.string.sure_password_empty, Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(email.getText().toString())) {
					Toast.makeText(RegisterAct.this, R.string.email_empty, Toast.LENGTH_SHORT).show();
					return;
				}
				if(!password.getText().toString().equals(surePassword.getText().toString())) {
					Toast.makeText(RegisterAct.this, R.string.password_not_same, Toast.LENGTH_SHORT).show();
				}
				final ProgressDialog pd = ProgressDialog.show(RegisterAct.this, getResources().getString(R.string.register), getResources().getString(R.string.register_to_server));
				Register.register(userName.getText().toString(), password.getText().toString(),email.getText().toString(),
						new Register.SuccessCallBack() {
							
							@Override
							public void onSuccess(String token) {
								pd.dismiss();
								Toast.makeText(RegisterAct.this, token, Toast.LENGTH_SHORT).show();
							}
						}, new Register.FailCallBack() {
							
							@Override
							public void onFail(String result) {
								pd.dismiss();
								Toast.makeText(RegisterAct.this, result, Toast.LENGTH_SHORT).show();
							}
						});
			}
		});
		//注册页面返回登陆界面
		registerLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 finish();
			}
		});
		
	}
}
