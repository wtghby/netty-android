package com.conny.netty.core;

import com.conny.netty.listener.OnNettyListener;
import com.conny.netty.proto.DataProto;

/**
 * Desc:
 * Created by zhanghui on 2018/1/23.
 */

public class NettyManager {

    private static NettyClient mClient;
    private static NettyConnector mConnector;
    private static String mHost;
    private static int mPort;

    public static void init(String host, int port) {
        mHost = host;
        mPort = port;

        if (mConnector == null) {
            mConnector = new NettyConnector();
        }
        mClient = new NettyClient(mConnector, host, port);
        mClient.start();
    }

    public static void register(OnNettyListener listener) {
        if (mConnector != null) {
            mConnector.setListener(listener);
        }
    }

    public static void send(DataProto.Data data) {
        if (mConnector != null) {
            if (mConnector.active()) {
                mConnector.write(data);
            } else {
                init(mHost, mPort);
                send(data);
            }
        }
    }

    public static void disconnect() {
        if (mConnector != null) {
            mConnector.disconnect();
        }
    }
}
