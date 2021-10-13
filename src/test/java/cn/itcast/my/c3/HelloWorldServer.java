package cn.itcast.my.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zdw
 * @Date: 2021/08/30/2:21
 * @Description:
 */
@Slf4j
public class HelloWorldServer {

    public static void main(String[] args) {
        //1 启动器 负责组装netty组件
        new ServerBootstrap()
                // 2BosseventLoop
                .group(new NioEventLoopGroup())
                // 3连接服务器socket实现
                .channel(NioServerSocketChannel.class) // OIO NIO
                //调整系统的接收缓冲区
                //  .option(ChannelOption.SO_RCVBUF,10) //接收缓冲器设置小，产生半包现象。

                //调整netty的接收缓冲区
                //发送的消息长度是18，产生半包现象
                .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16))
                //和客户端进行数据读写的通信 initialize初始化。
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                     //   ch.pipeline().addLast(new LineBasedFrameDecoder(1024));

                        //定长消息解码器,缺点会造成资源浪费
                        ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
                        //添加handler
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG)); //bytebuf转为字符串
                    }
                }).bind(6666);
    }
}
