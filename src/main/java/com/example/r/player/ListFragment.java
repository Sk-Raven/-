package com.example.r.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

/**
 * Created by R on 2017/8/14.
 */

public class ListFragment extends android.app.Fragment {
    private ListView listView;
    private List<Map<String, String>> data;
    private int current;
    private MediaPlayer player;
    private myreceiver receiver;
    private MainActivity mainActivity;
    private MyAdapter myAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle avedInstanceState)
    {
      View rootView=inflater.inflate(R.layout.listfragment,container,false);
        rootView.setFocusable(true);
        mainActivity=(MainActivity)getActivity();
        current=mainActivity.getcurrent();
        listView=(ListView)rootView.findViewById(R.id.fraglist);
        data=mainActivity.getdata();
        player=mainActivity.getPlayer();
        myAdapter=new MyAdapter(mainActivity,data,current);
        listView.setAdapter(myAdapter);
        ((Button)mainActivity.findViewById(R.id.menu)).setVisibility(View.GONE);
        ((Button)mainActivity.findViewById(R.id.configuration)).setVisibility(View.GONE);
        listView.setSelection(current);
        IntentFilter filter2=new IntentFilter("next");
        receiver=new myreceiver();
        mainActivity.registerReceiver(receiver,filter2);
        return rootView;
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    public class myreceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            current=mainActivity.getcurrent();
            myAdapter.setCurrent(current);
            listView.setAdapter(myAdapter);
            listView.setSelection(current);

        }
    }




}


