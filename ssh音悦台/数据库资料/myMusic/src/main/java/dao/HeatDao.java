package dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.imp.HeatDaoImp;
@Repository
public class HeatDao implements HeatDaoImp {
	@Autowired
	private SessionFactory sessionFactory;
}
