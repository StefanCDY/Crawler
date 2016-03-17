package com.zhihucrawler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Urllist entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "urllist", catalog = "zhihucrawler")
public class UrlList implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	
	private long id;
	private String url;
	private String info;
	private Integer frequency;
	private int state;

	// Constructors

	/** default constructor */
	public UrlList() {
	}

	/** minimal constructor */
	public UrlList(String url, Integer frequency, int state) {
		this.url = url;
		this.frequency = frequency;
		this.state = state;
	}

	/** full constructor */
	public UrlList(String url, String info, Integer frequency, int state) {
		this.url = url;
		this.info = info;
		this.frequency = frequency;
		this.state = state;
	}

	// Property accessors
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

	@Column(name = "info")
	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Column(name = "frequency")
	public Integer getFrequency() {
		return this.frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	@Column(name = "state")
	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

}