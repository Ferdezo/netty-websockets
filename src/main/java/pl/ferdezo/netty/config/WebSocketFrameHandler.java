package pl.ferdezo.netty.config;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.log4j.Log4j2;
import pl.ferdezo.netty.handlers.RequestHandler;
import pl.ferdezo.netty.handlers.RequestHandlerFactory;

import java.util.StringTokenizer;

@Log4j2
@ChannelHandler.Sharable
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    public static final String DELIMITER = "_";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        log.debug("WebSocket frame handled");

        if (frame instanceof CloseWebSocketFrame) {
            ctx.close();
            return;
        }

        if (frame instanceof PingWebSocketFrame) {
            ctx.writeAndFlush(frame.content().copy());
            return;
        }

        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            log.debug("Text WS frame: {}", text);
            StringTokenizer stringTokenizer = new StringTokenizer(text, DELIMITER);

            if (stringTokenizer.countTokens() != 2) {
                ctx.writeAndFlush(new TextWebSocketFrame("Not enought parameters"));
                return;
            }

            String operation = stringTokenizer.nextToken();
            log.debug("Operation {}", operation);
            RequestHandler requestHandler = RequestHandlerFactory.create(operation, ctx.channel());

            String param = stringTokenizer.nextToken();
            log.debug("Param: {}", param);
            String response = requestHandler.handleAndProduceResponse(param);
            ctx.writeAndFlush(new TextWebSocketFrame(response));
            return;
        }

        log.warn("Not supported frame: {}", frame);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Excetion while handling WebSocketFrame", cause);

        if (cause instanceof IllegalArgumentException) {
            ctx.writeAndFlush(new TextWebSocketFrame("Unrecognized operation"));
            return;
        }

        ctx.writeAndFlush(new TextWebSocketFrame("Unrecognized error"));
    }
}
