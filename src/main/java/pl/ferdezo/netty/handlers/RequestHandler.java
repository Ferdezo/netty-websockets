package pl.ferdezo.netty.handlers;

public interface RequestHandler {
    String handleAndProduceResponse(String text);
}
