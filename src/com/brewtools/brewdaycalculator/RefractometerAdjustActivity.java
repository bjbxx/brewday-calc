package com.brewtools.brewdaycalculator;

import com.brewtools.brewdaycalculator.MainActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RefractometerAdjustActivity extends Activity {
    
    private Double target_og = null;
    private Double preboil_amt = Double.valueOf(5);
    private Double postboil_amt = Double.valueOf(3.75);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refractometer_adjust);
        // Show the Up button in the action bar.
        setupActionBar();
        
        Intent i = getIntent();
        String message = i.getStringExtra(MainActivity.TARGET_OG);
        if (message != null && !message.equals("")) {
            target_og = Double.valueOf(message);
        }
        
        EditText preText = (EditText)findViewById(R.id.preboil_val);
        preText.setText(preboil_amt.toString());
        
        EditText postText = (EditText)findViewById(R.id.postboil_val);
        postText.setText(postboil_amt.toString());
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refractometer_adjust, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void calcSpecificGravity(View view) {
        
        updatePreBoilAmount();
        updatePostBoilAmount();
                
        EditText et = (EditText)findViewById(R.id.brix_val);
        String value = et.getText().toString();
        
        if (value != null && !value.equals("")) {
            
            Double brix = Double.valueOf(value);
            double sg = (brix / (258.6-((brix / 258.2)*227.1))) + 1;
            int sg_points = (int)(sg * 1000) - 1000;

            StringBuilder message = new StringBuilder(String.format("%.3f or %d gravity points \r\n\r\n",
                                                                    sg, sg_points));
            
            if(target_og != null) {
            
                int fg_points = (int)(target_og * 1000) - 1000;
                double point_diff = (sg_points * preboil_amt) - (fg_points * postboil_amt);
                double cur_og = 1 + (((sg_points * preboil_amt)/postboil_amt) / 1000); 
                
                message.append(String.format("To hit %.3f OG, you are off by %d points \r\n" +
                                             "You are on target for a %.3f OG \r\n",
                                              target_og, (int)point_diff, cur_og ));
                
                if(point_diff < 0) {
                    point_diff *= -1;
                    double lbs_sugar = point_diff / 37;
                    double lbs_extract = point_diff / 45;
                    message.append("\r\n");
                    message.append(String.format("You can add %.2f lbs of corn sugar \r\n"+
                                                 "Or %.2f lbs of malt extract", lbs_sugar, lbs_extract));
                }
            
            }
            
            TextView t = (TextView)findViewById(R.id.results);
            t.setText(message);
        }

    
    }

    @SuppressLint("NewApi")
    private void updatePreBoilAmount() {
        
        EditText editText = (EditText)findViewById(R.id.preboil_val);        
        String preBoil = editText.getText().toString();
        
        if (!preBoil.isEmpty() && !preBoil.equals(preboil_amt)) {
            
            preboil_amt = Double.valueOf(preBoil);
        
        }
    }
    
    @SuppressLint("NewApi")
    private void updatePostBoilAmount() {
        
        EditText editText = (EditText)findViewById(R.id.postboil_val);        
        String postBoil = editText.getText().toString();
        
        if (!postBoil.isEmpty() && !postBoil.equals(postboil_amt)) {
            
            postboil_amt = Double.valueOf(postBoil);
        
        }
    }
    
    

}
