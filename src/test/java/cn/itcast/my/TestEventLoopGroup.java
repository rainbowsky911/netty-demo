package cn.itcast.my;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: zdw
 * @Date: 2021/08/29/23:51
 * @Description:
 */
@Slf4j
public class TestEventLoopGroup {
    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup(2);
        //next
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        //执行普通任务
        group.next().submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("ok");
        });

        //执行定时任务
        group.next().scheduleAtFixedRate(() -> {
            log.debug("ok");

        }, 0, 1, TimeUnit.SECONDS);
        log.debug("main");
    }
}
