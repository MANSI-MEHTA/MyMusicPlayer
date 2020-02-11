package com.example.mansi.mymusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ListView lvMusic;
    Button btnPause,btnStop,btnForward,btnRewind;
    String name[],path[];
    MediaPlayer mp;
    final int seekForwardTime = 5 * 1000;
    final int seekBackwardTime = 5 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMusic= (ListView) findViewById(R.id.lvMusic);
        btnPause= (Button) findViewById(R.id.btnPause);
        btnStop= (Button) findViewById(R.id.btnStop);
        btnForward= (Button) findViewById(R.id.btnForward);
        btnRewind= (Button) findViewById(R.id.btnRewind);

        mp=new MediaPlayer();

        ContentResolver cr=getContentResolver();
        Cursor cursor=cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);

        name=new String[cursor.getCount()];
        path=new String[cursor.getCount()];

        int i=0;
        while(cursor.moveToNext()){
            name[i]=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            path[i]=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            i++;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,name);
        lvMusic.setAdapter(adapter);

        final int finalI = i;
        lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p=path[position];
                try{
                    mp.reset();
                    mp.setDataSource(p);
                    mp.prepare();
                    mp.start();
                    btnPause.setEnabled(true);
                    btnStop.setEnabled(true);
                    btnPause.setText("Pause");


                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pause();
                    btnPause.setText("Resume");
                }
                else{
                    mp.start();
                    btnPause.setText("Pause");
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                btnPause.setEnabled(false);
                btnStop.setEnabled(false);
            }
        });
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mp.getCurrentPosition();

                if(currentPosition + seekForwardTime <= mp.getDuration()){
                    mp.seekTo(currentPosition + seekForwardTime);
                }else{

                    mp.seekTo(mp.getDuration());
                }
            }

        });
        btnRewind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int currentPosition = mp.getCurrentPosition();

                if(currentPosition - seekBackwardTime >= 0){

                    mp.seekTo(currentPosition - seekBackwardTime);
                }else{

                    mp.seekTo(0);
                }

            }
        });

    }
}
