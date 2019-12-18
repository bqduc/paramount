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

package net.paramount.entity.stock;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.paramount.entity.emx.EnterpriseProduct;
import net.paramount.framework.entity.ObjectBase;

/**
 * Entity class ProductCategory
 * 
 * @author haky
 */
@Entity
@Table(name = "PRODUCT_CATEGORY")
@NamedQueries({ @NamedQuery(name = "ProductCategory.findAll", query = "SELECT p FROM ProductCategory p"),
		@NamedQuery(name = "ProductCategory.findById", query = "SELECT p FROM ProductCategory p WHERE p.id = :id"),
		// @NamedQuery(name = "ProductCategory.findByName", query = "SELECT p FROM ProductCategory p WHERE p.name = :name"),
		@NamedQuery(name = "ProductCategory.findByActive", query = "SELECT p FROM ProductCategory p WHERE p.active = :active") })
public class ProductCategory extends ObjectBase {

	private static final long serialVersionUID = 1L;

	@Column(name = "CODE", nullable = false, unique = true, length = 20)
	private String code;

	@Column(name = "INFO")
	private String info;

	@Column(name = "ISACTIVE")
	private Boolean active = Boolean.TRUE;

	@Column(name = "SYSTEM")
	private Boolean system;

	@Column(name = "WEIGHT")
	private Integer weight;

	///////////////////////////////////////////////
	@OneToMany(mappedBy = "category")
	// @LazyCollection(LazyCollectionOption.FALSE)
	private List<EnterpriseProduct> products;

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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getSystem() {
		return system;
	}

	public void setSystem(Boolean system) {
		this.system = system;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "com.ut.tekir.entities.ProductCategory[id=" + getId() + "]";
	}

}
