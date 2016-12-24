package com.example.tiger.hw1;

import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadingActivity extends AppCompatActivity implements View.OnClickListener {

    public final static int DOWNLOADING_TIME=2*5;
    private TextView tvDownloading;
    private Button btnDownload;
    private ProgressBar pbCircular, pbHorizontal;
    private Handler handler;
    private LinearLayout llDownloading;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading);

        tvDownloading= (TextView) findViewById(R.id.tvDownloading);
        btnDownload= (Button) findViewById(R.id.btnDownload);
        pbCircular= (ProgressBar) findViewById(R.id.pbCircular);
        pbHorizontal= (ProgressBar) findViewById(R.id.pbHorizontal);
        llDownloading= (LinearLayout) findViewById(R.id.llDownloading);

//        pbCircular.getIndeterminateDrawable().setColorFilter(0x000000, PorterDuff.Mode.MULTIPLY);

        btnDownload.setOnClickListener(this);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        llDownloading.setVisibility(View.VISIBLE);
                        pbCircular.setVisibility(View.VISIBLE);
                        break;
                    case -1:
                        tvDownloading.setVisibility(View.VISIBLE);
                        pbCircular.setVisibility(View.GONE);
                        break;
                    case -2:
                        tvDownloading.setVisibility(View.GONE);
                        llDownloading.setVisibility(View.GONE);
                        pbHorizontal.setVisibility(View.GONE);
                        btnDownload.setEnabled(true);
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDownload:
                pbHorizontal.setVisibility(View.VISIBLE);
                progress=0;
                btnDownload.setEnabled(false);
                Toast.makeText(this, "Button 'Back' is not working while downloading is not ended", Toast.LENGTH_SHORT).show();

                new Thread(){
                    public void run() {
                        handler.sendEmptyMessage(0);
                        for (int i=0;i<DOWNLOADING_TIME;i++){
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progress+=100/DOWNLOADING_TIME;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pbHorizontal.setProgress(progress);
                                }
                            });
                        }
                        handler.sendEmptyMessageDelayed(-1,(DOWNLOADING_TIME)*500);
                        handler.sendEmptyMessageDelayed(-2,(DOWNLOADING_TIME+2)*500);
                    }
                }.start();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (btnDownload.isEnabled())
            super.onBackPressed();
    }
}
