package com.maxfriedman.weather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.maxfriedman.weather.R;

/**
 * Created by Max on 12/12/15.
 */
public class SettingsActivity extends AppCompatActivity {

    private RadioButton FButton;
    private RadioButton CButton;
    private EditText numDaysField;
    private EditText zipField;
    int numDaysInt;
    String deg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        numDaysInt = prefs.getInt("days", 0);
        deg = prefs.getString("deg", null);

        FButton = (RadioButton) findViewById(R.id.FButton);
        CButton = (RadioButton) findViewById(R.id.CButton);
        numDaysField = (EditText) findViewById(R.id.numDaysField);
        zipField = (EditText) findViewById(R.id.zipField);

        //add a text watcher to save updates to these fields
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(zipField.getText().toString().equals("")){
                }
                else{
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (zipField.getText().length() == 5) {

                } else {

                }
            }
        };

        zipField.addTextChangedListener(textWatcher);

    }

    public void onButtonTap (View view) {

        RadioButton button = (RadioButton)view;

        if (button.isChecked()) {

            if (button.getId() == R.id.FButton) {
                deg = "F";
            } else {
                deg = "C";
            }

            SharedPreferences.Editor editor = getSharedPreferences("prefs", 0).edit();
            editor.putString("didChange", "1");
            editor.putString("deg", deg);
            editor.commit();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }


}
