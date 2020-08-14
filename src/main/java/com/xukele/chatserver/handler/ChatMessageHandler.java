package com.xukele.chatserver.handler;

import com.xukele.chatserver.protocol.pojo.ChatMessagePojo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class ChatMessageHandler extends SimpleChannelInboundHandler<ChatMessagePojo.ChatMessage> {

    // 定义channel组管理链接上的client;
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 链接建立 时执行;
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush(LocalDateTime.now() + " 客户端加入 " + ctx.channel().remoteAddress() + "\n");
        channelGroup.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush(LocalDateTime.now() + " 客户端离开 " + ctx.channel().remoteAddress() + "\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端上线:" + ctx.channel().remoteAddress());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessagePojo.ChatMessage msg) throws Exception {
        Channel current = ctx.channel();
        channelGroup.forEach(ch -> {
            if (ch != current) {
                ChatMessagePojo.ChatMessage build = ChatMessagePojo.ChatMessage.newBuilder().setId(1).setMess("客户端发言:" + msg.getMess()).build();
                ch.writeAndFlush(build);
            } else {
                ChatMessagePojo.ChatMessage build = ChatMessagePojo.ChatMessage.newBuilder().setId(1).setMess("you发言:" + msg.getMess()).build();
                ch.writeAndFlush(build);
            }

        });

    }
}
