package cn.itcast.nio.c1;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Auther: zdw
 * @Date: 2021/08/29/5:48
 * @Description:
 */
@Slf4j
public class TestByteBuf {

    public static void main(String[] args) {

        try {
            FileChannel fileChannel = new FileInputStream("data.txt").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                int len = 0;
                try {
                    len = fileChannel.read(buffer);
                    log.debug("读取到字节数{}", len);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (len == -1) {
                    break;
                }
                buffer.flip();//切换读模式
                while (buffer.hasRemaining()) { //是否还有未读数据
                    byte b = buffer.get();
                    log.debug("实际字节{}", (char) b);
                }
                buffer.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void test1() {
        System.out.println(ByteBuffer.allocate(16).getClass());
        System.out.println(ByteBuffer.allocateDirect(16).getClass());

        /***
         * class java.nio.HeapByteBuffer    堆内存  读写效率低  受gc影响
         * class java.nio.DirectByteBuffer  直接内存  读写效率高  不会受gc影响
         */


    }
    @Test
    public  void test2() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();

        buffer.get(new byte[4]);
        System.out.println(buffer);
        buffer.rewind();
    }
}
