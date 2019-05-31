package cn.zju.id21832004.liangyuwei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cn.iipc.android.tweetlib.SubmitProgram;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.upload_assignments:
                new SubmitProgram().doSubmit(this, "E1");//"D2");//"C3");//"C1");
                return true;
            case R.id.calculator:
                startActivity(new Intent(this, CalcActivity.class));
                return true;
            case R.id.post_weibo:
                startActivity(new Intent(this, StatusActivity.class));
                return true;
            case R.id.file_test:
                startActivity(new Intent(this, FileWriteActivity.class));
                return true;
            case R.id.activate_service:
            case R.id.deactivate_service:
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
