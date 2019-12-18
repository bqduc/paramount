/*
 * Copyleft 2007-2011 Ozgur Yazilim A.S.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * www.tekir.com.tr
 * www.ozguryazilim.com.tr
 *
 */

package net.paramount.entity.trade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.paramount.framework.entity.ObjectBase;

/**
 * 
 * @author ducbq
 *
 */

@Entity
@Table(name="EXPENSE_TYPE")
public class ExpenseType extends ObjectBase {

	private static final long serialVersionUID = 1L;
	
    @Column(name="CODE", length=20, unique=true, nullable=false)
	private String code;

    @Column(name="NAME", length=50)
    private String name;
    
    @Column(name="INFO")
	private String info;
    
    @Column(name="ISACTIVE")
	private Boolean active = Boolean.TRUE;

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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "com.ut.tekir.entities.ExpenseType[id=" + getId() + "]";
	}
	
	/**
	 * ExpenseType popuptan gelen degeri kullanan converter icin gerekli
	 * @see ExpenseTypeCaptionConverter
	 */
	@Transient
	public String getCaption(){
	    return "[" + getCode() + "] " + getName();
	}

}
