package dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.imp.CommentDaoImp;
import entity.Comment;
import entity.Mv;
import entity.Recommend;
@Repository
public class CommentDao implements CommentDaoImp{
	@Autowired
	private SessionFactory sessionFactory;

	public void saveComment(Comment comment) {
		Session session=sessionFactory.getCurrentSession();
		session.save(comment);
	}

	public List<Comment> queryCommentByMv(Mv mv) {
		Session session = sessionFactory.getCurrentSession();
		String hql="from Comment where mv = ? order by comment_date desc";
		Query query=session.createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(20);
		query.setParameter(0, mv);
		List<Comment> list=query.list();
		return list;
	}
}
