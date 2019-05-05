package com.usiel.eagleeyeclient.network;

import android.util.Log;

import com.usiel.eagleeyeclient.entity.Spy;
import com.usiel.eagleeyeclient.protocol.BasicProtocol;
import com.usiel.eagleeyeclient.protocol.ErrorCode;
import com.usiel.eagleeyeclient.protocol.FileSendRequest;
import com.usiel.eagleeyeclient.protocol.MsgId;
import com.usiel.eagleeyeclient.protocol.Parser;
import com.usiel.eagleeyeclient.protocol.ProtocolFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final String TAG = ClientHandler.class.getSimpleName();
    private ClientCallback callback;
    private ChannelHandlerContext channelHandlerContext;

    private ExecutorService executor;

    public ClientHandler(ClientCallback callback){
        this.callback = callback;
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelHandlerContext = ctx;
        BasicProtocol identificationRequest = ProtocolFactory.createIdentificationRequest();
        ctx.writeAndFlush(identificationRequest);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BasicProtocol basicProtocol = (BasicProtocol) msg;
        int transactionId = basicProtocol.getTransactionId();
        switch (basicProtocol.getMsgId()){
            case MsgId.SPY_LIST_RESPONSE:
                List<Spy> spyList = Parser.parseSpyListResponse(basicProtocol);
                callback.spyList(spyList);
                break;
            case MsgId.FILE_SEND_REQUEST:
                FileSendRequest fileSendRequest = Parser.parseFileSendRequest(basicProtocol.getDataArray());
                FileReceiver fileReceiver = new FileReceiver(fileSendRequest.getFileName(), fileSendRequest.getFileLength(), new FileReceiverCallback() {
                    @Override
                    public void ready(int port) {
                        BasicProtocol fileSendResponse = ProtocolFactory.createFileSendResponse(ErrorCode.SUCCESS, port);
                        ctx.writeAndFlush(fileSendResponse);
                    }

                    @Override
                    public void currentProgress(int progress) {

                    }

                    @Override
                    public void finish(File file) {
                        callback.screenShot(transactionId, file);
                    }
                });
                executor.execute(fileReceiver);
                break;
            case MsgId.SCREENSHOT_RESPONSE:
                callback.screenShotResponse(transactionId);
                break;
            case MsgId.IDENTIFICATION_RESPONSE:
                if(basicProtocol.getErrorCode() != ErrorCode.SUCCESS){
                    Log.e(TAG, "identification failure!");
                    ctx.close();
                }
                break;
                default:
                    Log.e(TAG, "unknown msg");
                    break;
        }
    }

    public void getSpyList(){
        BasicProtocol spyListRequest = ProtocolFactory.createSpyListRequest();
        channelHandlerContext.writeAndFlush(spyListRequest);
    }

    public void getScreenShot(int transactionId, Spy spy){
        BasicProtocol screenShotRequest = ProtocolFactory.createScreenShotRequest(transactionId, spy);
        channelHandlerContext.writeAndFlush(screenShotRequest);
    }
}
