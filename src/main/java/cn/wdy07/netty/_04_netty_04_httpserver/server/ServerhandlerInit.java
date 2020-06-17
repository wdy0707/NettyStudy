package cn.wdy07.netty._04_netty_04_httpserver.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author wdy
 * @create 2020-06-16 19:37
 */
public class ServerhandlerInit extends ChannelInitializer<Channel> {

    private HTTPServerHandler handler = new HTTPServerHandler();

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline().addLast("decode",new HttpRequestDecoder());
        ch.pipeline().addLast("encode",new HttpResponseEncoder());
        ch.pipeline().addLast("aggregate",new HttpObjectAggregator(10*1024*1024));

        ch.pipeline().addLast(handler);
    }
}
