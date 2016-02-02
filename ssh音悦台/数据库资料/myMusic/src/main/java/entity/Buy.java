package entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="m_Buy")
public class Buy {
	@Id
	@GeneratedValue
    private int id;
    private Date buy_date;
    @ManyToOne
    private User user;
    @ManyToOne
    private Mv mv;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getBuy_date() {
		return buy_date;
	}
	public void setBuy_date(Date buy_date) {
		this.buy_date = buy_date;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Mv getMv() {
		return mv;
	}
	public void setMv(Mv mv) {
		this.mv = mv;
	}
    
}
