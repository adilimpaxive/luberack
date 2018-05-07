package com.app.luberack.Profile_management;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared preferences
    private Editor editor;
    // Context
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "UserPref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // email (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERID = "userid";
    // User password (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    //Current latitude of user
    public static final String KEY_CUR_LAT = "curlat";
    //Current longitude of user profile
    public static final String KEY_CUR_LNG = "curlng";
    //user_type 0 means user, 1 means technician
    public static final String KEY_TYPE = "type";
    public static final String KEY_BALANCE = "balance";
    //FIrebase registration id for messaging
    public static final String KEY_FIREBASE_ID = "regid";

    public static final String KEY_CARD_NO = "cardnumber";
    public static final String KEY_CARD_MONTH = "expmonth";
    public static final String KEY_CARD_YEAR = "expyear";
    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */

    public void createLoginSession(String email) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing password in pref
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {
            return true;
        }
        return false;
    }

    /**
     * Clear session details
     */
    public boolean logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Profile.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Staring Login Activity
        _context.startActivity(i);
        // Add new Flag to start new Activity
        return true;
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    private boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void saveUserID(String id) {
        editor.putString(KEY_USERID, id);
        editor.commit();
    }

    public void saveUserName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public void savePassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public void saveLat(String lat) {
        editor.putString(KEY_LAT, lat);
        editor.commit();
    }

    public void saveLng(String lng) {
        editor.putString(KEY_LNG, lng);
        editor.commit();
    }
    public void saveCurrentLat(String lat) {
        editor.putString(KEY_CUR_LAT, lat);
        editor.commit();
    }

    public void saveCurrentLng(String lng) {
        editor.putString(KEY_CUR_LNG, lng);
        editor.commit();
    }
    public void saveAddress(String address) {
        editor.putString(KEY_ADDRESS, address);
        editor.commit();
    }

    public void saveCity(String city) {
        editor.putString(KEY_CITY, city);
        editor.commit();
    }

    public void saveUserType(String type) {
        editor.putString(KEY_TYPE, type);
        editor.commit();
    }

    public void saveBalance(String balance) {
        editor.putString(KEY_BALANCE, balance);
        editor.commit();
    }

    public void saveFirebaseId(String regid) {
        editor.putString(KEY_FIREBASE_ID, regid);
        editor.commit();
    }

    public void saveUserEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public void saveCardNo(String card) {
        editor.putString(KEY_CARD_NO, card);
        editor.commit();
    }

    public void saveCardMonth(String card) {
        editor.putString(KEY_CARD_MONTH, card);
        editor.commit();
    }
    public void saveCardYear(String card) {
        editor.putString(KEY_CARD_YEAR, card);
        editor.commit();
    }


    public String getUserName() {
        return pref.getString(KEY_NAME, null);
    }

    public String getRegistrationId() {
        return pref.getString(KEY_FIREBASE_ID, null);
    }

    public String getLat() {
        return pref.getString(KEY_LAT, null);
    }

    public String getLng() {
        return pref.getString(KEY_LNG, null);
    }

    public String getCurrentLat() {
        return pref.getString(KEY_CUR_LAT, null);
    }

    public String getCurrentLng() {
        return pref.getString(KEY_CUR_LNG, null);
    }

    public String getAddress() {
        return pref.getString(KEY_ADDRESS, null);
    }
    public String getCity() {
        return pref.getString(KEY_CITY, null);
    }

    public String getUserEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getPassword() {
        return pref.getString(KEY_PASSWORD, null);
    }
    public String getUserID() {
        return pref.getString(KEY_USERID, null);
    }
    public String getUserType() {
        return pref.getString(KEY_TYPE, null);
    }
    public String getBalance() {
        return pref.getString(KEY_BALANCE, null);
    }

    public String getCardNo() {
        return pref.getString(KEY_CARD_NO, null);
    }
    public String getCardMonth() {
        return pref.getString(KEY_CARD_MONTH, null);
    }
    public String getCardYear() {
        return pref.getString(KEY_CARD_YEAR, null);
    }
}