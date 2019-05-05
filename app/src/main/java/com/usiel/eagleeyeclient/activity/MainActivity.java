package com.usiel.eagleeyeclient.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.usiel.eagleeyeclient.adapter.OnItemClickListener;
import com.usiel.eagleeyeclient.adapter.SpyAdapter;
import com.usiel.eagleeyeclient.entity.Spy;
import com.usiel.eagleeyeclient.entity.TransactionIdPool;
import com.usiel.eagleeyeclient.network.Client;
import com.usiel.eagleeyeclient.network.ClientCallback;
import com.usiel.eagleeyeclient.ui.ScreenShotDialog;
import com.usiel.eagleeyeclient.utils.Config;
import com.usiel.eagleeyeclient.R;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_spy)
    RecyclerView rvSpy;

    private Handler handler = new Handler();


    private SpyAdapter spyAdapter;

    private final int EXTERNAL_STORAGE_REQUEST_CODE = 203;

    private Client client;

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            client.getSpyList();
        }
    };

    private ClientCallback clientCallback = new ClientCallback() {
        @Override
        public void spyList(List<Spy> spies) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    spyAdapter.updateDatas(spies);
                }
            });

        }

        @Override
        public void screenShot(int transactionId, File file) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(transactionId == screenShotDialog.getTransactionId()){
                        closeProgressDialog();
                        if(file == null){
                            Log.e(TAG, "file should not be null!");
                            return;
                        }
                        screenShotDialog.setImage(file);
                        Toast.makeText(MainActivity.this, "refresh success", Toast.LENGTH_SHORT).show();
                    }else {
                        Log.e(TAG, "error when take screenshot");
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        @Override
        public void screenShotResponse(int transactionId) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(transactionId == screenShotDialog.getTransactionId()){
                        progressDialog.setMessage("take screenshot...");
                    }else{
                        Log.e(TAG, "error when take screenshot");
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            showScreenShotDialog(spyAdapter.getDatas().get(position));
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };

    private ScreenShotDialog screenShotDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        spyAdapter = new SpyAdapter(null);
        rvSpy.setLayoutManager(new LinearLayoutManager(this));
        rvSpy.setItemAnimator(new DefaultItemAnimator());
        rvSpy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvSpy.setAdapter(spyAdapter);
        spyAdapter.setOnItemClickListener(onItemClickListener);

        requestExternalStorage();
        createEasyTransferDir();

        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        client = new Client("192.168.1.3", Config.REMOTE_SERVER_PORT, clientCallback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.start();
            }
        }).start();
    }

    private void showScreenShotDialog(Spy spy){
        screenShotDialog = new ScreenShotDialog(this);
        screenShotDialog.setOnClickBtnRefreshListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int transactionId = TransactionIdPool.getInstance().allocate();
                screenShotDialog.setTransactionId(transactionId);
                client.getScreenShot(transactionId, spy);
                showProgressDialog();
            }
        });
        screenShotDialog.show();
    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("waiting...");
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        progressDialog.cancel();
    }

    private void createEasyTransferDir(){
        Config.fileSaveDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EagleEyeClient";
        File file = new File(Config.fileSaveDir);
        if(!file.exists()){
            if(!file.mkdir()){
                Log.e(TAG, "create dirs error!");
                Toast.makeText(this, "create dirs error", Toast.LENGTH_SHORT).show();
            }
        }
        Log.w(TAG, "fileSaveDir:" + Config.fileSaveDir);
    }

    private void requestExternalStorage(){
        if(EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

        }else{
            String[] perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            EasyPermissions.requestPermissions(this, "需要获取外部存储权限", EXTERNAL_STORAGE_REQUEST_CODE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //Toast.makeText(this, "granted" + requestCode, Toast.LENGTH_SHORT).show();
        if(requestCode == EXTERNAL_STORAGE_REQUEST_CODE){
            createEasyTransferDir();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //Toast.makeText(this, "denied" + requestCode, Toast.LENGTH_SHORT).show();
        if(requestCode == EXTERNAL_STORAGE_REQUEST_CODE){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("外部存储权限未取得，即将退出应用")
                    .setPositiveButton("再试一次", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestExternalStorage();
                        }
                    })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }



}
