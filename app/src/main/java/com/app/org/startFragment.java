package com.app.org;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import org.ros.address.InetAddressFactory;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.node.NodeConfiguration;

import sensor_msgs.CompressedImage;

public class startFragment extends android.app.Fragment implements View.OnClickListener, Runnable {

    Handler handler = new Handler();
    private Button restartButton;
    private Button settingsButton;
    private TextView inputView;
    private RosImageView<CompressedImage> victimView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arg = getArguments();
        String RosMaster = arg.getString("RosMaster");
        Log.d("ROS", RosMaster);
        restartButton = (Button) getActivity().findViewById(R.id.restartButton);
        settingsButton = (Button) getActivity().findViewById(R.id.settingsButton);
        inputView = (TextView) getActivity().findViewById(R.id.inputText);
        restartButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        inputView.setOnClickListener(this);


        if (savedInstanceState == null) {
            handler.postDelayed(this, 3000);
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == inputView && inputView.getText().length() > 0) {
            GameActivity.logicHandler.checkAnswer();
            InputMethodManager inputMethodManager = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else if (view == settingsButton) {
            Intent start = new Intent(getActivity(), OptionsActivity.class);
            startActivity(start);
        } else if (view == restartButton)
            GameActivity.logicHandler.restart();
    }

    @Override
    public void run() {

    }
}
