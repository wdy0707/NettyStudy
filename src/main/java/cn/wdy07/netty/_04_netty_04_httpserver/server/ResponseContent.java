package cn.wdy07.netty._04_netty_04_httpserver.server;

import java.util.Random;

/**
 * @author wdy
 * @create 2020-06-16 20:08
 */
public class ResponseContent {

    public static String getResponse(){
        return content[new Random().nextInt(content.length)];
    }

    private static final String[] content = {"《再别康桥》（徐志摩）\n" +
            "\n" +
            "轻轻的我走了，\n" +
            "\n" +
            "正如我轻轻的来；\n" +
            "\n" +
            "我轻轻的招手，\n" +
            "\n" +
            "作别西天的云彩。\n" +
            "\n" +
            "那河畔的金柳，\n" +
            "\n" +
            "是夕阳中的新娘；\n" +
            "\n" +
            "波光里的艳影，\n" +
            "\n" +
            "在我的心头荡漾。\n" +
            "\n" +
            "软泥上的青荇，\n" +
            "\n" +
            "油油的在水底招摇；\n" +
            "\n" +
            "在康河的柔波里，\n" +
            "\n" +
            "我甘心做一条水草！\n" +
            "\n" +
            "那榆荫下的一潭，\n" +
            "\n" +
            "不是清泉，是天上虹\n" +
            "\n" +
            "揉碎在浮藻间，\n" +
            "\n" +
            "沉淀着彩虹似的梦。\n" +
            "\n" +
            "寻梦？撑一支长蒿，\n" +
            "\n" +
            "向青草更青处漫溯，\n" +
            "\n" +
            "满载一船星辉，\n" +
            "\n" +
            "在星辉斑斓里放歌。\n" +
            "\n" +
            "但我不能放歌，\n" +
            "\n" +
            "悄悄是别离的笙箫；\n" +
            "\n" +
            "夏虫也为我沉默，\n" +
            "\n" +
            "沉默是今晚的康桥！\n" +
            "\n" +
            "悄悄的我走了，\n" +
            "\n" +
            "正如我悄悄的来；\n" +
            "\n" +
            "我挥一挥衣袖，\n" +
            "\n" +
            "不带走一片云彩。","《雨巷》（戴望舒）\n" +
            "\n" +
            "撑着油纸伞，独自\n" +
            "\n" +
            "彷徨在悠长，悠长\n" +
            "\n" +
            "又寂寥的雨巷\n" +
            "\n" +
            "我希望逢着\n" +
            "\n" +
            "一个丁香一样地\n" +
            "\n" +
            "结着愁怨的姑娘。\n" +
            "\n" +
            "她是有\n" +
            "\n" +
            "丁香一样的颜色，\n" +
            "\n" +
            "丁香一样的芬芳\n" +
            "\n" +
            "丁香一样的忧愁，\n" +
            "\n" +
            "在雨中哀怨，\n" +
            "\n" +
            "哀怨又彷徨。\n" +
            "\n" +
            "她彷徨在这寂寥的雨巷，\n" +
            "\n" +
            "撑着油纸伞\n" +
            "\n" +
            "像我一样，\n" +
            "\n" +
            "像我一样地\n" +
            "\n" +
            "默默彳亍着\n" +
            "\n" +
            "冷漠，凄清，又惆怅。\n" +
            "\n" +
            "她静默地走近\n" +
            "\n" +
            "走近，又投出\n" +
            "\n" +
            "太息一般的眼光，\n" +
            "\n" +
            "她飘过\n" +
            "\n" +
            "像梦一般地，\n" +
            "\n" +
            "像梦一般地凄婉迷茫\n" +
            "\n" +
            "像梦中飘过\n" +
            "\n" +
            "一枝丁香地，\n" +
            "\n" +
            "我身旁飘过这女郎；\n" +
            "\n" +
            "她静默地远了，远了，\n" +
            "\n" +
            "到了颓圮的篱墙，\n" +
            "\n" +
            "走尽这雨巷\n" +
            "\n" +
            "在雨的哀曲里，\n" +
            "\n" +
            "消了她的颜色，\n" +
            "\n" +
            "散了她的芬芳，\n" +
            "\n" +
            "消散了，甚至她的\n" +
            "\n" +
            "太息般的眼光，\n" +
            "\n" +
            "丁香般的惆怅。\n" +
            "\n" +
            "撑着油纸伞，独自\n" +
            "\n" +
            "彷徨在悠长，悠长\n" +
            "\n" +
            "又寂寥的雨巷，\n" +
            "\n" +
            "我希望飘过\n" +
            "\n" +
            "一个丁香一样地\n" +
            "\n" +
            "结着愁怨的姑娘。"};

}
