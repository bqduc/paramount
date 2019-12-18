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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.paramount.framework.entity.ObjectBase;

/**
 * @author sinan.yumak
 *
 */
@Entity
@Table(name = "PRODUCT_DETAIL")
public class ProductDetail extends ObjectBase {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private Product owner;

	// satır üzerindeki masraf veya indirim bilgisini tutar.
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;

	@Column(name = "INFO")
	private String info;

	@Override
	public String toString() {
		return "com.ut.tekir.entities.ProductDetail[" + getId() + "]";
	}

	public Product getOwner() {
		return owner;
	}

	public void setOwner(Product owner) {
		this.owner = owner;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
