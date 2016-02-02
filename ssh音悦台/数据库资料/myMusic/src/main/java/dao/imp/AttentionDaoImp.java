package dao.imp;

import java.util.List;

import entity.Attention;
import entity.Singer;
import entity.User;

public interface AttentionDaoImp {

	public void saveAttention(Attention attention);
	public List<Attention> queryAttentionByUser(User user);
	public List<Attention> queryAttentionBySingerAndUser(Singer singer,User user);
}
