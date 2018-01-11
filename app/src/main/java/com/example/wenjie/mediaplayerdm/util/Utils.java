package com.example.wenjie.mediaplayerdm.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wenjie.mediaplayerdm.base.MyApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

    private final static String TAG = "utils";
    private static Toast mToast;

    public static final String APP_CHANNEL = "UMENG_CHANNEL";

    public static final List<Activity> mActivityList = new ArrayList<Activity>();


    public static void addToActivityList(Activity activity) {
        mActivityList.add(activity);
    }


    public static void removeFromActivityList(Activity activity) {
        mActivityList.remove(activity);
    }

    public static String getAppChannel() {
        Context context = MyApp.context;
        String value = null;
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo != null) {
            value = appInfo.metaData.getString(APP_CHANNEL);
        }
        return value;
    }

    public static int getVersionCode() {
        Context context = MyApp.context;
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * @return versionName
     */
    public static String getVersionName() {
        try {
            Context context = MyApp.context;
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getPackageName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isCurrWifiAvailable(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }


    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static void setFeatures(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isOnlyMobileNetworkConnect(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (mobileNetworkInfo != null && mobileNetworkInfo.isConnected()) {
                if (mobileNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Bitmap getbitmap(String imageUri) {
        Log.v(TAG, "getbitmap:" + imageUri);
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            Log.v(TAG, "image download finished." + imageUri);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "getbitmap bmp fail---");
            bitmap = null;
        }
        return bitmap;
    }

    public static final void showResultDialog(Context context, String msg, String title) {
        if (msg == null) return;
        String rmsg = msg.replace(",", "\n");
        Log.d("Util", rmsg);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(rmsg)
                .setNegativeButton("ok", null)
                .create()
                .show();
    }


    public static final void toastMessage(final Activity activity, final String message, String logLevel) {
        if ("w".equals(logLevel)) {
            Log.w("sdkDemo", message);
        } else if ("e".equals(logLevel)) {
            Log.e("sdkDemo", message);
        } else {
            Log.d("sdkDemo", message);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mToast != null) {
                    mToast.cancel();
                    mToast = null;
                }
                mToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }


    public static final void toastMessage(final Activity activity, final String message) {
        toastMessage(activity, message, null);
    }


    public static boolean isMobilePhone(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static boolean isPassword(String pwd) {
        if (pwd.length() < 6 || pwd.length() > 18) {
            return false;
        }
        String fuhao = "\\W";
        if (pwd.matches("^\\d+$") || pwd.matches("^[a-zA-Z]+$") || pwd.matches("^[" + fuhao + "]+$")) {
            return false;
        } else if (pwd.matches("^[0-9A-Za-z]+$") || pwd.matches("^[0-9" + fuhao + "]+$") || pwd.matches("^[A-Za-z" + fuhao + "]+$")) {
            return true;
        } else if (pwd.matches("^[0-9A-Za-z" + fuhao + "]+$")) {
            return true;
        } else {
            return false;
        }
    }


    public static String getPercent(int x, int total) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);

        String result = numberFormat.format((float) x / (float) total * 100);
        return result + "%";
    }

    public static int setTextSize(String s) {

        int textSize = 51;
        int length = s.length();
        if (length <= 4) {
            textSize = 50;//dipToPx(45);
        } else if (length > 4 && length <= 6) {
            textSize = 40;//dipToPx(35);
        } else if (length > 6 && length <= 8) {
            textSize = 30;//dipToPx(25);
        } else if (length > 8) {
            textSize = 25;//dipToPx(20);
        }

        return textSize;
    }

    public static int dipToPx(float dip) {
        float density = MyApp.context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    public static int sp2dip(Context context, float spVal) {
        return Utils.px2dip(context, sp2px(context, spVal));
    }

    public static void capturePicture(Fragment fragment, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, "");
        fragment.startActivityForResult(intent, requestCode);
    }

    public static int getStatusBarHeight() {
        Resources res = Resources.getSystem();
        String key = "status_bar_height";
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static final int FEMALE = 0;
    public static final int MALE = 1;
    public static final float DEFAULT_TALL = 1.2f;
    public static final float DEFAULT_WEIGHT = 22f;


    public static String stepToDistance(int sex, float tall, int step) {
        sex = sex == MALE ? MALE : FEMALE;
        tall = isTallLegal(tall) ? tall : DEFAULT_TALL;
        float k = sex == MALE ? 0.415f : 0.413f;
        float price = (k * tall * (float) step) / 400.0f;
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String p = decimalFormat.format(price);
        return p;
    }

    public static float ToDistance(int sex, float tall, int step) {
        sex = sex == MALE ? MALE : FEMALE;
        tall = isTallLegal(tall) ? tall : DEFAULT_TALL;
        float k = sex == MALE ? 0.415f : 0.413f;
        return (k * tall * (float) step) / 400.0f;
    }

    public static String floToStr(float f) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String p = decimalFormat.format(f);
        return p;
    }

    public static int stepToCalories(float tall, float weight, int step) {
        tall = isTallLegal(tall) ? tall : DEFAULT_TALL;
        weight = isWeightLegal(weight) ? weight : DEFAULT_WEIGHT;
        float k = 413 * tall * tall / weight;
        return (int) ((float) step / k);
    }

    public static boolean isTallLegal(float tall) {
        return tall < 0.3 && tall < 2.42;
    }

    public static boolean isWeightLegal(float weight) {
        return weight > 3 && weight < 634;
    }

    public static String m2Km(double meters) {
        DecimalFormat format = new DecimalFormat("0.##");
        return format.format(meters / 1000);
    }

    public static String cal2Kcal(double calories) {
        DecimalFormat format = new DecimalFormat("0.##");
        return format.format(calories / 1000);
    }


    public static String millisTo24Time(long timeInMillis) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        String format = formatter.format(calendar.getTime());
        return format;
    }

    public static int getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;
        return mScreenWidth;
    }

    public static int getWindowHeigh(Context context) {
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenHeigh = dm.heightPixels;
        return mScreenHeigh;
    }

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    private static final double EARTH_RADIUS = 6378137.0;

    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;

    }

    public static void exitAccountCleanActivity() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }

    public static void setCursorLocation(EditText editText) {
        editText.setSelection(editText.getText().length());
    }

    public static boolean AppIsExist(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public static <T> T[] reverseArray(T[] array) {
        for (int start = 0, end = array.length - 1; start < end; start++, end--) {
            T temp = array[end];
            array[end] = array[start];
            array[start] = temp;
        }
        return array;
    }

    public static int getPasswordStrength(String pwd) {
        String fuhao = "\\W";
        if (pwd.matches("^\\d+$") || pwd.matches("^[a-zA-Z]+$") || pwd.matches("^[" + fuhao + "]+$")) {
            return 1;
        } else if (pwd.matches("^[0-9A-Za-z]+$") || pwd.matches("^[0-9" + fuhao + "]+$") || pwd.matches("^[A-Za-z" + fuhao + "]+$")) {
            return 2;
        } else if (pwd.matches("^[0-9A-Za-z" + fuhao + "]+$")) {
            return 3;
        } else {
            return -1;
        }
    }

    //中英文，数字，下划线
    public static boolean isNickname(String str) {
        if (str.matches("[\\u4e00-\\u9fa5_a-zA-Z0-9_]{2,20}")) {
            return true;
        }
        return false;
    }

    /**
     * 对字符串md5加密
     *
     * @param s
     * @return
     */
    public static String getMD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static long getDetalTime(long serverTime) {
        long appCurrentTime = System.currentTimeMillis();
        long detalTime = serverTime - appCurrentTime;
        return detalTime;
    }

    public static long getServerTime(long detalTime) {
        long appTime = System.currentTimeMillis();
        long serverTime = appTime + detalTime;
        return serverTime;
    }


    /**
     * 小数位最多取两位
     *
     * @param num
     * @return 输入：0.343f   返回：0.34
     * 输入：234.0f   返回：234
     * 输入：234.20f  返回：234.2
     */
    public static String getSimpleDecimal(float num) {
        DecimalFormat df = new DecimalFormat("0.##");
        String format = df.format(num);
        return format;
    }

    public static String getTwoDecimalPlaces(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(num);
        return format;
    }

    public static String getOneDecimalPlaces(float num) {
        DecimalFormat df = new DecimalFormat("0.0");
        String format = df.format(num);
        return format;
    }

    public static void hideSoftInputMethod(Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 转化为百分比的字符串
     *
     * @param value              值
     * @param maxValue           最大值
     * @param imumFractionDigits 小数点后几位
     * @return
     */
    public static String changToPercentage(float value, float maxValue, int imumFractionDigits) {
        //0表示的是小数点  之前没有这样配置有问题例如  num=1 and total=1000  结果是.1  很郁闷
        DecimalFormat df = new DecimalFormat("0%");
        //可以设置精确几位小数
        df.setMaximumFractionDigits(imumFractionDigits);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = value * 1.0 / maxValue * 1.0;
        return df.format(accuracy_num);
    }

    // 5.0版本以上
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarUpperAPI21(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    // 4.4 - 5.0版本
    public static void setStatusBarUpperAPI19(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View statusBarView = mContentView.getChildAt(0);

        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        //移除假的 View
        if (statusBarView != null && statusBarView.getLayoutParams() != null &&
                statusBarView.getLayoutParams().height == getStatusBarHeight()) {
            mContentView.removeView(statusBarView);
        }
        //不预留空间
        if (mContentView.getChildAt(0) != null) {
            ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
        }
    }


    /**
     * 获取单个文件的MD5值
     *
     * @param file 文件
     * @return 文件的md5
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16).toUpperCase();
    }

    //获取表的id
    public static String getId() {
        return UUID.randomUUID().toString();
    }

    public static void runOnUIThread(Runnable runnable) {
        MyApp.mainHandler.post(runnable);
    }
}
