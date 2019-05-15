package cn.zju.id21832004.liangyuwei;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.iipc.android.tweetlib.SubmitProgram;

public class StatusActivity extends AppCompatActivity {

    private EditText editStatus;
    private Button btnPost;
    private TextView txtCount, pkgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        editStatus = (EditText) findViewById(R.id.editStatus);
        btnPost = (Button) findViewById(R.id.btnPost);
        txtCount = (TextView) findViewById(R.id.txtCount);

        pkgName = (TextView) findViewById(R.id.txtPkg);
        pkgName.setText(this.getPackageName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new SubmitProgram().doSubmit(this, "C1");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
