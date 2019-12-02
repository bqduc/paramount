package net.paramount.css.entity.system;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.paramount.framework.entity.ObjectBase;
import net.paramount.global.GlobalConstants;

/**
 * An attachment.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "system_sequence")
public class SystemSequence extends ObjectBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6042526746778066937L;

	@Column(name = "code", length=GlobalConstants.SIZE_SERIAL)
	private String code;

  @Column(name = "name", length=200)
  private String name;

  @Column(name = "serial")
  private Long serial;

  @Column(name = "info")
	private String info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SystemSequence book = (SystemSequence) o;

		if (!Objects.equals(getId(), book.getId()))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Long getSerial() {
		return serial;
	}

	public void setSerial(Long serial) {
		this.serial = serial;
	}

}
