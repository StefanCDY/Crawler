//package test;
//
//import java.io.IOException;  
//import java.io.InputStream;  
//import java.io.InterruptedIOException;  
//import java.net.MalformedURLException;  
//import java.net.URI;  
//import java.net.URISyntaxException;  
//import java.net.URL;  
//import java.net.UnknownHostException;  
//import java.nio.charset.Charset;  
//import java.security.KeyManagementException;  
//import java.security.KeyStoreException;  
//import java.security.NoSuchAlgorithmException;  
//import java.security.cert.CertificateException;  
//import java.security.cert.X509Certificate;  
//import java.util.Random;  
//import java.util.regex.Matcher;  
//import java.util.regex.Pattern;  
//import java.util.zip.GZIPInputStream;  
//  
//import javax.net.ssl.SSLContext;  
//import javax.net.ssl.SSLException;  
//  
//import org.apache.http.Header;  
//import org.apache.http.HeaderElement;  
//import org.apache.http.HttpEntity;  
//import org.apache.http.HttpEntityEnclosingRequest;  
//import org.apache.http.HttpHost;  
//import org.apache.http.HttpRequest;  
//import org.apache.http.client.ClientProtocolException;  
//import org.apache.http.client.HttpRequestRetryHandler;  
//import org.apache.http.client.config.CookieSpecs;  
//import org.apache.http.client.config.RequestConfig;  
//import org.apache.http.client.methods.CloseableHttpResponse;  
//import org.apache.http.client.methods.HttpGet;  
//import org.apache.http.client.protocol.HttpClientContext;  
//import org.apache.http.conn.ConnectTimeoutException;  
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;  
//import org.apache.http.entity.ContentType;  
//import org.apache.http.impl.client.CloseableHttpClient;  
//import org.apache.http.impl.client.HttpClients;  
//import org.apache.http.impl.client.LaxRedirectStrategy;  
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;  
//import org.apache.http.protocol.HttpContext;  
//import org.apache.http.ssl.SSLContextBuilder;  
//import org.apache.http.ssl.TrustStrategy;  
//import org.apache.http.util.ByteArrayBuffer;  
//  
//public class InfoLoad {  
//    // 创建httpclient连接池  
//    private PoolingHttpClientConnectionManager httpClientConnectionManager = null;  
//      
//    /******单例模式声明开始******/  
//    //类初始化时，自动实例化，饿汉单例模式  
//    private static final InfoLoad infoLoad = new InfoLoad();  
//    /** 
//     *  
//     * 方法名：getInfoLoadInstance 
//     * 作者：zhouyh 
//     * 创建时间：2015-10-14 上午08:59:54 
//     * 描述：单例的静态方法，返回InfoLoad的实例 
//     * @return 
//     */  
//    public static InfoLoad getInfoLoadInstance(){  
//        return infoLoad;  
//    }  
//    /******单例模式声明结束******/  
//    /** 
//     * 私有的构造函数 
//     */  
//    private InfoLoad(){  
//        //初始化httpClient  
//        initHttpClient();  
//    }  
//    /** 
//     *  
//     * 方法名：initHttpClient 
//     * 作者：zhouyh 
//     * 创建时间：2015-10-14 上午11:00:30 
//     * 描述：创建httpclient连接池，并初始化httpclient 
//     */  
//    public void initHttpClient(){  
//        //创建httpclient连接池  
//        httpClientConnectionManager = new PoolingHttpClientConnectionManager();  
//        //设置连接池最大数量  
//        httpClientConnectionManager.setMaxTotal(Constant.HTTPCLIENT_CONNECTION_COUNT);  
//        //设置单个路由最大连接数量  
//        httpClientConnectionManager.setDefaultMaxPerRoute(Constant.HTTPCLIENT_MAXPERROUTE_COUNT);  
//    }  
//    //请求重试机制  
//    HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {  
//        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {  
//            if (executionCount >= 3) {  
//                // 超过三次则不再重试请求  
//                return false;  
//            }  
//            if (exception instanceof InterruptedIOException) {  
//                // Timeout  
//                return false;  
//            }  
//            if (exception instanceof UnknownHostException) {  
//                // Unknown host  
//                return false;  
//            }  
//            if (exception instanceof ConnectTimeoutException) {  
//                // Connection refused  
//                return false;             
//            }  
//            if (exception instanceof SSLException) {  
//                // SSL handshake exception  
//                return false;  
//            }  
//            HttpClientContext clientContext = HttpClientContext.adapt(context);  
//            HttpRequest request = clientContext.getRequest();  
//            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);  
//            if (idempotent) {  
//                // Retry if the request is considered idempotent  
//                return true;  
//            }  
//            return false;  
//        }  
//    };  
//    /** 
//     *  
//     * 方法名：getHttpClient 
//     * 作者：zhouyh 
//     * 创建时间：2016-2-18 下午01:23:32 
//     * 描述：多线程调用时，需要创建自己的httpclient 
//     * @return 
//     */  
//    public CloseableHttpClient getHttpClient(){       
//        // 创建全局的requestConfig  
//        RequestConfig requestConfig = RequestConfig.custom()  
//                .setConnectTimeout(Constant.HTTPCLIENT_CONNECT_TIMEOUT)  
//                .setSocketTimeout(Constant.HTTPCLIENT_SOCKET_TIMEOUT)  
//                .setCookieSpec(CookieSpecs.BEST_MATCH).build();  
//        // 声明重定向策略对象  
//        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();  
//          
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpClientConnectionManager)  
//                                                    .setDefaultRequestConfig(requestConfig)  
//                                                    .setRedirectStrategy(redirectStrategy)  
//                                                    .setRetryHandler(myRetryHandler)  
//                                                    .build();     
//        return httpClient;   
//    }  
//      
//    /** 
//     *  
//     * 方法名：loadForString 
//     * 作者：zhouyh 
//     * 创建时间：2015-10-14 下午02:22:19 
//     * 描述：根据传入的url获取下载信息 
//     * @param url 
//     * @param type 
//     * @return 
//     */  
//    public static String loadForString(String urlString, int type){  
//        String src = "";  
//        if(null==urlString || urlString.isEmpty() || !urlString.startsWith("http")){//如果urlString为null或者urlString为空，或urlString非http开头，返回src空值  
//            return src;  
//        }  
//        //创建response  
//        CloseableHttpResponse response = null;  
//        HttpGet httpGet = null;  
//        urlString = urlString.trim();//防止传入的urlString首尾有空格  
//        //转化String url为URI,解决url中包含特殊字符的情况  
//        try {  
//            URL url = new URL(urlString);  
//            URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);  
//            httpGet = new HttpGet(uri);  
//            //针对https采用SSL的方式创建httpclient  
////          if(urlString.startsWith("https")){  
////              httpClient = createSSLClientDefault();  
////              System.setProperty ("jsse.enableSNIExtension", "false");  
////          }  
//            //设置请求头  
//            httpGet.addHeader("Accept","*/*");  
////          httpGet.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");  
//            httpGet.addHeader("Connection","keep-alive");  
//            httpGet.addHeader("Accept-Encoding", "gzip, deflate");  
//              
//            //设置USER_AGENT  
//            Random random = new Random();  
//            int randomInt = random.nextInt(4);  
//            System.err.println(randomInt);  
//              
//            httpGet.addHeader("User-Agent", Constant.USER_AGENT[randomInt]);  
//            //此处的代理暂时注释  
////          String[] proxys = Constant.HTTPCLIENT_PROXY[randomInt].split("\\s+");  
////          //添加代理  
////          HttpHost proxy = new HttpHost(proxys[0].trim(), Integer.parseInt(proxys[1].trim()), "http");  
////          RequestConfig config = RequestConfig.custom().setProxy(proxy).build();  
////          httpGet.setConfig(config);    
//            //执行请求        
//            try {  
//                if(urlString.startsWith("https")){  
//                    System.setProperty ("jsse.enableSNIExtension", "false");  
//                    response = createSSLClientDefault().execute(httpGet);  
//                }else{  
//                    response = infoLoad.getHttpClient().execute(httpGet);  
//                }  
//            } catch (Exception e) {  
//                e.printStackTrace();  
//            }  
//              
//            //得到响应状态码  
//            int statuCode = response.getStatusLine().getStatusCode();  
//            //根据状态码进行逻辑处理  
//            switch (statuCode){  
//            case 200:  
//                //获得响应实体  
//                HttpEntity entity = response.getEntity();  
//                /**  
//                 * 仿浏览器获取网页编码  
//                 * 浏览器是先从content-type的charset（响应头信息）中获取编码，  
//                 * 如果获取不了，则会从meta（HTML里的代码）中获取charset的编码值  
//                 */  
//                //第一步-->处理网页字符编码  
//                String charset = null;  
//                ContentType contentType = null;  
//                contentType = ContentType.getOrDefault(entity);  
//                Charset charsets = contentType.getCharset();  
//                if(null != charsets){  
//                    charset = charsets.toString();  
//                }  
//                //判断返回的数据流是否采用了gzip压缩  
//                Header header = entity.getContentEncoding();  
//                boolean isGzip = false;  
//                if(null != header){  
//                    for(HeaderElement headerElement : header.getElements()){  
//                        if(headerElement.getName().equalsIgnoreCase("gzip")){  
//                            isGzip = true;  
//                        }  
//                    }  
//                }  
//                //获得响应流  
//                InputStream inputStream = entity.getContent();  
//                ByteArrayBuffer buffer = new ByteArrayBuffer(4096);  
//                byte[] tmp = new byte[4096];  
//                int count;  
//                if(isGzip){//如果采用了Gzip压缩，则进行gizp压缩处理  
//                    GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);  
//                    while((count=gzipInputStream.read(tmp)) != -1){  
//                        buffer.append(tmp, 0, count);  
//                    }  
//                }else{//处理非gzip格式的数据  
//                    while((count=inputStream.read(tmp)) != -1){  
//                        buffer.append(tmp, 0, count);  
//                    }  
//                }  
//                //第二步--->如果第一步contenttyp未获取到编码，这里从meta标签中获取  
//                if(null==charset || "".equals(charset) || "null".equals(charset)   
//                        || "zh-cn".equalsIgnoreCase(charset)){  
//                    charset = getCharsetFromMetaTag(buffer, urlString);
//                }  
//                //根据获取的字符编码转为string类型  
//                src = new String(buffer.toByteArray(), charset);  
//                //替换特殊编码  
//                src = replaceStr(src);  
//                //转化Unicode编码格式]  
//                src = Common.decodeUnicode(src);  
////              System.out.println(src);  
//                break;  
//            case 400:  
//                System.out.println("下载400错误代码，请求出现语法错误" + urlString);  
//                //TODO 要进行判断是列表页还是正文页下载，再去修改数据库，下同  
//                //TODO 此处添加对mongodb数据库的操作，将该url的isStart改为0，暂时不在进行监控，后续根据模板状态为0的进行修改  
//                break;  
//            case 403:  
//                System.out.println("下载403错误代码，资源不可用" + urlString);                
//                //TODO 此处添加对mongodb数据库的操作，将该url的isStart改为0，暂时不在进行监控，后续根据模板状态为0的进行修改  
//                break;  
//            case 404:  
//                System.out.println("下载404错误代码，无法找到指定资源地址" + urlString);  
//                //TODO 此处添加对mongodb数据库的操作，将该url的isStart改为0，暂时不在进行监控，后续根据模板状态为0的进行修改  
//                break;  
//            case 503:  
//                System.out.println("下载503错误代码，服务不可用" + urlString);  
//                //TODO 此处添加对mongodb数据库的操作，将该url的isStart改为0，暂时不在进行监控，后续根据模板状态为0的进行修改  
//                break;  
//            case 504:  
//                System.out.println("下载504错误代码，网关超时" + urlString);  
//                //TODO 此处添加对mongodb数据库的操作，将该url的isStart改为0，暂时不在进行监控，后续根据模板状态为0的进行修改  
//                break;  
//            }  
//                  
//        } catch (MalformedURLException e) {  
//            //执行URL url = new URL()的异常  
//            e.printStackTrace();  
//        } catch (URISyntaxException e) {  
//            //执行URI uri = new URI()的异常  
//            e.printStackTrace();  
//        } catch (ClientProtocolException e) {  
//            // 执行httpClient.execute(httpGet)的异常  
//            e.printStackTrace();  
//        } catch (IOException e) {  
//            // 执行httpClient.execute(httpGet)的异常  
//            e.printStackTrace();  
//        } finally{  
//            if(response != null){  
//                try {  
//                    response.close();  
//                } catch (IOException e) {  
//                    e.printStackTrace();  
//                }  
//            }  
//            httpGet.abort();    //结束后关闭httpGet请求  
//            /** 
//             * httpclient的链接有线程池管理，这里不用直接关闭 
//             */  
////          try {//关闭连接  
////              httpClient.close();  
////          } catch (IOException e) {  
////              e.printStackTrace();  
////          }     
//        }  
//          
//        return src;  
//    }  
//    /** 
//     *  
//     * 方法名：createSSLClientDefault 
//     * 作者：zhouyh 
//     * 创建时间：2015-10-14 下午03:03:30 
//     * 描述：针对https采用SSL的方式创建httpclient 
//     * @return 
//     */  
//    public static CloseableHttpClient createSSLClientDefault(){       
//        try {             
//            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy(){  
//            // 信任所有  
//            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
//                return true;  
//            }}).build();  
//  
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);  
//  
//            return HttpClients.custom().setSSLSocketFactory(sslsf).build();  
//  
//        } catch (KeyManagementException e) {  
//            e.printStackTrace();  
//        } catch (NoSuchAlgorithmException e) {  
//            e.printStackTrace();  
//        } catch (KeyStoreException e) {  
//            e.printStackTrace();  
//        }  
//              
//        return  HttpClients.createDefault();  
//    }  
//    /** 
//     *  
//     * 方法名：getCharsetFromMetaTag 
//     * 作者：zhouyh 
//     * 创建时间：2015-10-14 下午05:23:08 
//     * 描述：从meta标签中获取编码格式 
//     * @param buffer 
//     * @param url 
//     * @return 
//     */  
//    public static String getCharsetFromMetaTag(ByteArrayBuffer buffer,String url){  
//        String charset = null;  
//        String regEx = Constant.CHARSET_REGEX;  
//        Pattern p = Pattern.compile(regEx,  
//                Pattern.CASE_INSENSITIVE);  
//        Matcher m = p.matcher(new String(buffer.toByteArray()));  
//        boolean result = m.find();  
//        if (result) {  
//            if (m.groupCount() == 1) {  
//                charset = m.group(1);  
//            }   
//            System.err.println("网页 中的编码:" + charset + "\t url:" + url);  
//        } else {  
//            //出现未匹配的编码，先赋值为gbk  
//            charset = "gbk";  
//            System.out.println("字符编码未匹配到 : " + url);  
//        }  
//        return charset;  
//    }  
//    /** 
//     *  
//     * 方法名：replaceStr 
//     * 作者：zhouyh 
//     * 创建时间：2015-10-14 下午05:33:01 
//     * 描述：替换原网页中的特殊字符 
//     * @param src 
//     * @return 
//     */  
//    public static String replaceStr(String src){
//        if (src == null || "".equals(src)) return null;
//        src = src.replaceAll("<!--", "");  
//        src = src.replaceAll("-->", "");  
//        src = src.replaceAll("<", "<");  
//        src = src.replaceAll(">", ">");  
//        src = src.replaceAll(""", "\"");  
//        src = src.replaceAll(" ", " ");  
//        src = src.replaceAll("&", "&");  
//        return src;  
//    }  
//      
//    /** 
//     * 方法名：main 
//     * 作者：zhouyh 
//     * 创建时间：2015-10-14 上午08:46:47 
//     * 描述：main方法 
//     * @param args 
//     */  
//    public static void main(String[] args) {  
//        // TODO Auto-generated method stub  
//        Random random = new Random();  
//        int randomInt = random.nextInt(4);  
//        System.out.println(randomInt);  
////      InfoLoad.getInfoLoadInstance().loadForString("http://weixin.sogou.com/remind/doc_list.php?callback=jQuery111006747886650961997_1446517725478&from=web&uid=B31F8214DA30BE47F750B8BE2BF0E4AA%40qq.sohu.com&start=0&num=20&wordid=237&clear=1&_=1446517725480", 0);  
//        InfoLoad.getInfoLoadInstance().loadForString("http://www.xinli001.com/info", 0);  
//    }  
//  
//}  