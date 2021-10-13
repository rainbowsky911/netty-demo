package cn.itcast.netty.c5;

import cn.itcast.config.Config;
import cn.itcast.message.LoginRequestMessage;
import cn.itcast.message.Message;
import cn.itcast.protocol.MessageCodec;
import cn.itcast.protocol.MessageCodecSharable;
import cn.itcast.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

import java.nio.ByteBuffer;

public class TestSerializer {

    public static void main(String[] args)  {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING, CODEC, LOGGING);

        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
//        channel.writeOutbound(message);
        ByteBuf buf = messageToByteBuf(message);
        channel.writeInbound(buf);
    }



    @Test
    public void test() throws Exception {
        LoggingHandler loggingHandler = new LoggingHandler();

        LengthFieldBasedFrameDecoder frameDecoder = new LengthFieldBasedFrameDecoder(1024,
                12,
                4,
                0,
                0
        );
        EmbeddedChannel channel = new EmbeddedChannel(
                loggingHandler,
                //避免粘包半包现象
                frameDecoder,
               /* new LoggingHandler(),*/
                new MessageCodec());

        //encode
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
      //  channel.writeOutbound(message);

        //decode
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message,buffer);


        ByteBuf slice1 = buffer.slice(0, 100);
        ByteBuf slice2 =buffer.slice(100, buffer.readableBytes()-100);


        //入站
        slice1.retain();
        slice2.retain();
        channel.writeInbound(slice1);  //releasee 0  引用计数减成0了
        channel.writeInbound(slice2);

    }


    public static ByteBuf messageToByteBuf(Message msg) {
        int algorithm = Config.getSerializerAlgorithm().ordinal();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(new byte[]{1, 2, 3, 4});
        out.writeByte(1);
        out.writeByte(algorithm);
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        out.writeByte(0xff);
        byte[] bytes = Serializer.Algorithm.values()[algorithm].serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        return out;
    }
}
