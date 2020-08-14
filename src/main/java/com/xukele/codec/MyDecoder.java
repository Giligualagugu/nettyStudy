package com.xukele.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyDecoder extends ReplayingDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("服务器解码调用");
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
