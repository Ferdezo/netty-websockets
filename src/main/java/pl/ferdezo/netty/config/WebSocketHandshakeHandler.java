package pl.ferdezo.netty.config;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.log4j.Log4j2;
import pl.ferdezo.netty.consts.ServerConsts;

import java.util.Optional;
import java.util.function.Predicate;

import static pl.ferdezo.netty.consts.PipelineHandlers.WS_FRAME_HANDLER;

@Log4j2
@ChannelHandler.Sharable
public class WebSocketHandshakeHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        final Runnable toNextHandler = () -> ctx.fireChannelRead(httpRequest);

        Optional.ofNullable(httpRequest)
            .map(HttpMessage::headers)
            .filter(this::areWsHeaders)
            .ifPresentOrElse(
                headers -> handleHandshake(ctx, httpRequest),
                toNextHandler
            );
    }

    private boolean areWsHeaders(HttpHeaders httpHeaders) {
        final Optional<HttpHeaders> validHeaders = Optional
            .ofNullable(httpHeaders)
            .filter(Predicate.not(HttpHeaders::isEmpty))
            .filter(headers -> headers.contains(HttpHeaderNames.CONNECTION))
            .filter(headers -> headers.contains(HttpHeaderNames.UPGRADE));

        final Optional<String> connectionUpgrade = validHeaders
            .map(headers -> headers.get(HttpHeaderNames.CONNECTION))
            .filter(connection -> connection.contains(ServerConsts.UPGRADE_CONNECTION));

        final Optional<String> upgradeWebsocket = validHeaders
            .map(headers -> headers.get(HttpHeaderNames.UPGRADE))
            .filter(upgrade -> upgrade.equalsIgnoreCase(ServerConsts.WEBSOCKET_UPGRADE));

        return connectionUpgrade.isPresent() && upgradeWebsocket.isPresent();
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
        final String hostHeader = httpRequest.headers().get(ServerConsts.HOST_HEADER);
        final String uri = httpRequest.uri();
        return "ws://" + hostHeader + uri;
    }
}
