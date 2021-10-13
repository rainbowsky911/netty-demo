package cn.itcast.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;

import static cn.itcast.netty.c4.TestByteBuf.log;

public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        log(buf);

        // 在切片过程中，没有发生数据复制
        ByteBuf f1 = buf.slice(0, 5);
        f1.retain();
        // 'a','b','c','d','e', 'x'
        ByteBuf f2 = buf.slice(5, 5);
        f2.retain();
        log(f1);
        log(f2);

        System.out.println("释放原有 byteBuf 内存");
        buf.release();
        log(f1);


        f1.release();
        f2.release();
       /* System.out.println("========================");
        f1.setByte(0, 'b');
        log(f1);
        log(buf);*/
    }

    /**
     * 对byteBuf进行切片
     */
    @Test
    public void test() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in inaction rocks!", utf8);
       /* ByteBuf slice = buf.slice(0, 15);
        System.out.println(slice.toString(utf8));
        buf.setByte(0,(byte)'J');
        assert buf.getByte(0) == slice.getByte(0);*/
        ByteBuf slice = buf.slice();
        slice.setByte(0, 'A');
        assert buf.getByte(0) == slice.getByte(0);
        System.out.println(slice.toString(utf8));

    }

    @Test
    public void testCopy() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in inaction rocks!", utf8);
        ByteBuf copy = buf.copy(0, 15);
        System.out.println(copy.toString(utf8));
        buf.setByte(0, (byte) 'J');
        assert buf.getByte(0) == copy.getByte(0);
    }


    /**
     * 组合两个buf ,避免内存复制。缺点维护麻烦
     */
    @Test
    public void test3() {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});
        buf1.retain();


        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf2.writeBytes(new byte[]{1, 2, 3, 4, 5});
        buf2.retain();

        CompositeByteBuf compositeBuffer = ByteBufAllocator.DEFAULT.compositeBuffer();
        compositeBuffer.addComponents(true, buf1, buf2);
        log(compositeBuffer);
    }
}
