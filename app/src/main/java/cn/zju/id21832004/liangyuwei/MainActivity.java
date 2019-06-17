package cn.zju.id21832004.liangyuwei;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import cn.iipc.android.tweetlib.Status;
import cn.iipc.android.tweetlib.SubmitProgram;



public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private TextView pkgName;
    private TextView text1;
    SQLiteDatabase db;
    Cursor cursor;
    DbHelper dbhlp;

    SimpleCursorAdapter adapter;
    ListView listStatus;
    private static final String[] FROM = {StatusContract.Column.USER,
        StatusContract.Column.MESSAGE, StatusContract.Column.CREATED_AT};
    private static final int[] TO = {R.id.textUser, R.id.textMsg, R.id.textTime};

    TimelineReceiver receiver;
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the receiver
        receiver = new TimelineReceiver();
        filter = new IntentFilter(StatusContract.NEW_STATUS);

        pkgName = (TextView) findViewById(R.id.pkgName);
        pkgName.setText("Context: " + this.getPackageName());
        listStatus = (ListView) findViewById(R.id.listStatus);

        dbhlp = new DbHelper(this);
        db = dbhlp.getReadableDatabase();
        cursor = db.query(StatusContract.TABLE, null, null, null, null, null,
                    StatusContract.DEFAULT_SORT);
        startManagingCursor(cursor);

        adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
        adapter.setViewBinder(new TimelineViewBinder());
        listStatus.setAdapter(adapter);

        listStatus.setOnItemClickListener(this);

    }

    // 删除onStart

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                new SubmitProgram().doSubmit(this, "F2");//"D2");//"C3");//"C1");
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
                if (serviceRunning){
                    stopService(new Intent(this, UpdateService.class));
                    serviceRunning = false;
                } else {
                    startService(new Intent(this, UpdateService.class));
                    serviceRunning = true;
                }
                return true;
            // case R.id.deactivate_service:
            //     stopService(new Intent(this, UpdateService.class));
            //    return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.get_music_info:
                startActivity(new Intent(this, MusicActivity.class));
                return true;
            case R.id.action_close:
                finish();
                return true;
            case R.id.action_delete:
                SQLiteDatabase dbw = dbhlp.getWritableDatabase();
                dbw.delete(StatusContract.TABLE, null, null);
                cursor.requery();
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "所有数据已经被删除！", Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) listStatus.getItemAtPosition(position);
        String user = cursor.getString(cursor.getColumnIndex(StatusContract.Column.USER));
        String msg = cursor.getString(cursor.getColumnIndex(StatusContract.Column.MESSAGE));

        new AlertDialog.Builder(this).setTitle(user)
                .setMessage(msg)
                .setNegativeButton("关闭", null)
                .show();
    }

    // Switch between two states
    private boolean serviceRunning = false;
    @Override
    public boolean onMenuOpened(int featureId, Menu menu){
        if(menu == null)
            return true;

        MenuItem toggleItem = menu.findItem(R.id.activate_service);
        toggleItem.setChecked(serviceRunning);

        if(serviceRunning){
            toggleItem.setTitle(R.string.deactivate_service_name);
            toggleItem.setIcon(android.R.drawable.ic_media_pause);
        }
        else {
            toggleItem.setTitle(R.string.activate_service_name);
            toggleItem.setIcon(android.R.drawable.ic_media_play);
        }

        return true;

    }


    // Handles custom binding of data to view.
    class TimelineViewBinder implements SimpleCursorAdapter.ViewBinder{
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex){
            switch (view.getId()){
                case R.id.textUser:
                    String usr = cursor.getString(columnIndex);
                    ((TextView) view).setText(usr);
                    return true;
                case R.id.textMsg:
                    String msg = cursor.getString(columnIndex);
                    if(msg.length() > 60){
                        msg = msg.substring(0, 60) + "......";
                    }
                    ((TextView) view).setText(msg);
                    return  true;
                case R.id.textTime:
                    // Convert timestamp to relative time
                    long timestamp = cursor.getLong(columnIndex); //

                    CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp);
                    ((TextView) view).setText(relativeTime); //
                    return true;
                default:
                    return true;
            }

        }
    }

    class TimelineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){

            Log.d("TimelineReceiver", "onReceived");

            int count = intent.getIntExtra("count", 0);

            if (count > 0) {
                cursor.requery();
                adapter.notifyDataSetChanged();
            }

            Toast.makeText(MainActivity.this, "更新了" + count + "条记录。", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop(){
        super.onStop();
        unregisterReceiver(receiver);
    }


}
