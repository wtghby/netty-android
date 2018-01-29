package com.conny.netty.core;

import com.conny.netty.listener.OnNettyListener;
import com.conny.netty.proto.DataProto;

import io.netty.channel.ChannelHandlerContext;

/**
 * Desc:
 * Created by zhanghui on 2018/1/23.
 */

public class NettyConnector {


    private OnNettyListener mListener;
    private ChannelHandlerContext mChannel;

    public OnNettyListener getListener() {
        return mListener;
    }

    public void setListener(OnNettyListener listener) {
        this.mListener = listener;
    }

    public ChannelHandlerContext getChannel() {
        return mChannel;
    }

    public void setChannel(ChannelHandlerContext channel) {
        this.mChannel = channel;
    }

    public void write(final DataProto.Data data) {
        if (mChannel != null || data != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mChannel.writeAndFlush(data);
                }
            }).start();
        }
    }

    public void disconnect() {
        if (mChannel != null) {
            mChannel.disconnect();
        }
    }

    public boolean active() {
        if (mChannel != null) {
            return mChannel.channel().isActive();
        }
        return false;
    }
}
