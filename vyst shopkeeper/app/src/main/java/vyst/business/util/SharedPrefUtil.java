package vyst.business.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import io.paperdb.Paper;
import vyst.business.pojo.location.Datum;


/**
 * Created by Admin on 8/24/2017.
 */

public class SharedPrefUtil {

    private static final String USER_PREFERENCES = "USER_PREFERENCES" ;

    private static final String USER_LOGGED_IN = "USER_LOGGED_IN" ;
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN" ;

    private static final String EMAIL  = "email" ;
    private static final String LOCATION_ADDRESS = "location_address" ;
    private static final String NAME  = "name" ;
    private static final String PHONE  = "phone" ;
    private static final String USER_ID  = "user_id" ;
    private static final String LOCATION_ID  = "location_id" ;

    public static void clearPreferences(Context context) {
        getPreferences(context).edit().clear().commit();
        try {
            Paper.book("items").destroy();
        } catch (Exception ex) {

        }

    }

    public static SharedPreferences getPreferences(Context context) {

        return context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
    }



    public static void setUserLoggedIn(Context context, Boolean isLoggedIn) {
        getPreferences(context).edit().putBoolean(USER_LOGGED_IN, isLoggedIn).apply();
    }

    public static Boolean isUserLoggedIn(Context context) {
        return getPreferences(context).getBoolean(USER_LOGGED_IN,false);
    }

    public static void saveLocationArray(ArrayList<Datum> datumArrayList) {
        Paper.book("items").write("locations", datumArrayList);
    }

    public static ArrayList<Datum> getLocationArray() {
        return Paper.book("items").read("locations", new  ArrayList<Datum>());
    }


    public static void setAccessToken(Context context, String access_token) {
        getPreferences(context).edit().putString(ACCESS_TOKEN, access_token).apply();
    }

    public static String getAccessToken(Context context) {
        return getPreferences(context).getString(ACCESS_TOKEN,"");
    }


    public static void setEmail(Context context, String email) {
        getPreferences(context).edit().putString(EMAIL, email).apply();
    }

    public static String getEmail(Context context) {
        return getPreferences(context).getString(EMAIL,"");
    }

    public static void setNotificationToken(String refreshedToken) {
        Paper.book("fcm").write("notification_token", refreshedToken);
    }


    public static String getNotificationToken() {
        return Paper.book("fcm").read("notification_token", "");
    }



    public static void setAddress(Context context, String loc_address) {
        getPreferences(context).edit().putString(LOCATION_ADDRESS, loc_address).apply();
    }

    public static String getAddress(Context context) {
        return getPreferences(context).getString(LOCATION_ADDRESS,"");
    }


    public static void setName(Context context, String name) {
        getPreferences(context).edit().putString(NAME, name).apply();
    }

    public static String getName(Context context) {
        return getPreferences(context).getString(NAME,"");
    }

    public static void setPhone(Context context, String phone) {
        getPreferences(context).edit().putString(PHONE, phone).apply();
    }

    public static String getPhone(Context context) {
        return getPreferences(context).getString(PHONE,"");
    }


    public static void setUserId(Context context, String user_id) {
        getPreferences(context).edit().putString(USER_ID, user_id).apply();
    }

    public static String getUserId(Context context) {
        return getPreferences(context).getString(USER_ID,"");
    }

    public static void setLocationId(Context context, String location_id) {
        getPreferences(context).edit().putString(LOCATION_ID, location_id).apply();
    }

    public static String getlocationId(Context context) {
        return getPreferences(context).getString(LOCATION_ID,"");
    }


}
