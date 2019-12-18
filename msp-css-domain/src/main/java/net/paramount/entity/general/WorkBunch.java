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

package net.paramount.entity.general;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import net.paramount.entity.contact.Contact;
import net.paramount.framework.entity.ObjectBase;

/**
 * İş takibi bilgilerini tutan model sınıfımızdır.
 * 
 * @author sinan.yumak
 *
 */
@Entity
@Table(name="WORK_BUNCH")
public class WorkBunch extends ObjectBase {

	private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name="CONTACT_ID", foreignKey = @ForeignKey(name = "FK_WORKBUNCH_CONTACTID"))
    private Contact contact;
    
    @Column(name="INFO")
    private String info;

    @Column(name="CODE",length=20,unique=true,nullable=false)
    @Size(max=20, min=1)
    private String code;

    @Column(name="NAME",length=40)
    private String name;

	@Enumerated(EnumType.ORDINAL)
    @Column(name="WORKBUNCH_STATUS")
    private WorkBunchStatus workBunchStatus;
    
	@Temporal(TemporalType.DATE)
    @Column(name="BEGIN_DATE")
    private Date beginDate;

	@Temporal(TemporalType.DATE)
    @Column(name="END_DATE")
    private Date endDate;
    
    @Column(name="ISACTIVE")
    private Boolean active = Boolean.TRUE;

	public WorkBunch() {
		super();
	}

/*
	public WorkBunch(WorkBunch parent) {
    	this.parent = parent;
	}
	public void addChild() {
		getChildList().add(new WorkBunch(this));
	}
 */

    @Override
    public String toString() {
        return "com.ut.tekir.entities.WorkBunch[id=" + getId() + "]";
    }

    public String getCaption(){
        return "[" + getCode() + "] " + getName();
    }
    
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

    public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public WorkBunchStatus getWorkBunchStatus() {
		return workBunchStatus;
	}

	public void setWorkBunchStatus(WorkBunchStatus workBunchStatus) {
		this.workBunchStatus = workBunchStatus;
	}

}
