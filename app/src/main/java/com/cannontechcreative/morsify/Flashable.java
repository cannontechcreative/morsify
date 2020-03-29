package com.cannontechcreative.morsify;

import android.hardware.camera2.CameraAccessException;

public interface Flashable {
    void flash(String[] message) throws CameraAccessException, InterruptedException;
}
