package com.juzipi.demo.filter;

import com.google.common.base.Charsets;
import com.juzipi.demo.rsa.RsaKeys;
import com.juzipi.demo.service.RsaService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RSARequestFilter extends ZuulFilter {

    @Autowired
    private RsaService rsaService;

    @Override
    public String filterType() {
        //过滤器在什么环节执行
        //解密操作需要在转发之前执行
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //设置过滤器执行顺序
        return FilterConstants.PRE_DECORATION_FILTER_ORDER+1;
    }

    @Override
    public boolean shouldFilter() {
        //是否使用过滤器
        //使用过滤器
        return true;
    }


    @Override
    public Object run() throws ZuulException {
        /**
         * 1. 从request body中读取出加密后的请求参数
         * 2. 将加密后的请求参数用私钥解密
         * 3. 将解密后的请求参数写回request body中
         * 4. 转发请求
         */
        System.out.println("zuulException执行了");

        //获取RequestContext容器 拿到Request请求和Response响应
        RequestContext currentContext = RequestContext.getCurrentContext();

        //通过容器获得Request和Response请求响应
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();

        String requestData = null;//声明存放加密后的数据的变量
        String decryptData = null;//声明存放解密后的数据变量

        try {
            //通过Request获取inputStream
            ServletInputStream inputStream = request.getInputStream();

            //从inputsteam中得到加密后的数据
            requestData = StreamUtils.copyToString(inputStream, Charsets.UTF_8);
            System.out.println(requestData);//输出加密后的数据

            //对加密后的数据进行解密操作
            if(StringUtils.isNotBlank(requestData)){
                decryptData = rsaService.RSADecryptDataPEM(requestData, RsaKeys.getServerPrvKeyPkcs8());
                System.out.println(decryptData);
            }

            if(StringUtils.isNotBlank(decryptData)) {
                System.out.println("json字符串写入request body");
                byte[] bytes = decryptData.getBytes();
                currentContext.setRequest(new HttpServletRequestWrapper(request) {
                    @Override
                    public ServletInputStream getInputStream() throws IOException {
                        return new ServletInputStreamWrapper(bytes);
                    }

                    @Override
                    public int getContentLength() {
                        return bytes.length;
                    }

                    @Override
                    public long getContentLengthLong() {
                        return bytes.length;
                    }
                });
            }

            // 设置request请求头中的Content-Type为application/json，否则api接口模块需要进行url转码操作
            currentContext.addZuulRequestHeader("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8");
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + "运行出错" + e.getMessage());
        }
        return null;
    }







//    @Override
//    public Object run() throws ZuulException {
//        //过滤器具体执行的逻辑
//        System.out.println("过滤器执行");
//        //获取requestContext容器
//        RequestContext currentContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = currentContext.getRequest();
//        HttpServletResponse response = currentContext.getResponse();
//        //声明存放加密后的数据的变量
//        String requestData = null;
//        //声明解密后的数据
//        String decryptData = null;
//            //通过request请求获取inputStream
//        ServletInputStream inputStream = null;
//        try {
//            inputStream = request.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //从inputStream中得到加密后的数据
//        try {
//            requestData = StreamUtils.copyToString(inputStream, Charsets.UTF_8);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("得到的加密数据："+requestData);
//            //requestData不为空就解密
//            if (StringUtils.isNotBlank(requestData)){
//                try {
//                    decryptData = rsaService.RSADecryptDataPEM(requestData, RsaKeys.getServerPrvKeyPkcs8());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                System.out.println("解密："+decryptData);
//            }
//            //解密后的数据转发给文章微服务
//            if (Strings.isNotBlank(decryptData)){
//                //获取到解密后的字节数组
//                byte[] bytes = decryptData.getBytes();
//                System.out.println("得到的字节数组："+bytes);
//
//                String url = request.getRequestURI().toString();
//
////                currentContext.setRequest(new HttpServletRequestWrapper(request) {
////                    @Override
////                    public ServletInputStream getInputStream() throws IOException {
////                        return new ServletInputStreamWrapper(bytes);
////                    }
////
////                    @Override
////                    public int getContentLength() {
////                        return bytes.length;
////                    }
////
////                    @Override
////                    public long getContentLengthLong() {
////                        return bytes.length;
////                    }
////                });
//            }
//            //设置request请求头中的Content-Type为Json格式的数据
//            //不设置的话，api接口模块就需要进行url转码的操作
//            currentContext.addZuulRequestHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//        System.out.println("过滤器执行完毕");
//        return null;
//    }
}
