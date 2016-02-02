package service;


import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import service.imp.MvServiceImp;
import dao.imp.MvDaoImp;
import dao.imp.SingerDaoImp;
import entity.Mv;
import entity.Singer;
@Service
@Transactional
//@Scope("prototype")
public class MvService implements MvServiceImp {

	@Resource
	private MvDaoImp mvDao;
	@Resource
	private SingerDaoImp singerDao;

	// 保存MV
	public void save(Mv mv) {
		mvDao.save(mv);
	}
	//修改MV
	public void update(Mv mv) {
		mvDao.update(mv);
		
	}
	//删除MV
	public void delMv(int id) {
		mvDao.delMv(id);
	}
	
	//查询所有歌手
	public List<Singer> getAllSinger(){
		return singerDao.allSinger();
	}
	public List<Mv> queryMvByAreaAndSinger(int page, String area,String Name) {
		return mvDao.queryMvByAreaAndSinger(page, area, Name);
	}
	
	
	public List<Mv> queryMvByName(String name) {
		return mvDao.queryMvByName(name);
	}
	
	public List<Mv> queryMvByName(String name, int page) {
		return mvDao.queryMvByName(name, page);
	}
	
	public List<Singer> querySingerByName(String name) {
		return singerDao.querySingerByName(name);
	}
	public List<Mv> queryMv() {
		return mvDao.queryMv();
	}
	public Mv queryMvById(int id) {
		return mvDao.queryMvById(id);
	}
	public List<Mv> queryMvBySinger(Singer singer) {
		return mvDao.queryMvBySinger(singer);
	}
	public List<Mv> queryMvByAreaAndDate(int page, String area) {
		return mvDao.queryMvByAreaAndDate(page, area);
	}
	public List<Mv> queryMvByAreaAndClick(int page, String area) {
		return mvDao.queryMvByAreaAndClick(page, area);
	}
	public List<Mv> queryMvByRandom() {
		return mvDao.queryMvByRandom();
	}
	
	
	
	
	
}
