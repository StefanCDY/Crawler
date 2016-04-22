package com.zhihucrawler.crawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ProxySelector;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import com.zhihucrawler.model.CrawlUrl;
import com.zhihucrawler.parser.HtmlParserTool;
import com.zhihucrawler.utils.CharsetUtil;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: HttpClient.java
 * @Description: HttpClient实体类
 * @Date 2016-4-4 下午9:45:00
 */
public class HttpClient {
	
	// HttpClient连接参数
	private final int connectTimeout = 5 * 1000;// 设置连接超时时间,单位毫秒.
	private final int socketTimeout = 5 * 1000 * 1000;// 请求获取数据的超时时间,单位毫秒.
	private final int connectionRequestTimeout = 5 * 1000 * 1000;// 设置从ConnectManager获取Connection超时时间,单位毫秒.
	private final int maxTotal = 200;// 设置最大连接数
	private final int defaultMaxPerRoute = 200;// 设置每个路由最大连接数
	
	public static Map<String,String> cookieMap = new HashMap<String, String>(64);
	
	// 登录信息
	private final String email = "848902343@qq.com";// 用户名
	private final String password = "cdy848902343";// 密码
	private final String captcha = "";
	private final String index = "http://www.zhihu.com/";// 知乎首页
	private final String loginAction = "http://www.zhihu.com/login/email";// 知乎登录跳转地址
	private final String charset = "UTF-8";// 网页默认编码方式
	
	private HashMap<String, String> headerParams = null;// 请求头参数
	private CloseableHttpClient httpClient = null;// 客户端
	
	public HttpClient() {
		
		this.initHttpClient();
		
		this.headerParams = new HashMap<String, String>();
		this.headerParams.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		this.headerParams.put("Accept-Encoding", "gzip, deflate, br");
		this.headerParams.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		this.headerParams.put("Connection", "keep-alive");
		this.headerParams.put("Host", "www.zhihu.com");
		this.headerParams.put("Referer", "https://www.zhihu.com/");
		this.headerParams.put("pragma", "no-cache");
		this.headerParams.put("cache-control", "no-cache");
		this.headerParams.put("Cookie", "udid=\"ACDAUOormQmPTrOy3T97S-p3qpOy1GpB8FA=|1457677990\"; _za=f930075c-8fbd-41c3-8de9-dc7831f9bfa5; _zap=5b99d5e9-9397-44d8-80e5-373dcc2f5ec4; _xsrf=f07d49ec27142c77fad002544a00c91e; q_c1=964e7cce754c4c159aff1cb5e5eb8520|1460532402000|1460532402000; d_c0=\"AEAAQHh3owmPTt6M2WGn1XoIFIXw_9yNqVE=|1461079198\"; l_n_c=1; l_cap_id=\"OGE1NzQ1MGEyYjYwNDMyNWExNWRkNjkxZTRiZjNiZWI=|1461133245|a2554ecbbd1489a321df7058c37ca5b27bcc26ac\"; cap_id=\"NWEzZmY5ODMzOTRkNDRjZGI1NmIwMTRjZDVhZjBhNzQ=|1461133245|77276bf8154b94e535cb6836bb1ddf36e231f546\"; __utmt=1; __utma=51854390.2024104712.1461080468.1461124306.1461132119.3; __utmb=51854390.8.10.1461132119; __utmc=51854390; __utmz=51854390.1461132119.3.3.utmcsr=hao123.com|utmccn=(referral)|utmcmd=referral|utmcct=/link/https/; __utmv=51854390.000--|2=registration_date=20160214=1^3=entry_date=20160413=1; login=\"NWI4YTAxMzk4Y2U3NDNkNjk3OGYwZDhjMjAyZDcxMDg=|1461133721|f96ba581aa9a56ffcda8450f51e3dc120570bea9\"; z_c0=\"QUJES0dUa2Zkd2tYQUFBQVlRSlZUWm11UGxmbkRiaUNqallFUHA0YUMwMnJiWHdLQVlKTWdnPT0=|1461133721|fd45274f214bd2f472c7e3b7e9f2badcc217171c\"; unlock_ticket=\"QUJES0dUa2Zkd2tYQUFBQVlRSlZUYUVvRjFlSjZ0UmZsUGt5S3g0NEJtMi1OcXY0UlpUdXJRPT0=|1461133721|c03fb5e367ae7fc8f23219499f9a706c00fcc170\"");
		this.headerParams.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0");
	}
	
