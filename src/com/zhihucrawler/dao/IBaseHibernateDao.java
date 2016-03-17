package com.zhihucrawler.dao;

import org.hibernate.Session;


/**
 * Data access interface for domain model
 * @author MyEclipse Persistence Tools
 */
public interface IBaseHibernateDao {
	public Session getSession();
}