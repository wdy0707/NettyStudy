package cn.wdy07.netty._04_netty_04_httpserver.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author wdy
 * @create 2020-06-16 19:33
 */
@ChannelHandler.Sharable
public class HTTPServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //因为是HTTP服务器，所以默认接收到的请求msg是HTTP请求，而且加了HTTPObjectAggregate，FullHttpRequest

        FullHttpRequest request = (FullHttpRequest) msg;
        String uri = request.uri();
        String content = request.content().toString(CharsetUtil.UTF_8);
        HttpMethod method = request.method();

        processRequest(uri,content,method,ctx);

    }

    private void processRequest(String uri,String content,HttpMethod method,ChannelHandlerContext ctx){
        String msg="";
        if(!"/test".equalsIgnoreCase(uri)){
            msg = "非法请求:"+uri;
            sendMsg(msg,ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }

        if(HttpMethod.GET.equals(method)){
            System.out.println("Get Request,content is : "+content);
            msg = ResponseContent.getResponse();
            sendMsg(msg,ctx,HttpResponseStatus.OK);
            return;
        }

        if(HttpMethod.POST.equals(method)){
            //.......
            return;
        }
    }

    private void sendMsg(String msg, ChannelHandlerContext ctx, HttpResponseStatus status){

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status, Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8)));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset:utf-8");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);//发送完消息关闭通道（不是长链接）
    }
}
