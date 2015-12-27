package com.mao.animprogressbar;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mao.anim_pb.AnimProgressBar;

public class MainActivity extends AppCompatActivity {

    private AnimProgressBar mProgressBar;
    private TextView mTextView;
    private int mProgress;
    private boolean mStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnimDialog();
                startAnim();
            }
        });
    }

    private void startAnim() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgress < 100) {
                    if (mStop) {
                        break;
                    }
                    SystemClock.sleep(400);
                    mProgress++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgress);
                            mTextView.setText(String.format("%s%", mProgress));
                        }
                    });
                }
            }
        }).start();
    }

    private void showAnimDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();
        dialog.show();
        mStop = false;
        mProgress = 0;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        dialog.getWindow().setContentView(R.layout.anim_pb_dialog);
        mProgressBar = (AnimProgressBar) dialog.getWindow().findViewById(R.id.progressbar);
        mProgressBar.setMax(100);
        Bitmap bitmap1 = ((BitmapDrawable) getResources().getDrawable(com.mao.anim_pb.R.drawable.pb_anim_pic1)).getBitmap();
        Bitmap bitmap2 = ((BitmapDrawable) getResources().getDrawable(com.mao.anim_pb.R.drawable.pb_anim_pic2)).getBitmap();
        mProgressBar.setBitmapBuffer1(bitmap1);
        mProgressBar.setBitmapBuffer2(bitmap2);
        mTextView = (TextView) dialog.getWindow().findViewById(R.id.tv_progress);
        dialog.getWindow().findViewById(R.id.tv_download_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mStop = true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        mStop = true;
        super.onDestroy();
    }
}
