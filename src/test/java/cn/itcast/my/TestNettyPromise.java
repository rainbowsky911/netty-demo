package cn.itcast.my;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @Auther: zdw
 * @Date: 2021/08/30/1:39
 * @Description:
 */
@Slf4j
public class TestNettyPromise {

    public static void main(String[] args) {

        EventLoop loop = new NioEventLoopGroup().next();
        DefaultPromise<Object> promise = new DefaultPromise<>(loop);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                promise.setSuccess(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        log.debug("等待结果....");
        try {
            log.debug("结果是:{}", promise.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
