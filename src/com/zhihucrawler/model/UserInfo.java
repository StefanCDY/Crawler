package com.zhihucrawler.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: UserInfo.java
 * @Description: 用户信息类
 * @Date 2016-3-17 下午1:14:57
 */
@Entity
@Table(name = "userinfo",schema="zhihucrawler")
public class UserInfo implements Serializable {

	// Fields

	public static final long serialVersionUID = 1L;

	public String id;
	public String url;
	public String name;
	public String gender;
	public String headline;
	public String description;
	public String location;
	public String business;
	public String employment;
	public String position;
	public String education;
	public String major;
	public String headimage;
	public String weibo;
	public int agree;
	public int thanks;
	public int asks;
	public int answers;
	public int posts;
	public int collections;
	public int logs;
	public int followees;
    public int followers;
    public int topics;
    public int followed;
    public int pv;
    public long createtime;
    public long updatetime;
    public int state;

	// Constructors

	/** default constructor */
	public UserInfo() {
	}

	/** minimal constructor */
	public UserInfo(String url, String name, String gender) {
		this.url = url;
		this.name = name;
		this.gender = gender;
	}

	@Id
	@GeneratedValue(generator="id")
	@GenericGenerator(name="id", strategy="assigned")/*手动主键*/
	@Column(name = "id", length = 32, unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "url", nullable = false)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "gender", length = 2)
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "headline")
	public String getHeadline() {
		return this.headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "location")
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "business")
	public String getBusiness() {
		return this.business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	@Column(name = "employment")
	public String getEmployment() {
		return this.employment;
	}

	public void setEmployment(String employment) {
		this.employment = employment;
	}

	@Column(name = "position")
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "education")
	public String getEducation() {
		return this.education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	@Column(name = "major")
	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	@Column(name = "agree")
	public int getAgree() {
		return this.agree;
	}

	public void setAgree(int agree) {
		this.agree = agree;
	}

	@Column(name = "thanks")
	public int getThanks() {
		return this.thanks;
	}

	public void setThanks(int thanks) {
		this.thanks = thanks;
	}

	@Column(name = "asks")
	public int getAsks() {
		return this.asks;
	}

	public void setAsks(int asks) {
		this.asks = asks;
	}

	@Column(name = "answers")
	public int getAnswers() {
		return this.answers;
	}

	public void setAnswers(int answers) {
		this.answers = answers;
	}

	@Column(name = "posts")
	public int getPosts() {
		return this.posts;
	}

	public void setPosts(int posts) {
		this.posts = posts;
	}

	@Column(name = "collections")
	public int getCollections() {
		return this.collections;
	}

	public void setCollections(int collections) {
		this.collections = collections;
	}

	@Column(name = "logs")
	public int getLogs() {
		return this.logs;
	}

	public void setLogs(int logs) {
		this.logs = logs;
	}

	@Column(name = "followees")
	public int getFollowees() {
		return followees;
	}

	public void setFollowees(int followees) {
		this.followees = followees;
	}

	@Column(name = "followers")
	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	@Column(name = "topics")
	public int getTopics() {
		return topics;
	}

	public void setTopics(int topics) {
		this.topics = topics;
	}

	@Column(name = "followed")
	public int getFollowed() {
		return followed;
	}

	public void setFollowed(int followed) {
		this.followed = followed;
	}

	@Column(name = "pv")
	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	@Column(name = "headimage")
	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	@Column(name = "weibo")
	public String getWeibo() {
		return weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

	@Column(name = "createtime", nullable = false)
	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	
	@Column(name = "updatetime")
	public long getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "state", nullable = false)
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}