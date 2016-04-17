package com.zhihucrawler.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "url")
public class CrawlUrl implements Serializable {

	private static final long serialVersionUID = 7268783704275835405L;
	
	private long id;
	private String url;
	private int depth;
	private int state;
	private long addTime;
	private long updateTime;

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

	@Column(name = "depth", nullable = false)
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

	@Column(name = "addTime")
	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	
	@Column(name = "updateTime")
	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "Url [id=" + id + ", url=" + url + ", depth=" + depth
				+ ", state=" + state + ", addTime=" + addTime + ", updateTime="
				+ updateTime + "]";
	}

}