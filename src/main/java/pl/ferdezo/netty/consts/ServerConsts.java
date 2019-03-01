package pl.ferdezo.netty.consts;

public final class ServerConsts {
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String CONTENT_LENGTH_HEADER = "Content-Length";
    public static final String CONTENT_TYPE_VALUE = "text/html; charset=UTF-8";
    public static final String HELLO_URI = "/hello";
    public static final String SIMULATE_URI = "/simulate";


    public static final int NETTY_PORT = 9000;
    public static final int BOSS_THREADS = 1;
    public static final String NETTY_HOST = "localhost";

    public static final String UPGRADE_CONNECTION = "Upgrade";
    public static final String WEBSOCKET_UPGRADE = "websocket";
    public static final String HOST_HEADER = "Host";
    public static final String SUBSCRIBE = "SUBSCRIBE";
    public static final String UNSUBSCRIBE = "UNSUBSCRIBE";

    private ServerConsts() {
    }
}
