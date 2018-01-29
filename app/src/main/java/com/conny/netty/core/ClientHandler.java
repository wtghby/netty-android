package com.conny.netty.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.conny.netty.listener.OnNettyListener;
import com.conny.netty.proto.DataProto;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class ClientHandler extends SimpleChannelInboundHandler<DataProto.Data> {

    Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (mConnector != null) {
                if (msg.what == 1) {
                    OnNettyListener listener = mConnector.getListener();
                    Object data = msg.obj;
                    if (listener != null && data != null && data instanceof DataProto.Data) {
                        listener.onReceive((DataProto.Data) data);
                    }
                }
            }
        }
    };

    private NettyConnector mConnector;

    public ClientHandler(NettyConnector connector) {
        mConnector = connector;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataProto.Data data) {
        if (data == null) {
            return;
        }

        if (mConnector != null) {

            if (mConnector.getChannel() == null) {
                mConnector.setChannel(channelHandlerContext);
            }

            Message message = Message.obtain();
            message.what = 1;
            message.obj = data;

            mainHandler.sendMessage(message);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        if (mConnector != null) {
            mConnector.setChannel(ctx);
        }

    }
}
