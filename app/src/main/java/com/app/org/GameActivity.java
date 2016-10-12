package com.app.org;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import org.ros.node.NodeListener;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;

import sensor_msgs.CompressedImage;


public class GameActivity extends RosActivity {
    public static LogicHandler logicHandler;
    private RosCameraPreviewView cameraView;
    private RosImageView<sensor_msgs.CompressedImage> victimView;
    Fragment fragment;

    public GameActivity() {
        super("ROS Galgeleg", "ROS Galgeleg");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        logicHandler = new LogicHandler(this);
        victimView = (RosImageView<CompressedImage>) findViewById(R.id.ros_image_view_right);
        victimView.setTopicName("/usb_cam/image_raw/compressed");
        victimView.setMessageType(sensor_msgs.CompressedImage._TYPE);
        victimView.setMessageToBitmapCallable(new BitmapFromCompressedImage());
        cameraView = (RosCameraPreviewView) findViewById(R.id.ros_image_view);

        if (savedInstanceState == null) {
            getFragmentManager();
            fragment = new startFragment();
            Bundle arg = new Bundle();
            arg.putString("RosMaster", "TEST");
            fragment.setArguments(arg);
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentindhold, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @SuppressWarnings("deprecation")
    protected void init(NodeMainExecutor nodeMainExecutor) {
        int cameraId = 1;
        Camera cam = Camera.open(cameraId);
        cam.setDisplayOrientation(90);
        cameraView.setCamera(cam);
        NodeConfiguration nodeConfiguration =
                NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
        nodeConfiguration.setMasterUri(getMasterUri());

        nodeMainExecutor.execute(cameraView, nodeConfiguration);
        nodeMainExecutor.execute(victimView, nodeConfiguration.setNodeName("android/video_view"));
    }
}