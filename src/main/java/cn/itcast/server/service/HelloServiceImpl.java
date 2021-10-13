package cn.itcast.server.service;

/**
 * @author 51473
 */
public class HelloServiceImpl implements  HelloService {
    @Override
    public String sayHello(String msg) {
        int i = 1 / 0;
        return "你好, " + msg;
    }
}