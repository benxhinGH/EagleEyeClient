package com.usiel.eagleeyeclient.network;

import com.usiel.eagleeyeclient.entity.Spy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Client {
    private final String remoteIp;
    private final int remotePort;

    private Channel channel;
    private ClientHandler clientHandler;

    private ClientCallback callback;

    public Client(String remoteIp, int remotePort, ClientCallback callback){
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
        this.callback = callback;
    }

    public void start(){
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            clientHandler = new ClientHandler(callback);
                            ch.pipeline().addLast(new ProtocolEncoder(), new ProtocolDecoder(), clientHandler);
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.connect(remoteIp, remotePort).sync();
            channel = f.channel();
            f.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
            worker.shutdownGracefully();
        }
    }

    public void getSpyList(){
        clientHandler.getSpyList();
    }

    public void getScreenShot(int transactionId, Spy spy){
        clientHandler.getScreenShot(transactionId, spy);
    }

    public void close(){
        if(channel.isOpen()){
            channel.close();
        }
    }
}
