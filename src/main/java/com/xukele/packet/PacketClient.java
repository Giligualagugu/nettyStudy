package com.xukele.packet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * 多次运行 可以观察到server端的 拆包粘包问题;
 */
@Slf4j
public class PacketClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try (Scanner scanner = new Scanner(System.in)) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class)
                    .group(group)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new StringEncoder())
                                    .addLast(new PacketClientHandler());
                        }
                    });
            ChannelFuture localhost = bootstrap.connect("localhost", PacketServer.SERVER_PORT).sync();
            localhost.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("链接到server :{}", localhost.channel().remoteAddress());
                }
            });
            localhost.channel().closeFuture().sync();
        } finally {

            group.shutdownGracefully();
        }
    }
}
