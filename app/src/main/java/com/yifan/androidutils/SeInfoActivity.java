package com.yifan.androidutils;

import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.miui.tsmclientsdk.MiTsmFuture;
import com.miui.tsmclientsdk.MiTsmManager;
import com.miui.tsmclientsdk.OperationCanceledException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SeInfoActivity extends AppCompatActivity {

    public final String KEY_CPLC_DATA = "key_cplc_data";
    private String mCplc, mSeid;

    private TextView mCplcTv;

    private MiTsmManager mMiTsmManager;

    private final int MSG_GET_CPLC_FINISHED = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_CPLC_FINISHED:
                    mCplc = ((Bundle) msg.obj).getString(KEY_CPLC_DATA);
                    mSeid = mCplc.substring(24, 36);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(mCplc);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(0xff58b2dc), 24, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mCplcTv.setText(spannableStringBuilder);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.se_info_layout);
        mMiTsmManager = MiTsmManager.getInstance();
        Button getCplc = (Button) findViewById(R.id.get_cplc);
        getCplc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MiTsmFuture<Bundle> future = mMiTsmManager.getCPLC(SeInfoActivity.this);
                        Bundle bundle;
                        try {
                            bundle = future.getResult();
                            mHandler.obtainMessage(MSG_GET_CPLC_FINISHED, bundle).sendToTarget();
                        } catch (OperationCanceledException | IOException | ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });

        mCplcTv = (TextView) findViewById(R.id.cplc_content);

        Button cplcCopyBtn = (Button) findViewById(R.id.copy_cplc);
        cplcCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clip = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clip.setText(mCplc);
                Toast.makeText(SeInfoActivity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
            }
        });

        Button seidCopyBtn = (Button) findViewById(R.id.copy_seid);
        seidCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clip = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clip.setText(mSeid);
                Toast.makeText(SeInfoActivity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
