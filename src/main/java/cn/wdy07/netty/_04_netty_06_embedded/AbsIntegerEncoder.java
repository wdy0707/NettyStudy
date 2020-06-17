package cn.wdy07.netty._04_netty_06_embedded;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author wdy
 * @create 2020-06-17 22:27
 */

//将数字转换成绝对值
public class AbsIntegerEncoder extends MessageToByteEncoder<Integer> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out) throws Exception {
        msg = Math.abs(msg);
        out.writeInt(msg);
    }
}
