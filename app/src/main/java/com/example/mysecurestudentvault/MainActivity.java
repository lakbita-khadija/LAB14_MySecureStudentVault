package com.example.mysecurestudentvault;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysecurestudentvault.models.ModuleRecord;
import com.example.mysecurestudentvault.storage.cache.TemporaryDraftStore;
import com.example.mysecurestudentvault.storage.externalstore.ReportExportStore;
import com.example.mysecurestudentvault.storage.internalstore.ModuleProgressStore;
import com.example.mysecurestudentvault.storage.internalstore.PrivateNoteStore;
import com.example.mysecurestudentvault.storage.preferences.UserSettingsStore;
import com.example.mysecurestudentvault.storage.secure.TokenVault;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "StudentVault";

    private EditText etStudentName;
    private EditText etToken;
    private Spinner spLanguage;
    private Switch swDarkMode;
    private TextView tvResult;

    private final List<String> languages =
            Arrays.asList("fr", "en", "ar");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        etStudentName = findViewById(R.id.etStudentName);
        etToken = findViewById(R.id.etToken);
        spLanguage = findViewById(R.id.spLanguage);
        swDarkMode = findViewById(R.id.swDarkMode);
        tvResult = findViewById(R.id.tvResult);

        setupSpinner();

        Button btnSavePrefs =
                findViewById(R.id.btnSavePrefs);

        Button btnLoadPrefs =
                findViewById(R.id.btnLoadPrefs);

        Button btnSaveJson =
                findViewById(R.id.btnSaveJson);

        Button btnLoadJson =
                findViewById(R.id.btnLoadJson);

        Button btnExport =
                findViewById(R.id.btnExport);

        Button btnClear =
                findViewById(R.id.btnClear);

        btnSavePrefs.setOnClickListener(v -> savePrefs());

        btnLoadPrefs.setOnClickListener(v -> loadPrefs());

        btnSaveJson.setOnClickListener(v -> saveJson());

        btnLoadJson.setOnClickListener(v -> loadJson());

        btnExport.setOnClickListener(v -> exportReport());

        btnClear.setOnClickListener(v -> clearAll());

        loadPrefs();
    }

    private void setupSpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        languages
                );

        spLanguage.setAdapter(adapter);
    }

    private void savePrefs() {

        String name =
                etStudentName.getText().toString().trim();

        String lang =
                languages.get(spLanguage.getSelectedItemPosition());

        String theme =
                swDarkMode.isChecked() ? "dark" : "light";

        UserSettingsStore.save(
                this,
                name,
                lang,
                theme,
                false
        );

        String token =
                etToken.getText().toString();

        try {

            if (!token.isEmpty()) {

                TokenVault.saveToken(this, token);
            }

        } catch (Exception e) {

            tvResult.setText(
                    "Encryption error"
            );

            return;
        }

        try {

            TemporaryDraftStore.write(
                    this,
                    "last_session.txt",
                    "User=" + name
            );

        } catch (Exception ignored) {
        }

        Log.d(TAG,
                "Preferences saved successfully");

        tvResult.setText(
                "Preferences saved successfully"
        );
    }

    private void loadPrefs() {

        UserSettingsStore.UserData data =
                UserSettingsStore.load(this);

        etStudentName.setText(data.name);

        swDarkMode.setChecked(
                "dark".equals(data.theme)
        );

        int index =
                languages.indexOf(data.lang);

        spLanguage.setSelection(
                index >= 0 ? index : 0
        );

        int tokenLength = 0;

        try {

            String token =
                    TokenVault.loadToken(this);

            if (token != null) {

                tokenLength = token.length();
            }

        } catch (Exception ignored) {
        }

        tvResult.setText(
                "Preferences loaded\n" +
                        "Token length = " +
                        tokenLength
        );
    }

    private void saveJson() {

        List<ModuleRecord> modules =
                Arrays.asList(
                        new ModuleRecord(
                                1,
                                "Android Java",
                                80,
                                17
                        ),
                        new ModuleRecord(
                                2,
                                "Cyber Security",
                                70,
                                15
                        ),
                        new ModuleRecord(
                                3,
                                "Mobile Storage",
                                90,
                                18
                        )
                );

        try {

            ModuleProgressStore.save(
                    this,
                    modules
            );

            PrivateNoteStore.write(
                    this,
                    "student_note.txt",
                    "JSON data saved successfully"
            );

        } catch (Exception e) {

            tvResult.setText(
                    "JSON save error"
            );

            return;
        }

        tvResult.setText(
                "Modules JSON saved"
        );
    }

    private void loadJson() {

        List<ModuleRecord> modules =
                ModuleProgressStore.load(this);

        StringBuilder builder =
                new StringBuilder();

        builder.append("Modules loaded\n\n");

        for (ModuleRecord module : modules) {

            builder.append(module.title)
                    .append(" | progress=")
                    .append(module.progress)
                    .append("% | score=")
                    .append(module.score)
                    .append("\n");
        }

        tvResult.setText(builder.toString());
    }

    private void exportReport() {

        try {

            String path =
                    ReportExportStore.exportReport(
                            this,
                            "report.txt",
                            "Secure export completed"
                    );

            tvResult.setText(
                    "Exported to:\n" + path
            );

        } catch (Exception e) {

            tvResult.setText(
                    "Export failed"
            );
        }
    }

    private void clearAll() {

        UserSettingsStore.clear(this);

        try {

            TokenVault.clear(this);

        } catch (Exception ignored) {
        }

        ModuleProgressStore.delete(this);

        PrivateNoteStore.delete(
                this,
                "student_note.txt"
        );

        int deleted =
                TemporaryDraftStore.clearCache(this);

        tvResult.setText(
                "Application cleaned\n" +
                        "Cache deleted = " +
                        deleted
        );

        etStudentName.setText("");
        etToken.setText("");

        swDarkMode.setChecked(false);

        spLanguage.setSelection(0);
    }
}