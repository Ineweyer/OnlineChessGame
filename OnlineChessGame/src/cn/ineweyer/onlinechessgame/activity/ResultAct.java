package cn.ineweyer.onlinechessgame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.ineweyer.onlinechessgame.R;

/**
 * 结果显示界面类
 * @author LQ
 *
 */
public class ResultAct extends Activity implements OnClickListener{
	
	private TextView result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);				// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);		// 设置全屏
		setContentView(R.layout.game_with_computer);
		
		setContentView(R.layout.result);
		
		findViewById(R.id.onceMore).setOnClickListener(this);
		findViewById(R.id.exitGameDirect).setOnClickListener(this);
		result = (TextView) findViewById(R.id.result);
		
		Intent data = getIntent();
		result.setText(data.getIntExtra("result", 0));
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.onceMore :
			startActivity(new Intent(ResultAct.this, IndexUIAct.class));
			ResultAct.this.finish();
			break;
		case R.id.exitGameDirect :
			ResultAct.this.finish();
			break;
		}
	}
}
