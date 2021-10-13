package cn.itcast.my;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @Auther: zdw
 * @Date: 2021/08/29/23:17
 * @Description:
 */
public class HelloClient {
    public static void main(String[] args) {

        try {
            new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //添加handler
                            ch.pipeline().addLast(new StringEncoder()); //bytebuf转为字符串

                        }
                    }).connect(new InetSocketAddress("localhost", 7777))
                    .sync()
                    .channel()
                    .writeAndFlush("hello,world!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
