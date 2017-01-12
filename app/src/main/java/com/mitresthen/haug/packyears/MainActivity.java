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
    private DecimalFormat df = new DecimalFormat("0.0");

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

        try {
            double cigs = Double.parseDouble(quantity.getText().toString());
            double years = Double.parseDouble(yearsOfCigs.getText().toString());
            double tobaccoFactor = Constants.TobaccoFactors.get(tobaccoType.getSelectedItem().toString());
            double usageFrequency = Constants.TimeFactors.get(frequency.getSelectedItem().toString());
            double packYears = Calculator.Calculate(tobaccoFactor, cigs, usageFrequency, years);
            sum += packYears;
            packYearsDisplay = df.format(packYears);
        } catch (NumberFormatException e){

        }

        String sumDisplay = df.format(sum);
        final TextView textField = (TextView) findViewById(R.id.calculatedYears);
        textField.setText(sumDisplay);

        tobaccoList.add(new TobaccoYear(tobaccoType.getSelectedItem().toString(), packYearsDisplay));
        arrayAdapter.notifyDataSetChanged();
    }

    public void DeleteAll(View view){
        tobaccoList.clear();
        arrayAdapter.notifyDataSetChanged();

        sum = 0.0;
        String sumDisplay = df.format(sum);
        final TextView textField = (TextView) findViewById(R.id.calculatedYears);
        textField.setText(sumDisplay);
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
            packYearRow.setText(data.get(i).yearsDisp);

            ImageButton deleteRow = (ImageButton) rowView.findViewById(R.id.removeItem);

            deleteRow.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    data.remove(i);
                    notifyDataSetChanged();
                }
            });

            return rowView;
        }
    }

    private class TobaccoYear{
        public String tobaccoTypeDisp = "";
        public String yearsDisp = "0.0";

        public TobaccoYear(String tobaccoTypeDisp, String yearsDisp){
            this.tobaccoTypeDisp = tobaccoTypeDisp;
            this.yearsDisp = yearsDisp;
        }
    }
}
