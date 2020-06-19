package cn.wdy07.ws_netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author wdy
 * @create 2020-06-19 16:40
 */
public class WSNPageShowHandler extends ChannelInboundHandlerAdapter {

    private final String WSPath;

    public WSNPageShowHandler(String WSPath) {
        this.WSPath = WSPath;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //不是HTTP，fire到下一个Handler
        if(! (msg instanceof FullHttpRequest)){
            ctx.fireChannelRead(msg);
            return;
        }

        FullHttpRequest request = (FullHttpRequest) msg;
        if(request.decoderResult().isFailure()){
            sendHttpResponse(ctx,request,new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //不是Get请求，直接返回
        if(!request.method().equals(HttpMethod.GET)){
            sendHttpResponse(ctx,request,new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,HttpResponseStatus.FORBIDDEN));
            return;
        }

        //返回访问的Page（先判断路径）
        if("/".equals(request.uri()) || "/index.html".equals(request.uri())){

            final String URL = getWSURL(request);
            System.out.println("已生成浏览器访问地址");
            //生成页面并返回
            ByteBuf content = IndexPageMaker.getContent(URL);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8");
            HttpUtil.setContentLength(response,response.content().readableBytes());

            sendHttpResponse(ctx,request,response);
        }else{
            sendHttpResponse(ctx,request,new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,HttpResponseStatus.NOT_FOUND));
            return;
        }


    }

    //应答消息
    private void sendHttpResponse(ChannelHandlerContext ctx,
                                  FullHttpRequest request, FullHttpResponse response){

        //处理错误消息
        if(response.status().compareTo(HttpResponseStatus.OK) != 0){
            response.content().writeBytes(
                    Unpooled.copiedBuffer(response.status().toString().getBytes(CharsetUtil.UTF_8)));

            HttpUtil.setContentLength(response,response.content().readableBytes());
        }
        //从最远端开始写
        ChannelFuture future = ctx.channel().writeAndFlush(response);
        //如果是错误消息，直接关闭
        if(response.status().compareTo(HttpResponseStatus.OK) != 0){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    //生成服务端访问地址
    private String getWSURL(HttpRequest request){
        return "ws://"+request.headers().get(HttpHeaderNames.HOST)+WSPath;
    }
}
