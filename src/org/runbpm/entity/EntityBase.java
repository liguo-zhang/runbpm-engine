package org.runbpm.entity;

import java.util.Date;

public class EntityBase {
	
	protected long id;

	protected String name;
	
	protected Date createDate;
	
	protected Date modifyDate;	
	
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	 public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (this.id == 0)
            return false;
        if (getClass() != o.getClass())
            return false;
        EntityBase e = (EntityBase) o;
        if (e.id == 0)
            return false;
        return id==e.id;
	}
}
