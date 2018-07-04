package com.example.r.player;

import android.app.*;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.audiofx.AcousticEchoCanceler;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.r.player.HeadSetHelper;
import com.example.r.player.HeadSetHelper.OnHeadSetListener;

public class MainActivity extends Activity {
    private ListFragment fragment;
    private TextView nameTextView;
    private SeekBar seekBar;
    private List<Map<String, String>> data;
    private int current;
    private int playcurrent;
    private int premax;
    private MediaPlayer player;
    private Handler handler = new Handler();
    private Button ppButton;
    private boolean isPause;
    private boolean isStartTrackingTouch;
    private List artist;
    private List time;
    private TextView singer;
    private TextView sumtime;
    private TextView currenttime;
    private MyReceiver receiver;
    private List<Integer> ablum;
    private ImageView imageView;
    boolean onceplay=false;

                            private class Mytime
                            {private String mytime;
                             private int curtime;
                                Mytime(Integer integer)
                                {integer/=1000;
                                    curtime=integer;
                                    if(integer%60>=10)
                                    mytime=new String(integer/60+":"+integer%60);
                                    else
                                        mytime=new String(integer/60+":0"+integer%60);



                                }
                            private String gettime()
                            {
                                return mytime;
                            }
                            private String getcurtime(int x)
                            {
                                x/=1000;
                                curtime=x;
                                if(curtime%60>=10)
                                    mytime=new String(curtime/60+":"+curtime%60);
                                else
                                    mytime=new String(curtime/60+":0"+curtime%60);

                                 return mytime;
                            }
                            private void set(int x)
                            {
                                curtime=x;
                            }

                            }
                            public int getcurrent()
                            {return current;}
    public List<Map<String, String>> getdata()
    {return  data;}
    public MediaPlayer getPlayer()
    {return player;}
    public void setcurrent(int x)
    {current=x;}


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            SharedPreferences preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
            current=preferences.getInt("current",0);
            playcurrent=preferences.getInt("playcurrent",0);
            premax=preferences.getInt("premax",100);
//        current=0;
//        playcurrent=0;
//        premax=100;
            seekBar=(SeekBar)findViewById(R.id.seekBar);
            nameTextView=(TextView)findViewById(R.id.name);
            sumtime=(TextView)findViewById(R.id.sumtime);
            currenttime=(TextView)findViewById(R.id.current);
            singer=(TextView)findViewById(R.id.artist);
            ppButton=(Button)findViewById(R.id.button3);
            imageView=(ImageView)findViewById(R.id.ablum);
            View view=(View) findViewById(R.id.Layout);
            view.getBackground().setAlpha(130);
            player=new MediaPlayer();
            generateListView();
            seekBar.setMax(premax);
            seekBar.setProgress(playcurrent);
            Mytime mytime1=new Mytime((Integer)premax);
            Mytime mytime2=new Mytime((Integer)playcurrent);
            sumtime.setText(mytime1.gettime());
            currenttime.setText(mytime2.gettime());
            String albumArt = getAlbumArt(ablum.get(current));
            if (albumArt == null)
            imageView.setVisibility(View.GONE);
            else {
                Bitmap bm=null;
            bm = BitmapFactory.decodeFile(albumArt);
            BitmapDrawable bmpDraw = new BitmapDrawable(bm);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(bmpDraw);
        }
        nameTextView.setText(data.get(current).get("name"));
        singer.setText(artist.get(current).toString());
            seekBar.setOnSeekBarChangeListener(new MySeekBarListener());

