package com.yifan.androidutils;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    //系统描述的信息
    private final String KEY_BUILD_DESCRIPTION = "ro.build.fingerprint";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ((TextView) findViewById(R.id.system_version_content)).setText(Build.VERSION.RELEASE);
        ((TextView) findViewById(R.id.imei_content)).setText(
                ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId());
        ((TextView) findViewById(R.id.system_description_content)).setText(SystemProperties.get(KEY_BUILD_DESCRIPTION));

        boolean deviceSupportNfc;
        boolean nfcIsEnable = false;
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        deviceSupportNfc = (adapter != null);
        if (deviceSupportNfc) {
            nfcIsEnable = (adapter.isEnabled());
        }
        ((TextView) findViewById(R.id.nfc_status_content)).setText(getString(R.string.nfc_status_text, deviceSupportNfc, nfcIsEnable));

        Button appBtn = (Button) findViewById(R.id.app_info);
        appBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AppInfoActivity.class));
            }
        });

        Button seBtn = (Button) findViewById(R.id.se_info);
        seBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SeInfoActivity.class));
            }
        });
    }

}
