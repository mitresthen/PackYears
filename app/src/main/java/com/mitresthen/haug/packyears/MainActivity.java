package com.mitresthen.haug.packyears;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void calculateYears(View view) {
        final EditText cigsaDay = (EditText) findViewById(R.id.cigsPerDay);
        final EditText yearsOfCigs = (EditText) findViewById(R.id.years);
        String packYearsDisplay = "0";
        try {
            double cigs = Double.parseDouble(cigsaDay.getText().toString());
            double years = Double.parseDouble(yearsOfCigs.getText().toString());
            double packYears = Calculator.Calculate(Constants.Cigarette, cigs, 1, years);
            DecimalFormat df = new DecimalFormat("0.0");
            packYearsDisplay = df.format(packYears);
        } catch (NumberFormatException e){

        }
        final TextView textField = (TextView) findViewById(R.id.packYearsNumber);
        textField.setText(packYearsDisplay);
    }

}
