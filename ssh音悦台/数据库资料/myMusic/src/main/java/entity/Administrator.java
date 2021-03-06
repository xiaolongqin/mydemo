package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_Administrator")
public class Administrator {
	@Id
	@GeneratedValue
	private int id;
	private String nickname;
	private String name;
	private String password;
	private int myaccess;

	public int getMyaccess() {
		return myaccess;
	}

	public void setMyaccess(int myaccess) {
		this.myaccess = myaccess;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

}
