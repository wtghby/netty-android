package com.conny.netty.listener;

import com.conny.netty.proto.DataProto;

/**
 * Desc:
 * Created by zhanghui on 2018/1/23.
 */

public interface OnNettyListener {

    void onReceive(DataProto.Data data);
}
