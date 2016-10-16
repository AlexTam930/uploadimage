package com.alextam.uploadimage;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;


import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理工具 (针对Android 6.0 系统)
 * Created by AlexTam on 2016/10/14.
 */
public class PermissionUtil {
    private static PermissionUtil permissionUtil = null;
    private static final String PERMISSIONS_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSIONS_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSIONS_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSIONS_PHONE = Manifest.permission.READ_PHONE_STATE;
    private static final String PERMISSIONS_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    private static final String PERMISSIONS_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSIONS_AUDIO = Manifest.permission.RECORD_AUDIO;


    public static final boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    /**
     *
     * @param activity
     * @param permissionName  such as Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE.
     * @return
     */
    public static final boolean isPermissionValid(Activity activity, String permissionName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, permissionName);
                if (checkCallPhonePermission == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * to find the permissions which were denied in this device.
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static final List<String> findDeniedPermissions(Activity activity, List<String> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return null;
        } else {
            List<String> denyPermissions = new ArrayList<>();

            for (String value : permissions) {
                try {
                    if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                        denyPermissions.add(value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return denyPermissions;
        }
    }

    /**
     * request Permissions.
     * @param activity
     * @param requestCode
     * @param mListPermissions
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static final void requestPermissions(Activity activity, int requestCode, List<String> mListPermissions) {
        if (mListPermissions == null || mListPermissions.size() == 0) {
            return;
        }

        if (!isOverMarshmallow()) {
            // should not be invoked when it is below Android 6.0.
            return;

        } else {
            List<String> deniedPermissionList = findDeniedPermissions(activity, mListPermissions);

            if (deniedPermissionList != null && deniedPermissionList.size() > 0) {
                activity.requestPermissions(deniedPermissionList.toArray(new String[deniedPermissionList.size()]),
                        requestCode);

            }
        }

    }


}
