package dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.imp.CollectDaoImp;
@Repository
public class CollectDao implements CollectDaoImp {
	@Autowired
	private SessionFactory sessionFactory;
}
