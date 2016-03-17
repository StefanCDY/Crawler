package com.zhihucrawler.crawler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.zhihucrawler.config.Const;
import com.zhihucrawler.utils.CharsetUtil;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: CrawlBase.java
 * @Description: 采集基类
 * @Date 2016-3-16 下午3:31:42
 */
public abstract class CrawlBase {
	
	private String pageSourceCode = "";// 页面源代码
	private Header[] requestHeaders = null;// 请求头信息
	private Header[] responseHeaders = null;// 响应头信息
	
	private static HashMap<String, String> params;
	
	// 请求参数(设置全局的标准cookie策略)
	private static RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).setConnectionRequestTimeout(Const.CONNECTION_REQUEST_TIMEOUT).setConnectTimeout(Const.CONNECT_TIMEOUT).setSocketTimeout(Const.SOCKET_TIMEOUT).build();
	// 注册连接套接字工厂
	private static Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
	// 连接池管理器
	private static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
	// 请求重试处理
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		
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
	// 客户端
	private static CloseableHttpClient httpClient = HttpClients.custom().setMaxConnPerRoute(Const.MAX_ROUTE_CONNECTIONS).setMaxConnTotal(Const.MAX_TOTAL_CONNECTIONS).setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager).setRetryHandler(requestRetryHandler).build();
	
	static {
		params = new HashMap<String, String>();
		params.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		params.put("Accept-Encoding", "gzip, deflate, br");
		params.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		params.put("Connection", "keep-alive");
		params.put("Cookie", "q_c1=6e908fb2156e44eeaa9200c7faaad076|1457511240000|1454752297000; cap_id=\"N2RiMTMxODQwZjAwNGEyZWI5YWJjMTFmYTVkMTQ4MWI=|1458204041|c54af4c9fede40f18d78fecc1524a4d58b7a41e6\"; _za=a7e8266d-324f-46fe-a243-70edcd17b647; __utma=51854390.613105210.1458200811.1458200811.1458200811.1; __utmz=51854390.1458200811.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); z_c0=\"QUJES0dUa2Zkd2tYQUFBQVlRSlZUWnI2RVZmYldGb1B5RDZNRmc1RU9naHFnQVg1ZWZnNzV3PT0=|1458204058|b0de11c46293634323c9247c3b0464b27d4eb3b5\"; _xsrf=95ddb08fc9b084718aac9aeafc9e9acf; udid=\"AIAAPuxSlAmPTmyUrcrtwMkNfMU7KwsAB2s=|1457511256\"; n_c=1; __utmc=51854390; __utmb=51854390.10.10.1458200811; __utmv=51854390.100--|2=registration_date=20160214=1^3=entry_date=20160206=1; unlock_ticket=\"QUJES0dUa2Zkd2tYQUFBQVlRSlZUYUowNmxiWmVLeEpPM2JqcDhWMWI5emo5ckIxUWxVZlh3PT0=|1458204058|c7f24fdf65d79c93e35bcb818c905cac4ee5e169\"; __utmt=1");
		params.put("Host", "www.zhihu.com");
		params.put("Referer", "https://www.zhihu.com/");
		params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0");
	}
	
	/** 
	 * @author Stefan
	 * @Title crawlPageByGet
	 * @Description GET方式请求页面
	 * @param url
	 * @param charset
	 * @return boolean
	 * @Date 2016-3-15 下午10:19:37
	 */
	public boolean crawlPageByGet(String url, String charset) {
		HttpGet method = this.createGetMethod(url);
		return this.crawlPage(method, charset, url);
	}
	
	/** 
	 * @author Stefan
	 * @Title crawlPageByPost
	 * @Description POST方式请求页面
	 * @param url
	 * @param charset
	 * @return boolean
	 * @Date 2016-3-15 下午10:21:31
	 */
	public boolean crawlPageByPost(String url, String charset) {
		HttpPost method = this.createPostMethod(url);
		return this.crawlPage(method, charset, url);
	}
	
	/** 
	 * @author Stefan
	 * @Title crawlPage
	 * @Description 执行request抓取网页，获取服务器返回的头信息和网页源代码
	 * @param request
	 * @param defaultCharset
	 * @param url
	 * @return boolean
	 * @Date 2016-3-15 下午9:58:34
	 */
	private boolean crawlPage(HttpUriRequest request, String defaultCharset, String url) {
		try {
//			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
//				
//				@Override
//				public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//					// TODO Auto-generated method stub
//					int statusCode = response.getStatusLine().getStatusCode();// 获取访问的状态码
//					System.out.println(statusCode);
//					if (statusCode == HttpStatus.SC_OK) {
//						// 5.处理HTTP响应内容
//						HttpEntity httpEntity = response.getEntity();// 获取响应消息主体
//						if (httpEntity != null) {
//							System.out.println("Content　Encoding　is " + httpEntity.getContentEncoding());
//							return EntityUtils.toString(httpEntity);// 获取消息字节数据
//						}
//					}
//					return null;
//				}
//			};
			
			CloseableHttpResponse response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode != HttpStatus.SC_OK) {
				request.abort();
				System.out.println(url + "==>请求状态码:" + statusCode);
				
			} else if (statusCode == HttpStatus.SC_OK) { 
				this.responseHeaders = response.getAllHeaders();
				this.requestHeaders = request.getAllHeaders();
				
				InputStream inputStream = response.getEntity().getContent();
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream, Const.CHARSET));
				StringBuffer stringBuffer = new StringBuffer();
				String lineString = "";
				while ((lineString = bufferReader.readLine()) != null) {
					stringBuffer.append(lineString);
					stringBuffer.append("\n");
				}
				this.pageSourceCode = stringBuffer.toString();
				inputStream.close();
				
				//检测流的编码方式
				InputStream in = new ByteArrayInputStream(this.pageSourceCode.getBytes(Const.CHARSET));
				String charset = CharsetUtil.getStreamChaset(in, defaultCharset);
				//如果编码方式不同，则进行转码操作
				if (!Const.CHARSET.toLowerCase().equals(charset.toLowerCase())) {
					this.pageSourceCode = new String(this.pageSourceCode.getBytes(Const.CHARSET), charset);
				}
				in.close();
				response.close();
				return true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/** 
	 * @author Stefan
	 * @Title createGetMethod
	 * @Description 创建GET请求
	 * @param url
	 * @return HttpGet
	 * @Date 2016-3-15 下午9:51:46
	 */
	private HttpGet createGetMethod(String url) {
		HttpGet method = new HttpGet(url);
		method.setConfig(requestConfig);
		if (params != null) {
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				method.setHeader(key, value);
			}
		}
		return method;
	}
	
	/** 
	 * @author Stefan
	 * @Title createPostMethod
	 * @Description 创建POST请求
	 * @param url
	 * @return HttpPost
	 * @Date 2016-3-15 下午9:55:30
	 */
	private HttpPost createPostMethod(String url) {
		HttpPost method = new HttpPost(url);
		method.setConfig(requestConfig);
		if (params != null) {
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				method.setHeader(key, value);
			}
		}
		return method;
	}
	
	/** 
	 * @author Stefan
	 * @Title getPageSourceCode
	 * @Description 获取页面源码
	 * @return String
	 * @Date 2016-3-15 下午10:40:57
	 */
	public String getPageSourceCode() {
		return this.pageSourceCode;
	}

	/** 
	 * @author Stefan
	 * @Title getRequestHeaders
	 * @Description 获取请求头信息
	 * @return Header[]
	 * @Date 2016-3-16 下午3:31:29
	 */
	public Header[] getRequestHeaders() {
		return requestHeaders;
	}

	
	/** 
	 * @author Stefan
	 * @Title getResponseHeaders
	 * @Description 获取响应头信息
	 * @return Header[]
	 * @Date 2016-3-16 下午3:31:46
	 */
	public Header[] getResponseHeaders() {
		return responseHeaders;
	}

}
