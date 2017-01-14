package com.mitresthen.haug.packyears;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TobaccoYear> tobaccoList;
    private ListView listView;
    private ListViewAdapter arrayAdapter;
    private double sum = 0.0;
    private DecimalFormat df = new DecimalFormat("0.00");
    private final String sumPrefix = "Total Pack Years: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usagedisplay);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Spinner tobaccoTypes = (Spinner) findViewById(R.id.tobaccotype);
        List<String> typesOfTobacco = new ArrayList<>(Constants.TobaccoFactors.keySet());
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typesOfTobacco);
        tobaccoTypes.setAdapter(typeAdapter);

        Spinner frequencies = (Spinner) findViewById(R.id.frequency);
        List<String> typesOfFrequency = new ArrayList<>(Constants.TimeFactors.keySet());
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typesOfFrequency);
        frequencies.setAdapter(freqAdapter);

        listView = (ListView) findViewById(R.id.tobaccolist);
        tobaccoList = new ArrayList<TobaccoYear>();
        arrayAdapter = new ListViewAdapter(this, tobaccoList);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.action_wipe:
                DeleteAll(item.getActionView());
                return true;
        }
        return false;
    }

    public void calculateYears(View view) {
        final EditText quantity = (EditText) findViewById(R.id.quantity);
        final EditText yearsOfCigs = (EditText) findViewById(R.id.nrofyears);
        final Spinner tobaccoType = (Spinner)  findViewById(R.id.tobaccotype);
        final Spinner frequency = (Spinner) findViewById(R.id.frequency);

        String packYearsDisplay = "0";

        double cigs = 0.0;
        double years = 0.0;
        double tobaccoFactor = 0.0;
        double usageFrequency = 0.0;

        try {
            cigs = Double.parseDouble(quantity.getText().toString());
            years = Double.parseDouble(yearsOfCigs.getText().toString());
            tobaccoFactor = Constants.TobaccoFactors.get(tobaccoType.getSelectedItem().toString());
            usageFrequency = Constants.TimeFactors.get(frequency.getSelectedItem().toString());
        } catch (NumberFormatException e){
        }

        double packYears = Calculator.Calculate(tobaccoFactor, cigs, usageFrequency, years);

        SumAdd(packYears);

        packYearsDisplay = df.format(packYears);
        tobaccoList.add(new TobaccoYear(tobaccoType.getSelectedItem().toString(), packYears));
        arrayAdapter.notifyDataSetChanged();
    }

    public void DeleteAll(View view){
        SumSet("0.0");
        tobaccoList.clear();
        arrayAdapter.notifyDataSetChanged();
    }

    public void SumSubtract(double sub){
        sum -= sub;
        if(sum < 0)
            sum = 0.0;

        UpdateSumText();
    }

    public void SumAdd(double sub){
        sum += sub;
        UpdateSumText();
    }

    public void SumSet(String sub){
        double subDouble = StringToUnsignedDouble(sub);
        if(subDouble < 0.0f)
            return;

        sum = subDouble;

        UpdateSumText();
    }

    private double StringToUnsignedDouble(String doubleString){
        double subDouble = -1.0f;
        try{
            subDouble = Double.parseDouble(doubleString);
        } catch (NumberFormatException e){
            subDouble = Double.parseDouble(doubleString.replace(',', '.'));
        }

        if(subDouble < 0.0f)
            return -1.0f;
        else{
            return subDouble;
        }
    }

    private void UpdateSumText(){
        String sumDisplay = df.format(sum);
        final TextView textField = (TextView) findViewById(R.id.calculatedYears);
        textField.setText(sumPrefix + sumDisplay);
    }

    private class ListViewAdapter extends BaseAdapter implements ListAdapter
    {
        private Context con;
        private ArrayList<TobaccoYear> data = new ArrayList<>();

        public ListViewAdapter(Context context, ArrayList<TobaccoYear> data)
        {
            this.con = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if(rowView == null) {
                LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.smoking_row_item, parent, false);
            }
            TextView tobaccoRow = (TextView) rowView.findViewById(R.id.tobaccoTypeRow);
            tobaccoRow.setText(data.get(i).tobaccoTypeDisp);

            TextView packYearRow = (TextView) rowView.findViewById(R.id.packYearsRow);
            packYearRow.setText(df.format(data.get(i).yearsDisp));

            ImageButton deleteRow = (ImageButton) rowView.findViewById(R.id.removeItem);

            deleteRow.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    SumSubtract(data.get(i).yearsDisp);
                    data.remove(i);
                    notifyDataSetChanged();
                }
            });

            return rowView;
        }
    }

    private class TobaccoYear{
        public String tobaccoTypeDisp = "";
        public double yearsDisp = 0.0;

        public TobaccoYear(String tobaccoTypeDisp, double yearsDisp){
            this.tobaccoTypeDisp = tobaccoTypeDisp;
            this.yearsDisp = yearsDisp;
        }
    }
}
