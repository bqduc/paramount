package net.paramount.framework.entity;

import java.beans.Transient;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class BizObjectBase extends ObjectBase implements BizEntity {
	private static final long serialVersionUID = -6323358535657100144L;

  @Basic(optional = false)
  @NotNull
	@Column(name="activated")
	private java.lang.Boolean activated = Boolean.FALSE;

	/**
	 * Set/Get the value related to the column: VISIBLE
	 */
	@Column(name = "visible")
	private java.lang.Boolean visible = false;

	public java.lang.Boolean isVisible() {
		return visible;
	}

	public void setVisible(java.lang.Boolean visible) {
		this.visible = visible;
	}

	public int compareTo(Object obj) {
		if (obj.hashCode() > hashCode())
			return 1;
		else if (obj.hashCode() < hashCode())
			return -1;
		else
			return 0;
	}

	public String toString() {
		return super.toString();
	}

	public Boolean isActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	@Transient
	public boolean isSelected(Long id){
		if (null != id){
			return this.getId().equals(id);
		}
		return false;
	}

	public static BizObjectBase buildObject(List<String> data) {
		return null;
	}
}