package pl.ferdezo.netty.consts;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PipelineHandlers {
    HTTP_SERVER_CODEC("httpServerCodec"),
    HTTP_HANDLER("httpHandler")
    ;

    private final String key;

    public String key() {
        return this.key;
    }
}
