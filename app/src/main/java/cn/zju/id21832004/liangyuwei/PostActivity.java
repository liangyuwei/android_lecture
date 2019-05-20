package cn.zju.id21832004.liangyuwei;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import cn.iipc.android.tweetlib.SubmitProgram;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

public class PostActivity extends AppCompatActivity {

    // Newly added
    private Button btnPost;
    private EditText editStatus;
    private TextView pkgName, txtCount;

    // ?????

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        editStatus = (EditText) findViewById(R.id.editStatus);
        btnPost = (Button) findViewById(R.id.btnPost);
        txtCount = (TextView) findViewById(R.id.txtCount);

        pkgName = (TextView) findViewById(R.id.txtPkg);
        pkgName.setText(this.getPackageName());
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
        if (id == R.id.upload_assignments) {
            new SubmitProgram().doSubmit(this, "C1");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final class postTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}

