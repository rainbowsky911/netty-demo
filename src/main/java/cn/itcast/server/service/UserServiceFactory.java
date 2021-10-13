package cn.itcast.server.service;

public abstract class UserServiceFactory {

    public static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
