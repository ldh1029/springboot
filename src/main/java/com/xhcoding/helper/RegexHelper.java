package com.xhcoding.helper;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * Created by Max on 2017/2/17.
 */
public class RegexHelper {


    /**
     * 正则：手机号（简单）, 1字头＋10位数字即可.
     */
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";

    /**
     * 中文， 數字， 字符 _
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{1,12}$";

    /**
     * 正则:支付密码,必须6位的纯数字密码
     */
    public static final String REGEX_PAY_PASSWORD = "^\\d{6}$";

    /**
     * 正则：手机号（精确）, 已知3位前缀＋8位数字
     * <p>
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     * </p>
     * <p>
     * 联通：130、131、132、145、155、156、175、176、185、186
     * </p>
     * <p>
     * 电信：133、153、173、177、180、181、189
     * </p>
     * <p>
     * 全球星：1349
     * </p>
     * <p>
     * 虚拟运营商：170
     * </p>
     */
    public static final String REGEX_MOBILE_EXACT = "^1[34578]\\d{9}$";

    /**
     * 正则：固定电话号码，可带区号，然后6至少8位数字
     */
    public static final String REGEX_TEL = "^(\\d{3,4}-)?\\d{6,8}$";

    /**
     * 正则：身份证号码15位, 数字且关于生日的部分必须正确
     */
    public static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

    /**
     * 正则：身份证号码18位, 数字且关于生日的部分必须正确
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";

    /**
     * 正则：邮箱, 有效字符(不支持中文), 且中间必须有@，后半部分必须有.
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 正则：URL, 必须有"://",前面必须是英文，后面不能有空格
     */
    public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";

    /**
     * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
     */
    public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";

    /**
     * 正则：IP地址
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    /**
     * 正则:登录/注册密码
     */

    public static final String REGEX_LOGIN_PASSWORD = "^(?![^a-zA-Z]+$)(?!\\D+$).{6,20}$";

    /**
     * 只能是数字
     */
    public static final String ONLY_IS_NUM = "^[0-9]*$";

    /**
     *银行卡
     */
    public static final String REGEX_BANK_CARD_ID="^\\d{15,21}$";

    /**
     * 匹配正则表达式与字符串
     *
     * @param regex 正则表达式
     * @param str   字符串
     * @return true 匹配成功  false 匹配不成功
     */
    public static boolean matches(String regex, String str) {
        return Pattern.matches(regex, str);
    }


}
