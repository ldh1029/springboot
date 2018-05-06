package com.xhcoding.helper;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * okhttp 帮助类
 * Created by Max on 17/4/11.
 */
public class OKHttpHelper {
    private static Logger logger = LoggerFactory.getLogger(OKHttpHelper.class);
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 根据Url获取OkHttpClient
     *
     * @param url
     * @return
     */
    private static OkHttpClient getHttpClient(String url) {
        if (url.startsWith("https://")) {
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            try {
                trustManager = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                };

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).build();
        } else {
            return new OkHttpClient.Builder().build();
        }
    }


    /**
     * 提交json数据
     *
     * @param url   请求链接
     * @param json  json 数据
     * @param heads 头部信息
     * @return
     */
    public static String postJson(String url, String json, Map<String, String> heads) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        initHeadData(heads, requestBuilder); // 初始化头部信息
        RequestBody body = RequestBody.create(JSON, json);
        requestBuilder.post(body);
        Request request = requestBuilder.build();
        try {
            Response response = getHttpClient(url).newCall(request).execute();
            if (!response.isSuccessful()) {
                logger.info("Ok http helper 提交json数据错误！");
                return null;
            }
            return response.body().string();
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error(String.format("Ok http helper 工具类：http post JSON 请求异常: %s", e.getMessage()));
            return null;
        }
    }

    /**
     * http get 提交表单
     *
     * @param url    链接
     * @param params 请求参数
     * @param heads  头部
     * @return
     */
    public static String get(String url, Map<String, String> params, Map<String, String> heads) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        url = joinGetParams(url, params);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        initHeadData(heads, requestBuilder);
        Request request = requestBuilder.build();
        try {
            Response response = getHttpClient(url).newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println(response.message());
                logger.info("Ok http helper 请求失败！");
                return null;
            }

            return response.body().string();
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error(String.format("Ok http helper 工具类：http get 请求异常: %s", e.getMessage()));
            return null;
        }

    }

    /**
     * 主要用于get参数拼接
     *
     * @param url
     * @param params
     * @return
     */
    private static String joinGetParams(String url, Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        if (!CollectionUtils.isEmpty(params)) {
            sb.append(url);
            Set<Map.Entry<String, String>> entries = params.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();


            Map.Entry<String, String> next = iterator.next();
            sb.append("?").append(next.getKey()).append("=").append(next.getValue());
            while (iterator.hasNext()) {
                next = iterator.next();
                sb.append("&").append(next.getKey()).append("=").append(next.getValue());
            }

            url = sb.toString();
        }
        return url;
    }


    /**
     * http post 提交表单
     *
     * @param url      请求前缀
     * @param bodyForm 请求参数
     * @param heads    头部参数
     * @return 处理结果
     */
    public static String postForm(String url, Map<String, String> bodyForm, Map<String, String> heads) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        initHeadData(heads, requestBuilder); // 初始化头部信息
        initFormData(bodyForm, requestBuilder); // 初始化表单信息
        Request request = requestBuilder.build();
        try {
            Response response = getHttpClient(url).newCall(request).execute();
            if (!response.isSuccessful()) {
                logger.info("Ok http helper 请求失败！");
                return null;
            }

            return response.body().string();
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error(String.format("Ok http helper 工具类：http post 请求异常: %s", e.getMessage()));
            return null;
        }
    }

    /**
     * 初始化请求头部信息
     *
     * @param heads          信息集合
     * @param requestBuilder 请求构建类
     */
    private static void initHeadData(Map<String, String> heads, Request.Builder requestBuilder) {
        if (!CollectionUtils.isEmpty(heads)) {
            Set<Map.Entry<String, String>> entries = heads.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            while ((iterator.hasNext())) {
                Map.Entry<String, String> next = iterator.next();
                requestBuilder.addHeader(next.getKey(), next.getValue());
            }
        }
    }

    /**
     * 初始化表单信息
     *
     * @param bodyForm       表单信息集合
     * @param requestBuilder 请求构建类
     */
    private static void initFormData(Map<String, String> bodyForm, Request.Builder requestBuilder) {
        if (!CollectionUtils.isEmpty(bodyForm)) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            Set<Map.Entry<String, String>> entries = bodyForm.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                formBuilder.addEncoded(next.getKey(), next.getValue());
            }

            RequestBody formBody = formBuilder.build();
            requestBuilder.post(formBody);
        }
    }

}
