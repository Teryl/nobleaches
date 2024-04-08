package com.example.nobleaches;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.Context;
import android.content.SharedPreferences;
import java.lang.reflect.Type;

public class UserDataManager {
    private static final String USER_DATA_KEY = "user_data";
    private static UserDataManager instance;
    private final SharedPreferences sharedPreferences;

    private UserDataManager(Context context) {
        sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
    }

    public static synchronized UserDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserDataManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveUserData(UserData userData) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userDataJson = gson.toJson(userData);
        editor.putString(USER_DATA_KEY, userDataJson);
        editor.apply();
    }

    public UserData getUserData() {
        Gson gson = new Gson();
        String userDataJson = sharedPreferences.getString(USER_DATA_KEY, null);
        Type type = new TypeToken<UserData>(){}.getType();
        return gson.fromJson(userDataJson, type);
    }

    public void saveUserDataToDataManager(UserData userData) {
        saveUserData(userData);
    }
}
