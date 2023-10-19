package com.hyeonjs.cryptocurrencyinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyeonjs.library.HttpRequester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, InfoActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "앱 정보");
        return true;
    }

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
            final ArrayList<CoinInfo> coins = new ArrayList<>();
            JSONArray data = new JSONArray(json);
            for (int n = 0; n < data.length(); n++) {
                JSONObject datum = data.getJSONObject(n);
                String mark = datum.getString("market");
                if (mark.startsWith("KRW-"))
                    coins.add(new CoinInfo(datum.getString("korean_name"), mark));
            }
            Collections.shuffle(coins);
            runOnUiThread(() -> {
                String[] names = new String[coins.size()];
                for (int n = 0; n < coins.size(); n++) {
                    CoinInfo coin = coins.get(n);
                    names[n] = coin.name + " (" + coin.mark.split("-")[1] + ")";
                }
                layout.removeAllViews();
                ListView list = new ListView(this);
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
                list.setAdapter(adapter);
                list.setOnItemClickListener((adapterView, view, pos, id) -> {
                    new Thread(() -> showCoinInfo(coins.get((int) id))).start();
                });
                layout.addView(list);
                int pad = dip2px(16);
                list.setPadding(pad, pad, pad, pad);
            });
        } catch (Exception e) {
            toast(e.toString());
        }
    }

    private void showCoinInfo(final CoinInfo coin) {
        try {
            String url = "https://api.upbit.com/v1/ticker?markets=" + coin.mark;
            String json = HttpRequester.create(url).get();
            JSONObject data = new JSONArray(json).getJSONObject(0);
            DecimalFormat df = new DecimalFormat("###.###");
            final String result = "현재 시세 : " + df.format(data.getLong("trade_price")) + "\n" +
                    "등락률 : " + (data.getString("change").equals("RISE") ? "+" : "-") +
                    Math.round(data.getDouble("change_rate") * 10000.0) / 100.0 + "%";
            runOnUiThread(() -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(coin.name);
                dialog.setMessage(result);
                dialog.setNegativeButton("닫기", null);
                dialog.show();
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