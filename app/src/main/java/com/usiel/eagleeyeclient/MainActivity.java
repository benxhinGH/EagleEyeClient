package com.usiel.eagleeyeclient;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.btn_connect)
    Button btnConnect;
//    @BindView(R.id.btn_capture)
    Button btnCapture;
//    @BindView(R.id.tv_console)
    TextView console;
//    @BindView(R.id.pic_image)
    ImageView imageView;

    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);
        btnConnect=findViewById(R.id.btn_connect);
        btnCapture=findViewById(R.id.btn_capture);
        console=findViewById(R.id.tv_console);
        imageView=findViewById(R.id.pic_image);


        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnConnect();
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnCapture();
            }
        });
    }

    //@OnClick(R.id.btn_connect)
    void onClickBtnConnect(){


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket= SocketFactory.getDefault().createSocket(Config.SERVER_ADDRESS,Config.PORT);


                    byte[] b=new byte[1];
                    b[0]=0;
                    socket.getOutputStream().write(b);

                    String fileName=System.currentTimeMillis()+Config.PIC_SUFFIX;
                    String filePath=getExternalCacheDir().getPath()+fileName;
                    Log.d("MainActivity","filePath is :"+filePath);
                    final File file=new File(filePath);
                    BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
                    BufferedInputStream bis=new BufferedInputStream(socket.getInputStream());
                    byte[] buffer=new byte[4096];
                    int r;
                    while((r=bis.read(buffer))!=-1) {
                        bos.write(buffer, 0, r);
                    }
                    bos.flush();
                    bos.close();
                    bis.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(
                                    ImageLoader.decodeSampledBitmapFromFile(file,imageView.getWidth(),imageView.getHeight())
                            );
                        }
                    });
                    socket.close();



                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();










    }

    //@OnClick(R.id.btn_capture)
    void onClickBtnCapture(){



    }





}