	public CloseableHttpClient getHttpClient() {
		return this.httpClient;
	}
	
	private RequestConfig getRequestConfig() {
		// 请求参数(设置全局的标准cookie策略)
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD_STRICT)
				.setConnectTimeout(this.connectTimeout)
				.setSocketTimeout(this.socketTimeout)
				.setConnectionRequestTimeout(this.connectionRequestTimeout)
				.build();
		return requestConfig;
	}
	
	private void initHttpClient() {
		
		// 创建通用Socket工厂
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		// 创建SSL Socket工厂
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		// 自定义的Socket工厂类,可以和指定的协议(Http,Https)联系起来,用来创建自定义的连接管理器.
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", plainsf)
				.register("https", sslsf)
				.build();
		
		// 连接池管理器
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(this.maxTotal);// 默认最大连接数为20
		connectionManager.setDefaultMaxPerRoute(this.defaultMaxPerRoute);// 默认每个路由基础的连接为2
		
		// 请求重试处理
		HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
			
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
				if (executionCount >= 3) {
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
	            HttpRequest request = HttpClientContext.adapt(httpContext).getRequest();
	            if (!(request instanceof HttpEntityEnclosingRequest)) {
	                return true;// 如果请求是幂等的,那么就重试
	            }
				return false;
			}
			
		};
		
		// 声明重定向策略对象
		@SuppressWarnings("unused")
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
		
		// 声明长连接策略
		@SuppressWarnings("unused")
		ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {
			
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				
				// 遍历Response的Header
				HeaderElementIterator iterator = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
				while (iterator.hasNext()) {
					HeaderElement element = iterator.nextElement();
					String param = element.getName();
					String value = element.getValue();
					if (value != null && param.equalsIgnoreCase("Timeout")) {//如果头部包含timeout信息,则使用
						// 超时时间设置为服务器指定的值
						return Long.parseLong(value) * 1000;
					}
				}
				return 0;
			}
		};
		
		// 声明代理服务器
		String ip = "127.0.0.1";
		int port = 8080;
		HttpHost proxy = new HttpHost(ip, port);
		@SuppressWarnings("unused")
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);// 使用默认的代理服务器
		@SuppressWarnings("unused")
		SystemDefaultRoutePlanner jreRoutePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());// 使用jre的代理服务器
		
		this.httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setRetryHandler(requestRetryHandler)
				.setDefaultRequestConfig(this.getRequestConfig())
