package cn.itcast.my;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * @Auther: zdw
 * @Date: 2021/08/30/0:04
 * @Description:
 */
@Slf4j
public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException {
        //future异步
        ChannelFuture future = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //添加handler
                        ch.pipeline().addLast(new StringEncoder()); //bytebuf转为字符串

                    }
                    //异步非阻塞 main发起了调用，真正执行connect是nio线程
                }).connect(new InetSocketAddress("localhost", 4444));

        //方法①使用sync方法同步处理结果
     /*   future.sync();
        Channel channel = future.channel();
        channel.writeAndFlush("hello,world!");//注释了sync()会无法向下获取channle ,客户端不会接收到信息
        log.debug("{}", channel);*/


        //方法② 使用addListener(回调对象) 方法异步处理结果
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel1 = future.channel();
                log.debug("{}", channel1);
                channel1.writeAndFlush("hello,world!");//
            }
        });


    }

    @Test
    public void test1() {
        try {
            Channel channel = new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //添加handler
                            ch.pipeline().addLast(new StringEncoder()); //bytebuf转为字符串

                        }
                    }).connect(new InetSocketAddress("localhost", 4444))
                    .sync()
                    .channel();

            System.out.println(channel);
            System.out.println("");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
