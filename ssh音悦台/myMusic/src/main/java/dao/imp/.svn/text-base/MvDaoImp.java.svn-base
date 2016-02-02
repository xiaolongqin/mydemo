package dao.imp;

import java.util.List;

import entity.Mv;
import entity.Singer;

public interface MvDaoImp {
	/**
	 * 添加MV
	 * @param mv
	 */
	public void save(Mv mv);
	//修改MV
	public void update(Mv mv);
	public void delMv(int id);
	public List<Mv> queryMvByName(String name);
	public Mv queryMvById(int id);
	public List<Mv> queryMv();
	public List<Mv> queryMvByAreaAndDate(int page,String area);
	public List<Mv> queryMvByAreaAndClick(int page, String area);
	public List<Mv> queryMvBySinger(Singer singer);
	public List<Mv> queryMvByRandom();
	//分页显示按歌手查询Mv
	public List<Mv> queryMvByName(String name, int page);
	//按歌手和地区查询MV
	public List<Mv> queryMvByAreaAndSinger(int page, String area,String  Name);
}
