package com.tugou.jgl.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tugou.jgl.activity.JuGeiLiActivity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

public class UploadCellIdThread extends HandlerThread {
    
    private static final boolean DEBUG_WIFI_LOCATION = false;

    public static final int NETWORK_AND_GPS_DISABLE = 1000;
    public static final int LOCATION_FAILED = 1001;
    
    public interface UpdateLocationListener {
        void onSuccessUpdateLocation();
        void onFailedUpdateLocation(int reason);
    }

    private Context mContext;
    //private UserDataController userData = new UserDataController();
    private static final long lSleepTime = 3 * 60 * 1000;
    private LocationManager mLocationManager;
    private UpdateLocationListener mUpdateLocationListener;
    
    private Location mCurrentLocation;
    
    private static UploadCellIdThread gUploadCellIdThread;
    
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        
        @Override
        public void onProviderEnabled(String s) {
        }
        
        @Override
        public void onProviderDisabled(String s) {
        }
        
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double latitude = (int) location.getLatitude();
                double longitude = (int) location.getLongitude();
                Debug.LOGD("location: " + "position : lat = " + location.getLatitude() + " lon = "
                        + location.getLongitude() + " >>>>><<<<<<<<");
                mCurrentLocation = location;

                mHandler.sendEmptyMessageDelayed(LOCATE_ROUND_FINISH, 200);
            }
        }
    };
    
    private static final int UPDATE_LOCATION = 1;
    private static final int LOCATE_ROUND_FINISH = 2;
    private static final int LOCATE_FAILED = 3;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case UPDATE_LOCATION:
                locateByNetworkAndGPS();
