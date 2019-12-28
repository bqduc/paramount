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
package net.paramount.entity.general;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.paramount.framework.entity.BizObjectBase;
import net.paramount.global.GlobalConstants;

/**
 * A user.
 * 
 * @author bqduc
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "currency")
@EqualsAndHashCode(callSuper = true)
public class Currency extends BizObjectBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2260919441709332618L;

	@NotNull
	@Size(min = 3, max=GlobalConstants.SIZE_CURRENCY_CODE)
	@Column(name="alphabetic_code", unique = true)
	private String alphabeticCode;

	@Size(max = GlobalConstants.SIZE_CURRENCY_CODE)
	@Column(name="numeric_code")
	private String numericCode;

	@Column(name="decimal_digits")
	private Byte decimalDigits; //The number of digits after the decimal separator. 

	@Lob
	@Column(name = "info", columnDefinition = "TEXT")
	@Type(type = "org.hibernate.type.TextType")
	private String info;
}
