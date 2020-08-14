package com.xukele.packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("建立链接");


        /**
         * 客户端再同一个链接内发送多次信息，会有拆包粘包问题；
         *
         * 如果时建立socket，发送一次信息， 则不会；
         *
         * 如果复用socket， 则需要解决拆包粘包问题；
         */
        for (int i = 0; i < 6; i++) {
            ctx.writeAndFlush("jack1234567890kk ");
        }

//        ctx.writeAndFlush("jack1234567890kk ");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
    }
}
