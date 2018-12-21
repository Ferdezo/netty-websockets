package pl.ferdezo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;

import static pl.ferdezo.netty.ServerConsts.*;

@Log4j2
public class WebSocketServer {

    public static void main(String[] args) {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(BOSS_THREADS);
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        log.info("test");

        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new NettyServerInitializer());

            serverBootstrap
                .bind( new InetSocketAddress(NETTY_HOST, NETTY_PORT))
                .sync()
                .channel()
                .closeFuture()
                .sync(); // block

        } catch (InterruptedException e) {
            log.error("Netty Server thread interrupted error", e);
            Thread.currentThread().interrupt();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.info("Event Loop Groups closed gracefully");
        }
    }
}
