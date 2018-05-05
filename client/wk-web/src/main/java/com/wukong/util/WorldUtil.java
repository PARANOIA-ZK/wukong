package com.wukong.util;

import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

/**
 * http://www.hankcs.com/nlp/hanlp.html
 * @author PARANOIA_ZK
 * @date 2018/5/5 12:44
 */
public class WorldUtil {

    /**
     * N-最短路径分词
     * @param str
     * @return
     */
    public static List<Term> nShortSegment(String str){
        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);

        return nShortSegment.seg(str);

    }
}
