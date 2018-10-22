package pl.ferdezo.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import static pl.ferdezo.netty.PipelineHandlers.HTTP_HANDLER;
import static pl.ferdezo.netty.PipelineHandlers.HTTP_SERVER_CODEC;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel socketChannel) {
        final ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(HTTP_SERVER_CODEC.key(), new HttpServerCodec());
        pipeline.addLast(HTTP_HANDLER.key(), new HttpServerHandler());
    }
}
