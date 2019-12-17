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
package net.paramount.auth.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.paramount.auth.model.CryptoAlgorithm;
import net.paramount.common.CommonUtility;
import net.paramount.common.DateTimeUtility;
import net.paramount.entity.Attachment;
import net.paramount.framework.entity.SsoEntityBase;
import net.paramount.framework.entity.auth.AuthAccount;
import net.paramount.model.DateTimePatterns;

/**
 * A user.
 * 
 * @author bqduc
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "aux_user_account")
@ToString(exclude = { "authorities" })
@EqualsAndHashCode(callSuper = true)
public class UserAccount extends SsoEntityBase implements AuthAccount {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3124167777154600539L;

	@Size(max = 50)
	@Column(name = "first_name")
	private String firstName;

	@Size(max = 50)
	@Column(name = "last_name")
	private String lastName;
	
	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	//@formatter:off
	@JoinTable(
			name = "sa_granted_user_authority", 
			inverseJoinColumns = {@JoinColumn(name = "authority_id")},
			joinColumns = {@JoinColumn(name = "account_id")}
	)
	//@formatter:on
	private Set<Authority> authorities;

	@ManyToOne(targetEntity=Attachment.class, fetch=FetchType.EAGER)
	@JoinColumn(name = "attachment_id")
	private Attachment attachment;

	@Transient
	private UserDetails userDetails;
	
	@Transient
	@Builder.Default
	private Map<String, Authority> authorityMap = new HashMap<>();

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

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	@Transient
	public String getDisplayName() {
		return this.firstName + " " + this.lastName;
	}

	public Map<String, Authority> getAuthorityMap() {
		return authorityMap;
	}

	public void setAuthorityMap(Map<String, Authority> authorityMap) {
		this.authorityMap = authorityMap;
	}

	@Transient
	public String getDisplayIssueDate() {
		return DateTimeUtility.dateToString(this.getIssueDate(), DateTimePatterns.ddMMyyyy_SLASH.getDateTimePattern());
	}

	public static UserAccount createInstance(String email, String firstName, String lastName, String login, String password, Set<Authority> authorities) {
		UserAccount instance = UserAccount.builder()
		.firstName(firstName)
		.lastName(lastName)
		.authorities(authorities)
		.build();

		instance.setEncryptAlgorithm(CryptoAlgorithm.BASIC.getAlgorithm());
		instance.setIssueDate(DateTimeUtility.getSystemDateTime());
		instance.setEmail(email);
		instance.setSsoId(login);
		instance.setPassword(password);
		return instance;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Iterator<Authority> itr = null;
		Authority authority = null;
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		if (CommonUtility.isNotEmpty(this.authorities)) {
			itr = this.authorities.iterator();
			while (itr.hasNext()) {
				authority = itr.next();
				grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
			}
		}
		return grantedAuthorities;
	}
}