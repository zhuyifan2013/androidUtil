package com.yifan.androidutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yifan.androidutils.util.StringUtil;

import java.util.Calendar;
import java.util.List;

public class AppInfoFragment extends Fragment {

    private final static String SCHEME_PACKAGE = "package";

    private RecyclerView mAppRecyclerView;
    private AppListAdapter mAppListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAppRecyclerView = (RecyclerView) view.findViewById(R.id.app_list);
        mAppListAdapter = new AppListAdapter(getActivity());
        mAppRecyclerView.setAdapter(mAppListAdapter);
        mAppRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<PackageInfo> packageInfos = getActivity().getPackageManager().getInstalledPackages(0);
        mAppListAdapter.setData(packageInfos);
    }

    private class AppListAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {

        private Context mContext;
        private List<PackageInfo> mPackageInfoList;

        public AppListAdapter(Context context) {
            mContext = context;
        }

        public void setData(List<PackageInfo> packageInfos) {
            mPackageInfoList = packageInfos;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                    .app_list_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            final PackageInfo packageInfo = mPackageInfoList.get(i);
            viewHolder.mAppInfoLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts(SCHEME_PACKAGE, packageInfo.packageName, null);
                    intent.setData(uri);
                    startActivity(intent);
                    return true;
                }
            });
            viewHolder.mAppIcon.setImageDrawable(packageInfo.applicationInfo.loadIcon(mContext.getPackageManager()));
            viewHolder.mAppName.setText(packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
            viewHolder.mVersionCode.setText(Integer.toString(packageInfo.versionCode));
            viewHolder.mPackageName.setText(packageInfo.packageName);
            try {
                Signature[] signature = mContext.getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_SIGNATURES).signatures;
                viewHolder.mHash.setText(StringUtil.encodeMD5(String.valueOf(signature[0].toChars())));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(packageInfo.firstInstallTime);
            viewHolder.mFirstInstallDate.setText(calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH));
            calendar.setTimeInMillis(packageInfo.lastUpdateTime);
            viewHolder.mUpdateDate.setText(calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mPackageInfoList.size();
        }

        @Override
        public void onClick(View v) {

        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mAppIcon;
        TextView mAppName;
        TextView mPackageName;
        TextView mHash;
        TextView mFirstInstallDate;
        TextView mUpdateDate;
        TextView mVersionCode;
        LinearLayout mAppInfoLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mAppIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            mAppName = (TextView) itemView.findViewById(R.id.app_name);
            mPackageName = (TextView) itemView.findViewById(R.id.package_name);
            mHash = (TextView) itemView.findViewById(R.id.hash);
            mFirstInstallDate = (TextView) itemView.findViewById(R.id.first_install_date);
            mUpdateDate = (TextView) itemView.findViewById(R.id.update_date);
            mVersionCode = (TextView) itemView.findViewById(R.id.version_code);
            mAppInfoLayout = (LinearLayout) itemView.findViewById(R.id.app_info_layout);
        }
    }
}
