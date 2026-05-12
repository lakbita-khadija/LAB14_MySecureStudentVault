package com.example.mysecurestudentvault.storage.internalstore;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public final class PrivateNoteStore {

    private PrivateNoteStore() {}

    public static void write(Context context,
                             String fileName,
                             String content) throws Exception {

        try (FileOutputStream fos =
                     context.openFileOutput(fileName, Context.MODE_PRIVATE)) {

            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static String read(Context context,
                              String fileName) throws Exception {

        try (FileInputStream fis =
                     context.openFileInput(fileName)) {

            byte[] data = fis.readAllBytes();

            return new String(data, StandardCharsets.UTF_8);
        }
    }

    public static boolean delete(Context context,
                                 String fileName) {

        return context.deleteFile(fileName);
    }
}