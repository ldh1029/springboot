package com.xhcoding.helper;

/**
 * Created by Administrator on 2017/7/18 0018.
 */
public class HtmlHelper {

    /**
     * 返回纯文本,去掉html的所有标签
     *
     * @param input
     * @return
     */
    public static String  filterHtml(String input) {
        if (input == null || input.trim().equals("")) {
            return "";
        }
        // 去掉所有html元素,
        String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
                "<[^>]*>", "");
        return str.replaceAll("[(/>)<]", "");
    }

}
