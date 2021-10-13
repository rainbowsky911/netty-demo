package cn.itcast.my;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @Auther: zdw
 * @Date: 2021/08/30/1:44
 * @Description:
 */
@Slf4j
public class TestPipeline {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 1. 通过 channel 拿到 pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        // ChannelInboundHandlerAdapter   1->2->3
                        pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("1");
                                ByteBuf buffer = (ByteBuf) msg;
                                String name = buffer.toString(Charset.defaultCharset());
                                super.channelRead(ctx, name);
                            }
                        });
                        pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object name) throws Exception {
                                log.debug("2");
                                Teacher teacher = new Teacher(name.toString());
                                super.channelRead(ctx, teacher);
                               // ctx.fireChannelRead(teacher);//将数据传递给下一个handler,如果不掉调用链断开
                            }
                        });
                        // ChannelInboundHandlerAdapter   1->2->3->test_Outbound
                        pipeline.addLast("test_Outbound", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("test_Outbound");
                                super.write(ctx, msg, promise);
                            }
                        });

                        pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("3,结果{},class:{}", msg, msg.getClass());

                                //向前找出栈处理器 , 此方法不会触发出栈处理器会
                                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes()));


                                //调用writeAndFlush触发出栈处理器
                                //从尾巴找出栈处理器
                               // ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes()));


                               // super.channelRead(ctx, msg); //已经没有入站处理器
                            }
                        });

                        //ChannelOutboundHandlerAdapter   6->5->4
                        pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("4");
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("5");
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("6");
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                }).bind(2222);
        ;

    }
}

@Data
@AllArgsConstructor
class Teacher {
    String name;
}