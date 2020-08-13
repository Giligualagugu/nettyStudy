package com.xukele.wsc.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 处理websocket 的数据帧
 * <p>
 * TextWebSocketFrame 表示一个文本帧;
 */
@Slf4j
public class MyWebSocketMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("server get message from client:{}", msg.text());
        TextWebSocketFrame frame = new TextWebSocketFrame(LocalDateTime.now() + ": 我是服务器,我收到了你的消息");
        ctx.writeAndFlush(frame);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        log.info("connect build by :{}", ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        log.error("connect error by :{}, cause by :{}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}
