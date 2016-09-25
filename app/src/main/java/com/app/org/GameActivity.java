package com.app.org;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import org.ros.address.InetAddressFactory;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.android.view.camera.RosCameraPreviewView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import sensor_msgs.CompressedImage;


public class GameActivity extends RosActivity implements View.OnClickListener {
    private LogicHandler logicHandler;
    private TextView inputView;
    private Button restartButton;
    private Button settingsButton;
    private RosCameraPreviewView cameraView;
    private RosImageView<sensor_msgs.CompressedImage> victimView;

    public GameActivity() {
        super("ROS Galgeleg", "ROS Galgeleg");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        logicHandler = new LogicHandler(this);
        restartButton = (Button) findViewById(R.id.restartButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        inputView = (TextView) findViewById(R.id.inputText);
        inputView.setOnClickListener(this);
        restartButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        victimView = (RosImageView<CompressedImage>) findViewById(R.id.ros_image_view_right);
        victimView.setTopicName("/usb_cam/image_raw/compressed");
        victimView.setMessageType(sensor_msgs.CompressedImage._TYPE);
        victimView.setMessageToBitmapCallable(new BitmapFromCompressedImage());
        cameraView = (RosCameraPreviewView) findViewById(R.id.ros_image_view);

    }

    @Override
    public void onClick(View view) {
        if (view == inputView && inputView.getText().length() > 0) {
            logicHandler.checkAnswer();
            InputMethodManager inputMethodManager = (InputMethodManager)
                   view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else if (view == settingsButton) {
            Intent start = new Intent(this, OptionsActivity.class);
            startActivity(start);
        } else if (view == restartButton)
            logicHandler.restart();
    }

    @SuppressWarnings("deprecation")
    protected void init(NodeMainExecutor nodeMainExecutor) {
        int cameraId = 1;
        Camera cam = Camera.open(cameraId);
        cam.setDisplayOrientation(90);
        cameraView.setCamera(cam);
        cameraView.set
        NodeConfiguration nodeConfiguration =
                NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(cameraView, nodeConfiguration);
        nodeMainExecutor.execute(victimView, nodeConfiguration.setNodeName("android/video_view"));
    }
}