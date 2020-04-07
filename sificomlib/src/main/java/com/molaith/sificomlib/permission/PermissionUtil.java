package com.molaith.sificomlib.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by molaith on 2017/4/11.
 */

public class PermissionUtil {
    private static final int OVERLAY_PERMISSION_REQ_CODE = 0x012;
    public static final int OTHER_PERMISSION_REQ_CODE = 0x013;

    /**
     * 请求权限
     *
     * @param context
     * @param permissions
     * @param listener
     */
    public static void requestPermissions(final Context context, String[] permissions, PermissionRequestListener listener) {
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((Activity) context).requestPermissions(permissions, OTHER_PERMISSION_REQ_CODE);
            }
        } else {
            RequestPermissionFragment fragment = new RequestPermissionFragment();
            fragment.requestPermission(permissions);
            fragment.addPermissionRequestListener(listener);
            FragementAttachActivity.attachLaunch(context, fragment);
        }
    }

    /**
     * 检测权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean checkPermissions(Context context, String[] permissions) {
        List<String> grantedPermissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions.add(permission);
                }
            }
        } else {
            return true;
        }
        return grantedPermissions.size() == permissions.length;
    }

    /**
     * 检测权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkFloatWindowPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }


    /**
     * Intention please: onActivityResult() must be implimented
     *
     * @param context must be Activity
     * @return requestCode
     */
    public static int requestFloatWindowPermissionForResult(Activity context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
        context.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        return OVERLAY_PERMISSION_REQ_CODE;
    }
}
