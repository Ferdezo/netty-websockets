package pl.ferdezo.netty.consts;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PipelineHandlers {
    HTTP_SERVER_CODEC("httpServerCodec"),
    HTTP_HANDLER("httpHandler"),
    WS_HANDSHAKE_HANDLER("webSocketHandshakeHandler"),
    WS_FRAME_HANDLER("webSocketFrameHandler")
    ;

    private final String key;

    public String key() {
        return this.key;
    }
}
