package com.example.r.player;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by R on 2017/8/13.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    public List<Boolean> flag;
    public HashMap<String, Object> map;
    private List<String> songname;
    public int current;
    public MyAdapter(Context context, List<Map<String,String>> data,int current) {
        this.context = context;
        songname = new ArrayList<String>();
        flag=new ArrayList<Boolean>();
        map = new HashMap<String, Object>();
        for (int i = 0; i < data.size(); i++) {
            songname.add(data.get(i).get("name"));
            flag.add(false);
        }
        this.current=current;
        flag.set(current,true);
    }



    @Override
    public int getCount() {
        return songname.size();
    }

    @Override
    public Object getItem(int position) {
        return songname.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
            holder.tv=(TextView)convertView.findViewById(R.id.mName);
                    convertView.setTag(holder);
        }
        else {


            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(songname.get(position));
        map.put("" + position, holder.tv);
        if(!flag.get(position))
            holder.tv.setTextColor(context.getResources().getColor(R.color.black));
        else
            holder.tv.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBackGround();
                ((TextView) (map.get("" + position))).setTextColor(context.getResources().getColor(R.color.colorPrimary));
                flag.set(position,true);
                Intent intent=new Intent();
                intent.setAction("play");
                intent.putExtra("current",position);
                context.sendBroadcast(intent);
            }
            public void resetBackGround() {
                for (int i = 0; i < map.size(); i++) {
                    ((TextView)(map.get("" + i))).setTextColor(context.getResources().getColor(R.color.black));
                    flag.set(i,false);
                }
            }
        });

        return convertView;
    }
    public void setCurrent(int x)
    {current=x;
        for (int i = 0; i < map.size(); i++) {
            ((TextView) (map.get("" + i))).setTextColor(context.getResources().getColor(R.color.black));
            flag.set(i, false);
        }
        ((TextView) (map.get("" + x))).setTextColor(context.getResources().getColor(R.color.colorPrimary));
        flag.set(x,true);
    }

    private final class ViewHolder {
        TextView tv;
    }
}

