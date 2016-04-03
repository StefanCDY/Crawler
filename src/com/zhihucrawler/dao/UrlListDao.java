package com.zhihucrawler.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhihucrawler.model.UrlList;

/**
 * A data access object (DAO) providing persistence and search support for
 * Urllist entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.zhihucrawler.model.UrlList
 * @author MyEclipse Persistence Tools
 */
public class UrlListDao {
	private static final Logger log = LoggerFactory.getLogger(UrlListDao.class);

	public void save(UrlList transientInstance) {
		log.debug("saving Urllist instance");
		try {
//			Transaction transaction= getSession().beginTransaction();
//			getSession().save(transientInstance);
//			transaction.commit();
//			getSession().close();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(UrlList persistentInstance) {
		log.debug("deleting Urllist instance");
		try {
//			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UrlList findById(Long id) {
		log.debug("getting Urllist instance with id: " + id);
		try {
//			UrlList instance = (UrlList) getSession().get("com.zhihucrawler.model.Urllist", id);
//			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
		return null;
	}

	public List<UrlList> findByExample(UrlList instance) {
		log.debug("finding Urllist instance by example");
		try {
//			List<UrlList> results = (List<UrlList>) getSession()
//					.createCriteria("com.zhihucrawler.model.Urllist")
//					.add(create(instance)).list();
//			log.debug("find by example successful, result size: "
//					+ results.size());
//			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
		return null;
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Urllist instance with property: " + propertyName
				+ ", value: " + value);
		try {
//			String queryString = "from Urllist as model where model."
//					+ propertyName + "= ?";
//			Query queryObject = getSession().createQuery(queryString);
//			queryObject.setParameter(0, value);
//			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
		return null;
	}

	public List findAll() {
		log.debug("finding all Urllist instances");
		try {
//			String queryString = "from Urllist";
//			Query queryObject = getSession().createQuery(queryString);
//			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
		return null;
	}

	public UrlList merge(UrlList detachedInstance) {
		log.debug("merging Urllist instance");
		try {
//			UrlList result = (UrlList) getSession().merge(detachedInstance);
			log.debug("merge successful");
//			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
		return null;
	}

	public void attachDirty(UrlList instance) {
		log.debug("attaching dirty Urllist instance");
		try {
//			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UrlList instance) {
		log.debug("attaching clean Urllist instance");
		try {
//			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}