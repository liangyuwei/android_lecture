package cn.zju.id21832004.liangyuwei;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import cn.iipc.android.tweetlib.SubmitProgram;

public class MusicActivity extends AppCompatActivity {

    private TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        text1 = (TextView) findViewById(R.id.textMusic);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else {
            read_musice_info();
        }

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
            new SubmitProgram().doSubmit(this, "F1");//"E1"); //"D2");//"C3");//"C1");
            return true;
        }

        if (id == R.id.action_close){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void read_musice_info(){

        ContentResolver provider = getContentResolver();
        Cursor cursor = provider.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{"artist", "title", "duration"}, null, null, null);
        String s = "";
        while (cursor.moveToNext()){
            s += String.format("作者：%s   曲名：%s   时间：%d(秒)\n\n",
                    cursor.getString(0),
                    cursor.getString(1),
                    Integer.parseInt(cursor.getString(2))/1000);
        }
        text1.setText(s);

        // free up cursor after use
        cursor.close();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    read_musice_info();
                }else{
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}
