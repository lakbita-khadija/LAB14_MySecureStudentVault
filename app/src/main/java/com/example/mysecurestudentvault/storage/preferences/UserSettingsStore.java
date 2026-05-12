package com.example.mysecurestudentvault.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public final class UserSettingsStore {

    private static final String PREF_NAME = "vault_settings";

    private static final String KEY_USER = "user_name";
    private static final String KEY_LANG = "language";
    private static final String KEY_THEME = "theme";

    private UserSettingsStore() {}

    public static boolean save(Context context,
                               String name,
                               String lang,
                               String theme,
                               boolean sync) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(KEY_USER, name);
        editor.putString(KEY_LANG, lang);
        editor.putString(KEY_THEME, theme);

        if (sync) {
            return editor.commit();
        } else {
            editor.apply();
            return true;
        }
    }

    public static UserData load(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String name = prefs.getString(KEY_USER, "");
        String lang = prefs.getString(KEY_LANG, "fr");
        String theme = prefs.getString(KEY_THEME, "light");

        return new UserData(name, lang, theme);
    }

    public static void clear(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    public static class UserData {

        public final String name;
        public final String lang;
        public final String theme;

        public UserData(String name, String lang, String theme) {
            this.name = name;
            this.lang = lang;
            this.theme = theme;
        }
    }
}