//                updateLastPositionNow();
//                mHandler.sendEmptyMessageDelayed(UPDATE_LOCATION, lSleepTime);
                break;
            case LOCATE_ROUND_FINISH:
                if (mCurrentLocation != null) {
//                    double latitude = mCurrentLocation.getLatitude();
//                    double longitude = mCurrentLocation.getLongitude();
//                    userData.uploadLocationInGPS(longitude, latitude, Criteria.ACCURACY_COARSE, null);
                    JuGeiLiActivity.locationData.longitude = mCurrentLocation.getLongitude();
                    JuGeiLiActivity.locationData.latitude = mCurrentLocation.getLatitude();
                    // userData.uploadLocationInGPS(116.52007752499999,
                    // 39.794562549999995, Criteria.ACCURACY_COARSE, null);
                    if (mLocationManager != null) {
                        mLocationManager.removeUpdates(mLocationListener);
                    }
                    mCurrentLocation = null;
                    if (mUpdateLocationListener != null) {
                        mUpdateLocationListener.onSuccessUpdateLocation();
                    }
                }
                mHandler.removeMessages(UPDATE_LOCATION);
                mHandler.sendEmptyMessageDelayed(UPDATE_LOCATION, lSleepTime);

                break;
            case LOCATE_FAILED:
                if (mUpdateLocationListener != null) {
                    mUpdateLocationListener.onFailedUpdateLocation(LOCATION_FAILED);
                }
                if (mLocationManager != null) {
                    mLocationManager.removeUpdates(mLocationListener);
                }
                mHandler.removeMessages(UPDATE_LOCATION);
                mHandler.removeMessages(LOCATE_ROUND_FINISH);
                mCurrentLocation = null;
                break;
            }
        }
    };

    public static UploadCellIdThread getInstance(Context context) {
        if (gUploadCellIdThread == null) {
            gUploadCellIdThread = new UploadCellIdThread(context);
        }

        return gUploadCellIdThread;
    }

    private UploadCellIdThread(Context context) {
        super("HandlerThread");
        this.mContext = context;
    }

    public void setUpdateLocationListener(UpdateLocationListener l) {
        mUpdateLocationListener = l;
    }

    /**
     * stop the thread for update location
     */
    public void stopRunning() {
        stopLocationPosition();
        gUploadCellIdThread = null;
        this.quit();
    }

    /*
     * start the thread loop, but not send the handle message
     */
    public void startThread() {
        if (!this.isAlive()) {
            Debug.LOGD(this.getClass().getName(), "[[startThread]]  start HandlerThread ::::::");
            start();
        }
    }

    public void onLooperPrepared() {
    }

    /**
     * Get the location and update to server now, first use cellID, 
     * second use network and gps.
     * @return true if get location success, false not get.
     */
    public boolean updateLastPositionNow() {
        // if (mLocationManager == null) {
        // mLocationManager = (LocationManager)
        // mContext.getSystemService(Context.LOCATION_SERVICE);
        // }
        //
        // Location lastLocation =
        // mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        // Location lastGpsLocation =
        // mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //
        // mCurrentLocation = lastLocation != null ? lastLocation :
        // lastGpsLocation;
        //
        // if (mCurrentLocation != null) {
        // Debug.LOGD(this.getClass().getName(),
        // "[[updateLastPositionNow]] update my last location =====");
        //
        // double latitude = mCurrentLocation.getLatitude();
        // double longitude = mCurrentLocation.getLongitude();
        // userData.uploadLocationInGPS(longitude, latitude,
        // Criteria.ACCURACY_COARSE, null);
        //
        // return true;
        // }

        // now use cell id for location for first
        if (DEBUG_WIFI_LOCATION || !getLocationByCell()) {
//            getWifiLocation();
            return locateByNetworkAndGPS();
        } else {
            if (mUpdateLocationListener != null) {
                mUpdateLocationListener.onSuccessUpdateLocation();
            }
        }
        
        return true;
    }

    /*
     * send a handle message for update location
     */
    public void startLocationPosition() {
        mHandler.sendEmptyMessageDelayed(UPDATE_LOCATION, 2 * 1000);
    }

    
    private void stopLocationPosition() {
        mHandler.removeMessages(UPDATE_LOCATION);
        mHandler.removeMessages(LOCATE_ROUND_FINISH);
    }

    private void getWifiLocation() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (wm.isWifiEnabled() && wm.getConnectionInfo() != null) {
            wifiLocation(tm);
        }
    }
    
    private void wifiLocation(TelephonyManager tm) {
        Debug.LOGD(this.getClass().getName(), "[[wifiLocation]] use wifi for location <><><<<><><");
        WifiInfoManager wifiManager = new WifiInfoManager(mContext);
        JSONArray jArray = wifiManager.wifiTowers();
        String type = null;
        if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            type = "gsm";
        } else if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            type = "cdma";
        }
        Debug.LOGD(this.getClass().getName(), "[[wifiLocation]] jArray = " + jArray);
        String googleJsonData = null;
        try {
            JSONObject holder = new JSONObject();
            holder.put("version", "1.0.0");
            holder.put("host", "maps.google.com");
            holder.put("request_address", true);
            holder.put("radio_type", type);
            holder.put("address_language", "zh_CN");
            holder.put("wifi_towers", jArray);

            DefaultHttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://www.google.com/loc/json");

            Debug.LOGD(this.getClass().getName(), "[[wifiLocation]] holder = " + holder);
            StringEntity se = new StringEntity(holder.toString());
            post.setEntity(se);
            HttpResponse resp = client.execute(post);

            HttpEntity entity = resp.getEntity();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    entity.getContent()));
            StringBuffer sb = new StringBuffer();

            String l = null;
            while ((l = br.readLine()) != null) {
                sb.append(l);
            }
            googleJsonData = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Debug.LOGD(this.getClass().getName(), "[[wifiLocation]] data = " + googleJsonData);
        if (!TextUtils.isEmpty(googleJsonData)) {
            String lati = null, lon = null;
            Pattern p = Pattern.compile("latitude\":[^,]+");
            Matcher m = p.matcher(googleJsonData);
            if (m.find()) {
                lati = m.group().split(":")[1];
            }
            p = Pattern.compile("longitude\":[^,]+");
            m = p.matcher(googleJsonData);
            if (m.find()) {
                lon = m.group().split(":")[1];
            }
            if (lati != null && lon != null) {
//                Location location = new Location(LocationManager.NETWORK_PROVIDER);
//                location.setLatitude(Double.valueOf(lati));
//                location.setLongitude(Double.valueOf(lon));
                
                Debug.LOGD(this.getClass().getName(), "[[wifiLocation]] wifi to locaiton lon = " + lon + " lati = " + lati + " ==================");
                //userData.uploadLocationInGPS(Double.valueOf(lon), Double.valueOf(lati), Criteria.ACCURACY_COARSE, null);
                JuGeiLiActivity.locationData.longitude = Double.valueOf(lon);
                JuGeiLiActivity.locationData.latitude = Double.valueOf(lati);
            }
        }
    }

    private boolean getLocationByCell() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        CellLocation cl = tm.getCellLocation();
        
        Debug.LOGD(this.getClass().getName(), "[[getLocationByCell]] cell info = " + cl + ">>><<<<");
        if (cl == null) {
            Debug.LOGD(this.getClass().getName(), "[[getLocationByCell]] !!! NO Cell Info !!!!");
            return false;
        }
        
        if ((tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
                || (tm.getSimState() == TelephonyManager.SIM_STATE_UNKNOWN)) {
            Debug.LOGD(this.getClass().getName(), "[[getLocationByCell]] !!!! NO SIM CARD !!!!");
            return false;
        }

        try {
            int cid = 0;
            int lac = 0;
            int mcc = 0;
            int mnc = 0;
            if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                GsmCellLocation gcl = (GsmCellLocation) cl;
                cid = gcl.getCid();
                lac = gcl.getLac();
                mcc = Integer.valueOf(tm.getNetworkOperator().substring(0, 3));
                mnc = Integer.valueOf(tm.getNetworkOperator().substring(3, 5));
            } else if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                CdmaCellLocation ccl = (CdmaCellLocation) cl;
                // int mcc = Integer.valueOf(tm.getNetworkOperator().substring(0,
                // 3));
                mcc = Integer.valueOf(tm.getNetworkOperator().substring(3, 5));
                lac = ccl.getNetworkId();
                mnc = ccl.getSystemId();
                cid = ccl.getBaseStationId();
            } else {
                return false;
            }
    
            Debug.LOGD(this.getClass().getName(), "[[getLocationByCell]] update location with cell success >>>>>>>>>>>");
            //userData.uploadLocationInCellId(cid, mcc, mnc, lac, null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    private boolean locateByNetworkAndGPS() {
        Debug.LOGD(this.getClass().getName(), "[[locateByNetworkAndGPS]] >>>>>>><<<<<<<<");

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }

        if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                && !mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (mUpdateLocationListener != null) {
                mUpdateLocationListener.onFailedUpdateLocation(NETWORK_AND_GPS_DISABLE);
            }
            return false;
        }

        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Debug.LOGD(this.getClass().getName(), "[[locateByNetworkAndGPS]] use network for location >>>>>>>>>><<<<<<");
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, mLocationListener);
            mHandler.sendEmptyMessageDelayed(LOCATE_FAILED, 20 * 1000);
        } else if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, mLocationListener);
            mHandler.sendEmptyMessageDelayed(LOCATE_FAILED, 20 * 1000);
        }
        
        return true;
    }
}
