package cn.itcast.my.c3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Auther: zdw
 * @Date: 2021/08/30/3:19
 * @Description:
 */
public class TestLengthFieldDecoder {

    public static void main(String[] args) {

        new EmbeddedChannel(new LengthFieldBasedFrameDecoder(1024,
                0,
                4,0,0),

                new LoggingHandler(LogLevel.DEBUG));
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        byte[] bytes = "hello,world".getBytes();
        int length = bytes.length;
        buffer.writeInt(length);
        buffer.writeBytes(bytes);

    }

}
