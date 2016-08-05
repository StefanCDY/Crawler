package com.zhihucrawler.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CrawlUrl")
public class CrawlUrl implements Serializable {

	private static final long serialVersionUID = 7268783704275835405L;
	
	private long id;
    private String url;
    private int statusCode;
    private String pageCode;
    private String requestHeader;
    private String responseHeader;
    private int depth;
    private int state;
    private long createTime;

	public CrawlUrl() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "url", nullable = false)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name = "statusCode")
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@Column(name = "pageCode")
	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	@Column(name = "requestHeader")
	public String getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(String requestHeader) {
		this.requestHeader = requestHeader;
	}

	@Column(name = "responseHeader")
	public String getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(String responseHeader) {
		this.responseHeader = responseHeader;
	}

	@Column(name = "depth")
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	@Column(name = "state", nullable = false)
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "createTime")
	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "CrawlUrl [id=" + id + ", url=" + url + ", statusCode="
				+ statusCode + ", pageCode=" + pageCode + ", requestHeaders="
				+ requestHeader + ", responseHeaders="
				+ responseHeader + ", depth=" + depth
				+ ", state=" + state + ", createTime=" + createTime + "]";
	}
	
}