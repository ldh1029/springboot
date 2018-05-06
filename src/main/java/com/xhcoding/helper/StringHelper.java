package com.xhcoding.helper;

import org.springframework.util.ObjectUtils;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Max on 2017/3/6.
 */
public class StringHelper {

    public static String toString(Object obj) {
        return ObjectUtils.isEmpty(obj) ? "" : obj.toString();
    }

    public static String toString(Object obj, String defaultStr) {
        return ObjectUtils.isEmpty(obj) ? defaultStr : obj.toString();
    }

    /**
     * 格式化金额
     *
     * @param number 单位分
     * @param symbol 是否有千分符
     * @return
     */
    public static String formatDouble(double number, boolean symbol) {
        String va = "0.0";
        if (number > 0.001) {
            String fomat = null;
            if (symbol) {
                fomat = "#,##0.00";
            } else {
                fomat = "###0.00";
            }
            DecimalFormat df = new DecimalFormat(fomat);
            va = df.format(number);
        }
        return va;
    }

    /**
     * 格式化金额
     *
     * @param number 单位分
     * @param symbol 是否添加千分符
     * @return
     */
    public static String formatDouble(Long number, double divisor, boolean symbol) {
        return formatDouble(NumberHelper.toDouble(number) / NumberHelper.toDouble(divisor), symbol);
    }

    /**
     * 格式化金额
     *
     * @param number 单位分
     * @param symbol 是否添加千分符
     * @return
     */
    public static String formatDouble(Integer number, double divisor, boolean symbol) {
        return formatDouble(NumberHelper.toDouble(number) / NumberHelper.toDouble(divisor), symbol);
    }

    /**
     * 格式化金额
     *
     * @param number 单位分
     * @param symbol 是否添加千分符
     * @return
     */
    public static String formatDouble(Double number, double divisor, boolean symbol) {
        return formatDouble(NumberHelper.toDouble(number) / NumberHelper.toDouble(divisor), symbol);
    }

    /**
     * 替换模板
     *
     * @param template 魔板
     * @param params   替换参数
     * @return 替换后模板
     */
    public static String replateTemplace(String template, String leftPlaceholder, Map<String, String> params, String rightPlaceholder) {
        Iterator<String> iterator = params.keySet().iterator();
        String key = null;
        String value = null;

        while (iterator.hasNext()) {
            key = iterator.next();
            value = params.get(key);
            key = String.format("%s%s%s", leftPlaceholder, key, rightPlaceholder);
            template = template.replace(key, value);
        }

        return template;
    }


    /**
     * 获取Map的待签名字符串
     *
     * @param map
     * @return
     */
    public static String mergeMap(Map<String, String> map) {
        checkNotNull(map, "");
        Map<String, String> reqMap = new TreeMap<String, String>(map);

        StringBuffer buff = new StringBuffer();

        Iterator<Map.Entry<String, String>> iter = reqMap.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            if (!"sign".equalsIgnoreCase(entry.getKey())) {
                if (entry.getValue() == null) {
                    entry.setValue("");
                    buff.append("");
                } else {
                    buff.append(String.valueOf(entry.getValue()));
                }
            }
        }

        return buff.toString();
    }

    public static String formatMon(double number) {
        String money = "0.0";
        if (number > 0.001) {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            money = df.format(number);
            if (money.substring(money.length() - 3, money.length()).equals(".00")) {
                return money.substring(0, money.length() - 3);
            }
        }
        return money;
    }

    public static String formatDouble(double number) {
//        String text = String.valueOf(d);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(number);
//        DecimalFormat df = null;
//        if (text.indexOf(".") > 0) {
//            if (text.length() - text.indexOf(".") - 1 == 0) {
//                df = new DecimalFormat("###,##0.");
//            } else if (text.length() - text.indexOf(".") - 1 == 1) {
//                df = new DecimalFormat("###,##0.0");
//            } else {
//                df = new DecimalFormat("###,##0.00");
//            }
//        } else {
//            df = new DecimalFormat("###,##0");
//        }
//        double number = 0.0;
//        try {
//            number = Double.parseDouble(text);
//        } catch (Exception e) {
//            number = 0.0;
//        }
//        return df.format(number);
    }


}
