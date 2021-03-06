package entities;

import java.io.Serializable;

public class Unit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private long classType;

	
	public Unit(long id, String name, long classType) {
		super();
		this.id = id;
		this.name = name;
		this.classType = classType;
	}
	public Unit() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public long getClassType() {
		return classType;
	}
	public void setClassType(long classType) {
		this.classType = classType;
	}
	@Override
	public String toString() {
		return "Unit [id=" + id + ", name=" + name + ", classType=" + classType + "]";
	}
	
	
}
