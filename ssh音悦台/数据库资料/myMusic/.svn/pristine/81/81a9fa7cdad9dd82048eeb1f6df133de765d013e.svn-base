package entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="m_Attention")
public class Attention {
	@Id
	@GeneratedValue
  private int id;
  private Date attention_date=new Date();
  @ManyToOne
  private User user;
  @ManyToOne
  private Singer singer;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getAttention_date() {
	return attention_date.toString();
}
public void setAttention_date(Date attention_date) {
	this.attention_date = attention_date;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public Singer getSinger() {
	return singer;
}
public void setSinger(Singer singer) {
	this.singer = singer;
}
  
}
