package com.example.mysecurestudentvault.storage.cache;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public final class TemporaryDraftStore {

    private TemporaryDraftStore() {}

    public static void write(Context context,
                             String fileName,
                             String content) throws Exception {

        File file = new File(context.getCacheDir(), fileName);

        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }

    public static String read(Context context,
                              String fileName) throws Exception {

        File file = new File(context.getCacheDir(), fileName);

        if (!file.exists()) {
            return null;
        }

        BufferedReader reader =
                new BufferedReader(new FileReader(file));

        StringBuilder builder = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        reader.close();

        return builder.toString();
    }

    public static int clearCache(Context context) {

        File[] files = context.getCacheDir().listFiles();

        if (files == null) {
            return 0;
        }

        int deleted = 0;

        for (File file : files) {

            if (file.delete()) {
                deleted++;
            }
        }

        return deleted;
    }
}