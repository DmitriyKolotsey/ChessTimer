package com.kolotseyd.chesstimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton ibSettings, ibStart, ibRefresh;
    TextView tvTopTimer, tvBotTimer, tvTopPlayerMovesCount, tvBotPlayerMovesCount;

    CountDownTimer cdtTopTimer, cdtBotTimer;

    SharedPreferences shpSettings;

    SoundPool soundPool;

    public static final String APP_PREFERENCES = "my_settings";
    public static final String APP_PREFERENCES_LAST_ACTIVE = "last_active_player";
    public static final String APP_PREFERENCES_START_TIME = "start_time";
    public static final String APP_PREFERENCES_TOP_START_TIME = "top_start_time";
    public static final String APP_PREFERENCES_BOT_START_TIME = "bot_start_time";
    public static final String APP_PREFERENCES_BONUS_TIME = "bonus_time";
    public static final String APP_PREFERENCES_CONTROL_BUTTON_STATE = "control_button_state";
    
    public static final int DEFAULT_START_TIME = 60000;
    public static final int DEFAULT_BONUS_TIME = 1000;
    public static final String DEFAULT_CONTROL_BUTTON_STATE = "start";
    public static final int INTERVAL_TIME = 1000;

    public boolean isTopTimerWorking = false;
    public boolean isBotTimerWorking = false;

    public int topPlayerMovesCount = 0;
    public int botPlayerMovesCount = 0;
    public int buttonSound;

    public long topStartTime, botStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ibSettings = findViewById(R.id.ibSettings);
        ibStart = findViewById(R.id.ibStart);
        ibRefresh = findViewById(R.id.ibRefresh);

        tvTopTimer = findViewById(R.id.tvTopPlayer);
        tvTopTimer.setClickable(false);
        tvBotTimer = findViewById(R.id.tvBotPlayer);
        tvBotTimer.setClickable(false);

        tvTopPlayerMovesCount = findViewById(R.id.tvTopPlayerMovesCount);
        tvTopPlayerMovesCount.setText(getString(R.string.moves) + topPlayerMovesCount);
        tvBotPlayerMovesCount = findViewById(R.id.tvBotPlayerMovesCount);
        tvBotPlayerMovesCount.setText(getString(R.string.moves)+ botPlayerMovesCount);

        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        buttonSound = soundPool.load(getApplicationContext(), R.raw.clock_button, 1);

        shpSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        long start_time = shpSettings.getLong(APP_PREFERENCES_START_TIME, DEFAULT_START_TIME);
        long min = start_time/(1000*60);
        long sec = (start_time/1000) - min*60;
        String text;
        if (sec < 10){
             text = min + ":0" + sec;
        } else  text = min + ":" + sec;
        tvTopTimer.setText(text);
        tvBotTimer.setText(text);

        SharedPreferences.Editor editor = shpSettings.edit();
        editor.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "start");
        editor.apply();

        botStartTime = shpSettings.getLong(APP_PREFERENCES_START_TIME, DEFAULT_START_TIME);

        Toast.makeText(getApplicationContext(), "onCreate " + shpSettings.getString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "0"), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        if (isTopTimerWorking){
            cdtTopTimer.cancel();
            isTopTimerWorking = false;
            tvTopTimer.setBackgroundResource(R.drawable.tv_shape);
        } else if (isBotTimerWorking){
            cdtBotTimer.cancel();
            isBotTimerWorking = false;
            tvBotTimer.setBackgroundResource(R.drawable.tv_shape);
        }

        ibSettings.setVisibility(View.VISIBLE);
        ibRefresh.setVisibility(View.VISIBLE);

        tvTopTimer.setClickable(false);
        tvBotTimer.setClickable(false);

        ibStart.setImageResource(R.drawable.play_arrow_48);

        SharedPreferences.Editor editor = shpSettings.edit();
        editor.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "resume");
        editor.apply();

        Toast.makeText(getApplicationContext(), "onStop " + shpSettings.getString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "0"), Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (isTopTimerWorking){
            cdtTopTimer.cancel();
            isTopTimerWorking = false;
            tvTopTimer.setBackgroundResource(R.drawable.tv_shape);
        } else if (isBotTimerWorking){
            cdtBotTimer.cancel();
            isBotTimerWorking = false;
            tvBotTimer.setBackgroundResource(R.drawable.tv_shape);
        }

        ibSettings.setVisibility(View.VISIBLE);
        ibRefresh.setVisibility(View.VISIBLE);

        tvTopTimer.setClickable(false);
        tvBotTimer.setClickable(false);

        ibStart.setImageResource(R.drawable.play_arrow_48);

        SharedPreferences.Editor editor = shpSettings.edit();
        editor.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "resume");
        editor.apply();

        Toast.makeText(getApplicationContext(), "onPause " + shpSettings.getString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "0"), Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (isTopTimerWorking){
            cdtTopTimer.cancel();
            isTopTimerWorking = false;
            tvTopTimer.setBackgroundResource(R.drawable.tv_shape);
        } else if (isBotTimerWorking){
            cdtBotTimer.cancel();
            isBotTimerWorking = false;
            tvBotTimer.setBackgroundResource(R.drawable.tv_shape);
        }

        SharedPreferences.Editor editor = shpSettings.edit();
        editor.putLong(APP_PREFERENCES_TOP_START_TIME, shpSettings.getLong(APP_PREFERENCES_START_TIME, 0));
        editor.putLong(APP_PREFERENCES_BOT_START_TIME, shpSettings.getLong(APP_PREFERENCES_START_TIME, 0));
        editor.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "start");
        editor.apply();
        Toast.makeText(getApplicationContext(), "onDestroy " + shpSettings.getString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "0"), Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public void onTopPlayerClick(View view) {
        isTopTimerWorking = false;
        tvTopTimer.setClickable(false);
        tvBotTimer.setClickable(true);
        cdtTopTimer.cancel();

        soundPool.play(buttonSound, 1, 1, 1, 0, 1);

        botPlayerMovesCount++;
        tvBotPlayerMovesCount.setText(getString(R.string.moves) + botPlayerMovesCount);

        SharedPreferences.Editor editor = shpSettings.edit();
        editor.putString(APP_PREFERENCES_LAST_ACTIVE, "bot");
        editor.apply();

        tvTopTimer.setBackgroundResource(R.drawable.tv_shape);
        tvBotTimer.setBackgroundResource(R.drawable.tv_shape_active);
        isBotTimerWorking = true;
        cdtBotTimer = new CountDownTimer(botStartTime, INTERVAL_TIME) {
            @Override
            public void onTick(long milliTillFinish) {
                long min = milliTillFinish/(1000*60);
                long sec = (milliTillFinish/1000) - min*60;
                String text;
                if (sec < 10){
                    text = min + ":0" + sec;
                } else text = min + ":" + sec;
                tvBotTimer.setText(text);

                SharedPreferences.Editor editor = shpSettings.edit();
                editor.putLong(APP_PREFERENCES_BOT_START_TIME, milliTillFinish);
                editor.apply();
            }

            @Override
            public void onFinish() {
                ibSettings.setVisibility(View.VISIBLE);
                ibRefresh.setVisibility(View.VISIBLE);

                tvTopTimer.setClickable(false);
                tvBotTimer.setClickable(false);

                ibStart.setImageResource(R.drawable.play_arrow_48);
                tvBotTimer.setBackgroundResource(R.drawable.tv_shape_end);
            }
        }.start();

        topStartTime = shpSettings.getLong(APP_PREFERENCES_TOP_START_TIME, DEFAULT_START_TIME) + shpSettings.getLong(APP_PREFERENCES_BONUS_TIME, DEFAULT_BONUS_TIME);
        long min = topStartTime/(1000*60);
        long sec = (topStartTime/1000) - min*60;
        String text;
        if (sec < 10){
            text = min + ":0" + sec;
        } else text = min + ":" + sec;
        tvTopTimer.setText(text);

    }

    public void onBotPlayerClick(View view) {
        isBotTimerWorking = false;
        tvTopTimer.setClickable(true);
        tvBotTimer.setClickable(false);
        cdtBotTimer.cancel();

        soundPool.play(buttonSound, 1, 1, 1, 0, 1);

        topPlayerMovesCount++;
        tvTopPlayerMovesCount.setText(getString(R.string.moves) + topPlayerMovesCount);

        SharedPreferences.Editor editor = shpSettings.edit();
        editor.putString(APP_PREFERENCES_LAST_ACTIVE, "top");
        editor.apply();

        tvBotTimer.setBackgroundResource(R.drawable.tv_shape);
        tvTopTimer.setBackgroundResource(R.drawable.tv_shape_active);
        isTopTimerWorking = true;
        cdtTopTimer = new CountDownTimer(topStartTime, INTERVAL_TIME) {
            @Override
            public void onTick(long milliTillFinish) {
                long min = milliTillFinish/(1000*60);
                long sec = (milliTillFinish/1000) - min*60;
                String text;
                if (sec < 10){
                    text = min + ":0" + sec;
                } else text = min + ":" + sec;
                tvTopTimer.setText(text);

                SharedPreferences.Editor editor = shpSettings.edit();
                editor.putLong(APP_PREFERENCES_TOP_START_TIME, milliTillFinish);
                editor.apply();
            }

            @Override
            public void onFinish() {
                ibSettings.setVisibility(View.VISIBLE);
                ibRefresh.setVisibility(View.VISIBLE);

                tvTopTimer.setClickable(false);
                tvBotTimer.setClickable(false);

                ibStart.setImageResource(R.drawable.play_arrow_48);
                tvTopTimer.setBackgroundResource(R.drawable.tv_shape_end);
            }
        }.start();

        botStartTime = shpSettings.getLong(APP_PREFERENCES_BOT_START_TIME, DEFAULT_START_TIME) + shpSettings.getLong(APP_PREFERENCES_BONUS_TIME, DEFAULT_BONUS_TIME);
        long min = botStartTime/(1000*60);
        long sec = (botStartTime/1000) - min*60;
        String text;
        if (sec < 10){
            text = min + ":0" + sec;
        } else text = min + ":" + sec;
        tvBotTimer.setText(text);
    }

    public void onButtonSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onButtonStartOrPauseClick(View view) {
        if (shpSettings.getString(APP_PREFERENCES_CONTROL_BUTTON_STATE, DEFAULT_CONTROL_BUTTON_STATE).contains("start")){
            isTopTimerWorking = true;
            topPlayerMovesCount++;
            tvTopPlayerMovesCount.setText(getString(R.string.moves) + topPlayerMovesCount);
            SharedPreferences.Editor editor = shpSettings.edit();
            editor.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "pause");
            editor.putString(APP_PREFERENCES_LAST_ACTIVE, "top");
            editor.apply();

            ibSettings.setVisibility(View.INVISIBLE);
            ibRefresh.setVisibility(View.INVISIBLE);

            ibStart.setImageResource(R.drawable.pause_48);

            tvTopTimer.setClickable(true);
            tvTopTimer.setBackgroundResource(R.drawable.tv_shape_active);
            cdtTopTimer = new CountDownTimer(shpSettings.getLong(APP_PREFERENCES_START_TIME, DEFAULT_START_TIME), INTERVAL_TIME) {
                @Override
                public void onTick(long milliTillFinish) {
                    long min = milliTillFinish/(1000*60);
                    long sec = (milliTillFinish/1000) - min*60;
                    String text;
                    if (sec < 10){
                        text = min + ":0" + sec;
                    } else text = min + ":" + sec;
                    tvTopTimer.setText(text);

                    SharedPreferences.Editor editor = shpSettings.edit();
                    editor.putLong(APP_PREFERENCES_TOP_START_TIME, milliTillFinish);
                    editor.apply();
                }

                @Override
                public void onFinish() {
                    ibSettings.setVisibility(View.VISIBLE);
                    ibRefresh.setVisibility(View.VISIBLE);

                    tvTopTimer.setClickable(false);
                    tvBotTimer.setClickable(false);

                    ibStart.setImageResource(R.drawable.play_arrow_48);
                    tvTopTimer.setBackgroundResource(R.drawable.tv_shape_end);
                }
            }.start();
        }
        else if (shpSettings.getString(APP_PREFERENCES_CONTROL_BUTTON_STATE, DEFAULT_CONTROL_BUTTON_STATE).contains("pause")){
            if (isTopTimerWorking){
                cdtTopTimer.cancel();
                isTopTimerWorking = false;
                tvTopTimer.setBackgroundResource(R.drawable.tv_shape);
            } else if (isBotTimerWorking){
                cdtBotTimer.cancel();
                isBotTimerWorking = false;
                tvBotTimer.setBackgroundResource(R.drawable.tv_shape);
            }

            ibSettings.setVisibility(View.VISIBLE);
            ibRefresh.setVisibility(View.VISIBLE);

            tvTopTimer.setClickable(false);
            tvBotTimer.setClickable(false);

            ibStart.setImageResource(R.drawable.play_arrow_48);

            SharedPreferences.Editor editor = shpSettings.edit();
            editor.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "resume");
            editor.apply();

        }
        else if (shpSettings.getString(APP_PREFERENCES_CONTROL_BUTTON_STATE, DEFAULT_CONTROL_BUTTON_STATE).contains("resume")){
            SharedPreferences.Editor editor = shpSettings.edit();
            editor.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "pause");
            editor.apply();

            ibSettings.setVisibility(View.INVISIBLE);
            ibRefresh.setVisibility(View.INVISIBLE);

            ibStart.setImageResource(R.drawable.pause_48);

            if (shpSettings.getString(APP_PREFERENCES_LAST_ACTIVE, "").contains("top")){
                editor = shpSettings.edit();
                editor.putString(APP_PREFERENCES_LAST_ACTIVE, "top");
                editor.apply();

                isTopTimerWorking = true;
                tvTopTimer.setClickable(true);

                tvTopTimer.setBackgroundResource(R.drawable.tv_shape_active);
                cdtTopTimer = new CountDownTimer(shpSettings.getLong(APP_PREFERENCES_TOP_START_TIME, DEFAULT_START_TIME), INTERVAL_TIME) {
                    @Override
                    public void onTick(long milliTillFinish) {
                        long min = milliTillFinish/(1000*60);
                        long sec = (milliTillFinish/1000) - min*60;
                        String text;
                        if (sec < 10){
                            text = min + ":0" + sec;
                        } else text = min + ":" + sec;
                        tvTopTimer.setText(text);

                        SharedPreferences.Editor editor = shpSettings.edit();
                        editor.putLong(APP_PREFERENCES_TOP_START_TIME, milliTillFinish);
                        editor.apply();
                    }

                    @Override
                    public void onFinish() {
                        ibSettings.setVisibility(View.VISIBLE);
                        ibRefresh.setVisibility(View.VISIBLE);

                        tvTopTimer.setClickable(false);
                        tvBotTimer.setClickable(false);

                        ibStart.setImageResource(R.drawable.play_arrow_48);
                        tvTopTimer.setBackgroundResource(R.drawable.tv_shape_end);
                    }
                }.start();
            } else if (shpSettings.getString(APP_PREFERENCES_LAST_ACTIVE, "").contains("bot")){
                editor = shpSettings.edit();
                editor.putString(APP_PREFERENCES_LAST_ACTIVE, "bot");
                editor.apply();

                isBotTimerWorking = true;
                tvBotTimer.setClickable(true);

                tvBotTimer.setBackgroundResource(R.drawable.tv_shape_active);
                cdtBotTimer = new CountDownTimer(shpSettings.getLong(APP_PREFERENCES_BOT_START_TIME, DEFAULT_START_TIME), INTERVAL_TIME) {
                    @Override
                    public void onTick(long milliTillFinish) {
                        long min = milliTillFinish/(1000*60);
                        long sec = (milliTillFinish/1000) - min*60;
                        String text;
                        if (sec < 10){
                            text = min + ":0" + sec;
                        } else text = min + ":" + sec;
                        tvBotTimer.setText(text);

                        SharedPreferences.Editor editor = shpSettings.edit();
                        editor.putLong(APP_PREFERENCES_BOT_START_TIME, milliTillFinish);
                        editor.apply();
                    }

                    @Override
                    public void onFinish() {
                        ibSettings.setVisibility(View.VISIBLE);
                        ibRefresh.setVisibility(View.VISIBLE);

                        tvTopTimer.setClickable(false);
                        tvBotTimer.setClickable(false);

                        ibStart.setImageResource(R.drawable.play_arrow_48);
                        tvBotTimer.setBackgroundResource(R.drawable.tv_shape_end);
                    }
                }.start();
            }

        }
    }

    public void onButtonRefreshClick(View view) {
        if (isTopTimerWorking){
            cdtTopTimer.cancel();
            isTopTimerWorking = false;
            tvTopTimer.setBackgroundResource(R.drawable.tv_shape);
        } else if (isBotTimerWorking){
            cdtBotTimer.cancel();
            isBotTimerWorking = false;
            tvBotTimer.setBackgroundResource(R.drawable.tv_shape);
        }

        tvTopTimer.setClickable(false);
        tvBotTimer.setClickable(false);

        topPlayerMovesCount = 0;
        botPlayerMovesCount = 0;
        tvTopPlayerMovesCount.setText(getString(R.string.moves) + topPlayerMovesCount);
        tvBotPlayerMovesCount.setText(getString(R.string.moves) + botPlayerMovesCount);

        SharedPreferences.Editor editor = shpSettings.edit();
        editor.putString(APP_PREFERENCES_LAST_ACTIVE, "");
        editor.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "start");
        editor.putLong(APP_PREFERENCES_TOP_START_TIME, shpSettings.getLong(APP_PREFERENCES_START_TIME, 0));
        editor.putLong(APP_PREFERENCES_BOT_START_TIME, shpSettings.getLong(APP_PREFERENCES_START_TIME, 0));
        editor.apply();

        long start_time = shpSettings.getLong(APP_PREFERENCES_START_TIME, DEFAULT_START_TIME);
        long min = start_time/(1000*60);
        long sec = (start_time/1000) - min*60;
        String text;
        if (sec < 10){
            text = min + ":0" + sec;
        } else text = min + ":" + sec;
        tvTopTimer.setText(text);
        tvBotTimer.setText(text);
    }

    public void onTopTimerStart(){

    }

    public void onBotTimerStart(){

    }
}
