package service.imp;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import dao.imp.CommentDaoImp;
import entity.Attention;
import entity.Comment;
import entity.Mv;
import entity.Singer;
import entity.User;

@Service
@Transactional
public interface FrontServiceImp {


	public void saveComment(Comment comment);
	public List<Comment> queryCommentByMv(Mv mv);
	public void saveAttention(Attention attention);
	public List<Attention> queryAttentionByUser(User user);
	public List<Attention> queryAttentionBySingerAndUser(Singer singer,User user);
	
}
