package com.zhihucrawler.crawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
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
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;

import com.zhihucrawler.config.Const;
import com.zhihucrawler.utils.CharsetUtil;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: CrawlBase.java
 * @Description: 采集基类
 * @Date 2016-3-16 下午3:31:42
 */
public class CrawlBase {
	
	private static CloseableHttpClient httpClient = null;// 客户端
	private static HashMap<String, String> headerParams = null;// 请求头参数
	private static HashMap<String, String> loginParams = null;// 登录参数
	private String pageSourceCode = "";// 页面源代码
	private Header[] requestHeaders = null;// 请求头信息
	private Header[] responseHeaders = null;// 响应头信息
	
	public static final Object signal = new Object();   //线程间通信变量
	
	static {
		headerParams = new HashMap<String, String>();
		headerParams.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headerParams.put("Accept-Encoding", "gzip, deflate, br");
		headerParams.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		headerParams.put("Connection", "keep-alive");
		headerParams.put("Host", "www.zhihu.com");
		headerParams.put("Referer", "https://www.zhihu.com/");
		headerParams.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0");
		
		loginParams = new HashMap<String, String>();
		loginParams.put("email", Const.EMAIL);
		loginParams.put("password", Const.PASSWORD);
		loginParams.put("remember_me", "true");
	}
	
	private static RequestConfig getRequestConfig() {
		// 请求参数(设置全局的标准cookie策略)
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD_STRICT)
				.setConnectionRequestTimeout(Const.CONNECTION_REQUEST_TIMEOUT)
				.setConnectTimeout(Const.CONNECT_TIMEOUT)
				.setSocketTimeout(Const.SOCKET_TIMEOUT).build();
		return requestConfig;
	}
	
	public static CloseableHttpClient getHttpClient() {
		if (httpClient == null) {
			synchronized (signal) {
				if (httpClient == null) {
					httpClient = createHttpClient();
				}
			}
		}
		return httpClient;
	}
	
	private static CloseableHttpClient createHttpClient() {
		// 注册连接套接字工厂
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder
				.<ConnectionSocketFactory> create().register("http", plainsf)
				.register("https", sslsf).build();
		
		// 连接池管理器
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(Const.MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(Const.MAX_ROUTE_CONNECTIONS);
		
		// 请求重试处理
		HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
			
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
				if (executionCount >= Const.EXECUTION_COUNT) {
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
//		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
		
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setRetryHandler(requestRetryHandler)
//				.setRedirectStrategy(redirectStrategy)
				.setDefaultRequestConfig(getRequestConfig()).build();

		return httpClient;
	}
	
	private HttpGet createGetRequest(String url) {
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
	
	
	public String crawlByPost(String url) {
		HttpPost request = this.createPostRequest(url);
		return this.crawlPage(request, url);
	}
	
	public String crawlByGet(String url) {
		HttpGet request = this.createGetRequest(url);
		return this.crawlPage(request, url);
	}
	
	private String crawlPage(HttpUriRequest request, String url) {
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient httpClient = getHttpClient();
			response = httpClient.execute(request);
//			System.out.println(getHttpClient().hashCode());
			
			this.responseHeaders = response.getAllHeaders();
			this.requestHeaders = request.getAllHeaders();
			int statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode == HttpStatus.SC_OK) { 
				// 获得响应实体
				HttpEntity httpEntity = response.getEntity();

				// 从Content-Type的Charset(响应头信息)中获取编码
				String charset = null;
				ContentType contentType = ContentType.getOrDefault(httpEntity);
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
				
				// 获得响应流
				InputStream inputStream = httpEntity.getContent();
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
					charset = CharsetUtil.getStreamCharset(new ByteArrayInputStream(buffer.toByteArray()), Const.CHARSET);
				}
				
				return this.pageSourceCode = new String(buffer.toByteArray(), charset);
//				return true;
			} else if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY) || (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) || (statusCode == HttpStatus.SC_SEE_OTHER) || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
				Header location = response.getFirstHeader("Location");
				String redirectUrl = location.getValue();
				System.out.println(url + "==>请求状态码:" + statusCode + "===重定向Url:" + redirectUrl);
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
	
	private List<NameValuePair> getNameValuePair() {
		List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
        if (loginParams != null) {
			Iterator<Entry<String, String>> iterator = loginParams.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				valuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		return valuePairs;
	}
	
	public void login() {
		try {
			String url = "http://www.zhihu.com/";
			String pageSource = this.crawlByGet(url);
			String xsrfValue = pageSource.split("<input type=\"hidden\" name=\"_xsrf\" value=\"")[1].split("\"/>")[0];
//			System.out.println("xsrfValue:" + xsrfValue);
			
			List<NameValuePair> valuePairs = this.getNameValuePair();
			valuePairs.add(new BasicNameValuePair("_xsrf" , xsrfValue));
			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
			HttpPost post = createPostRequest("http://www.zhihu.com/login/email");
			post.setEntity(entity);
			getHttpClient().execute(post);// 登录
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPageSourceCode() {
		return this.pageSourceCode;
	}

	public Header[] getRequestHeaders() {
		return requestHeaders;
	}

	public Header[] getResponseHeaders() {
		return responseHeaders;
	}
	
	public static void main(String[] args) {
		CrawlBase base = new CrawlBase();
//		base.crawlPageByGet("http://www.zhihu.com/app/");
//		System.out.println(base.pageSourceCode);
		base.login();
		for (Header header : base.getResponseHeaders()) {
			System.out.println(header.getName() + ":" + header.getValue());
		}
	}

}
