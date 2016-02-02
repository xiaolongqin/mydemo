package dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.imp.AttentionDaoImp;
import entity.Attention;
import entity.Comment;
import entity.Singer;
import entity.User;
@Repository
public class AttentionDao implements AttentionDaoImp {
	@Autowired
	private SessionFactory sessionFactory;


	public List<Attention> queryAttentionByUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		String hql="from Attention where user = ?";
		Query query=session.createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(10);
		query.setParameter(0, user);
		List<Attention> list=query.list();
		return list;
	}

	public void saveAttention(Attention attention) {
		Session session=sessionFactory.getCurrentSession();
		session.save(attention);
		
	}

	public List<Attention> queryAttentionBySingerAndUser(Singer singer,
			User user) {
		Session session = sessionFactory.getCurrentSession();
		String hql="from Attention where singer = ? and user = ?";
		Query query=session.createQuery(hql);
		query.setParameter(0, singer);
		query.setParameter(1, user);
		List<Attention> list=query.list();
		return list;
	}
}
