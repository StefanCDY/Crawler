package com.zhihucrawler.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import static org.hibernate.criterion.Example.create;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.utils.JsonUtil;

/**
 * A data access object (DAO) providing persistence and search support for
 * Userinfo entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.zhihucrawler.model.UserInfo
 * @author MyEclipse Persistence Tools
 */
public class UserInfoDao extends BaseHibernateDao {
	private static final Logger log = LoggerFactory.getLogger(UserInfoDao.class);

	public void save(UserInfo transientInstance) {
		log.debug("saving Userinfo instance");
		try {
			Session session = getSession();
			session.beginTransaction();
			
			session.save(transientInstance);
			
			session.getTransaction().commit();
			session.close();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			re.printStackTrace();
		}
	}

	public void delete(UserInfo persistentInstance) {
		log.debug("deleting Userinfo instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public void update(UserInfo persistentInstance) {
		log.debug("updating Userinfo instance");
		try {
			Session session = getSession();
			session.beginTransaction();
			
			session.update(persistentInstance);
			
			session.getTransaction().commit();
			session.close();
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	public UserInfo findById(long id) {
		log.debug("getting Userinfo instance with id: " + id);
		try {
			UserInfo instance = (UserInfo) getSession().get(
					"com.zhihucrawler.model.Userinfo", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/**
	 * @description 随机获取未访问的用户URL
	 * @param state
	 * @return
	 */
	public String getRandUserInfo(int state) {
		log.debug("finding Userinfo instance with property: state" + ", value: " + state);
		try {
			String queryString = "from UserInfo as model where model.state = ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, state);
			queryObject.setMaxResults(1);
			if (queryObject.list().size() > 0) {
				return (String) queryObject.list().get(0);
			}
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
		return null;
	}
	
	public List<UserInfo> findByProperty(String propertyName, Object value) {
		log.debug("finding Userinfo instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from UserInfo as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all Userinfo instances");
		try {
			String queryString = "from Userinfo";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {                   
			log.error("find all failed", re);
			throw re;
		}
	}

	public List<Object[]> getUserGenderRatio() {
		try {
			String hql = "select gender, count(1) as count from userinfo group by gender";
			Query query = getSession().createSQLQuery(hql);
			return query.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
}