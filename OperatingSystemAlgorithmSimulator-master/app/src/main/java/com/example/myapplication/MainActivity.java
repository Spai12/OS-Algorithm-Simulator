package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button gotoDeadDetect =  findViewById(R.id.deadDetect);
        gotoDeadDetect.setOnClickListener(view -> {
            Intent i= new Intent(MainActivity.this,bankers.class);
            startActivity(i);
        });
        Button gotoProcSched =  findViewById(R.id.procSched);
        gotoProcSched.setOnClickListener(view -> {
            Intent j= new Intent(MainActivity.this,process_Sched.class);
            startActivity(j);
        });
        Button gotoPaging= findViewById(R.id.paging);
        gotoPaging.setOnClickListener(v -> {
            Intent k= new Intent(MainActivity.this,paging.class);
            startActivity(k);
        });

        Button gotoDisk= findViewById(R.id.diskSched);
        gotoDisk.setOnClickListener(v -> {
            Intent k= new Intent(MainActivity.this,diskschedule.class);
            startActivity(k);
        });
        Button gotoSynch= findViewById(R.id.procSynch);
        gotoSynch.setOnClickListener(v -> {
            Intent k= new Intent(MainActivity.this,process_Synch.class);
            startActivity(k);
        });
        Button gotoMemAlloc= findViewById(R.id.memAlloc);
        gotoMemAlloc.setOnClickListener(v -> {
            Intent k= new Intent(MainActivity.this,memoryAllocation.class);
            startActivity(k);
        });
    }
}