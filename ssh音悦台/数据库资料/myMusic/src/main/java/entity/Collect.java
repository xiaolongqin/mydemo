package entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "m_Collect")
public class Collect {
	@Id
	@GeneratedValue
	private int id;
	private Date collect_date=new Date();
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

	public String getCollect_date() {
		return collect_date.toString();
	}

	public void setCollect_date(Date collect_date) {
		this.collect_date = collect_date;
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
