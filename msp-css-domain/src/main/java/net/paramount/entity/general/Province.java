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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import net.paramount.framework.entity.ObjectBase;

/**
 * Entity class Province
 * 
 * @author dumlupinar
 */
@Entity
@Table(name="PROVINCE")
public class Province extends ObjectBase {

    private static final long serialVersionUID = 1L;

    @Column(name="PLATE", length=6, nullable=false )
	@Size(max=6, min=1)
	private String plate;
    
    @Column(name="NAME", length=40)
    @Size(max=40)
	private String name;
	
    @Column(name="WEIGHT")
    private Integer weight;
	
    @ManyToOne
    @JoinColumn(name="CITY_ID")
    private City city;
	
    @Column(name="SYSTEM")
    private Boolean system;
	
    @Column(name="ISACTIVE")
	private Boolean active = Boolean.TRUE;

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Boolean getSystem() {
		return system;
	}

	public void setSystem(Boolean system) {
		this.system = system;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

    @Override
    public String toString() {
        return "com.ut.tekir.entities.Province[id=" + getId() + "]";
    }

}