            player.setOnCompletionListener(new MyPlayerListener());
            IntentFilter filter=new IntentFilter();
            filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
            registerReceiver(new PhoneListener(), filter);
            IntentFilter filter1=new IntentFilter("play");
            receiver=new MyReceiver();
            registerReceiver(receiver,filter1);
            TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        HeadSetHelper.getInstance().setOnHeadSetListener(headSetListener);
        HeadSetHelper.getInstance().open(this);


        }
        private final class MyPhoneStateListener extends PhoneStateListener{
            @Override
            public void onCallStateChanged(int state, String incomingNumber)
            { pause();
            }

        }

    private final class MyPlayerListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }
    public void next(View view) {
        next();
    }

    public void previous(View view) {
        previous();
    }

    private void previous() {

        current = current - 1 < 0 ? data.size() - 1 : current - 1;
        playcurrent=0;
        play();
    }

    private void next() {

        current = (current + 1) % data.size();
        Intent intent=new Intent();
        intent.setAction("next");
        intent.putExtra("current",current);
        sendBroadcast(intent);
        playcurrent=0;
        play();
    }

    private final class MySeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isStartTrackingTouch = true;
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.seekTo(seekBar.getProgress());
            isStartTrackingTouch = false;
        }
    }




    private void generateListView() {
        ablum=new ArrayList<Integer>();
        artist=new ArrayList<String>();
        time=new ArrayList<Integer>();
        data=new ArrayList<Map<String, String>>();
        ContentResolver cr=this.getContentResolver();
        Cursor cursor=cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.IS_MUSIC + " = 1 AND "
                + MediaStore.Audio.Media.DURATION + " > 10000", null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            map.put("path",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            artist.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            time.add(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            data.add(map);
            ablum.add(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
            cursor.moveToNext();
        }
       cursor.close();




    }



    public void play(){
        onceplay=true;
        try {
            Bitmap bm = null;
            String albumArt = getAlbumArt(ablum.get(current));
            if (albumArt == null)
                imageView.setVisibility(View.GONE);
            else {
                bm = BitmapFactory.decodeFile(albumArt);
                BitmapDrawable bmpDraw = new BitmapDrawable(bm);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(bmpDraw);
            }

            player.reset();
            player.setDataSource(data.get(current).get("path"));
            player.prepare();
            player.seekTo(playcurrent);
            player.start();
            seekBar.setProgress(player.getCurrentPosition());
            final Mytime cur=new Mytime(0);

            nameTextView.setText(data.get(current).get("name"));
            singer.setText(artist.get(current).toString());

            final Mytime mytime=new Mytime((Integer)time.get(current));
            sumtime.setText(mytime.gettime());
            seekBar.setMax(player.getDuration());
            ppButton.setText("||");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!isStartTrackingTouch) {
                        {
                            seekBar.setProgress(player.getCurrentPosition());
                            cur.set(player.getCurrentPosition()/1000);

                        }
                       currenttime.setText(cur.getcurtime(player.getCurrentPosition()));

                    }
                    handler.postDelayed(this, 1000);

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pp(View view) {

        if (!player.isPlaying() && !isPause) {
            play();
            return;
        }
        Button button = (Button) view;

        if ("||".equals(button.getText())) {
            pause();
            button.setText("▶");
        } else {
            resume();
            button.setText("||");
        }
    }

    private void resume() {
        if (isPause) {
            player.start();
            isPause = false;
        }
    }

    private void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
            isPause = true;
        }
    }

    private final class PhoneListener extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            pause();
        }
    }

    protected void onResume() {
        super.onResume();

    }

    public void configuration(View view)
    {
        Bundle bundle=new Bundle();
        Intent intent=new Intent(MainActivity.this,configuration.class);



            intent.putExtra("ID", player.getAudioSessionId());
            startActivity(intent);


    }
    @Override
    protected void onDestroy()
    {if(player.isPlaying())
        player.stop();
        player.release();
        HeadSetHelper.getInstance().close(this);
        super.onDestroy();

    }


    OnHeadSetListener headSetListener = new OnHeadSetListener() {

        @Override
        public void onDoubleClick() {
            next();

        }

        @Override
        public void onClick() {
            Button button=(Button)findViewById(R.id.button3);
            if ("||".equals(button.getText())) {
                pause();
                button.setText("▶");
            } else {
                resume();
                button.setText("||");
            }

        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main,menu);
        return true;
    }

    public void menu(View view)
    {
        fragment=new ListFragment();
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.Layout,fragment);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK&&fragment!=null) {
            FragmentManager manager=getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.remove(fragment);
            fragment=null;
            transaction.commit();
            ((Button)findViewById(R.id.menu)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.configuration)).setVisibility(View.VISIBLE);
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            SharedPreferences preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("current",current);
            if(!onceplay) {
                editor.putInt("playcurrent", playcurrent);
                editor.putInt("premax", premax);
            }
            else {editor.putInt("playcurrent", player.getCurrentPosition());
                editor.putInt("premax", player.getDuration());

            }
            editor.commit();
        }

        return super.onKeyDown(keyCode, event);

    }


    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int current=intent.getIntExtra("current",0);
            setcurrent(current);
            play();
        }
    }
    private String getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = this.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }


}
