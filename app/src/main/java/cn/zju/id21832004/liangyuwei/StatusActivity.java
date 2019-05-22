package cn.zju.id21832004.liangyuwei;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.iipc.android.tweetlib.*;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText editStatus;
    private Button btnPost;
    private TextView txtCount, pkgName;

    private static final String TAG="StatusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        editStatus = (EditText) findViewById(R.id.editStatus);
        btnPost = (Button) findViewById(R.id.btnPost);
        txtCount = (TextView) findViewById(R.id.txtCount);

        pkgName = (TextView) findViewById(R.id.txtPkg);

        pkgName.setText(this.getPackageName());

        btnPost.setOnClickListener(this); // ??

        editStatus.addTextChangedListener(this); // editStatus is the "publisher", i.e. from which the event is listened

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
            new SubmitProgram().doSubmit(this, "D1");//"C3");//"C1");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {  // ???
        // 右键 implements 的View.OnClickListener，选择generate，选implement methods
        //editStatus.setText("Test button onClick");

        // Log
        String status = editStatus.getText().toString();
        Log.d(TAG, "onClicked with status: " + status);

        // Post
        new PostTask().execute(status);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int count = 140 - editStatus.length();
        txtCount.setText(Integer.toString(count));

        txtCount.setTextColor(Color.GREEN);
        if (count <= 10) txtCount.setTextColor(Color.YELLOW);
        if (count <= 0) txtCount.setTextColor(Color.RED);

    }

    private final class PostTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = "liangyuwei";
            String password = "ipconfigall478";
            YambaClient yambaCloud = new YambaClient(username, password);
            try{
                yambaCloud.postStatus(params[0]);
                return "Successfully posted: " + params[0].length() + " chars";
            } catch (YambaClientException e){
                e.printStackTrace();
                return "Failed to post to yamba service";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
            pkgName.setText(result + " BY<" + getPackageName() +">");
            if(result.startsWith("Successfully")){
                editStatus.setText("");
            }
        }

    }

}
