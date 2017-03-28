/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.enums;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午3:56
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public enum NationalityEnum {

    HAN("汉族"), ZHUANG("壮族"), MANCHU("满族"), HUI("回族"),

    MIAO("苗族"), UYGHUR("维吾尔族"), YI("彝族"), TUJIA("土家族"),

    MONGOL("蒙古族"), TIBETAN("藏族"), BUYEI("布依族"), DONG("侗族"),

    YAO("瑶族"), KOREAN("朝鲜族"), BAI("白族"), HANI("哈尼族"),

    LI("黎族"), KAZAK("哈萨克族"), DAI("傣族"), SHE("畲族"),

    LISU("僳僳族"), GELAO("仡佬族"), LAHU("拉祜族"), DONGXIANG("东乡族"),

    VA("佤族"), SUI("水族"), NAXI("纳西族"), QIANG("羌族"),

    TU("土族"), XIBE("锡伯族"), MULAO("仫佬族"), KIRGIZ("柯尔克孜族"),

    DAUR("达斡尔族"), JINGPO("景颇族"), SALAR("撒拉族"), BLANG("布朗族"),

    MAONAN("毛南族"), TAJIK("塔吉克族"), PUMI("普米族"), ACHANG("阿昌族"),

    NU("怒族"), EWENKI("鄂温克族"), GIN("京族"), JINO("基诺族"),

    DEANG("德昂族"), UZBEK("乌孜别克族"), RUSSIANS("俄罗斯族"), YUGUR("裕固族"),

    BONAN("保安族"), MONBA("门巴族"), OROQEN("鄂伦春族"), DERUNG("独龙族"),

    TATAR("塔塔尔族"), HEZHEN("赫哲族"), LHOBA("珞巴族"), GAOSHAN("高山族");


    private String name;

    public static NationalityEnum findByName(String name) {
        for (NationalityEnum ne : NationalityEnum.values()) {
            if (ne.getName().equals(name)) {
                return ne;
            }
        }
        return null;
    }

    private NationalityEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
