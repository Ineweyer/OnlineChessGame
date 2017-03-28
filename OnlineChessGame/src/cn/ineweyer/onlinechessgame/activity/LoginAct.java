package cn.ineweyer.onlinechessgame.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.ineweyer.onlinechessgame.Config;
import cn.ineweyer.onlinechessgame.R;
import cn.ineweyer.onlinechessgame.net.Login;
/**
 * 用户登陆界面
 * @author LQ
 *
 */
public class LoginAct extends Activity {
	
	private Button loginBtn;
	private TextView loginRegisterBtn;
	private EditText loginUsername,loginPassword;
	private ProgressDialog pd; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		loginBtn = (Button) findViewById(R.id.loginBtn);
		loginUsername = (EditText) findViewById(R.id.login_username);
		loginPassword = (EditText) findViewById(R.id.login_passwrod);
		loginRegisterBtn = (TextView) findViewById(R.id.login_register);
		
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(loginUsername.getText().toString())) {
					Toast.makeText(LoginAct.this, R.string.username_empty, Toast.LENGTH_SHORT).show();
					return ;
				}
				if(TextUtils.isEmpty(loginPassword.getText().toString())) {
					Toast.makeText(LoginAct.this, R.string.password_empty, Toast.LENGTH_SHORT).show();
					return ;
				}
				pd = ProgressDialog.show(LoginAct.this, getResources().getString(R.string.login), getResources().getString(R.string.login_to_server));
				Login.login(loginUsername.getText().toString(), loginPassword.getText().toString(),
						new Login.SeccuessCallBack() {

							@Override
							public void onSeccuess(String token) {
								pd.dismiss();
								
								Config.setCachedPassword(LoginAct.this, loginPassword.getText().toString());
								Config.setCachedUserName(LoginAct.this, loginUsername.getText().toString());
								Config.setChessBg(LoginAct.this, R.drawable.chess_bg1);
								Config.setPlayerChessColor(LoginAct.this, R.drawable.chess_img1);
								Config.setCompeterChessColor(LoginAct.this, R.drawable.chess_img2);
								Intent i = new Intent(LoginAct.this, IndexUIAct.class);
								i.putExtra(Config.KEY_TOKEN, token);
								i.putExtra(Config.PARA_USER_NAME, loginUsername.getText().toString());
								startActivity(i);
								
								finish();
							}
						}, new Login.FailCallBack() {
							
							@Override
							public void onFail(String result) {
								pd.dismiss();
								Toast.makeText(LoginAct.this, result, Toast.LENGTH_SHORT).show();
							}
						});
			}
		});
		
		loginRegisterBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginAct.this, RegisterAct.class));
			}
		});
	}
}
