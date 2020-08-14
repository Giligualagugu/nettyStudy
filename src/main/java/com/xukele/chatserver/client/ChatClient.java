package com.xukele.chatserver.client;

import com.xukele.chatserver.handler.ClientMessageHandler;
import com.xukele.chatserver.protocol.pojo.ChatMessagePojo;
import com.xukele.chatserver.server.ChatServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class ChatClient {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();


        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class)
                    .group(group)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new ProtobufDecoder(ChatMessagePojo.ChatMessage.getDefaultInstance()))
                                    .addLast(new ClientMessageHandler());
                        }
                    });

            ChannelFuture localhost = bootstrap.connect("localhost", ChatServer.port).sync();

            localhost.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("链接到server :{}", localhost.channel().remoteAddress());
                }
            });

            Channel current = localhost.channel();
            //输入;
            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                ChatMessagePojo.ChatMessage build = ChatMessagePojo.ChatMessage.newBuilder().setId(2).setMess(line).build();
                current.writeAndFlush(build);
            }
            localhost.channel().closeFuture().sync();
        } finally {

            group.shutdownGracefully();
        }

    }
}
