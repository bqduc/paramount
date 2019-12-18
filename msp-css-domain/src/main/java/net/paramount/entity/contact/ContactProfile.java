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
package net.paramount.entity.contact;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.model.GenderType;

/**
 * A contact.
 * 
 * @author Bui Quy Duc
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("6")
public class ContactProfile extends Contact {
	private static final long serialVersionUID = -5019226095410649159L;

	@Column(name="saluation", length=5)
	private String saluation;

	@Column(name="first_name", length=50)
	private String firstName;

	@Column(name="last_name", length=150)
	private String lastName;

	@Column(name="department", length=50)
	private String department;

  @Column(name="birthdate")
  private Date birthdate;

	@Column(name="birthplace", length=50)
	private String birthplace;

	@Column(name="gender")
  @Enumerated(EnumType.ORDINAL)
  private GenderType gender;

	@Column(name="activation_key", length=20)
	@JsonIgnore
	private String activationKey;

	@Column(name="reset_key", length=20)
	@JsonIgnore
	private String resetKey;

	@Column(name="reset_date")
	private ZonedDateTime resetDate;

	@Transient
	private Set<ContactAddress> contactAddresses = ListUtility.newHashSet();

	@Transient
	private Set<ContactTeam> contactTeams = ListUtility.newHashSet();

	/*@ManyToMany(cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
  })
  @JoinTable(name = "contact_profile_document",
      joinColumns = @JoinColumn(name = "contact_id"),
      inverseJoinColumns = @JoinColumn(name = "document_id")
  )
  private List<Document> documents = ListUtility.createArrayList();*/
	
	public String getSaluation() {
		return saluation;
	}

	public void setSaluation(String saluation) {
		this.saluation = saluation;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public String getResetKey() {
		return resetKey;
	}

	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}

	public ZonedDateTime getResetDate() {
		return resetDate;
	}

	public void setResetDate(ZonedDateTime resetDate) {
		this.resetDate = resetDate;
	}

	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}

	public Set<ContactAddress> getContactAddresses() {
		return contactAddresses;
	}

	public void setContactAddresses(List<ContactAddress> contactAddresses) {
		if (null==this.contactAddresses)
			this.contactAddresses = ListUtility.newHashSet();

		if (CommonUtility.isNotEmpty(contactAddresses)){
			this.contactAddresses.addAll(contactAddresses);
		}
	}

	public void setContactAddresses(Set<ContactAddress> contactAddresses) {
		this.contactAddresses = contactAddresses;
	}

	public Set<ContactTeam> getContactTeams() {
		return contactTeams;
	}

	public void setContactTeams(List<ContactTeam> contactTeams) {
		if (null==this.contactTeams)
			this.contactTeams = ListUtility.newHashSet();

		if (CommonUtility.isNotEmpty(contactTeams)){
			this.contactTeams.addAll(contactTeams);
		}
	}

	public void setContactTeams(Set<ContactTeam> contactTeams) {
		this.contactTeams = contactTeams;
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}
