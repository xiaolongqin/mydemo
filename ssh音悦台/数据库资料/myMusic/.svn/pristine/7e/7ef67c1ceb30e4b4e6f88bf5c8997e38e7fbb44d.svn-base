package service.imp;
import java.util.List;

import entity.Mv;
import entity.Singer;
public interface MvServiceImp {
   /**
    * 增加mv
    * @param mv
    */
	public void save(Mv mv);
	/**
	 * 查询所有歌手
	 * @return
	 */
	public List<Singer> getAllSinger();
	//按姓名查询Mv
	public List<Mv> queryMvByName(String name);
	//分页显示按姓名查询MV
	public List<Mv> queryMvByName(String name, int page);
	//修改MV
	public void update(Mv mv);
	//删除MV
	public void delMv(int id);
	//按歌手和地区查询MV
	public List<Mv> queryMvByAreaAndSinger(int page, String area,String  Name);
	public List<Mv> queryMv();
	public List<Singer> querySingerByName(String name);
	public Mv queryMvById(int id);
	public List<Mv> queryMvBySinger(Singer singer);
	public List<Mv> queryMvByAreaAndDate(int page,String area);
	public List<Mv> queryMvByAreaAndClick(int page, String area);
	public List<Mv> queryMvByRandom();
	
	
	

}
