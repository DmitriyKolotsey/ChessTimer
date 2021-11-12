package com.kolotseyd.chesstimer;

import static com.kolotseyd.chesstimer.MainActivity.APP_PREFERENCES_BONUS_TIME;
import static com.kolotseyd.chesstimer.MainActivity.APP_PREFERENCES_START_TIME;
import static com.kolotseyd.chesstimer.MainActivity.APP_PREFERENCES_CONTROL_BUTTON_STATE;
import static com.kolotseyd.chesstimer.MainActivity.DEFAULT_BONUS_TIME;
import static com.kolotseyd.chesstimer.MainActivity.DEFAULT_START_TIME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class SelectBonusTimeFragment extends DialogFragment {

    Button b0sec, b1sec, b3sec, b5sec, b7sec, b10sec;

    SharedPreferences shpSettings;

    public SelectBonusTimeFragment() {
    }

    public static SelectBonusTimeFragment newInstance(String title){
        SelectBonusTimeFragment frag = new SelectBonusTimeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_select_bonus_time, container);

        shpSettings = requireActivity().getSharedPreferences("my_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorBonusTime = shpSettings.edit();

        b0sec = view.findViewById(R.id.b0sec);
        b1sec = view.findViewById(R.id.b1sec);
        b3sec = view.findViewById(R.id.b3sec);
        b5sec = view.findViewById(R.id.b5sec);
        b7sec = view.findViewById(R.id.b7sec);
        b10sec = view.findViewById(R.id.b10sec);

        b0sec.setText("+0 \n" + getString(R.string.timer_sec));
        view.findViewById(R.id.b0sec).setOnClickListener(view13 -> {
            editorBonusTime.putLong(APP_PREFERENCES_BONUS_TIME, 0);
            editorBonusTime.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "start");
            editorBonusTime.apply();
            showSelectedSettings();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            dismiss();
        });

        b1sec.setText("+1 \n" + getString(R.string.timer_sec));
        view.findViewById(R.id.b1sec).setOnClickListener(view12 -> {
            editorBonusTime.putLong(APP_PREFERENCES_BONUS_TIME, 1000);
            editorBonusTime.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "start");
            editorBonusTime.apply();
            showSelectedSettings();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            dismiss();
        });

        b3sec.setText("+3 \n" + getString(R.string.timer_sec));
        view.findViewById(R.id.b3sec).setOnClickListener(view1 -> {
            editorBonusTime.putLong(APP_PREFERENCES_BONUS_TIME, 3000);
            editorBonusTime.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "start");
            editorBonusTime.apply();
            showSelectedSettings();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            dismiss();
        });

        b5sec.setText("+5 \n" + getString(R.string.timer_sec));
        view.findViewById(R.id.b5sec).setOnClickListener(view14 -> {
            editorBonusTime.putLong(APP_PREFERENCES_BONUS_TIME, 5000);
            editorBonusTime.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "start");
            editorBonusTime.apply();
            showSelectedSettings();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            dismiss();
        });

        b7sec.setText("+7 \n" + getString(R.string.timer_sec));
        view.findViewById(R.id.b7sec).setOnClickListener(view15 -> {
            editorBonusTime.putLong(APP_PREFERENCES_BONUS_TIME, 7000);
            editorBonusTime.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "start");
            editorBonusTime.apply();
            showSelectedSettings();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            dismiss();
        });

        b10sec.setText("+10 \n" + getString(R.string.timer_sec));
        view.findViewById(R.id.b10sec).setOnClickListener(view16 -> {
            editorBonusTime.putLong(APP_PREFERENCES_BONUS_TIME, 10000);
            editorBonusTime.putString(APP_PREFERENCES_CONTROL_BUTTON_STATE, "start");
            editorBonusTime.apply();
            showSelectedSettings();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            dismiss();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getString(R.string.bonus_time_title);
        Objects.requireNonNull(getDialog()).setTitle(title);
    }

    public void showSelectedSettings(){
        long start_time = shpSettings.getLong(APP_PREFERENCES_START_TIME, DEFAULT_START_TIME);
        long min_s = start_time/(1000*60);
        long sec_s = (start_time/1000) - min_s*60;
        String text_s = String.valueOf(min_s);

        long bonus_time = shpSettings.getLong(APP_PREFERENCES_BONUS_TIME, DEFAULT_BONUS_TIME);
        long min_b = bonus_time/(1000*60);
        long sec_b = (bonus_time/1000) - min_b*60;
        String text_b = String.valueOf(sec_b);
        Toast.makeText(getContext(), getString(R.string.selected_settings) + getString(R.string.start_time_equals) + text_s + " " + getString(R.string.timer_min) + "; " + getString(R.string.bonus_time_equals) + text_b + " " + getString(R.string.timer_sec) + ".", Toast.LENGTH_LONG).show();
    }
}
