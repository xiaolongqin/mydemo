package dao;



import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.imp.AdministratorDaoImp;
import entity.Administrator;
@Repository
public class AdministratorDao implements AdministratorDaoImp {
	@Autowired
	private SessionFactory sessionFactory;
    //新增管理员
	public void savaAdmin(Administrator Admin) {
		Session session = sessionFactory.getCurrentSession();
		session.save(Admin);
	}
	//查询管理员
	public Administrator queryAdmin(String adminname, String password) {
		Session session = sessionFactory.getCurrentSession();
        String hql = "from Administrator where name= ? and password= ?";
        Query q =  (Query) session.createQuery(hql);
        q.setParameter(0, adminname);
        q.setParameter(1,password);
        Administrator admin =  (Administrator) q.uniqueResult();
		return admin;
	}
	
}
