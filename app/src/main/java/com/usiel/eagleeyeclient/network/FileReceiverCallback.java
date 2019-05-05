package com.usiel.eagleeyeclient.network;

import java.io.File;

public interface FileReceiverCallback {
    void ready(int port);
    void currentProgress(int progress);
    void finish(File file);
}
