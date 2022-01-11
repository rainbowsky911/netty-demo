package cn.itcast.my.c1;

import org.junit.Test;

/**
 * @Auther: zdw
 * @Date: 2021/10/13/22:46
 * @Description:
 */
public class CoreTest {

    @Test
    public void timeServerTest() throws Exception {
        new TimeServer().bind(8080);
    }

    @Test
    public void timeClientTest() throws Exception {
        new TimeClient().connect(8080, "127.0.0.1");
    }
}
