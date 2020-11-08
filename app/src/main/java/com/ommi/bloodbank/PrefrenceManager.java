package com.ommi.bloodbank;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefrenceManager {

    public static String App = "Bloodback";

    public static SharedPreferences getDefaultSharedPrefrence(Context context)
    {
        return context.getSharedPreferences(App,Context.MODE_PRIVATE);
    }
    public static String getDeviceToken(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("device_token", "");
    }
    public static String getUserId(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("user_id", null);
    }

    public static String getUserName(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("name", "");
    }
    public static String getUserAddress(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("address", "");
    }

    public static String getUserAge(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("age", "");
    }

    public static String getUserLattitude(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("lattitude", "30.932093");
    }

    public static String getUserLongitude(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("longitude", "73.444584");
    }

    public static String getUserCountry(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("country", "");
    }

    public static String getUserCity(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("city", "");
    }
    public static String getUserPhone(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("phone", "");
    }

    public static String getUserImage(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("image", "");
    }

    public static String getUserType(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("type", "");
    }

    public static String getUserBloodGroup(Context context) {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("blood_group", "O-");
    }

    public static void setUserId(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("user_id", id).apply();
    }

    public static void setUserCity(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("city", id).apply();
    }

    public static void setUserCountry(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("country", id).apply();
    }

    public static void setUserLattitude(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("lattitude", id).apply();
    }

    public static void setUserLongitude(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("longitude", id).apply();
    }

    public static void setUserAge(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("age", id).apply();
    }

    public static void setUserName(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("name", id).apply();
    }

    public static void setUserPhone(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("phone", id).apply();
    }

    public static void setUserImage(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("image", id).apply();
    }

    public static void setUsertype(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("type", id).apply();
    }

    public static void setUserBloodGroup(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("blood_group", id).apply();
    }
    public static void setDecviceToken(Context context, String token) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("device_token", token).apply();
    }
    public static void setUserAddress(Context context, String id) {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("address", id).apply();
    }
    public static void setFile(Context context, String file)
    {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("File", file).apply();

    }
    public static String getFile(Context context)
    {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("File", null);

    }
    public static void setAvailibility(Context context, String availibility)
    {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("available",availibility).apply();

    }
    public static String getAvailability(Context context)
    {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("available", "0");

    }
    public static void setDefaultLang(Context context, String availibility)
    {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("lang",availibility).apply();

    }
    public static String getDefaultLang(Context context)
    {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("lang", "");

    }
    public static void setPlasmaDonation(Context context, String availibility)
    {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("plasma",availibility).apply();

    }
    public static String getPlasmaDonation(Context context)
    {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("plasma", "0");

    }
    public static void setTransfusion(Context context, String availibility)
    {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("transfusion",availibility).apply();

    }
    public static String getTransfusion(Context context)
    {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("transfusion", "0");

    }
    public static void setCountryCode(Context context, String CountryCode)
    {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("country_code",CountryCode).apply();

    }
    public static String getCountryCode(Context context)
    {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("country_code", "");

    }
    public static void setGender(Context context, String Gender)
    {
        context.getSharedPreferences(App, Context.MODE_PRIVATE).edit().putString("gender",Gender).apply();

    }
    public static String getGender(Context context)
    {
        return context.getSharedPreferences(App, Context.MODE_PRIVATE).getString("gender", "");

    }
}
