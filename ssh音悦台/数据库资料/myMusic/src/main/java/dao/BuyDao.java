package dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.imp.BuyDaoImp;
@Repository
public class BuyDao implements BuyDaoImp {
	@Autowired
	private SessionFactory sessionFactory;
}
