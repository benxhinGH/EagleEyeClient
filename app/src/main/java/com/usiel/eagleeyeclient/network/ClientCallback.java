package com.usiel.eagleeyeclient.network;

import com.usiel.eagleeyeclient.entity.Spy;

import java.io.File;
import java.util.List;

public interface ClientCallback {
    void spyList(List<Spy> spies);
    void screenShot(int transactionId, File file);
    void screenShotResponse(int transactionId);
}
