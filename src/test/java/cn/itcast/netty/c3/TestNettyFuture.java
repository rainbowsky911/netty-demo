package cn.itcast.netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@Slf4j
public class TestNettyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();
        Future<Integer> future = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行计算");
                Thread.sleep(1000);
                return 70;
            }
        });
//        log.debug("等待结果");
//        log.debug("结果是 {}", future.get());
        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                log.debug("接收结果:{}", future.getNow());
            }
        });
    }


    @Test
    public void test1() throws ExecutionException, InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoop loop = eventLoopGroup.next();
        Future<Integer> futute = loop.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                log.debug("开始计算");
                Thread.sleep(1000);
                return 1024;
            }
        });
        log.debug("结果:{}",+futute.get());

    }
}
