package com.hyeonjs.cryptocurrencyinfo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyeonjs.library.HttpRequester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

        new Thread(() -> loadCoinList(layout)).start();
    }

    private void loadCoinList(final LinearLayout layout) {
        try {
            String url = "https://api.upbit.com/v1/market/all";
            String json = HttpRequester.create(url).get();
            final ArrayList<Pair<String, String>> coins = new ArrayList<>();
            JSONArray data = new JSONArray(json);
            for (int n = 0; n < data.length(); n++) {
                JSONObject datum = data.getJSONObject(n);
                String mark = datum.getString("market");
                if (mark.startsWith("KRW-")) coins.add(new Pair<>(datum.getString("korean_name"), mark));
            }
            runOnUiThread(() -> {
                String[] names = new String[coins.size()];
                for (int n = 0; n < coins.size(); n++) {
                    Pair<String, String> coin = coins.get(n);
                    names[n] = coin.first + " (" + coin.second + ")";
                }
                layout.removeAllViews();
                ListView list = new ListView(this);
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
                list.setAdapter(adapter);
                list.setOnItemClickListener((adapterView, view, pos, id) -> {
                    //show coin info
                });
                layout.addView(list);
            });
        } catch (Exception e) {
            toast(e.toString());
        }
    }

    private void toast(final String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    private int dip2px(int dips) {
        return (int) Math.ceil(dips * getResources().getDisplayMetrics().density);
    }
}