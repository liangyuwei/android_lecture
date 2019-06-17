package cn.zju.id21832004.liangyuwei;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.jar.Manifest;

import cn.iipc.android.tweetlib.SubmitProgram;

public class FileWriteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button1, button2, button3;
    private TextView textMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_write);

        button1 = (Button) findViewById(R.id.btnGetFileDir);
        button2 = (Button) findViewById(R.id.btnGetExtStorageDir);
        button3 = (Button) findViewById(R.id.btnGetExtFileDir);

        textMsg = (TextView) findViewById(R.id.txtFileWrite);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

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
                new SubmitProgram().doSubmit(this, "E1"); //"D2");//"C3");//"C1");
                return true;
            case R.id.action_close:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fileTestWrite(Environment.getExternalStorageDirectory().getPath());
                }else{
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onClick(View v) {
        if(v==button1){
            fileTestWrite(getFilesDir().getPath());
        }else if(v==button2){
            // Check for permission
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }else {
                fileTestWrite(Environment.getExternalStorageDirectory().getPath());
            }
        }else if(v==button3){
            fileTestWrite(getExternalFilesDir(null).getPath());
        }
    }

    private void fileTestWrite(String dir){

        // Set file's absolute path
        String fn = dir + "/hello.txt";

        // Print to File
        try{
            textMsg.setText(textMsg.getText() + "\nWrite to: " + fn);
            PrintWriter o = new PrintWriter(new BufferedWriter(new FileWriter(fn)));
            o.println(fn);
            o.close();
        } catch (Exception e){
            textMsg.setText(textMsg.getText() + "\nWrite to: " + e.toString());
        }
    }

}
