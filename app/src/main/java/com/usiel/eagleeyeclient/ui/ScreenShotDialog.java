package com.usiel.eagleeyeclient.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.usiel.eagleeyeclient.R;
import com.usiel.eagleeyeclient.utils.ImageLoader;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScreenShotDialog extends AlertDialog.Builder {
    private final String TAG = ScreenShotDialog.class.getSimpleName();

    private Context context;

    @BindView(R.id.img_screenshot)
    ImageView ivScreenShot;
    @BindView(R.id.btn_refresh)
    Button btnRefresh;

    private int transactionId;

    public ScreenShotDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_screenshot, null, false);
        setView(dialogView);
        ButterKnife.bind(this, dialogView);
    }

    public void setImage(File file){
        ivScreenShot.setImageBitmap(
                ImageLoader.decodeSampledBitmapFromFile(file,ivScreenShot.getWidth(),ivScreenShot.getHeight())
        );
    }

    public void setOnClickBtnRefreshListener(View.OnClickListener onClickBtnRefreshListener){
        btnRefresh.setOnClickListener(onClickBtnRefreshListener);
    }

    public int getTransactionId(){
        return transactionId;
    }
    public void setTransactionId(int transactionId){
        this.transactionId = transactionId;
    }
}
