package com.example.wenjie.mediaplayerdm.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.wenjie.mediaplayerdm.BuildConfig;
import com.example.wenjie.mediaplayerdm.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static final int CODE_CAMERA = 0;
    public static final int CODE_READ_EXTERNAL_STORAGE = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_ACCESS_FINE_LOCATION = 3;
    public static final int CODE_ACCESS_COARSE_LOCATION = 4;
    public static final int CODE_MULTI_PERMISSION = 100;
    public static final int CODE_LOCATION_PERMISSION = 101;
    public static final int CODE_PERMISSION_DENIED = -1;


    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;


    private static final String[] requestPermissions = {
            PERMISSION_CAMERA,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION
    };

    private static final String[] cameraAndStoragePermissions = {
            PERMISSION_CAMERA,
            PERMISSION_READ_EXTERNAL_STORAGE
    };

    public interface PermissionGrant {
        void onPermissionGranted(int requestCode);
    }

    /**
     * Requests permission.
     *
     * @param activity
     * @param requestCode request code, e.g. if you need request CAMERA permission,parameters is PermissionUtils.CODE_CAMERA
     */
    public static void requestPermission(final Activity activity, final int requestCode, PermissionGrant permissionGrant) {
        if (activity == null) {
            return;
        }

        Log.i(TAG, "requestPermission requestCode:" + requestCode);
        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            Log.w(TAG, "requestPermission illegal requestCode:" + requestCode);
            return;
        }

        final String requestPermission = requestPermissions[requestCode];


        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
        } catch (RuntimeException e) {
            Log.e(TAG, "RuntimeException:" + e);
            return;
        }

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED");


            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                Log.i(TAG, "requestPermission shouldShowRequestPermissionRationale");
                shouldShowRationale(activity, requestCode, requestPermission);

            } else {
                Log.d(TAG, "requestCameraPermission else");
                ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);
            }

        } else {
            Log.d(TAG, "ActivityCompat.checkSelfPermission ==== PackageManager.PERMISSION_GRANTED");
            permissionGrant.onPermissionGranted(requestCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestPermission(final Fragment fragment, final int requestCode, PermissionGrant permissionGrant) {
        if (fragment == null) {
            return;
        }

        Log.i(TAG, "requestPermission requestCode:" + requestCode);
        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            Log.w(TAG, "requestPermission illegal requestCode:" + requestCode);
            return;
        }

        final String requestPermission = requestPermissions[requestCode];


        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(fragment.getActivity(), requestPermission);
        } catch (RuntimeException e) {
            Toast.makeText(fragment.getActivity(), "please open this permission", Toast.LENGTH_SHORT)
                    .show();
            Log.e(TAG, "RuntimeException:" + e);
            return;
        }

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED");


            if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), requestPermission)) {
                Log.i(TAG, "requestPermission shouldShowRequestPermissionRationale");
                showMessageOKNoCancel(fragment.getActivity(), R.string.need_permission, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.requestPermissions(
                                new String[]{requestPermission},
                                requestCode);
                        Log.d(TAG, "showMessageOKNoCancel requestPermissions:" + requestPermission);
                    }
                });

            } else {
                Log.d(TAG, "requestCameraPermission else");
                fragment.requestPermissions(new String[]{requestPermission}, requestCode);
            }

        } else {
            Log.d(TAG, "ActivityCompat.checkSelfPermission ==== PackageManager.PERMISSION_GRANTED");
            Toast.makeText(fragment.getActivity(), "opened:" + requestPermissions[requestCode], Toast.LENGTH_SHORT).show();
            permissionGrant.onPermissionGranted(requestCode);
        }
    }

    private static void requestMultiResult(final Activity activity, String[] permissions, int[] grantResults,
                                           final PermissionGrant permissionGrant) {

        if (activity == null) {
            return;
        }

        Log.d(TAG, "onRequestPermissionsResult permissions length:" + permissions.length);
        Map<String, Integer> perms = new HashMap<>();

        ArrayList<String> notGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            Log.d(TAG, "permissions: [i]:" + i + ", permissions[i]" + permissions[i] + ",grantResults[i]:" + grantResults[i]);
            perms.put(permissions[i], grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permissions[i]);
            }
        }

        if (notGranted.isEmpty()) {
            permissionGrant.onPermissionGranted(CODE_MULTI_PERMISSION);
        } else {
            openCustomSettingActivity(activity, permissionGrant, CODE_MULTI_PERMISSION);
        }

    }


    public static void requestMultiPermissions(final Activity activity, PermissionGrant grant) {

        final List<String> permissionsList = getNoGrantedPermission(activity, false);
        final List<String> shouldRationalePermissionsList = getNoGrantedPermission(activity, true);

        if (permissionsList == null || shouldRationalePermissionsList == null) {
            return;
        }
        Log.d(TAG, "requestMultiPermissions permissionsList:" + permissionsList.size() + ",shouldRationalePermissionsList:" + shouldRationalePermissionsList.size());

        if (!permissionsList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                    CODE_MULTI_PERMISSION);
            Log.d(TAG, "showMessageOKNoCancel requestPermissions");

        } else if (!shouldRationalePermissionsList.isEmpty()) {

            ActivityCompat.requestPermissions(activity, shouldRationalePermissionsList.toArray(new String[shouldRationalePermissionsList.size()]),
                    CODE_MULTI_PERMISSION);
            Log.d(TAG, "showMessageOKNoCancel requestPermissions");
        } else {
            grant.onPermissionGranted(CODE_MULTI_PERMISSION);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestCameraAndStoragePermissions(final Fragment fragment, PermissionGrant grant) {

        final List<String> permissionsList = getNoGrantedPermissionForCS(fragment.getActivity(), false);
        final List<String> shouldRationalePermissionsList = getNoGrantedPermissionForCS(fragment.getActivity(), true);

        if (permissionsList == null || shouldRationalePermissionsList == null) {
            return;
        }
        Log.d(TAG, "requestMultiPermissions permissionsList:" + permissionsList.size() + ",shouldRationalePermissionsList:" + shouldRationalePermissionsList.size());

        if (!permissionsList.isEmpty()) {
            fragment.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    CODE_MULTI_PERMISSION);
            Log.d(TAG, "showMessageOKNoCancel requestPermissions");

        } else if (!shouldRationalePermissionsList.isEmpty()) {

            fragment.requestPermissions(shouldRationalePermissionsList.toArray(new String[shouldRationalePermissionsList.size()]),
                    CODE_MULTI_PERMISSION);
            Log.d(TAG, "showMessageOKNoCancel requestPermissions");
        } else {
            grant.onPermissionGranted(CODE_MULTI_PERMISSION);
        }

    }

    public static void requestCameraAndStoragePermissions(final Activity activity, PermissionGrant grant) {

        final List<String> permissionsList = getNoGrantedPermissionForCS(activity, false);
        final List<String> shouldRationalePermissionsList = getNoGrantedPermissionForCS(activity, true);

        if (permissionsList == null || shouldRationalePermissionsList == null) {
            return;
        }
        Log.d(TAG, "requestMultiPermissions permissionsList:" + permissionsList.size() + ",shouldRationalePermissionsList:" + shouldRationalePermissionsList.size());

        if (!permissionsList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                    CODE_MULTI_PERMISSION);
            Log.d(TAG, "showMessageOKNoCancel requestPermissions");

        } else if (!shouldRationalePermissionsList.isEmpty()) {

            ActivityCompat.requestPermissions(activity, shouldRationalePermissionsList.toArray(new String[shouldRationalePermissionsList.size()]),
                    CODE_MULTI_PERMISSION);
            Log.d(TAG, "showMessageOKNoCancel requestPermissions");
        } else {
            grant.onPermissionGranted(CODE_MULTI_PERMISSION);
        }

    }

    public static void requestLocationPermissions(final Activity activity) {
        if (activity == null) {
            return;
        }
        ArrayList<String> permissions = new ArrayList<>();
        if(ActivityCompat.checkSelfPermission(activity,PERMISSION_ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissions.add(PERMISSION_ACCESS_FINE_LOCATION);
        }
        if(ActivityCompat.checkSelfPermission(activity,PERMISSION_ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissions.add(PERMISSION_ACCESS_COARSE_LOCATION);
        }
        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(activity,permissions.toArray(new String[permissions.size()]), CODE_LOCATION_PERMISSION);
        }
    }

    public static void requestLocationResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults, PermissionGrant permissionGrant) {
        if (activity == null) {
            return;
        }
        if (requestCode == CODE_LOCATION_PERMISSION) {
            Map<String, Integer> perms = new HashMap<>(2);
            ArrayList<String> notGranted = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                Log.d(TAG, "permissions: [i]:" + i + ", permissions[i]" + permissions[i] + ",grantResults[i]:" + grantResults[i]);
                perms.put(permissions[i], grantResults[i]);
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    notGranted.add(permissions[i]);
                }
            }

            if (notGranted.isEmpty()) {
                permissionGrant.onPermissionGranted(CODE_LOCATION_PERMISSION);
            } else {
                openCustomSettingActivity(activity, permissionGrant, requestCode);
            }
        }
    }

    public static boolean checkLocationPermissions(Activity activity) {
        if(Build.VERSION.SDK_INT >= 23) {
            if(ActivityCompat.checkSelfPermission(activity,PERMISSION_ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
            if(ActivityCompat.checkSelfPermission(activity,PERMISSION_ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
            return true;
        } else {
            return true;
        }

    }

    private static void shouldShowRationale(final Activity activity, final int requestCode, final String requestPermission) {
        showMessageOKNoCancel(activity, R.string.need_permission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{requestPermission},
                        requestCode);
                Log.d(TAG, "showMessageOKNoCancel requestPermissions:" + requestPermission);
            }
        });
    }

    private static void showMessageOKNoCancel(final Activity context, int message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, okListener)
                .setCancelable(false)
                .create()
                .show();

    }

    /**
     * @param activity
     * @param requestCode  Need consistent with requestPermission
     * @param permissions
     * @param grantResults
     */
    public static void requestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                                @NonNull int[] grantResults, PermissionGrant permissionGrant) {

        if (activity == null) {
            return;
        }
        Log.d(TAG, "requestPermissionsResult requestCode:" + requestCode);

        if (requestCode == CODE_MULTI_PERMISSION) {
            requestMultiResult(activity, permissions, grantResults, permissionGrant);
            return;
        }
        if(requestCode == CODE_LOCATION_PERMISSION){
            requestLocationResult(activity,requestCode,permissions,grantResults,permissionGrant);
            return;
        }

        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            Log.w(TAG, "requestPermissionsResult illegal requestCode:" + requestCode);
            return;
        }

        Log.i(TAG, "onRequestPermissionsResult requestCode:" + requestCode + ",permissions:" + Arrays.toString(permissions)
                + ",grantResults:" + Arrays.toString(grantResults) + ",length:" + grantResults.length);

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult PERMISSION_GRANTED");
            if(permissionGrant != null){
                permissionGrant.onPermissionGranted(requestCode);
            }
        } else {
            Log.i(TAG, "onRequestPermissionsResult PERMISSION NOT GRANTED");
//            openSettingActivity(activity, R.string.please_grant_app_permission,requestCode);
            openCustomSettingActivity(activity, permissionGrant, requestCode);
        }

    }

    private static void openSettingActivity(final Activity activity, int message, final int requestCode) {

        showMessageOKNoCancel(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Log.d(TAG, "getPackageName(): " + activity.getPackageName());
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivityForResult(intent,requestCode);
//                activity.finish();
            }
        });
    }

    /**
     * @param activity
     * @param isShouldRationale true: return no granted and shouldShowRequestPermissionRationale permissions, false:return no granted and !shouldShowRequestPermissionRationale
     * @return
     */
    public static List<String> getNoGrantedPermissionForCS(Activity activity, boolean isShouldRationale) {

        ArrayList<String> permissions = new ArrayList<>();

        for (int i = 0; i < cameraAndStoragePermissions.length; i++) {
            String requestPermission = cameraAndStoragePermissions[i];


            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
            } catch (RuntimeException e) {
                Toast.makeText(activity, "please open those permission", Toast.LENGTH_SHORT)
                        .show();
                Log.e(TAG, "RuntimeException:" + e);
                return null;
            }

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "getNoGrantedPermission ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED:" + requestPermission);

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                    Log.d(TAG, "shouldShowRequestPermissionRationale if");
                    if (isShouldRationale) {
                        permissions.add(requestPermission);
                    }

                } else {

                    if (!isShouldRationale) {
                        permissions.add(requestPermission);
                    }
                    Log.d(TAG, "shouldShowRequestPermissionRationale else");
                }

            }
        }

        return permissions;
    }

    /**
     * @param activity
     * @param isShouldRationale true: return no granted and shouldShowRequestPermissionRationale permissions, false:return no granted and !shouldShowRequestPermissionRationale
     * @return
     */
    public static List<String> getNoGrantedPermission(Activity activity, boolean isShouldRationale) {

        ArrayList<String> permissions = new ArrayList<>();

        for (int i = 0; i < requestPermissions.length; i++) {
            String requestPermission = requestPermissions[i];


            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
            } catch (RuntimeException e) {
                Toast.makeText(activity, "please open those permission", Toast.LENGTH_SHORT)
                        .show();
                Log.e(TAG, "RuntimeException:" + e);
                return null;
            }

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "getNoGrantedPermission ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED:" + requestPermission);

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                    Log.d(TAG, "shouldShowRequestPermissionRationale if");
                    if (isShouldRationale) {
                        permissions.add(requestPermission);
                    }

                } else {

                    if (!isShouldRationale) {
                        permissions.add(requestPermission);
                    }
                    Log.d(TAG, "shouldShowRequestPermissionRationale else");
                }

            }
        }

        return permissions;
    }

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if(grantResults.length < 1){
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPermissionGranted(Context context, int requestCode) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        Log.i(TAG, "requestPermission requestCode:" + requestCode);
        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            Log.w(TAG, "requestPermission illegal requestCode:" + requestCode);
            return result;
        }
        final String requestPermission = requestPermissions[requestCode];
        int checkSelfPermission = PackageManager.PERMISSION_DENIED;
        try {
            checkSelfPermission = ContextCompat.checkSelfPermission(context, requestPermission);
        } catch (RuntimeException e) {
            Log.e(TAG, "RuntimeException:" + e);
        }
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            result = true;
        }
        return result;
    }

    /**
     * 打开国内手机厂商自定义的权限管理界面
     */
    public static void openCustomSettingActivity(final Activity activity, final PermissionGrant permissionGrant, final int requestCode) {


    }

    /**
     * miui的权限管理页面
     */
    private static void gotoMiuiPermission(Activity activity, int requestCode) {
        Intent i = new Intent();
        i.setAction("miui.intent.action.APP_PERM_EDITOR");
        i.addCategory("android.intent.category.DEFAULT");
        i.putExtra("extra_pkgname", activity.getPackageName());
        try {
            activity.startActivityForResult(i, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            gotoMeizuPermission(activity, requestCode);
        }
    }

    /**
     * 魅族的权限管理系统
     */
    private static void gotoMeizuPermission(Activity activity, int requestCode) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            gotoHuaweiPermission(activity, requestCode);
        }
    }

    /**
     * 华为的权限管理页面
     */
    private static void gotoHuaweiPermission(Activity activity, int requestCode) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            gotoDefaultSettingIntent(activity, requestCode);
        }
    }

    /**
     * 获取应用详情页面intent
     */
    private static void gotoDefaultSettingIntent(Activity activity, int requestCode) {
        Intent localIntent = new Intent();
        localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivityForResult(localIntent, requestCode);
    }
}
