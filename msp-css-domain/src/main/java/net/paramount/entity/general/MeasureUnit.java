/**
 * 
 */
package net.paramount.entity.general;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.paramount.framework.entity.BizObjectBase;

/**
 * @author bqduc
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "unit")
public class MeasureUnit extends BizObjectBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4182427455638019608L;

	@Column(name = "code", unique = true)
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "name_local")
	private String nameLocal;

	@Column(name = "comments")
	private String info;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private MeasureUnit parent;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameLocal() {
		return nameLocal;
	}

	public void setNameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
	}

	public MeasureUnit getParent() {
		return parent;
	}

	public void setParent(MeasureUnit parent) {
		this.parent = parent;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
