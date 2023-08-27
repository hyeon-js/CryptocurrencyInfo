package com.hyeonjs.cryptocurrencyinfo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);

        final LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(1);
        lay.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        lay.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);

        int pad = dip2px(8);
        ProgressBar bar = new ProgressBar(this);
        bar.setPadding(pad, pad, pad, pad);
        lay.addView(bar);
        TextView txt = new TextView(this);
        txt.setText("암호화폐 목록 불러오는 중...");;
        txt.setTextColor(Color.BLACK);
        txt.setTextSize(14);
        txt.setGravity(Gravity.CENTER);
        lay.addView(txt);

        layout.addView(lay);
        setContentView(layout);
    }


    private int dip2px(int dips) {
        return (int) Math.ceil(dips * getResources().getDisplayMetrics().density);
    }
}