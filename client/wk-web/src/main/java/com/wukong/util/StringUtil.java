package com.wukong.util;

/**
 * @author PARANOIA_ZK
 * @date 2018/5/4 21:31
 */
public class StringUtil {

    /**
     * 去除字符串中的标点符号
     * @param s
     * @return
     */
    public static String format(String s) {
        String str = s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘<< >> 《 》 _ – ；：”“’。，、？|-]", "");
        return str;
    }
}
