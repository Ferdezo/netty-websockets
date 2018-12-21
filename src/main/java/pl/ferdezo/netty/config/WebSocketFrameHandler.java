package pl.ferdezo.netty.config;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

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

        log.warn("Not supported frame: {}", frame);
    }
}
