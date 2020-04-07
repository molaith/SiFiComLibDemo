package com.molaith.sificomlib.permission;


import androidx.fragment.app.Fragment;

/**
 * Created by molaith on 2017/3/14.
 */

public interface PermissionRequestListener {
    void permissionGranted(Fragment view);

    void permissionDenied(Fragment view);
}
