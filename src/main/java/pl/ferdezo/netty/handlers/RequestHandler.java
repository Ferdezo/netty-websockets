package pl.ferdezo.netty.handlers;

@FunctionalInterface
public interface RequestHandler {
    String handleAndProduceResponse(String text);
}
