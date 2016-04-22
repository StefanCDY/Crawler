package com.zhihucrawler.spider;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import com.zhihucrawler.utils.CharsetUtil;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: HttpClient.java
 * @Description: TODO
 * @Date 2016-4-20 下午2:37:01
 */
public class HttpClientTool {

	// HttpClient连接参数
	private static final int connectTimeout = 10 * 1000;// 设置连接超时时间,单位毫秒.
	private static final int socketTimeout = 10 * 1000;// 请求获取数据的超时时间,单位毫秒.
	private static final int connectionRequestTimeout = 10 * 1000;// 设置从ConnectManager获取Connection超时时间,单位毫秒.
	private static final int maxTotal = 100;// 设置最大连接数
	private static final int defaultMaxPerRoute = 100;// 设置每个路由最大连接数
	private static final int count = 3;// 设置请求重试次数
	private static final String defaultCharset = "UTF-8";// 网页默认编码方式

	public static Map<String,String> cookieMap = new HashMap<String, String>(64);
	private static HashMap<String, String> headerParams;// 请求头参数
	private static String cookie;
	
	// 请求参数(设置全局的标准cookie策略)
	private static RequestConfig requestConfig = RequestConfig.custom()
			.setCookieSpec(CookieSpecs.STANDARD_STRICT)
			.setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout)
			.setConnectionRequestTimeout(connectionRequestTimeout).build();

	// 请求重试处理
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {

		@Override
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext httpContext) {
			if (executionCount >= count) {
				return false;// 如果超过最大重试次数,那么久不要继续了
			}
			if (exception instanceof NoHttpResponseException) {
				return true;// 如果服务器丢掉了连接,那么就重试
			}
			if (exception instanceof SSLHandshakeException) {
				return false;// 不要重试SSL握手异常
			}
			if (exception instanceof InterruptedIOException) {
				return false;// 超时
			}
			if (exception instanceof UnknownHostException) {
				return false;// 目标服务器不可达
			}
			if (exception instanceof ConnectTimeoutException) {
				return false;// 连接被拒绝
			}
			if (exception instanceof SSLException) {
				return false;// SSL握手异常
			}
			HttpRequest request = HttpClientContext.adapt(httpContext)
					.getRequest();
			if (!(request instanceof HttpEntityEnclosingRequest)) {
				return true;// 如果请求是幂等的,那么就重试
			}
			return false;
		}
	};
	
	// 声明重定向策略对象
	private static LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

	// 创建通用Socket工厂
	private static ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
			.getSocketFactory();
	// 创建SSL Socket工厂
	private static LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
			.getSocketFactory();
	// 自定义的Socket工厂类,可以和指定的协议（Http,Https）联系起来,用来创建自定义的连接管理器.
	private static Registry<ConnectionSocketFactory> registry = RegistryBuilder
			.<ConnectionSocketFactory> create().register("http", plainsf)
			.register("https", sslsf).build();

	// 连接池管理器
	private static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
			registry);

	private static CloseableHttpClient httpClient = HttpClients.custom()
			.setConnectionManager(connectionManager)
			.setRetryHandler(requestRetryHandler)
			.setRedirectStrategy(redirectStrategy)
//			.setDefaultCookieStore(new BasicCookieStore())
			.setDefaultRequestConfig(requestConfig).build();// 客户端

	static {
//		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
		connectionManager.setMaxTotal(maxTotal);// 默认最大连接数为20
		connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);// 默认每个路由基础的连接为2
		
