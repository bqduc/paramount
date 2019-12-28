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

import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author haky
 */
@Embeddable
public class Quantity implements java.io.Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 6197188777904513286L;

	@Column(name = "unit_id")
	private Long unitId;

	@Column(name = "quantity", precision = 5, scale = 2)
	private Double value = 0d;

	public Quantity() {
		this.unitId = null;
		this.value = 0d;
	}

	public Quantity(Quantity quantity) {
		this.value = new Double(quantity.getValue());
		this.unitId = quantity.getUnitId();
	}

	public Quantity(double value, Long unitId) {
		this.value = value;
		this.unitId = unitId;
	}

	public void moveFieldsOf(Quantity anotherQuantity) {
		if (anotherQuantity != null) {
			this.unitId = anotherQuantity.getUnitId();
			this.value = anotherQuantity.getValue();
		}
	}

	public Double getValue() {
		return value;
	}

	public BigDecimal asBigDecimal() {
		return BigDecimal.valueOf(value);
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public String toString() {

		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);

		return f.format(getValue()) + "#" + getUnitId();
	}

	public String toStringInNarrowFormat() {
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);

		String result = f.format(getValue()) + "#" + getUnitId();
		if (result.length() > 7) {
			result = result.substring(0, 7);
		}
		return result;
	}

	public boolean isZero() {
		return this.value == 0d;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
}
