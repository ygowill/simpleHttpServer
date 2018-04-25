package context.impl;

import java.util.*;
import org.apache.log4j.Logger;
import context.Request;

public class HttpRequest implements Request {

    private Logger logger= Logger.getLogger(HttpRequest.class);

    //参数
    private Map<String, Object> attribute = new HashMap<>();

    //请求头(Request Header)
    private Map<String, Object> headers = new HashMap<>();

    //请求方法
    private String method;

    //uri
    private String uri;

    //协议版本
    private String protocol;

    public HttpRequest(String httpHeader) {
        init(httpHeader);
    }

    private void init(String httpHeader) {
        //将请求分行
        String[] headers = httpHeader.split("\r\n");
        //设置请求方式
        initMethod(headers[0]);
        //设置URI
        initURI(headers[0]);
        //设置版本协议
        initProtocol(headers[0]);
        //设置请求头
        initRequestHeaders(headers);
    }

    private void initMethod(String str) {
        method = str.substring(0, str.indexOf(" "));
        logger.info("method: "+method);
    }

    private void initAttribute(String attr) {
        String[] attrs = attr.split("&");
        for (String string : attrs) {
            String key = string.substring(0, string.indexOf("="));
            String value = string.substring(string.indexOf("=") + 1);
            attribute.put(key, value);
        }
    }

    private void initURI(String str) {
        uri = str.substring(str.indexOf(" ") + 1, str.indexOf(" ", str.indexOf(" ") + 1));
        logger.info("uri: "+uri);
        //如果是get方法，则后面跟着参数   /index?a=1&b=2
        if(method.toUpperCase().equals("GET")) {
            //有问号表示后面跟有参数
            if(uri.contains("?")) {
                String attr = uri.substring(uri.indexOf("?") + 1, uri.length());
                uri = uri.substring(0, uri.indexOf("?"));
                initAttribute(attr);
            }
        }
    }

    private void initRequestHeaders(String[] strs) {
        //去掉第一行
        for(int i = 1; i < strs.length; i++) {
            String key = strs[i].substring(0, strs[i].indexOf(":"));
            String value = strs[i].substring(strs[i].indexOf(":") + 1);
            headers.put(key, value);
            logger.info(key+": "+value);
        }
    }

    private void initProtocol(String str) {
        protocol = str.substring(str.lastIndexOf(" ") + 1, str.length());
        logger.info("protocol: "+protocol);
    }

    @Override
    public Map<String, Object> getAttribute() {
        return attribute;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public Map<String, Object> getHeaders() {
        return headers;
    }

    @Override
    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    @Override
    public Object getHeader(String key) {
        return headers.get(key);
    }
}