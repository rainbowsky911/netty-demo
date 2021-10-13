package cn.itcast.my.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Random;

/**
 * @Auther: zdw
 * @Date: 2021/08/30/2:22
 * @Description:
 */
@Slf4j
public class HelloWorldClient {
    public static void main(String[] args) {

        //一次一次发.发一次断开一次连接，效率低
      /*  for (int i = 0; i < 10; i++) {
            send();
        }*/


        //FixedLengthFrameDecoder
         send3();

        //LineBasedFrameDecoder
        //send4();

    }

    public static StringBuilder makeString(char c, int len) {
        StringBuilder sb = new StringBuilder(len + 2);
        for (int i = 0; i < len; i++) {
            sb.append(c);
        }
        sb.append("\n");
        return sb;
    }

    public static byte[] fill10Bytes(char c, int len) {
        byte[] bytes = new byte[10];
        Arrays.fill(bytes, (byte) '_');
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) c;
        }
        System.out.println(new String(bytes));
        return bytes;
    }

    private static void send4() {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        // 会在连接 channel 建立成功后，会触发 active 事件
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            ByteBuf buf = ctx.alloc().buffer();
                            char c = '0';
                            Random r = new Random();
                            for (int i = 0; i < 10; i++) {
                                StringBuilder string = makeString(c, r.nextInt(256) + 1);
                                c++;
                                buf.writeBytes(string.toString().getBytes());
                            }
                            ctx.writeAndFlush(buf);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error", e);
        } finally {
            worker.shutdownGracefully();
        }

    }

    private static void send3() {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        // 会在连接 channel 建立成功后，会触发 active 事件
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            ByteBuf buf = ctx.alloc().buffer();
                            char c = '0';
                            Random r = new Random();
                            for (int i = 0; i < 10; i++) {
                                byte[] bytes = fill10Bytes(c, r.nextInt(10) + 1);
                                c++;
                                buf.writeBytes(bytes);
                            }
                            ctx.writeAndFlush(buf);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error", e);
        } finally {
            worker.shutdownGracefully();
        }

    }


    //会产生粘包的代码
    @Test
    public void test2() {
        try {
            ChannelFuture future = new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //添加handler
                            //  ch.pipeline().addLast(new StringEncoder()); //bytebuf转为字符串
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                //连上服务器触发
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    for (int i = 0; i < 10; i++) {
                                        ByteBuf buffer = ctx.alloc().buffer(16);
                                        buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
                                        ctx.writeAndFlush(buffer);
                                        ctx.channel().close();
                                    }
                                }
                            });
                        }

                    }).connect(new InetSocketAddress("localhost", 6666));
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static void send() {
        try {
            ChannelFuture future = new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //添加handler
                            //  ch.pipeline().addLast(new StringEncoder()); //bytebuf转为字符串
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                //连上服务器触发
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ByteBuf buffer = ctx.alloc().buffer(16);
                                    buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17});
                                    ctx.writeAndFlush(buffer);
                                    ctx.channel().close();
                                }
                            });
                        }

                    }).connect(new InetSocketAddress("localhost", 6666));
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
