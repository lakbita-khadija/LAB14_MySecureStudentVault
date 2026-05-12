package com.example.mysecurestudentvault.storage.internalstore;

import android.content.Context;

import com.example.mysecurestudentvault.models.ModuleRecord;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class ModuleProgressStore {

    public static final String FILE_NAME =
            "modules_progress.json";

    private ModuleProgressStore() {}

    public static void save(Context context,
                            List<ModuleRecord> modules) throws Exception {

        JSONArray array = new JSONArray();

        for (ModuleRecord module : modules) {

            JSONObject object = new JSONObject();

            object.put("id", module.id);
            object.put("title", module.title);
            object.put("progress", module.progress);
            object.put("score", module.score);

            array.put(object);
        }

        try (var fos =
                     context.openFileOutput(FILE_NAME,
                             Context.MODE_PRIVATE)) {

            fos.write(array.toString()
                    .getBytes(StandardCharsets.UTF_8));
        }
    }

    public static List<ModuleRecord> load(Context context) {

        try (var fis = context.openFileInput(FILE_NAME)) {

            byte[] data = fis.readAllBytes();

            String json =
                    new String(data, StandardCharsets.UTF_8);

            JSONArray array = new JSONArray(json);

            List<ModuleRecord> modules =
                    new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {

                JSONObject object =
                        array.getJSONObject(i);

                modules.add(
                        new ModuleRecord(
                                object.getInt("id"),
                                object.getString("title"),
                                object.getInt("progress"),
                                object.getInt("score")
                        )
                );
            }

            return modules;

        } catch (Exception e) {

            return new ArrayList<>();
        }
    }

    public static boolean delete(Context context) {

        return context.deleteFile(FILE_NAME);
    }
}