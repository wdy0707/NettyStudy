package cn.wdy07.ws_netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wdy
 * @create 2020-06-19 16:42
 */
public class WSNServerWSHandler extends ChannelInboundHandlerAdapter {

    private ChannelGroup channelGroup;

    public WSNServerWSHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    private static final Logger logger = LoggerFactory.getLogger(WSNServerWSHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //不是HTTP，fire到下一个Handler
        if(!(msg instanceof WebSocketFrame)){
            ctx.fireChannelRead(msg);
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        //先处理文本帧
        if(frame instanceof TextWebSocketFrame){
            //获得其中的消息内容
            String info = ((TextWebSocketFrame) frame).text();
            logger.info("Server accept msg ,Channel :" + ctx.channel() +"; msg :" + info);

            //分别进行一对一回复和群发
            ctx.channel().writeAndFlush(
                    new TextWebSocketFrame("server reply: "+ info.toUpperCase()));

            channelGroup.writeAndFlush(
                    new TextWebSocketFrame("Client :"+ctx.channel()+" send msg :" + info));

        }else {
            String res = "unsupport msg type : " + frame.getClass().getName();
            ctx.writeAndFlush(new TextWebSocketFrame(res));

            throw new UnsupportedOperationException(res);
        }
    }
}
