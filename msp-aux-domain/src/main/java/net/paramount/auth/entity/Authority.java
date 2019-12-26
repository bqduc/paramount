
package net.paramount.auth.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.paramount.framework.entity.ObjectBase;

/**
 * 
 * @author ducbq
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "aux_authority")
@NamedQueries({ @NamedQuery(name = "Role.findAll", query = "SELECT u FROM UserProfile u"),
		@NamedQuery(name = "Role.findById", query = "SELECT u FROM UserProfile u WHERE u.id = :id"),
		@NamedQuery(name = "Role.findByName", query = "SELECT u FROM UserProfile u WHERE u.name = :name"),
		@NamedQuery(name = "Role.findByActive", query = "SELECT u FROM UserProfile u WHERE u.active = :active") })
public class Authority extends ObjectBase {
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 64, message = "{LongString}")
	@Column(name = "name")
	private String name;

	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "display_name")
	private String displayName;

	@Basic(optional = false)
	@NotNull
	@Column(name = "active")
	private Boolean active;

	@Column(name = "info")
	private String info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (getId() != null ? getId().hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Authority)) {
			return false;
		}
		Authority other = (Authority) object;
		if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Role[ id=" + getId() + " ]";
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
