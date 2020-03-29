package com.cannontechcreative.morsify;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import java.util.Arrays;

// Represents a message in morse code
public class MorseCodeMessage implements Flashable {

    private Context mContext;
    public String[] morseMessage;
    CameraManager cm;
    String cameraID;
    private long ditLength = 200;
    private long dahLength = 1000;

    MorseCodeMessage(Context mContext, String morseMessage) {
        this.morseMessage = morseMessage.split("");
        this.mContext = mContext;
        this.cm = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        this.cameraID =  setCameraId();
    }

    public String setCameraId() {
        try {
            return this.cm.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void dit() throws CameraAccessException {
        //TODO
        cm.setTorchMode(cameraID, true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                try {
                    cm.setTorchMode(cameraID, false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        }, ditLength);
    }
    public void dah() throws CameraAccessException {
        //TODO
        cm.setTorchMode(cameraID, true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                try {
                    cm.setTorchMode(cameraID, false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        }, dahLength);
    }

    public void flash(final String[] message) throws CameraAccessException {
        if(message.length == 0) {
            return;
        }
        final Handler handler = new Handler();
        if(message[0].equals("-")) {
            dah();
            handler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        flash(Arrays.copyOfRange(message, 1, message.length));
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }, dahLength);
        }
        if(message[0].equals(".")) {
            dit();
            handler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        flash(Arrays.copyOfRange(message, 1, message.length));
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }, ditLength);
        }

    }

}
