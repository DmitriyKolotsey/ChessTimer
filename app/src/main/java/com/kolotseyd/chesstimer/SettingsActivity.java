package com.kolotseyd.chesstimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES_START_TIME = "start_time";
    public static final String APP_PREFERENCES_TOP_START_TIME = "top_start_time";
    public static final String APP_PREFERENCES_BOT_START_TIME = "bot_start_time";
    TextView tv1min, tv3min, tv5min, tv7min, tv10min, tv30min;
    Button bCreateCustomMode;

    SharedPreferences shpSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tv1min = findViewById(R.id.tv1min);
        tv1min.setText(" 1 \n" + getString(R.string.timer_min));
        tv3min = findViewById(R.id.tv3min);
        tv3min.setText(" 3 \n" + getString(R.string.timer_min)) ;
        tv5min = findViewById(R.id.tv5min);
        tv5min.setText(" 5 \n" + getString(R.string.timer_min)) ;
        tv7min = findViewById(R.id.tv7min);
        tv7min.setText(" 7 \n" + getString(R.string.timer_min)) ;
        tv10min = findViewById(R.id.tv10min);
        tv10min.setText(" 10 \n" + getString(R.string.timer_min)) ;
        tv30min = findViewById(R.id.tv30min);
        tv30min.setText(" 30 \n" + getString(R.string.timer_min)) ;

        bCreateCustomMode = findViewById(R.id.bCreateCustomMode);
    }

    public void tv1minOnClick(View view) {
        long start_time = 60000;
        editStartTime(start_time);
        showSelectBonusTimeDialog();
    }

    public void tv3minOnClick(View view) {
        long start_time = 180000;
        editStartTime(start_time);
        showSelectBonusTimeDialog();
    }

    public void tv5minOnClick(View view) {
        long start_time = 300000;
        editStartTime(start_time);
        showSelectBonusTimeDialog();
    }

    public void tv7minOnClick(View view) {
        long start_time = 420000;
        editStartTime(start_time);
        showSelectBonusTimeDialog();
    }

    public void tv10minOnClick(View view) {
        long start_time = 600000;
        editStartTime(start_time);
        showSelectBonusTimeDialog();
    }

    public void tv30minOnClick(View view) {
        long start_time = 1800000;
        editStartTime(start_time);
        showSelectBonusTimeDialog();
    }

    public void onCreateCustomModeButtonClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        CreateCustomModeFragment createCustomModeFragment = CreateCustomModeFragment.newInstance(getString(R.string.create_custom_mode_title));
        createCustomModeFragment.show(fm, "fragment_create_custom_mode");
    }

    public void showSelectBonusTimeDialog(){
        FragmentManager fm = getSupportFragmentManager();
        SelectBonusTimeFragment selectBonusTimeFragment = SelectBonusTimeFragment.newInstance(getString(R.string.bonus_time_title));
        selectBonusTimeFragment.show(fm, "fragment_select_bonus_time");
    }

    public void editStartTime(Long l){
        shpSettings = getSharedPreferences("my_settings", MODE_PRIVATE);
        SharedPreferences.Editor editorStartTime = shpSettings.edit();
        editorStartTime.putLong(APP_PREFERENCES_START_TIME, l);
        editorStartTime.putLong(APP_PREFERENCES_TOP_START_TIME, l);
        editorStartTime.putLong(APP_PREFERENCES_BOT_START_TIME, l);
        editorStartTime.apply();
    }
}