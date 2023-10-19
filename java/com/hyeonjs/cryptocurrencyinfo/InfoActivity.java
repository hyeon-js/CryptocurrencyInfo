package com.hyeonjs.cryptocurrencyinfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
    }
    
}
