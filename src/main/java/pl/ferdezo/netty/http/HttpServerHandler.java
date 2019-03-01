package pl.ferdezo.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j2;
import pl.ferdezo.netty.domain.match.MatchEventSimulator;

import static pl.ferdezo.netty.consts.ServerConsts.*;

@Log4j2
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        final HttpMethod httpMethod = httpRequest.method();
        final String uri = httpRequest.uri();

        if (HttpMethod.POST.equals(httpMethod) && SIMULATE_URI.equals(uri)) {
            new MatchEventSimulator().simulate();
            return;
        }

        if (!HttpMethod.GET.equals(httpMethod)) {
            sendBadRequest(ctx);
            return;
        }

        if (HELLO_URI.equals(uri)) {
            final HttpResponse response = preparePositiveResponse("hello");
            final ChannelFuture writeFuture = ctx.write(response);

            if (!HttpUtil.isKeepAlive(httpRequest)) {
                writeFuture.addListener(ChannelFutureListener.CLOSE);
            }

            ctx.flush();
            return;
        }

        sendBadRequest(ctx);

    }

    private HttpResponse preparePositiveResponse(String message) {
        final HttpContent content = new DefaultHttpContent(
                Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));

        final HttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                content.content());

        final HttpHeaders headers = response.headers();
        headers.add(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE);
        headers.set(CONTENT_LENGTH_HEADER, content.content().readableBytes());
        return response;
    }

    private void sendBadRequest(ChannelHandlerContext ctx) {
        final HttpResponse badRequestResponse = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.BAD_REQUEST);

        ctx
                .writeAndFlush(badRequestResponse)
                .addListener(ChannelFutureListener.CLOSE);
    }
}
