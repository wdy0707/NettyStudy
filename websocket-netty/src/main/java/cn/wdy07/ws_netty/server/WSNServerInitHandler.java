package cn.wdy07.ws_netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.http2.Http2FrameCodec;

/**
 * @author wdy
 * @create 2020-06-19 16:25
 */
public class WSNServerInitHandler extends ChannelInitializer<Channel> {

    //websocket的访问路径
    private static final String WSPATH = "/ws";

    private ChannelGroup channelGroup;

    public WSNServerInitHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline()
                    //首先加入对HTTP的支持
                   .addLast(new HttpServerCodec())
                    .addLast(new HttpObjectAggregator(65535))
                    //对WS应答的数据压缩
                    .addLast(new WebSocketServerCompressionHandler())
                    //netty提供的websocket协议的升级以及通信的控制Handler
                    .addLast(new WebSocketServerProtocolHandler(WSPATH,null,true))
                    //页面展示Handler
                    .addLast(new WSNPageShowHandler(WSPATH))
                    //WebSocker业务处理Handler
                    .addLast(new WSNServerWSHandler(channelGroup));
    }
}
