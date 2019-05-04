package com.usiel.eagleeyeclient.entity;

import android.util.Log;

public class TransactionIdPool {

    final String TAG = TransactionIdPool.class.getSimpleName();
    int capability = 10;
    int[] id;
    private static TransactionIdPool instance;

    private TransactionIdPool(){
        id = new int[capability];
    }

    public static TransactionIdPool getInstance(){
        if(instance == null){
            instance = new TransactionIdPool();
        }
        return instance;
    }

    public int allocate(){
        for(int i = 0; i < capability; i++){
            if(id[i] == 0){
                id[i] = 1;
                return i;
            }
        }
        Log.e(TAG, "no valid task id");
        return -1;
    }

    public void release(int i){
        if(i < 0 || i >capability -1){
            Log.e(TAG, "task id beyond 0~" + capability);
            return;
        }
        id[i] = 0;
    }
}
