package com.kolotseyd.chesstimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    EditText etAddedMinutes, etAddedSeconds, etAddedBonusSeconds;
    Button bCreate;

    SharedPreferences shpSettings;

    long addedMinutes = 0;
    long addedSeconds = 0;
    long addedBonusSeconds = 0;

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

        etAddedMinutes = view.findViewById(R.id.etAddedMinutes);
        etAddedSeconds = view.findViewById(R.id.etAddedSeconds);
        etAddedBonusSeconds = view.findViewById(R.id.etAddedBonusSeconds);

        bCreate = view.findViewById(R.id.bCreate);

        shpSettings = requireActivity().getSharedPreferences("my_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shpSettings.edit();

        bCreate.setOnClickListener(view17 -> {
            if (String.valueOf(etAddedMinutes.getText()).equals("") || Integer.parseInt(String.valueOf(etAddedMinutes.getText())) == 0){
                Toast.makeText(getContext(), R.string.value_must_be_more_than_0, Toast.LENGTH_SHORT).show();
            } else {
                addedMinutes = Long.parseLong(String.valueOf(etAddedMinutes.getText()));
                if (String.valueOf(etAddedSeconds.getText()).equals("")){
                    addedSeconds = 0;
                } else {
                    addedSeconds = Long.parseLong(String.valueOf(etAddedSeconds.getText()));
                }

                if (String.valueOf(etAddedBonusSeconds.getText()).equals("")){
                    addedBonusSeconds = 0;
                } else {
                    addedBonusSeconds = Long.parseLong(String.valueOf(etAddedBonusSeconds.getText()));
                }

                long start_time = (addedMinutes*60000) + addedSeconds*1000;
                long bonus_time = addedBonusSeconds*1000;

                editor.putLong(APP_PREFERENCES_START_TIME, start_time);
                editor.putLong(APP_PREFERENCES_TOP_START_TIME, start_time);
                editor.putLong(APP_PREFERENCES_BOT_START_TIME, start_time);
                editor.putLong(APP_PREFERENCES_BONUS_TIME, bonus_time);
                editor.apply();
                dismiss();
                etAddedMinutes.setHintTextColor(getResources().getColor(R.color.elements));
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), getString(R.string.selected_settings) + getString(R.string.start_time_equals)
                        + addedMinutes + ":" + addedSeconds + " " + getString(R.string.timer_min) + "; " + getString(R.string.bonus_time_equals) + addedBonusSeconds + " " + getString(R.string.timer_sec) + ".", Toast.LENGTH_LONG).show();

            }

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
