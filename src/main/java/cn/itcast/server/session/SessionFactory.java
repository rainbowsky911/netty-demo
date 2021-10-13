package cn.itcast.server.session;

public abstract class SessionFactory {

    public static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
