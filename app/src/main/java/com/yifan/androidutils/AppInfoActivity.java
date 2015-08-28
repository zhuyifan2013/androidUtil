package com.yifan.androidutils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list_fragment);
        List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
        ListView listView = (ListView) findViewById(R.id.app_list);
        MyAdapter adapter = new MyAdapter(this);
        adapter.setData(packageInfos);
        listView.setAdapter(adapter);
    }

    private class MyAdapter extends BaseAdapter {

        private Context mContext;
        private List<PackageInfo> mPackageInfoList;

        public MyAdapter(Context context) {
            mContext = context;
        }

        public void setData(List<PackageInfo> packageInfos) {
            mPackageInfoList = packageInfos;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mPackageInfoList.size();
        }

        @Override
        public PackageInfo getItem(int position) {
            return mPackageInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                convertView = mInflater.inflate(R.layout.app_list_item, null);
                viewHolder.mAppIcon = (ImageView) convertView.findViewById(R.id.app_icon);
                viewHolder.mAppName = (TextView) convertView.findViewById(R.id.app_name);
                viewHolder.mPackageName = (TextView) convertView.findViewById(R.id.package_name);
                viewHolder.mHash = (TextView) convertView.findViewById(R.id.hash);
                viewHolder.mFirstInstallDate = (TextView) convertView.findViewById(R.id.first_install_date);
                viewHolder.mUpdateDate = (TextView) convertView.findViewById(R.id.update_date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            PackageInfo packageInfo = getItem(position);
            if (packageInfo != null) {
                viewHolder.mAppIcon.setImageDrawable(packageInfo.applicationInfo.loadIcon(mContext.getPackageManager()));
                viewHolder.mAppName.setText(packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
                viewHolder.mPackageName.setText(packageInfo.packageName);
                try {
                    Signature[] signature = mContext.getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_SIGNATURES).signatures;
                    viewHolder.mHash.setText(encodeMD5(String.valueOf(signature[0].toChars())));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(packageInfo.firstInstallTime);
                viewHolder.mFirstInstallDate.setText(calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH));
                calendar.setTimeInMillis(packageInfo.lastUpdateTime);
                viewHolder.mUpdateDate.setText(calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH));
            }
            return convertView;
        }

    }

    private static class ViewHolder {

        ImageView mAppIcon;
        TextView mAppName;
        TextView mPackageName;
        TextView mHash;
        TextView mFirstInstallDate;
        TextView mUpdateDate;

    }

    public static final String encodeMD5(String string) {
        byte[] digest = encodeMD5Bytes(string);
        return digest != null ? byteArrayToString(digest) : null;
    }

    public static final byte[] encodeMD5Bytes(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        digester.update(string.getBytes());
        return digester.digest();
    }

    public static String byteArrayToString(byte[] b) {
        return byteArrayToString(b, false);
    }

    public static String byteArrayToString(byte[] b, boolean upperCase) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;

        return HEX_DIGITS[d1] + HEX_DIGITS[d2];

    }

    private final static String[] HEX_DIGITS = {
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"
    };


}
