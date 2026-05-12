package com.example.mysecurestudentvault.storage.externalstore;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public final class ReportExportStore {

    private ReportExportStore() {}

    public static String exportReport(Context context,
                                      String fileName,
                                      String content) throws Exception {

        File dir = context.getExternalFilesDir(null);

        if (dir == null) {
            return null;
        }

        File file = new File(dir, fileName);

        FileWriter writer = new FileWriter(file);

        writer.write(content);

        writer.close();

        return file.getAbsolutePath();
    }

    public static String readReport(Context context,
                                    String fileName) throws Exception {

        File dir = context.getExternalFilesDir(null);

        if (dir == null) {
            return null;
        }

        File file = new File(dir, fileName);

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

    public static boolean deleteReport(Context context,
                                       String fileName) {

        File dir = context.getExternalFilesDir(null);

        if (dir == null) {
            return false;
        }

        File file = new File(dir, fileName);

        return file.delete();
    }
}