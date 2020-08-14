package com.xukele.chatserver.handler;

import com.xukele.chatserver.protocol.pojo.ChatMessagePojo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientMessageHandler extends SimpleChannelInboundHandler<ChatMessagePojo.ChatMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessagePojo.ChatMessage msg) throws Exception {
        System.out.println(msg.getMess());


    }
}
