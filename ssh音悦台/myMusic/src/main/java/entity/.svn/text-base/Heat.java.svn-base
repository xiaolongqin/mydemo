package entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "m_heat")
public class Heat {
	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	private User user;
	@ManyToOne
	private Mv mv;
	private Date heat_date=new Date();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getHeat_date() {
		return heat_date.toString();
	}

	public void setHeat_date(Date heat_date) {
		this.heat_date = heat_date;
	}

}
