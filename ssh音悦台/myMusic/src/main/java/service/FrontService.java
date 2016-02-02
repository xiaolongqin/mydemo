package service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.imp.AttentionDaoImp;
import dao.imp.BuyDaoImp;
import dao.imp.CollectDaoImp;
import dao.imp.CommentDaoImp;
import dao.imp.HeatDaoImp;
import entity.Attention;
import entity.Comment;
import entity.Mv;
import entity.Singer;
import entity.User;
import service.imp.FrontServiceImp;
@Service
@Transactional
public class FrontService implements FrontServiceImp {

	@Resource
	private CommentDaoImp commentDao;
	@Resource
	private CollectDaoImp collectDao;
	@Resource
	private HeatDaoImp heatDao;
	@Resource
	private AttentionDaoImp attentionDao;
	@Resource
	private BuyDaoImp buyDao;
	public void saveComment(Comment comment) {
		commentDao.saveComment(comment);
		
	}
	public List<Comment> queryCommentByMv(Mv mv) {
		// TODO Auto-generated method stub
		return commentDao.queryCommentByMv(mv);
	}
	public void saveAttention(Attention attention) {
		attentionDao.saveAttention(attention);
		
	}
	public List<Attention> queryAttentionByUser(User user) {
		// TODO Auto-generated method stub
		return attentionDao.queryAttentionByUser(user);
	}
	public List<Attention> queryAttentionBySingerAndUser(Singer singer,
			User user) {
		// TODO Auto-generated method stub
		return attentionDao.queryAttentionBySingerAndUser(singer, user);
	}
	
}
