package com.example.r.player;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.NoiseSuppressor;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R on 2017/8/12.
 */

public class configuration extends Activity {
    private Switch switch1;
    private Switch switch2;
    private Switch switch3 ;
    private Equalizer equalizerq;
    private BassBoost bassBoost;
    private PresetReverb presetReverb;
    private List<Short> reverbNames=new ArrayList<Short>();
    private List<String> reverbVals=new ArrayList<String>();
    private int audiosessionid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent intent=getIntent();
        audiosessionid=intent.getIntExtra("ID",0);
        final AcousticEchoCanceler canceler=AcousticEchoCanceler.create(intent.getIntExtra("ID",0));
        final AutomaticGainControl automaticGainControl=AutomaticGainControl.create(intent.getIntExtra("ID",0));
        final NoiseSuppressor noiseSuppressor=NoiseSuppressor.create(intent.getIntExtra("ID",0));
        final Toast toast=Toast.makeText(getApplicationContext(),"你的渣机不支持！",Toast.LENGTH_SHORT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        setupEqualizer();
        setupBassBoost();
        setupPresrtReverb();
        switch1=(Switch)findViewById(R.id.switch1);
        switch2=(Switch)findViewById(R.id.switch2);
        switch3=(Switch)findViewById(R.id.switch3);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!canceler.isAvailable())
                    toast.show();
                 else {
                    if (b)
                        canceler.setEnabled(true);
                    else
                        canceler.setEnabled(false);
                }
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!automaticGainControl.isAvailable())
                    toast.show();
                else {
                    if (b)
                        automaticGainControl.setEnabled(true);
                    else
                        automaticGainControl.setEnabled(false);
                }
            }
        });
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!noiseSuppressor.isAvailable())
                    toast.show();
                else {
                    if (b)
                        noiseSuppressor.setEnabled(true);
                    else
                        noiseSuppressor.setEnabled(false);
                }
            }
        });

    }
    private void setupEqualizer()
    {
        equalizerq=new Equalizer(0,audiosessionid);
        equalizerq.setEnabled(true);
        final short minEQLevel=equalizerq.getBandLevelRange()[0];
        short maxEQLevel=equalizerq.getBandLevelRange()[1];
        short brands=equalizerq.getNumberOfBands();
        int a;
        SeekBar seekBar1=(SeekBar)findViewById(R.id.seekBar1);
        SeekBar seekBar2=(SeekBar)findViewById(R.id.seekBar2);
        SeekBar seekBar3=(SeekBar)findViewById(R.id.seekBar3);
        SeekBar seekBar4=(SeekBar)findViewById(R.id.seekBar4);
        SeekBar seekBar5=(SeekBar)findViewById(R.id.seekBar5);
        seekBar1.setMax(maxEQLevel-minEQLevel);
        seekBar2.setMax(maxEQLevel-minEQLevel);
        seekBar3.setMax(maxEQLevel-minEQLevel);
        seekBar4.setMax(maxEQLevel-minEQLevel);
        seekBar5.setMax(maxEQLevel-minEQLevel);
        short i=0;
        seekBar1.setProgress(equalizerq.getBandLevel(i));
        i=1;
        seekBar2.setProgress(equalizerq.getBandLevel(i));
        i=2;
        seekBar3.setProgress(equalizerq.getBandLevel(i));
        i=3;
        seekBar4.setProgress(equalizerq.getBandLevel(i));
        i=4;
        seekBar5.setProgress(equalizerq.getBandLevel(i));

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                equalizerq.setBandLevel((short)0,(short)(progress+minEQLevel));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                equalizerq.setBandLevel((short)1,(short)(progress+minEQLevel));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                equalizerq.setBandLevel((short)2,(short)(progress+minEQLevel));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                equalizerq.setBandLevel((short)3,(short)(progress+minEQLevel));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                equalizerq.setBandLevel((short)4,(short)(progress+minEQLevel));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    private void setupBassBoost()
    {
        bassBoost=new BassBoost(0,audiosessionid);
        bassBoost.setEnabled(true);
        SeekBar seekBar7=(SeekBar) findViewById(R.id.seekBar7);
        seekBar7.setMax(1000);
        seekBar7.setProgress(0);
        seekBar7.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                bassBoost.setStrength((short)progress);
                if(progress==1000)
                {
                    Toast toast=Toast.makeText(getApplicationContext(), "d ^_^ b,The True Music", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
    }
    private void setupPresrtReverb()
    {
        presetReverb=new PresetReverb(0,audiosessionid);
        presetReverb.setEnabled(true);
        for(short i=0;i<equalizerq.getNumberOfBands();i++) {
            reverbNames.add(i);
            reverbVals.add(equalizerq.getPresetName(i));
        }
            Spinner spinner=(Spinner)findViewById(R.id.spinner);
            ArrayAdapter<String>arrayAdapter=new ArrayAdapter<String>(configuration.this, android.R.layout.simple_spinner_item,reverbVals);

          spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               presetReverb.setPreset(reverbNames.get(i));
          }

         @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
       });
        }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        equalizerq.release();
        presetReverb.release();
        bassBoost.release();
    }


    }



