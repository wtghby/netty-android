package com.conny.netty.core;

import com.conny.netty.listener.OnNettyListener;
import com.conny.netty.proto.DataProto;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    private ClientHandler mHandler;

    public NettyClientInitializer(NettyConnector connector) {
        mHandler = new ClientHandler(connector);
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(DataProto.Data.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(mHandler);

    }

}
