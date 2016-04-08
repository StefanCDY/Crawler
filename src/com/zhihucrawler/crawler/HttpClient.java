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
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

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
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;

import com.zhihucrawler.utils.CharsetUtil;
import com.zhihucrawler.utils.JsonUtil;

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
	
	// 登录信息
	private final String email = "848902343@qq.com";// 用户名
	private final String password = "cdy848902343";// 密码
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
		// 注册连接套接字工厂
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder
				.<ConnectionSocketFactory> create().register("http", plainsf)
				.register("https", sslsf).build();
		
		// 连接池管理器
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(this.maxTotal);
		connectionManager.setDefaultMaxPerRoute(this.defaultMaxPerRoute);
		
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
		
//		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();// 声明重定向策略对象
		
		this.httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setRetryHandler(requestRetryHandler)
//				.setRedirectStrategy(redirectStrategy)
				.setDefaultRequestConfig(this.getRequestConfig()).build();
		
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
	
	public void login() {
		try {
			String pageSource = this.crawlPage(this.index);
			String xsrfValue = pageSource.split("<input type=\"hidden\" name=\"_xsrf\" value=\"")[1].split("\"/>")[0];
			System.out.println(Thread.currentThread().getName() + "  xsrfValue:" + xsrfValue);
			List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("email" , this.email));
			valuePairs.add(new BasicNameValuePair("password" , this.password));
			valuePairs.add(new BasicNameValuePair("remember_me" , "true"));
			valuePairs.add(new BasicNameValuePair("_xsrf" , xsrfValue));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, "UTF-8");
			
			HttpPost post = this.createPostRequest(this.loginAction);
			post.setEntity(entity);
			
			CloseableHttpResponse response = this.getHttpClient().execute(post);// 进行登录
			Header[] responseHeaders = response.getAllHeaders();
//			System.out.println(JsonUtil.parseJson(responseHeaders));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String crawlPage(String url) {
		CloseableHttpClient httpClient = this.getHttpClient();
		HttpGet request = this.createGetRequest(url);
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
				return new String(buffer.toByteArray(), charset);
			} else if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY) || (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) || (statusCode == HttpStatus.SC_SEE_OTHER) || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
				Header location = response.getFirstHeader("Location");
				String redirectUrl = location.getValue();
				System.out.println(url + "==>请求状态码:" + statusCode + "===重定向url:" + redirectUrl);
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
}
