package com.chashurin.homeworka2l2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText keyEditText, valueEditText;
    Button addButton, checkButton, deleteButton, loadInternalMemoryButton, loadExternalMemoryButton, showInternalMemoryButton, showExternalMemoryButton;
    ImageView pictureImageView;

    SharedPreferences sPref;
    private final static String SHARED_PREFERENCES_KEY = "shared_preferences_key";
    private final static String SHARED_PREFERENCES_VALUE = "shared_preferences_value";
    private final static String SERIALIZED_FILE_NAME = "/picture.ser";

    Set<String> keyValueList = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        buttonBehavior();
    }

    private void initView() {
        keyEditText = (EditText) findViewById(R.id.keyEditText);
        valueEditText = (EditText) findViewById(R.id.valueEditText);
        addButton = (Button) findViewById(R.id.addButton);
        checkButton = (Button) findViewById(R.id.checkButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        loadInternalMemoryButton = (Button) findViewById(R.id.loadInternalMemory);
        loadExternalMemoryButton = (Button) findViewById(R.id.loadExternalMemory);
        showInternalMemoryButton = (Button) findViewById(R.id.showInternalMemory);
        showExternalMemoryButton = (Button) findViewById(R.id.showExternalMemory);
        pictureImageView = (ImageView) findViewById(R.id.pictureImageView);
    }

    private void buttonBehavior() {
        addButton.setOnClickListener(this);
        checkButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        loadInternalMemoryButton.setOnClickListener(this);
        loadExternalMemoryButton.setOnClickListener(this);
        showInternalMemoryButton.setOnClickListener(this);
        showExternalMemoryButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                addValueAndKey();
                break;
            case R.id.checkButton:
                checkKeyAndPrintValue();
                break;
            case R.id.deleteButton:
                deleteValueByKey();
                break;
            case R.id.loadInternalMemory:
                loadInternalMemory();
                break;
            case R.id.loadExternalMemory:
                loadExternalMemory();
                break;
            case R.id.showInternalMemory:
                showInternalMemory();
                break;
            case R.id.showExternalMemory:
                showExternalMemory();
                break;
            default:
                break;
        }
    }

    private void addValueAndKey() {
        if (!keyEditText.getText().toString().isEmpty() && !valueEditText.getText().toString().isEmpty()) {
            keyValueList.add(keyEditText.getText().toString());
            sPref = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString(keyEditText.getText().toString(), valueEditText.getText().toString());
            editor.putStringSet(SHARED_PREFERENCES_VALUE, keyValueList);
            editor.apply();
            Toast.makeText(this, "Text add", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkKeyAndPrintValue() {
        if (!keyEditText.getText().toString().isEmpty()) {
            String keyValue = keyEditText.getText().toString();
            sPref = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
            Set<String> keyValueList = sPref.getStringSet(SHARED_PREFERENCES_VALUE, null);
            for (String keyValueIsList : keyValueList) {
                if (keyValue.equals(keyValueIsList)) {
                    valueEditText.setText(sPref.getString(keyValue, ""));
                    Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void deleteValueByKey() {
        if (!keyEditText.getText().toString().isEmpty()) {
            String keyValue = keyEditText.getText().toString();
            sPref = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
            Set<String> keyValueList = sPref.getStringSet(SHARED_PREFERENCES_VALUE, null);
            for (String keyValueIsList : keyValueList) {
                if (keyValue.equals(keyValueIsList)) {
                    keyValueList.remove(keyValue);
                    Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadInternalMemory() {
        File file;
        try {
            file = new File(getFilesDir() + SERIALIZED_FILE_NAME);
            FileOutputStream fileOut;
            ObjectOutputStream out;
            if (!file.exists()) {
                file.createNewFile();
            }
            File source = new File("src/main/res/drawable/picture.png");
            fileOut = new FileOutputStream(file, false);
            out = new ObjectOutputStream(fileOut);
            out.writeObject(source);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExternalMemory() {

    }

    @SuppressWarnings("all")
    private void showInternalMemory() {
        FileInputStream fileIn = null;
        ObjectInputStream in = null;

        try {
            fileIn = new FileInputStream(getFilesDir() + SERIALIZED_FILE_NAME);
            in = new ObjectInputStream(fileIn);
            pictureImageView.setImageDrawable((Drawable) in.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                fileIn.close();
            } catch (Exception ignored) {}
        }
    }

    private void showExternalMemory() {

    }
}
