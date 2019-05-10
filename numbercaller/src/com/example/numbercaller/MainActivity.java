package com.example.numbercaller;

import android.R.string;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText et_number;
	private Button btn;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        et_number = (EditText)findViewById(R.id.txt);
        
        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new MyClickListener());
        
        
    }
    
    private class MyClickListener implements OnClickListener{
    	@Override
    	public void onClick(View arg0) {
    		String number = et_number.getText().toString().trim();
    		
    		if(number.equals("")){
    			Toast.makeText(MainActivity.this, "²»ÄÜÎª¿Õ", Toast.LENGTH_SHORT).show();
    			return;
    		}
    		
    		Intent intent = new Intent();
    		intent.setAction(Intent.ACTION_CALL);
    		intent.setData(Uri.parse("tel:"+number));
    		startActivity(intent);
    		
    		Log.d("MainActivity", "clicked");
    	}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
