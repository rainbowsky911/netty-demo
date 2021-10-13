package cn.itcast.my;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @Auther: zdw
 * @Date: 2021/08/30/1:00
 * @Description:
 */
@Slf4j
public class CloseFutureClient {
    public static void main(String[] args) {
        try {
            NioEventLoopGroup group = new NioEventLoopGroup();
            Channel channel = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //添加handler
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new StringEncoder()); //bytebuf转为字符串

                        }
                    }).connect(new InetSocketAddress("localhost", 2222))
                    .sync()
                    .channel();
            log.debug("{}", channel);
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String line = scanner.nextLine();
                    if ("q".equals(line)) {
                        channel.close();
                        // log.debug("处理关闭之后操作");
                        break;
                    }
                    channel.writeAndFlush(line);
                }
            }, "input_client").start();

            //  方法①获取closeFuture对象
           /* ChannelFuture closeFuture = channel.closeFuture();
            log.debug("waiting close");
            closeFuture.sync();
            log.debug("处理关闭之后操作");*/

            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.debug("处理关闭之后操作");
                    group.shutdownGracefully();//线程组停下来
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
