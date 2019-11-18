/*
* Copyright 2017, Bui Quy Duc
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package net.paramount.crs.entity.base;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.paramount.auth.entity.AuthenticateAccount;
import net.paramount.css.entity.general.Item;
import net.paramount.css.model.ContactType;
import net.paramount.embeddable.Phone;
import net.paramount.framework.entity.BizObjectBase;
import net.paramount.global.GlobalConstants;

/**
 * A contact.
 * 
 * @author Bui Quy Duc
 */
/*@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_class")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "contact_type", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue(value = "0")
public class ContactClass extends BizObjectBase {
	*//**
	 * 
	 *//*
	private static final long serialVersionUID = -913646501472973260L;

	@Column(name="code", length=GlobalConstants.SIZE_SERIAL, unique=true)
	private String code;

	@Column(name="account_name", length=150)
	private String accountName;

	@Column(name="title", length=50)
	private String title;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name="office", column=@Column(name="phone_office")),
    @AttributeOverride(name="mobile", column=@Column(name="phone_mobile")),
    @AttributeOverride(name="home", column=@Column(name="phone_home")),
    @AttributeOverride(name="others", column=@Column(name="phone_others")),
  })
  private Phone phone;

	@Column(name="portal_name", length=50)
	private String portalName;

	@JsonIgnore
	@Column(name="portal_secret_key", length=50)
	private String portalSecretKey;

	@Column(name = "portal_active")
	private java.lang.Boolean portalActive = false;

	@Column(name="email", length=120)
	private String email;

	@Column(name="email_others", length=120)
	private String emailOthers;

	@Column(name = "email_opt_out")
	private java.lang.Boolean emailOptOut = false;

	@Column(name = "email_invalid")
	private java.lang.Boolean emailInvalid = false;

	@ManyToOne
	@JoinColumn(name = "sms_opt_in_id")
	private Item smsOptIn;

	@ManyToOne
	@JoinColumn(name = "lead_source_id")
	private Item leadSource;

	@Column(name="fax", length=20)
	private String fax;

	@Column(name="contact_type", insertable=false, updatable=false)
  @Enumerated(EnumType.ORDINAL)
  private ContactType contactType;

	@Lob
	@Column(name = "description", columnDefinition = "TEXT")
	@Type(type = "org.hibernate.type.TextType")
	private String description;

	@ManyToOne
	@JoinColumn(name = "reports_contact_id")
	private ContactClass reportsTo;

	@ManyToOne
	@JoinColumn(name = "assistant_contact_id")
	private ContactClass assistant;

	@Column(name = "sync_contact")
	private java.lang.Boolean syncContact = false;

	@Column(name = "do_not_call")
	private java.lang.Boolean doNotCall = false;

	@ManyToOne
	@JoinColumn(name = "owner_user_id")
	private AuthAccount ownerUserAccount;

  @Column(name="issue_date")
  private Date issueDate;
	
	@ManyToOne
	@JoinColumn(name="issue_user_id")
  private AuthAccount issueUserAccount;

  public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailOthers() {
		return emailOthers;
	}

	public void setEmailOthers(String emailOthers) {
		this.emailOthers = emailOthers;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public java.lang.Boolean getEmailOptOut() {
		return emailOptOut;
	}

	public void setEmailOptOut(java.lang.Boolean emailOptOut) {
		this.emailOptOut = emailOptOut;
	}

	public java.lang.Boolean getEmailInvalid() {
		return emailInvalid;
	}

	public void setEmailInvalid(java.lang.Boolean emailInvalid) {
		this.emailInvalid = emailInvalid;
	}

	public Item getSmsOptIn() {
		return smsOptIn;
	}

	public void setSmsOptIn(Item smsOptIn) {
		this.smsOptIn = smsOptIn;
	}

	public Item getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(Item leadSource) {
		this.leadSource = leadSource;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public ContactClass getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(ContactClass reportsTo) {
		this.reportsTo = reportsTo;
	}

	public ContactClass getAssistant() {
		return assistant;
	}

	public void setAssistant(ContactClass assistant) {
		this.assistant = assistant;
	}

	public java.lang.Boolean getSyncContact() {
		return syncContact;
	}

	public void setSyncContact(java.lang.Boolean syncContact) {
		this.syncContact = syncContact;
	}

	public java.lang.Boolean getDoNotCall() {
		return doNotCall;
	}

	public void setDoNotCall(java.lang.Boolean doNotCall) {
		this.doNotCall = doNotCall;
	}

	public AuthAccount getOwnerUserAccount() {
		return ownerUserAccount;
	}

	public void setOwnerUserAccount(AuthAccount ownerUserAccount) {
		this.ownerUserAccount = ownerUserAccount;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPortalName() {
		return portalName;
	}

	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}

	public String getPortalSecretKey() {
		return portalSecretKey;
	}

	public void setPortalSecretKey(String portalSecretKey) {
		this.portalSecretKey = portalSecretKey;
	}

	public java.lang.Boolean getPortalActive() {
		return portalActive;
	}

	public void setPortalActive(java.lang.Boolean portalActive) {
		this.portalActive = portalActive;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public AuthAccount getIssueUserAccount() {
		return issueUserAccount;
	}

	public void setIssueUserAccount(AuthAccount issueUserAccount) {
		this.issueUserAccount = issueUserAccount;
	}
}*/
