package com.brewtools.brewdaycalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    public static final String TARGET_OG = "com.brewtools.brewdaycalculator.TARGET_OG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState != null) {
            Double myDouble = savedInstanceState.getDouble("og");
            EditText editText = (EditText)findViewById(R.id.og_val);
            editText.setText(myDouble.toString());
        }

    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        
      super.onSaveInstanceState(savedInstanceState);
        
      // Save UI state changes to the savedInstanceState.
      // This bundle will be passed to onCreate if the process is
      // killed and restarted.
      EditText editText = (EditText)findViewById(R.id.og_val);
      String message = editText.getText().toString();
      
      if (message != null && !message.equals("")) {
          savedInstanceState.putDouble("og", Double.valueOf(message));          
      }

      
    }
    
    
    public void hydroAdjust(View view) {
        Intent intent = new Intent(this, HydrometerAdjustActivity.class);
        startActivity(intent);

    }
    
    public void refractAdjust(View view) {
        Intent intent = new Intent(this, RefractometerAdjustActivity.class);
        EditText editText = (EditText)findViewById(R.id.og_val);
        String message = editText.getText().toString();
        intent.putExtra(TARGET_OG, message);
        startActivity(intent);
    }
}
