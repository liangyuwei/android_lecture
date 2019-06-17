package cn.zju.id21832004.liangyuwei;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class Music2Activity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private TextView txtPkg;
    private ListView listMusic;
    private ArrayList<Music> list = new ArrayList<>();

    private void getList(){
        ContentResolver provider = getContentResolver();
        Cursor cursor = provider.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{"title", "duration", "artist", MediaStore.Audio.Media.DATA},
                null, null, null);
        String s;
        while (cursor.moveToNext()){
            int t = Integer.parseInt(cursor.getString(1))/1000;
            s = String.format("%d:%d", t/60, t%60);
            list.add(new Music(cursor.getString(0), s,
                    cursor.getString(2), cursor.getString(3)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music2);

        txtPkg = (TextView) findViewById(R.id.txtPkg);
        txtPkg.setText(getPackageName());
        listMusic = (ListView) findViewById(R.id.listMusic);

        // Set adapter
        MusicAdapter adapter = new MusicAdapter(this, R.layout.row_music, list);
        listMusic.setAdapter(adapter);

        // set clicklistener
        listMusic.setOnItemClickListener(this);

        // Request for permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else {
            getList();
        }

        // get music list on start, after requesting for permission
        getList();

    }

    private  MediaPlayer player = new MediaPlayer();
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        player.reset();
        try{
            player.setDataSource(list.get(position).uri);
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(player.isPlaying())
            player.stop();
        player.release();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getList();
                }else{
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}