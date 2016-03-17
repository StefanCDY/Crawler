package com.zhihucrawler.dao;

import org.hibernate.Session;

import com.zhihucrawler.config.HibernateSessionFactory;


/**
 * Data access object (DAO) for domain model
 * @author MyEclipse Persistence Tools
 */
public class BaseHibernateDao implements IBaseHibernateDao {
	
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}
	
}