package cn.itcast.advance.c1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

public class TestLengthFieldDecoder {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(
                        1024,
                        0,
                        4,
                        1,
                        4),
                new LoggingHandler(LogLevel.DEBUG)
        );

        //  4 个字节的内容长度， 实际内容
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "Hello, world");
        send(buffer, "Hi!");
        channel.writeInbound(buffer);
    }

   /* private static void send(ByteBuf buffer, String content) {
        byte[] bytes = content.getBytes(); // 实际内容
        int length = bytes.length; // 实际内容长度
        buffer.writeInt(length);
        buffer.writeByte(1);
        buffer.writeBytes(bytes);
    }*/


    @Test
    public void test() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,
                        0,
                        4,
                        1,  //加上长度之后调整的字节
                        4),//剥离开头4个字节长度
                new LoggingHandler(LogLevel.DEBUG)
        );
        //4个字节的内容长度
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "Hello, world");
        send(buffer, "Hi");
        channel.writeInbound(buffer);
    }

    public static void send(ByteBuf buffer, String content) {
        byte[] bytes = content.getBytes();      //实际内容
        int length = bytes.length;          //实际内容长度
        buffer.writeInt(length);
        buffer.writeByte(1); //版本号
        buffer.writeBytes(bytes);
    }
}

