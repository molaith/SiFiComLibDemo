package com.sificomlib.permission;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Created by Master Fang on 2017/3/9.
 */

public class RequestPermissionFragment extends Fragment {
    private PermissionRequestListener listener;
    private String[] permissions = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout rootView = new RelativeLayout(getActivity());
        rootView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (permissions != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 2);
            }
        }
    }

    public void requestPermission(String[] permissions) {
        this.permissions = permissions;
    }

    public void addPermissionRequestListener(PermissionRequestListener listener) {
        this.listener = listener;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            boolean result = true;
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    result = false;
                    break;
                }
            }
            if (listener != null) {
                if (result) {
                    listener.permissionGranted(this);
                } else {
                    listener.permissionDenied(this);
                }
            }
            getActivity().finish();
        }
    }
}
