package me.exerosis.spartangame.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gov.pppl.androidmessaginglibrary.AndroidMessagingAPI;
import gov.pppl.androidmessaginglibrary.bluetooth.Bluetooth;
import gov.pppl.androidmessaginglibrary.bluetooth.BluetoothManager;
import me.exerosis.spartangame.game.GameActivity;
import me.exerosis.spartangame.game.GameView;
import me.exerosis.spartangame.util.redis.RedisMessageListener;
import me.exerosis.spartangame.util.redis.RedisMessager;
import gov.pppl.blah.R;
import me.exerosis.spartangame.util.ExActivity;
import me.exerosis.spartangame.util.Redis;
import redis.clients.jedis.Jedis;

public class JoinActivity extends ExActivity implements AbsListView.OnItemClickListener {
    private ListView listView;
    private Bundle settings = new Bundle();
    private ArrayList<String> listText = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        if (getIntent().getExtras() != null)
            settings.putAll(getIntent().getExtras());

        listView = getByID(R.id.list, ListView.class);
        listView.setOnItemClickListener(this);

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listText);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);
        startRefresh();
    }

    public void onClickCancelButton(View button) {
        intend(RESULT_CANCELED);
    }


    private void startRefresh() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    listText.clear();
                    try {
                        listText.addAll(Redis.get(settings).smembers(HostActivity.ARGS_REDIS_SERVER_LIST));
                    } catch (Exception e) {
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView textView = (TextView) view;
        String text = textView.getText().toString();
        RedisMessager.sendMessage("game.join", text, settings);
        intend(RESULT_OK);
    }

    private void intend(int id) {
        Intent intent = new Intent();
        intent.putExtras(settings);
        setResult(id, intent);
        finish();
    }
}