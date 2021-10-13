package cn.itcast.my;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Auther: zdw
 * @Date: 2021/08/29/23:07
 * @Description:
 */
public class HelloServer {
    public static void main(String[] args) {
        //1 启动器 负责组装netty组件
        new ServerBootstrap()
                // 2BosseventLoop
                .group(new NioEventLoopGroup())
                // 3连接服务器socket实现
                .channel(NioServerSocketChannel.class) // OIO NIO
                //和客户端进行数据读写的通信 initialize初始化。
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //添加handler
                        ch.pipeline().addLast(new StringDecoder()); //bytebuf转为字符串
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });

                    }
                }).bind(7777);
    }
}
