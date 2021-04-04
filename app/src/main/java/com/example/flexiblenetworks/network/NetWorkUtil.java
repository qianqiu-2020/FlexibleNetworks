package com.example.flexiblenetworks.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.example.flexiblenetworks.MyApplication;

import java.util.List;

public class NetWorkUtil {

    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Gps是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = locationManager.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }


    /**
     * 判断当前网络是否是wifi网络
     * if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) {
     *
     * @param context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前网络是否移动网络
     *
     * @param context
     * @return boolean
     */
    public static boolean isMobileNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }
    /**
     * 获取运营商
     *
     * @return 中国移动/中国联通/中国电信/未知
     */
    public static String getProvider(Context context) {
        String  opeType = "unknown";
        try {

            // No sim
            if (!hasSim(MyApplication.getContext())) {
                return opeType;
            }

            TelephonyManager tm = (TelephonyManager)MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String operator = tm.getSimOperator();
            if ("46001".equals(operator) || "46006".equals(operator) || "46009".equals(operator)) {
                opeType = "中国联通";
            } else if ("46000".equals(operator) || "46002".equals(operator) || "46004".equals(operator) || "46007".equals(operator)) {
                opeType = "中国移动";

            } else if ("46003".equals(operator) || "46005".equals(operator) || "46011".equals(operator)) {
                opeType = "中国电信";
            } else {
                opeType = "unknown";
            }
            return opeType;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return opeType;
    }
    private static boolean hasSim(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm.getSimOperator();
        if (TextUtils.isEmpty(operator)) {
            return false;
        }
        return true;
    }

    /**
     * 获取连接网络类型(3G/4G/wifi,不包含运营商信息)
     *
     * @param context
     * @return 返回结果中,不包含运营商,返回连接网络类型(3G/4G/wifi),如果网络未连接,返回"";
     */
    private static String getNetworkTypeNoProvider(Context context) {
        String strNetworkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "wifi";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
//                Log.d(TAG, "Network getSubtypeName : " + _strSubTypeName);
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2G
                    case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2G
                    case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2G
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3G
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") ||
                                _strSubTypeName.equalsIgnoreCase("WCDMA") ||
                                _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
//                Log.d(TAG, "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }
//        Log.d(TAG, "Network Type : " + strNetworkType);
        return strNetworkType;
    }
    /**
     * 获取连接网络类型(3G/4G/wifi,包含运营商信息)
     *
     * @param context
     * @return 返回连接网络类型(运营商3G/4G/wifi),如果网络未连接,返回"";
     */
    public static String getNetworkType(Context context) {
        String networkType = "";
        networkType = getNetworkTypeNoProvider(context);
// 如果使用的数据流量,则添加运营商信息
        if (networkType.contains("G")) {
            networkType = getProvider(context) + networkType;
        }
        return networkType;
    }

}