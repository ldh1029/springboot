package com.xhcoding.helper;

import java.util.Arrays;

/**
 * Created by Max on 2017/3/9.
 */
public class PhoneHelper {
    public static final int CHINA_MOBILE_NUM = 0;
    public static final int CHINA_TELECOM_NUM = 1;
    public static final int CHINA_UNICOM_NUM = 2;
    private static final String[] CHINA_UNICOM = new String[]{"130", "131", "132", "145", "155", "156", "1707", "1708", "1709", "1718", "1719", "175", "176", "185", "186"};//联通
    private static final String[] CHINA_TELECOM = new String[]{"133", "153", "1700", "1701", "1702", "173", "177", "180", "181", "189"};//电信
    private static final String[] CHINA_MOBILE = new String[]{"134", "135", "136", "137", "138", "139", "147", "150", "151", "152", "154", "157", "158", "159", "1703", "1705", "1706", "178", "182", "183", "184", "187", "188"};//移动

    /**
     * 通过手机号码获取运营商
     *
     * @param phone
     * @return -1 未匹配 0 移动 1 电信 2 联通
     */
    public static int getTelCorpCode(String phone) {
        String sectionNo = phone.substring(0, 3);
        if ("170".equals(sectionNo)) {
            sectionNo = phone.substring(0, 4);
        }

        if (Arrays.binarySearch(CHINA_UNICOM, sectionNo) >= 0) {//判断是否联调的号码
            return CHINA_UNICOM_NUM;
        }

        if (Arrays.binarySearch(CHINA_TELECOM, sectionNo) >= 0) {//电信
            return CHINA_TELECOM_NUM;
        }

        if (Arrays.binarySearch(CHINA_MOBILE, sectionNo) >= 0) {//移动
            return CHINA_MOBILE_NUM;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(getTelCorpCode("15270856117"));
    }
}
