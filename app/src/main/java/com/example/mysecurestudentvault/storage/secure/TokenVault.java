package com.example.mysecurestudentvault.storage.secure;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public final class TokenVault {

    private static final String FILE_NAME = "secure_vault";
    private static final String TOKEN_KEY = "api_token";

    private TokenVault() {}

    private static SharedPreferences securePrefs(Context context) throws Exception {

        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void saveToken(Context context, String token) throws Exception {

        securePrefs(context)
                .edit()
                .putString(TOKEN_KEY, token)
                .apply();
    }

    public static String loadToken(Context context) throws Exception {

        return securePrefs(context)
                .getString(TOKEN_KEY, "");
    }

    public static void clear(Context context) throws Exception {

        securePrefs(context)
                .edit()
                .clear()
                .apply();
    }
}