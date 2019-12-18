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
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.paramount.entity.contact.Contact;
import net.paramount.framework.entity.AuditBase;

/**
 * Kurum bilgilerini tutan model sınıfımızdır.
 * @author "sinan.yumak"
 *
 */
@Entity
@Table(name="FOUNDATION")
public class Foundation extends AuditBase {

	private static final long serialVersionUID = 1L;
	
    @Column(name="CODE", length=20, unique=true, nullable=false)
    @Size(max=20, min=1)
    @NotNull
	private String code;

    @Column(name="NAME", length=50)
    @Size(max=50)
    private String name;

    @Column(name="IS_ACTIVE")
    private Boolean active = Boolean.TRUE;

    @Column(name="INFO")
	private String info;

	/**
	 * Kurumun bağlı olduğu cari bilgisini tutar.
	 */
	@ManyToOne
	@JoinColumn(name="CONTACT_ID", foreignKey = @ForeignKey(name = "FK_FOUNDATION_CONTACTID"))
	private Contact contact;
	
	/**
	 * Posun çalışabileceği max. taksit sayısını tutar.
	 */
    @Column(name="MAX_PERIOD")
	private Integer maxPeriod = 1;

	@Override
    public String toString() {
        return "com.ut.tekir.entities.Foundation[id=" + getId() + "]";
    }

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

	public Integer getMaxPeriod() {
		return maxPeriod;
	}

	public void setMaxPeriod(Integer maxPeriod) {
		this.maxPeriod = maxPeriod;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
