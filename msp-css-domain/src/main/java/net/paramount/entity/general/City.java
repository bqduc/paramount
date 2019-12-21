package net.paramount.entity.general;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.paramount.framework.entity.BizObjectBase;

/**
 * A city.
 */
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "city")
@EqualsAndHashCode(callSuper=false)
public class City extends BizObjectBase{
	/**
	 * 
	 */
	private static final long serialVersionUID = -292350370506500701L;

	@Column(name = "name", nullable = false, unique=true, length=150)
	private String name;

	@Lob
	@Column(name = "info", columnDefinition="TEXT")
	private String info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
