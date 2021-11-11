package com.kolotseyd.chesstimer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class CreateCustomModeFragment extends DialogFragment {

    public static final String APP_PREFERENCES_START_TIME = "start_time";
    public static final String APP_PREFERENCES_TOP_START_TIME = "top_start_time";
    public static final String APP_PREFERENCES_BOT_START_TIME = "bot_start_time";
    public static final String APP_PREFERENCES_BONUS_TIME = "bonus_time";

    TextView tvAddedMinutes, tvAddedSeconds, tvAddedBonusSeconds;
    ImageButton ibAddMinutes, ibRemoveMinutes, ibAddSeconds, ibRemoveSeconds, ibAddBonusSeconds, ibRemoveBonusSeconds;
    Button bCreate;

    SharedPreferences shpSettings;

    long addedMinutes = 0, addedSeconds = 0, addedBonusSeconds = 0;
    long minValue = 0;
    long secondsMaxValue = 59;
    CountDownTimer cdt;
    //boolean isStillPressed = true;

    public CreateCustomModeFragment() {
    }

    public static CreateCustomModeFragment newInstance(String title){
        CreateCustomModeFragment frag = new CreateCustomModeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_create_custom_mode, container);

        shpSettings = requireActivity().getSharedPreferences("my_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shpSettings.edit();

        tvAddedMinutes = view.findViewById(R.id.tvAddedMinutes);
        tvAddedMinutes.setText(addedMinutes + " " + getString(R.string.timer_min));
        tvAddedSeconds = view.findViewById(R.id.tvAddedSeconds);
        tvAddedSeconds.setText(addedSeconds + " " + getString(R.string.timer_sec));
        tvAddedBonusSeconds = view.findViewById(R.id.tvAddedBonusSeconds);
        tvAddedBonusSeconds.setText(addedBonusSeconds + " " + getString(R.string.timer_sec));

        ibAddMinutes = view.findViewById(R.id.ibAddMinutes);
        ibRemoveMinutes = view.findViewById(R.id.ibRemoveMinutes);
        ibAddSeconds = view.findViewById(R.id.ibAddSeconds);
        ibRemoveSeconds = view.findViewById(R.id.ibRemoveSeconds);
        ibAddBonusSeconds = view.findViewById(R.id.ibAddBonusSeconds);
        ibRemoveBonusSeconds = view.findViewById(R.id.ibRemoveBonusSeconds);

        bCreate = view.findViewById(R.id.bCreate);

        ibAddMinutes.setOnClickListener(view12 -> {
            addedMinutes++;
            tvAddedMinutes.setText(addedMinutes + " " + getString(R.string.timer_min));
        });

        ibRemoveMinutes.setOnClickListener(view1 -> {
            if (addedMinutes != minValue){
                addedMinutes--;
            }
            tvAddedMinutes.setText(addedMinutes + " " + getString(R.string.timer_min));
        });

        ibAddSeconds.setOnClickListener(view13 -> {
            if (addedSeconds != secondsMaxValue){
                addedSeconds++;
            }
            tvAddedSeconds.setText(addedSeconds + " " + getString(R.string.timer_sec));
        });

        ibRemoveSeconds.setOnClickListener(view14 -> {
            if (addedSeconds != minValue){
                addedSeconds--;
            }
            tvAddedSeconds.setText(addedSeconds + " " + getString(R.string.timer_sec));
        });

        ibAddBonusSeconds.setOnClickListener(view15 -> {
            if (addedBonusSeconds != secondsMaxValue){
                addedBonusSeconds++;
            }
            tvAddedBonusSeconds.setText(addedBonusSeconds + " " + getString(R.string.timer_sec));
        });

        ibRemoveBonusSeconds.setOnClickListener(view16 -> {
            if (addedBonusSeconds != minValue){
                addedBonusSeconds--;
            }
            tvAddedBonusSeconds.setText(addedBonusSeconds + " " + getString(R.string.timer_sec));
        });

        bCreate.setOnClickListener(view17 -> {
            long start_time = (addedMinutes*60000) + addedSeconds*1000;
            long bonus_time = addedBonusSeconds*1000;

            editor.putLong(APP_PREFERENCES_START_TIME, start_time);
            editor.putLong(APP_PREFERENCES_TOP_START_TIME, start_time);
            editor.putLong(APP_PREFERENCES_BOT_START_TIME, start_time);
            editor.putLong(APP_PREFERENCES_BONUS_TIME, bonus_time);
            editor.apply();
            dismiss();
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
            Toast.makeText(getContext(), getString(R.string.selected_settings) + getString(R.string.start_time_equals)
                    + addedMinutes + ":" + addedSeconds + " " + getString(R.string.timer_min) + "; " + getString(R.string.bonus_time_equals) + addedBonusSeconds + " " + getString(R.string.timer_sec) + ".", Toast.LENGTH_LONG).show();

        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getString(R.string.create_custom_mode_title);
        Objects.requireNonNull(getDialog()).setTitle(title);
    }


}
