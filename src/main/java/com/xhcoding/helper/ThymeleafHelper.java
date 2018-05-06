package com.xhcoding.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

/**
 * Thymeleaf 模版引擎工具类
 * Created by Max on 2017/3/16.
 */
@Component
public class ThymeleafHelper {

    private static Context ctx = new Context(Locale.CHINA);

    @Autowired
    public TemplateEngine templateEngine;

    /**
     * 获取模版引擎渲染后的结果
     *
     * @return
     */
    public final String build(String templateName, Map<String, Object> paranMap) {
        ctx.clearVariables();
        if (!CollectionUtils.isEmpty(paranMap)) {
            ctx.setVariables(paranMap);
        }
        return templateEngine.process(templateName, ctx);
    }

}