//				.setRedirectStrategy(redirectStrategy)
//				.setKeepAliveStrategy(keepAliveStrategy)
//				.setRoutePlanner(routePlanner)
				.build();
	}
	
	private HttpGet createGetRequest(String url) {
		url= url.replaceAll(" ", "%20");
		HttpGet request = new HttpGet(url);
		if (headerParams != null) {
			Iterator<Entry<String, String>> iterator = headerParams.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}
		return request;
	}
	
	private HttpPost createPostRequest(String url) {
		url= url.replaceAll(" ", "%20");
		HttpPost request = new HttpPost(url);
		request.setConfig(getRequestConfig());
		if (headerParams != null) {
			Iterator<Entry<String, String>> iterator = headerParams.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}
		return request;
	}
	
	public void login() {
		try {
			// 创建一个get请求用来接收_xsrf信息  
			HttpGet get = new HttpGet("http://www.zhihu.com/");  
			//获取_xsrf  
			CloseableHttpResponse response = httpClient.execute(get);
			String pageCode = EntityUtils.toString(response.getEntity());
			String xsrfValue = pageCode.split("<input type=\"hidden\" name=\"_xsrf\" value=\"")[1].split("\"/>")[0];  
			System.out.println("xsrfValue:" + xsrfValue);  
			response.close();
			
			// 构造post数据
			List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("_xsrf" , xsrfValue));
			valuePairs.add(new BasicNameValuePair("email" , this.email));
			valuePairs.add(new BasicNameValuePair("password" , this.password));
			valuePairs.add(new BasicNameValuePair("remember_me" , "true"));
//			valuePairs.add(new BasicNameValuePair("captcha" , "nexp"));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
			
			// 创建一个post请求  
			HttpPost post = new HttpPost("http://www.zhihu.com/login/eamil");
//			post.setHeader("Cookie", " cap_id=\"MjhhMzI3NWI4NjQyNDM4MTg4NzQ2ZDFmN2RhZmE2ZjU=|1461132161|1a6d9e0d60bfaef3b146a933ae277414c78ab291\"; ");
          
			// 注入post数据  
			post.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(post);
			
			//构造一个get请求，用来测试登录cookie是否拿到  
			HttpGet g = new HttpGet("http://www.zhihu.com/question/following");  
			CloseableHttpResponse r = httpClient.execute(g);  
			String content = EntityUtils.toString(r.getEntity());  
			System.out.println(content);  
			r.close(); 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 从响应信息中获取cookie  
	public static String setCookie(HttpResponse httpResponse) {
		System.out.println("----setCookieStore");
		Header headers[] = httpResponse.getHeaders("Set-Cookie");
		if (headers == null || headers.length == 0) {
			System.out.println("----there are no cookies");
			return null;
		}
		String cookie = "";
		for (int i = 0; i < headers.length; i++) {
			cookie += headers[i].getValue();
			if (i != headers.length - 1) {
				cookie += ";";
			}
		}

		String cookies[] = cookie.split(";");
		for (String c : cookies) {
			c = c.trim();
			if (cookieMap.containsKey(c.split("=")[0])) {
				cookieMap.remove(c.split("=")[0]);
			}
			cookieMap.put(c.split("=")[0], c.split("=").length == 1 ? "" : (c.split("=").length == 2 ? c.split("=")[1] : c.split("=", 2)[1]));
		}
		System.out.println("----setCookieStore success");
		String cookiesTmp = "";
		for (String key : cookieMap.keySet()) {
			cookiesTmp += key + "=" + cookieMap.get(key) + ";";
		}
		return cookiesTmp.substring(0, cookiesTmp.length() - 2);
	}
	
	public String crawlPage(String url) {
		CloseableHttpClient httpClient = this.getHttpClient();
		HttpGet request = this.createGetRequest(url);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(request, HttpClientContext.create());
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
						System.out.println(element.getName() + "-->" + element.getValue());
						if (element.getName().equals("gzip")) {
							isGzip = true;
						}
					}
				}
				
				InputStream inputStream = httpEntity.getContent();// 获得响应流
				ByteArrayBuffer buffer = new ByteArrayBuffer(4096);
				byte[] tmp = new byte[4 * 1024];
				int count = 0;
				if (isGzip) {// 如果采用了Gzip压缩，则进行Gizp压缩处理
					GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
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
				if (charset == null || "".equals(charset) || "null".equals(charset)) {
					charset = CharsetUtil.getStreamCharset(new ByteArrayInputStream(buffer.toByteArray()), this.charset);
				}
				String pageCode = new String(buffer.toByteArray(), charset);
				return pageCode;
			} else if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY) || (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) || (statusCode == HttpStatus.SC_SEE_OTHER) || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
				Header location = response.getFirstHeader("Location");
				String redirectUrl = location.getValue();
				System.out.println(url + "==>请求状态码:" + statusCode + "===重定向url:" + redirectUrl);
//			} else if (statusCode == 429) {
//				System.out.println(url + "==>请求状态码:" + statusCode);
//				for (Header header : response.getAllHeaders()) {
//					System.out.println(header.getName() + ":" + header.getValue());
//				}
			} else {
				System.out.println(url + "==>请求状态码:" + statusCode);
			} 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			request.abort();
			if(response != null){
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		HttpClient httpClient = new HttpClient();
//		httpClient.login();
		System.out.println(httpClient.crawlPage("https://www.zhihu.com/"));
	}
	
}
