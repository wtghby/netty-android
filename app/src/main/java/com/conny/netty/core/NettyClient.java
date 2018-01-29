package com.conny.netty.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Administrator on 2018/1/13 0013.
 */
public class NettyClient extends Thread {

    private String mHost;
    private int mPort;

    private NettyConnector mConnector;

    public NettyClient(NettyConnector connector, String host, int port) {
        mConnector = connector;
        mHost = host;
        mPort = port;
    }

    @Override
    public void run() {
        try {
            connect(mHost, mPort);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connect(String host, int port) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap boot = new Bootstrap();
            boot.group(workerGroup);
            boot.channel(NioSocketChannel.class);
            boot.remoteAddress(host, port);
            boot.option(ChannelOption.SO_KEEPALIVE, true);
            boot.handler(new NettyClientInitializer(mConnector));

            ChannelFuture cf = boot.connect().sync();
            cf.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
