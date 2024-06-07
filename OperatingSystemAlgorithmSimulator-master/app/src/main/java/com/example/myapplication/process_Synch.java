package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
class MultiThread extends Thread{
    Semaphore s;
    int i;
    LinearLayout threadLayout;
    TextView threads,semVals;
    ImageView lockImg;
    public void run()
    {
        this.threads.post(() -> threads.setText("W"));
        System.out.println("Thread " + i + " requesting access");
        try {
            s.acquire();
            this.threads.post(() -> threads.setBackgroundColor(Color.GREEN));
            this.semVals.post(() -> {
                process_Synch.semaphore-=1;
                semVals.setText("  S = " + process_Synch.semaphore);
            });
            this.lockImg.post(()->{
                if(process_Synch.semaphore==0) {
                    lockImg.setImageResource(R.drawable.lock);
                }
            });
            Random ran=new Random();
            System.out.println("Thread " + i + " acquired access");
            TimeUnit.SECONDS.sleep(ran.nextInt()%10+5);
        } catch (InterruptedException e) {
            System.out.println("Thread " + i + " waiting");
        }
        s.release();
        this.threads.post(() -> {
            threads.setText("S");
            threads.setBackgroundColor(Color.parseColor("#FFA500"));
        });
        this.lockImg.post(()->{
            if(process_Synch.semaphore==0) {
                lockImg.setImageResource(R.drawable.unlock);
            }
        });
        this.semVals.post(() -> {
            process_Synch.semaphore+=1;
            semVals.setText("  S = " + process_Synch.semaphore);

        });
        System.out.println("Thread " + i + " released access");
    }
    public void setValues(Semaphore sem,int val,LinearLayout threadLay,TextView passedThreads,TextView semView,ImageView img)
    {
        this.s=sem;
        this.i=val;
        this.threadLayout=threadLay;
        this.threads=passedThreads;
        this.semVals=semView;
        this.lockImg=img;
    }
}
public class process_Synch extends AppCompatActivity {
    static int semaphore;
    int threadnum;
    Semaphore s;
    Button submitBtn;
    EditText semval,threadval;
    TextView semView;
    LinearLayout threadLayout;
    ImageView lock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_synch);
        submitBtn=findViewById(R.id.button);
        lock=findViewById(R.id.critSectionImg);
        semval=findViewById(R.id.semValue);
        threadval=findViewById(R.id.threadNum);
        threadLayout=findViewById(R.id.threadLayout);
        semView=findViewById(R.id.sValue);
        submitBtn.setOnClickListener(v -> {
            lock.setImageResource(R.drawable.lock);
            threadLayout.removeAllViews();
            threadnum=Integer.parseInt(threadval.getText().toString());
            semView.setText(String.valueOf(semval));
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(50,ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(50,0,0,0);
            System.out.println("Threads:" + threadnum);
            TextView[] threads=new TextView[threadnum];
            for (int i = 0; i <threadnum; i++) {
                TextView newThread=new TextView(this);
                String tname="T" + i;
                newThread.setText(tname);
                newThread.setBackgroundColor(Color.parseColor("#FFA500"));
                newThread.setLayoutParams(lp);
                newThread.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                threads[i]=newThread;
            }
            try{
                semaphore=Integer.parseInt(semval.getText().toString());
                threadnum=Integer.parseInt(threadval.getText().toString());
                s=new Semaphore(semaphore);
                for (int i = 0; i <threadnum ; i++) {
                    threadLayout.addView(threads[i]);
                    MultiThread inst=new MultiThread();
                    inst.setValues(s,i,threadLayout,threads[i],semView,lock);
                    inst.start();
                }
            }
            catch (Exception e) {
                Toast.makeText(process_Synch.this,"Error:" + e,Toast.LENGTH_LONG ).show();
            }
        });
    }
}
