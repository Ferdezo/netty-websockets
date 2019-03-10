package pl.ferdezo.netty.config;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import pl.ferdezo.netty.http.HttpServerHandler;

import static pl.ferdezo.netty.consts.PipelineHandlers.*;

public class BaseChannelInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel socketChannel) {
        final ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(HTTP_SERVER_CODEC.key(), new HttpServerCodec());
        pipeline.addLast(HTTP_HANDLER.key(), new HttpServerHandler());
    }
}