//		cookie = "udid=\"ACDAUOormQmPTrOy3T97S-p3qpOy1GpB8FA=|1457677990\"; _za=f930075c-8fbd-41c3-8de9-dc7831f9bfa5; _zap=5b99d5e9-9397-44d8-80e5-373dcc2f5ec4; _xsrf=f07d49ec27142c77fad002544a00c91e; d_c0=\"AEAAQHh3owmPTt6M2WGn1XoIFIXw_9yNqVE=|1461079198\"; q_c1=f530e131d7dd45a999f6fba9ac49325a|1461157281000|1461157281000; l_n_c=1; l_cap_id=\"MDAyZjYwY2JjZGJiNGM4NGJmMThjOGNiMDQ1MjZhZmI=|1461218095|6818235a0c2bf737bd97805768603acbfb54140a\"; cap_id=\"MjAwNjQ0MjkxYWMxNDM5NGFlYWYzMjYxNmZhOTY5Y2E=|1461218095|aef6421a71fe56f9b916d128ccbbaab4b1c6a144\"; __utmt=1; login=\"ZGIyNGM5ZjU2MWY4NGQzNGE4M2FkZGRmMjg2MmRmMWI=|1461218120|90655f6106d82b272008dab61c8319e77899eb9e\"; z_c0=\"QUJES0dUa2Zkd2tYQUFBQVlRSlZUVWo0UDFmZ2ZkUV9HdHlPcmdIbXppQ2JUeGt2RW45bk5RPT0=|1461218120|e0b4bd8190136d8832c71a8ca21f53cc6a5c62fa\"; unlock_ticket=\"QUJES0dUa2Zkd2tYQUFBQVlRSlZUVkJ5R0ZjQjFmZW9DdTR5d1VfTnBYMmZ5U2haTWdhMmFRPT0=|1461218120|189c3162b43b3142286a84b8e30284c2dece26a7\"; __utma=51854390.706842575.1461217380.1461217380.1461217380.1; __utmb=51854390.16.10.1461217380; __utmc=51854390; __utmz=51854390.1461217380.1.1.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/; __utmv=51854390.100--|2=registration_date=20160214=1^3=entry_date=20160214=1";

		headerParams = new HashMap<String, String>();
		headerParams.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headerParams.put("Accept-Encoding", "gzip, deflate, sdch");
		headerParams.put("Accept-Language", "zh-CN,zh;q=0.8");
		headerParams.put("Connection", "keep-alive");
		headerParams.put("Host", "www.zhihu.com");
		headerParams.put("Referer", "https://www.zhihu.com/");
		headerParams.put("pragma", "no-cache");
		headerParams.put("cache-control", "no-cache");
		headerParams.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");
	}

	public HttpClientTool() {
//		login();
	}
	
	// 获取HttpClient对象
	private static CloseableHttpClient getHttpClient() {
		return httpClient;
	}
	
	// 获取Get请求
	private static HttpGet createGetRequest(String url) {
		url= url.replaceAll(" ", "%20");
		HttpGet request = new HttpGet(url);
		request.setConfig(requestConfig);
//		request.setHeader(new BasicHeader("Cookie", cookie));
		if (headerParams != null) {
			Iterator<Entry<String, String>> iterator = headerParams.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}
		return request;
	}
	
	// 获取Post请求
	private static HttpPost createPostRequest(String url) {
		url= url.replaceAll(" ", "%20");
		HttpPost request = new HttpPost(url);
		request.setConfig(requestConfig);
//		request.setHeader(new BasicHeader("Cookie", cookie));
		if (headerParams != null) {
			Iterator<Entry<String, String>> iterator = headerParams.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}
		return request;
	}

	// 模拟登录
	public void login() {
		HttpGet get = createGetRequest("https://www.zhihu.com/");// 创建一个get请求用来接收_xsrf信息
		CloseableHttpResponse response = null;
		try {
			response = getHttpClient().execute(get);
//			String pageCode = EntityUtils.toString(response.getEntity());
//			String xsrfValue = pageCode
//					.split("<input type=\"hidden\" name=\"_xsrf\" value=\"")[1]
//					.split("\"/>")[0];
			String headerCookie = setCookie(response);
			String l_n_c = cookieMap.get("l_n_c");
			String q_c1 = cookieMap.get("q_c1");
	        String _xsrf = cookieMap.get("_xsrf");
	        String l_cap_id = cookieMap.get("l_cap_id");
	        String cap_id = cookieMap.get("cap_id");
	        String n_c = cookieMap.get("n_c");
			StringBuffer sb = new StringBuffer();
	        sb.append("l_n_c=").append(l_n_c).append("; ");  
	        sb.append("q_c1=").append(q_c1).append("; ");
	        sb.append("_xsrf=").append(_xsrf).append("; ");  
	        sb.append("l_cap_id=").append(l_cap_id).append("; ");
	        sb.append("cap_id=").append(cap_id).append("; ");  
	        sb.append("n_c=").append(n_c).append("");
	        System.out.println(sb.toString());
	        response.close();
	        HttpGet validGet = createGetRequest("https://www.zhihu.com/captcha.gif?r=1461316607520&type=login");
	        validGet.addHeader("cookie", sb.toString());
	        response = getHttpClient().execute(validGet);
	        String validCode = saveGif(response);
	        System.out.println("validCode:" + validCode);
			response.close();
			// 构造post数据
			List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("_xsrf", _xsrf));
			valuePairs.add(new BasicNameValuePair("email", "848902343@qq.com"));
			valuePairs.add(new BasicNameValuePair("password", "cdy848902343"));
			valuePairs.add(new BasicNameValuePair("remember_me", "true"));
			valuePairs.add(new BasicNameValuePair("captcha", validCode));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs,
					Consts.UTF_8);
			// 创建一个登录请求
			HttpPost post = createPostRequest("https://www.zhihu.com/login/email");
			// 注入post数据
			post.setEntity(entity);
			getHttpClient().execute(post);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String saveGif(CloseableHttpResponse response) throws IOException {
        File storeFile = new File("d:/zhihu.gif");
        FileOutputStream output = new FileOutputStream(storeFile);
        //得到网络资源的字节数组,并写入文件
        response.getEntity().writeTo(output);
        output.close();
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String validCode = "";
        try {
        	validCode = br.readLine();
            br.close();
            is.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return validCode;
    }
	
	// 从响应信息中获取cookie
	public static String setCookie(HttpResponse httpResponse) {
		Header headers[] = httpResponse.getHeaders("Set-Cookie");
		if (headers == null || headers.length==0) {
			return null;
		}
		String cookie = "";
		for (int i = 0; i < headers.length; i++) {
			cookie += headers[i].getValue();
			if(i != headers.length - 1) {
				cookie += "; ";
			}
		}
		String cookies[] = cookie.split(";");
		for (String c : cookies) {
			c = c.trim();
			if(cookieMap.containsKey(c.split("=")[0])) {
				cookieMap.remove(c.split("=")[0]);
			}
			cookieMap.put(c.split("=")[0], c.split("=").length == 1 ? "" : (c.split("=").length == 2 ? c.split("=")[1] : c.split("=", 2)[1]));
		}
		String cookiesTmp = "";
		for (String key :cookieMap.keySet()) {
			cookiesTmp += key + "=" + cookieMap.get(key) + ";";
		}
		return cookiesTmp.substring(0, cookiesTmp.length());
	}
	
	private static void printResponse(HttpResponse httpResponse) throws IOException {
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		System.out.println("status:" + httpResponse.getStatusLine());
		System.out.println("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			System.out.println("\t" + iterator.next());
		}
		// 判断响应实体是否为空
		if (entity != null) {
			String responseString = EntityUtils.toString(entity);
			System.out.println("response length:" + responseString.length());
			System.out.println("response content:" + responseString.replace("\r\n", ""));
		}
	}
	
	// 抓取网页代码
	public String crawlPage(String url) {
		CloseableHttpClient httpClient = getHttpClient();
		HttpGet request = createGetRequest(url);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(request);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();// 获得响应实体

				String charset = null;
				ContentType contentType = ContentType.getOrDefault(httpEntity);// 获取Content-Type中的编码
				Charset charsets = contentType.getCharset();
				if (charsets != null) {
					charset = charsets.toString();
				}

				// 判断返回的数据流是否采用了gzip压缩
				Header header = httpEntity.getContentEncoding();
				boolean isGzip = false;
				if (header != null) {
					for (HeaderElement element : header.getElements()) {
						if (element.getName().equals("gzip")) {
							isGzip = true;
						}
					}
				}

				// 获得响应流
				InputStream inputStream = httpEntity.getContent();
				ByteArrayBuffer buffer = new ByteArrayBuffer(4096);
				byte[] tmp = new byte[4 * 1024];
				int count = 0;
				if (isGzip) {// 如果采用了Gzip压缩，则进行Gizp压缩处理
					GZIPInputStream gzipInputStream = new GZIPInputStream(
							inputStream);
					while ((count = gzipInputStream.read(tmp)) != -1) {
						buffer.append(tmp, 0, count);
					}
					gzipInputStream.close();
				} else {// 处理非Gzip格式的数据
					while ((count = inputStream.read(tmp)) != -1) {
						buffer.append(tmp, 0, count);
					}
				}

				// 如果ContentTyp未获取到编码,检测流的编码方式
				if (charset == null || "".equals(charset)
						|| "null".equals(charset)) {
					charset = CharsetUtil.getStreamCharset(
							new ByteArrayInputStream(buffer.toByteArray()),
							defaultCharset);
				}
				String pageCode = new String(buffer.toByteArray(), charset);
				return pageCode;
			} else {
				System.out.println(url + " ==>请求状态码:" + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			request.abort();
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * @author Stefan
	 * @Title main
	 * @Description TODO
	 * @param args
	 * @Date 2016-4-20 下午2:37:01
	 */
	public static void main(String[] args) {
//		System.out.println(System.currentTimeMillis());
		HttpClientTool httpClientTool = new HttpClientTool();
		httpClientTool.login();
		System.out.println(httpClientTool.crawlPage("https://www.zhihu.com/"));
//		
//		String cookie = "Set-Cookie:l_cap_id=\"OTk4YTRkZWM0ZDM5NGMwYWI2MzA5N2Q5ZDJlNDc0ODc=|1461254349|33d57bb7462a5ac9ed8cbcf29d4435c0da073b2e\"; Domain=zhihu.com; expires=Sat, 21 May 2016 15:59:09 GMT; Path=/";;
//		String SessionId = cookie.substring(cookie.indexOf("l_cap_id=") + "l_cap_id=".length(), cookie.indexOf(";"));  
//        System.out.println(SessionId);
//        System.out.println(cookie.indexOf("l_cap_id="));
//        System.out.println("l_cap_id=".length());
//		InputStreamReader is = new InputStreamReader(System.in);
//        BufferedReader br = new BufferedReader(is);
//        String ValidCode = "";
//        try {
//            ValidCode = br.readLine();
//            br.close();
//            is.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
	}

}
