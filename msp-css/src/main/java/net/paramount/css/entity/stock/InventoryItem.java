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
package net.paramount.css.entity.stock;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import net.paramount.common.ListUtility;
import net.paramount.css.entity.general.Catalogue;
import net.paramount.css.entity.general.Item;
import net.paramount.framework.entity.BizObjectBase;
import net.paramount.global.GlobalConstants;

/**
 * A product.
 * 
 * @author bqduc
 */
@Builder
@Entity
@Table(name = "inventory_item")
@EqualsAndHashCode(callSuper = true)
public class InventoryItem extends BizObjectBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8397420404962585057L;

	@NotNull
	@Size(min = 3, max=GlobalConstants.SIZE_SERIAL)
	@Column(unique = true)
	private String code;

	@Column(name = "barcode", length=GlobalConstants.SIZE_BARCODE)
	private String barcode;

	@Size(max = GlobalConstants.SIZE_NAME)
	@Column(name="name")
	private String name;

	@Size(max = GlobalConstants.SIZE_NAME)
	@Column(name="translated_name")
	private String translatedName;

	@Column(name="composition", length=150)
	private String composition; //Content also

	@Column(name="active_principle", length=150)
	private String activePrinciple;	

	@Column(name="processing_type", length=50)
	private String processingType;

	@Column(name="packaging", length=50)
	private String packaging;

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "master_photo", columnDefinition="TEXT")
	private String masterPhoto;

	@Column(name = "description", columnDefinition="TEXT")
	private String description;

	@Column(name = "manufacturing_date")
	private Date manufacturingDate;

	@Column(name = "available_date")
	private Date availableDate;

	@Column(name = "master_vendor_id")
	private Long masterVendorId;

	@Column(name="vendor_part_number", length=GlobalConstants.SIZE_CODE)
	private String vendorPartNumber;

	@Column(name = "manufacturer_id")
	private Long manufacturerId;

	@Column(name="manufacturer_part_number", length=GlobalConstants.SIZE_CODE)
	private String manufacturerPartNumber;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private InventoryItem parent;

	@Column(name="minimum_options")
	private Integer minimumOptions;

	@Column(name="maximum_options")
	private Integer maximumOptions;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Catalogue masterCategory;

	@Builder.Default
	@JsonIgnore
	@OneToMany(mappedBy = "inventoryItem", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<InventoryItemCatalog> productCatalogues = ListUtility.createArrayList();

	@Transient
	@Builder.Default
	private List<Catalogue> catalogues = ListUtility.createArrayList();

	@Transient
	@Builder.Default
	private List<InventoryItemProfile> profiles = ListUtility.createArrayList();

	@ManyToOne
	@JoinColumn(name = "master_usage_direction_id")
	private Item masterUsageDirection; // Đường dùng

	@ManyToOne
	@JoinColumn(name = "master_generic_drug_id")
	private Item masterGenericDrug; //Generic drug/Chemical Name//Hoạt chất

	@Builder.Default
	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	//@formatter:off
	@JoinTable(
			name = "inventory_item_catagory", 
			inverseJoinColumns = {@JoinColumn(name = "catalogue_id")},
			joinColumns = {@JoinColumn(name = "inventory_item_id")}
	)
	//@formatter:on
	private Set<Catalogue> categories = ListUtility.createHashSet();
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTranslatedName() {
		return translatedName;
	}

	public void setTranslatedName(String translatedName) {
		this.translatedName = translatedName;
	}

	public String getComposition() {
		return composition;
	}

	public void setComposition(String composition) {
		this.composition = composition;
	}

	public String getActivePrinciple() {
		return activePrinciple;
	}

	public void setActivePrinciple(String activePrinciple) {
		this.activePrinciple = activePrinciple;
	}

	public String getProcessingType() {
		return processingType;
	}

	public void setProcessingType(String processingType) {
		this.processingType = processingType;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getManufacturingDate() {
		return manufacturingDate;
	}

	public void setManufacturingDate(Date manufacturingDate) {
		this.manufacturingDate = manufacturingDate;
	}

	public Date getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(Date availableDate) {
		this.availableDate = availableDate;
	}

	public String getVendorPartNumber() {
		return vendorPartNumber;
	}

	public void setVendorPartNumber(String vendorPartNumber) {
		this.vendorPartNumber = vendorPartNumber;
	}

	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}

	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}

	public InventoryItem getParent() {
		return parent;
	}

	public void setParent(InventoryItem parent) {
		this.parent = parent;
	}

	public Integer getMinimumOptions() {
		return minimumOptions;
	}

	public void setMinimumOptions(Integer minimumOptions) {
		this.minimumOptions = minimumOptions;
	}

	public Integer getMaximumOptions() {
		return maximumOptions;
	}

	public void setMaximumOptions(Integer maximumOptions) {
		this.maximumOptions = maximumOptions;
	}

	public List<InventoryItemCatalog> getProductCatalogues() {
		return productCatalogues;
	}

	public void setProductCatalogues(List<InventoryItemCatalog> productCatalogues) {
		this.productCatalogues = productCatalogues;
	}

	public List<Catalogue> getCatalogues() {
		return catalogues;
	}

	public void setCatalogues(List<Catalogue> catalogues) {
		this.catalogues = catalogues;
	}

	public List<InventoryItemProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<InventoryItemProfile> profiles) {
		this.profiles = profiles;
	}

	public String getMasterPhoto() {
		return masterPhoto;
	}

	public void setMasterPhoto(String masterPhoto) {
		this.masterPhoto = masterPhoto;
	}

	public Long getMasterVendorId() {
		return masterVendorId;
	}

	public void setMasterVendorId(Long masterVendorId) {
		this.masterVendorId = masterVendorId;
	}

	public Long getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Item getMasterUsageDirection() {
		return masterUsageDirection;
	}

	public void setMasterUsageDirection(Item masterUsageDirection) {
		this.masterUsageDirection = masterUsageDirection;
	}

	public Item getMasterGenericDrug() {
		return masterGenericDrug;
	}

	public void setMasterGenericDrug(Item masterGenericDrug) {
		this.masterGenericDrug = masterGenericDrug;
	}

	public Set<Catalogue> getCategories() {
		return categories;
	}

	public void setCategories(Set<Catalogue> categories) {
		this.categories = categories;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Catalogue getMasterCategory() {
		return masterCategory;
	}

	public void setMasterCategory(Catalogue masterCategory) {
		this.masterCategory = masterCategory;
	}
}
