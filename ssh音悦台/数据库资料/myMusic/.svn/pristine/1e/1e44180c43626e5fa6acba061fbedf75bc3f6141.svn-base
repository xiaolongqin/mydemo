package dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.imp.RecommendDaoImp;
import entity.Recommend;
@Repository
public class RecommendDao implements RecommendDaoImp {
	@Autowired
	private SessionFactory sessionFactory;

	public boolean saveRecommend(Recommend recommend) {
		Session session = sessionFactory.getCurrentSession();
		session.save(recommend);
		return true;
	}

	public boolean updateRecommend(Recommend recommend) {
		Session session = sessionFactory.getCurrentSession();
		session.update(recommend);
		return true;
	}

	public List<Recommend> queryRecommendByPeriods(String periods, int page) {
		int pagesize =5;
		Session session = sessionFactory.getCurrentSession();
		String hql="from Recommend where periods like ?";
		Query query=session.createQuery(hql);
		query.setParameter(0, "%"+periods+"%");
		query.setFirstResult((page-1)*pagesize);
		query.setMaxResults(pagesize);
		List<Recommend> list=query.list();
		return list;
	}

	public List<Recommend> queryRecommendByDate() {
		int pagesize =5;
		Session session = sessionFactory.getCurrentSession();
		String hql="from Recommend order by upload_date desc";
		Query query=session.createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(pagesize);
		List<Recommend> list=query.list();
		return list;
	}

	public boolean deleteRecommend(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(session.get(Recommend.class, id));
		return false;
	}
}
