package pl.ferdezo.netty.config;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.log4j.Log4j2;

import static pl.ferdezo.netty.consts.PipelineHandlers.WS_FRAME_HANDLER;

@Log4j2
@ChannelHandler.Sharable
public class WebSocketHandshakeHandler extends SimpleChannelInboundHandler<HttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        final HttpHeaders headers = httpRequest.headers();

        if (areWsHeaders(headers)) {
            log.info("Handshake requested!");
            handleHandshake(ctx, httpRequest);
            return;
        }

        ctx.fireChannelRead(httpRequest);
    }

    private boolean areWsHeaders(HttpHeaders headers) {
        final String connectionHeaders = headers.get(HttpHeaderNames.CONNECTION);
        final String upgrade = headers.get(HttpHeaderNames.UPGRADE);

        return connectionHeaders.contains("Upgrade") &&
            "websocket".equalsIgnoreCase(upgrade);
    }

    private void handleHandshake(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        ctx.pipeline().replace(this, WS_FRAME_HANDLER.key(), new WebSocketFrameHandler());

        final String wsUrl = prepareWsUrl(httpRequest);
        final WebSocketServerHandshaker handshaker = new WebSocketServerHandshakerFactory(wsUrl, null, true)
                .newHandshaker(httpRequest);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            return;
        }

        handshaker.handshake(ctx.channel(), httpRequest)
            .addListener(future -> log.info("HANDSHAKE"));
    }

    private String prepareWsUrl(HttpRequest httpRequest) {
        final String hostHeader = httpRequest.headers().get("Host");
        final String uri = httpRequest.uri();
        return "ws://" + hostHeader + uri;
    }
}
