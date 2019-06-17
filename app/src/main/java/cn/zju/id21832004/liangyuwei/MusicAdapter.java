package cn.zju.id21832004.liangyuwei;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class MusicAdapter extends ArrayAdapter<Music> {
    private int rowResource;
    public MusicAdapter(Context context, int rowResource, List<Music> objs){
        super(context, rowResource, objs);
        this.rowResource = rowResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Music music = getItem(position);
        View view;
        if(convertView==null)
            view = LayoutInflater.from(getContext()).inflate(rowResource, parent, false);
        else
            view = convertView;

        TextView txtName, txtDuration, txtAuthor;
        txtName = (TextView)view.findViewById(R.id.txtName);
        txtDuration = (TextView)view.findViewById(R.id.txtDuration);
        txtAuthor = (TextView)view.findViewById(R.id.txtAuthor);
        txtName.setText(music.name);
        txtDuration.setText(music.duration);
        txtAuthor.setText(music.author);
        return view;
    }


}
