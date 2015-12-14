package com.maxfriedman.weather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

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
    String zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        numDaysInt = prefs.getInt("days", 0);
        deg = prefs.getString("deg", null);
        zip = prefs.getString("zip", null);

        FButton = (RadioButton) findViewById(R.id.FButton);
        CButton = (RadioButton) findViewById(R.id.CButton);
        numDaysField = (EditText) findViewById(R.id.numDaysField);
        zipField = (EditText) findViewById(R.id.zipField);

        // pre-fill old settings

        if (deg.equals("F")) {
            FButton.setChecked(true);
        } else if (deg != null) {
            CButton.setChecked(true);
        }

        if (numDaysInt != 0) {
            numDaysField.setText(String.valueOf(numDaysInt));
        }

        if (zip != null) {
            zipField.setText(zip);
        }

        //add a text watcher to save updates to these fields
        TextWatcher textWatcherZip = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 5) {

                    // Check for valid zipcode and save to shared prefs

                    zip = editable.toString();
                    System.out.println("Valid zip: " + zip);
                    SharedPreferences.Editor editor = getSharedPreferences("prefs", 0).edit();
                    editor.putString("didChange", "1");
                    editor.putString("zip", zip);
                    editor.commit();

                } else {

                    // Show error for invalid zip
                    System.out.println("Invalid zip: " + zip);
                    makeToast("" + R.string.invalid_zip_code);

                }
            }
        };

        TextWatcher textWatcherNumDays = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString() != null && !editable.toString().equals("")) {

                    numDaysInt = Integer.parseInt(editable.toString());

                    if (numDaysInt > 0 || numDaysInt <= 10) {

                        // Check for valid num of days and save to shared prefs

                        System.out.println("Valid number of days: " + numDaysInt);
                        SharedPreferences.Editor editor = getSharedPreferences("prefs", 0).edit();
                        editor.putString("didChange", "1");
                        editor.putInt("days", numDaysInt);
                        editor.commit();

                    } else {

                        // Show error for invalid num days
                        System.out.println("Invalid number of days: " + numDaysInt);
                        makeToast("" + R.string.invalid_num_days);
                        numDaysField.setText("");

                    }
                }
            }
        };

        zipField.addTextChangedListener(textWatcherZip);
        numDaysField.addTextChangedListener(textWatcherNumDays);

    }

    public void makeToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT);
    }

    public void onButtonTap (View view) {

        RadioButton button = (RadioButton)view;

        if (button.isChecked()) {

            if (button.getId() == R.id.FButton) {
                deg = "F";
            } else {
                deg = "C";
            }

            // Check and save degreee unit

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
