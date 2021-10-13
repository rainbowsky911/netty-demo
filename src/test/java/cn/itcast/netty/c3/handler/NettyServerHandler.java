package cn.itcast.netty.c3.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * @Auther: zdw
 * @Date: 2021/07/26/13:23
 * @Description:
 */
@Slf4j
public
class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //数据读取事件
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buffer = (ByteBuf) msg;
        System.out.println(LocalDateTime.now());
        System.out.println(buffer.toString(Charset.defaultCharset()));

        // 建议使用 ctx.alloc() 创建 ByteBuf
        ByteBuf response = ctx.alloc().buffer();
        response.writeBytes(buffer);
        ctx.writeAndFlush(response);
        //传来的消息包装成字节缓冲区ByteBuf byteBuf = (ByteBuf) msg;
        //Netty提供了字节缓冲区的toString方法，并且可以设置参数为编码格式：CharsetUtil.UTF_8System.out.println("客户端发来的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
    }


    //数据读取完毕事件
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //数据读取完毕，将信息包装成一个Buffer传递给下一个Handler，Unpooled.copiedBuffer会返回一个Buffer//调用的是事件处理器的上下文对象的writeAndFlush方法
        //意思就是说将  你好  传递给了下一个handler
        //ctx.writeAndFlush(Unpooled.copiedBuffer("你好!", CharsetUtil.UTF_8));
    }

    //异常发生的事件
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //异常发生时关闭上下文对象
        ctx.close();
    }
}
