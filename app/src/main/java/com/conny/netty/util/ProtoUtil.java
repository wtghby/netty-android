package com.conny.netty.util;

import com.google.protobuf.ByteString;

public class ProtoUtil {

    public static ByteString bytes2String(byte[] data) {
        if (data == null || data.length <= 0) {
            return null;
        }

        return ByteString.copyFrom(data);
    }

    public static byte[] string2Bytes(ByteString data) {
        if (data == null || data.size() == 0)
            return null;

        return data.toByteArray();
    }

}
