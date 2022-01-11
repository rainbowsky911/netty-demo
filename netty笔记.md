handler如何执行中间人操作

```
static void invokeChannelRead(final AbstractChannelHandlerContext next, Object msg) {
    final Object m = next.pipeline.touch(ObjectUtil.checkNotNull(msg, "msg"), next);
	//下一个handler的事件循环是否与当前的事件循环是同一个线程  
 	 EventExecutor executor = next.executor();  //返回下一个handler的eventLoop
	    
	//当前handler中的线程 是否和eventLoop是同一个线程
	//是,直接调用
	if (executor.inEventLoop()) {	
        next.invokeChannelRead(m);
    //不是 将要执行的代码作为任务提交给下一个事件循环处理
	} else {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                next.invokeChannelRead(m);
            }
        });
    }
}
```

- 如果两个handler绑定的是同一个线程,那么就直接调用，把要调用的代码封装成一个任务对象，由下一个

  handler的线程来调用，目的是切换线程





###### Channel

channel的主要作用

- close()关闭
- closeFuture()用来处理channel的关闭
  - sync方法同步等待channel关闭
  - addListener方法异步等待channel关闭

- pipline方法添加处理器
- write方法将数据写入
- writeAndFlush方法将数据写入并刷出



connect（）异步方法



> ```
> super.channelRead(ctx, student);	//将数据传递给下个handler,如果不调用连接会断开
>  ctx.fireChannelRead(msg);
> ```





默认实现使用池化实现




LengthFieldBasedFrameDecoder

```java
   lengthFieldOffset   = 0  //长度字段偏移量
   lengthFieldLength   = 2	//长度字段长度
   lengthAdjustment    = 0	//长度字段为基准,还有几个字节是内容
   initialBytesToStrip = 0 (= do not strip header) //从头剥离几个字节
  
   BEFORE DECODE (14 bytes)         AFTER DECODE (14 bytes)
   +--------+----------------+      +--------+----------------+
   | Length | Actual Content |----->| Length | Actual Content |
   | 0x000C | "HELLO, WORLD" |      | 0x000C | "HELLO, WORLD" |
   +--------+----------------+      +--------+----------------+
```

![image-20210802140440069](C:\Users\51473\AppData\Roaming\Typora\typora-user-images\image-20210802140440069.png)



连接建立后 触发active

channelActive()

![image-20210830031245175](C:\Users\51473\AppData\Roaming\Typora\typora-user-images\image-20210830031245175.png)