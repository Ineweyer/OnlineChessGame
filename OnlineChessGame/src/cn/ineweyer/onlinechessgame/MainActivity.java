package cn.ineweyer.onlinechessgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import cn.ineweyer.onlinechessgame.activity.IndexUIAct;
import cn.ineweyer.onlinechessgame.activity.LoginAct;

/**
 * 应用启动引导类
 * @author LQ
 *
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String token = Config.getCachedUserName(this);

        //没有登陆，跳转到登陆界面
        if(token == null || "".equals(token)) {
        	Intent i = new Intent(MainActivity.this, LoginAct.class);
        	startActivity(i);
        }
        else {         //已经登陆，跳转到主页面
        	Intent i = new Intent(MainActivity.this, IndexUIAct.class);
        	startActivity(i);
        }
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
