package net.paramount.entity.general;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.paramount.auth.entity.UserAccount;
import net.paramount.framework.entity.BizObjectBase;
import net.paramount.global.GlobalConstants;

/**
 * An office or business unit.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "business_unit")
public class BusinessUnit extends BizObjectBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1396860561985366652L;

	@Version
	@Column(name = "version")
	private Integer version;

	@Size(min = 5, max = GlobalConstants.SIZE_SERIAL)
	@Column(name = "code")
	private String code;

	@Size(min = 5, max = 200)
	@Column(name = "name_local")
	private String nameLocal;

	@Size(min = 5, max = 200)
	@Column(name = "name", nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "publish_user_id")//issue_user_id
	private UserAccount publishUserAccount;

	@ManyToOne
	@JoinColumn(name = "issued_user_id")
	private UserAccount issuedUser;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private BusinessUnit parent;

	@Column(name = "issued_date")
	private Date issuedDate;

	@Column(name = "published_date")
	private Date publishedDate;

	@ManyToOne
	@JoinColumn(name = "spoc_user_id")
	private UserAccount spocUser;

	@ManyToOne
	@JoinColumn(name = "manager_user_id")
	private UserAccount managerUser;

	@ManyToOne
	@JoinColumn(name = "level_id")
	private Item level;

	@Column(name = "support_level", length=GlobalConstants.SIZE_STRING_TINY)
	private String supportLevel;

	@Column(name = "support_category", length=GlobalConstants.SIZE_STRING_TINY)
	private String supportCategory;

	@Column(name = "management_model", length = GlobalConstants.SIZE_STRING_TINY)
	private String managementModel;

	@Column(name = "address", length = GlobalConstants.SIZE_ADDRESS_DEFAULT)
	private String address;

	@Column(name = "operating_model", length = GlobalConstants.SIZE_STRING_TINY)
	private String operatingModel;

	@Column(name = "activity_status")
	private String activityStatus;

	@Column(name = "organizational_model")
	private String organizationalModel;

	@Column(name = "license_no")
	private String licenseNo;

	@Column(name = "info", columnDefinition = "TEXT")
	private String info;

	@Override
	public String toString() {
		return "Business unit {" + "id:" + getId() + '}';
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNameLocal() {
		return nameLocal;
	}

	public void setNameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BusinessUnit getParent() {
		return parent;
	}

	public void setParent(BusinessUnit parent) {
		this.parent = parent;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public Item getLevel() {
		return level;
	}

	public void setLevel(Item level) {
		this.level = level;
	}

	public String getManagementModel() {
		return managementModel;
	}

	public void setManagementModel(String managementModel) {
		this.managementModel = managementModel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOperatingModel() {
		return operatingModel;
	}

	public void setOperatingModel(String operatingModel) {
		this.operatingModel = operatingModel;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getOrganizationalModel() {
		return organizationalModel;
	}

	public void setOrganizationalModel(String organizationalModel) {
		this.organizationalModel = organizationalModel;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSupportLevel() {
		return supportLevel;
	}

	public void setSupportLevel(String supportLevel) {
		this.supportLevel = supportLevel;
	}

	public String getSupportCategory() {
		return supportCategory;
	}

	public void setSupportCategory(String supportCategory) {
		this.supportCategory = supportCategory;
	}

	public UserAccount getIssuedUser() {
		return issuedUser;
	}

	public void setIssuedUser(UserAccount issuedUser) {
		this.issuedUser = issuedUser;
	}

	public UserAccount getSpocUser() {
		return spocUser;
	}

	public void setSpocUser(UserAccount spocUser) {
		this.spocUser = spocUser;
	}

	public UserAccount getManagerUser() {
		return managerUser;
	}

	public void setManagerUser(UserAccount managerUser) {
		this.managerUser = managerUser;
	}

	public UserAccount getPublishUserAccount() {
		return publishUserAccount;
	}

	public void setPublishUserAccount(UserAccount publishUserAccount) {
		this.publishUserAccount = publishUserAccount;
	}
}
