package cn.wdy07.netty._04_netty_06_embedded;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import junit.framework.TestCase;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author wdy
 * @create 2020-06-17 22:33
 */
public class AbsIntegerEncoderTest {

    @Test
    public void testEncode(){
        //创建一个ByteBuf，并写入9个负整数
        ByteBuf byteBuf = Unpooled.buffer();
        for(int i=1;i<10;i++){
            byteBuf.writeInt(-1*i);
        }

        //创建一个EmbeddedChannel,并安装需要测试的Handler
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new AbsIntegerEncoder());

        //写入ByteBuf，并断言结果
        assertTrue(embeddedChannel.writeOutbound(byteBuf));

        //测试该channel的状态是否为finish
        assertTrue(embeddedChannel.finish());

        //读取EmbeddedChannel中的数据，并断言结果
        for(int i=1;i<10;i++){
            Integer num = i;
            assertEquals(num, embeddedChannel.readOutbound());
        }

    }
}
