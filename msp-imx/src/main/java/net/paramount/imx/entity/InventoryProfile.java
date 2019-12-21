package net.paramount.imx.entity;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.paramount.entity.contact.Contact;
import net.paramount.entity.general.Category;
import net.paramount.entity.general.Department;
import net.paramount.entity.general.MeasureUnit;
import net.paramount.entity.general.TaxGroup;
import net.paramount.framework.entity.BizObjectBase;
import net.paramount.global.GlobalConstants;
import net.paramount.model.BindingType;
import net.paramount.model.DustJacketType;
import net.paramount.model.InventoryCodeType;
import net.paramount.model.InventoryConditionType;
import net.paramount.model.InventoryType;

/**
 * A Book.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inventory_profile")
public class InventoryProfile extends BizObjectBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -361762227077850400L;

	@Version
	@Column(name = "version")
	private Short version;

	@Size(min = 5, max = GlobalConstants.SIZE_SERIAL)
	@Column(name = "code")
	private String code;

	@Size(min = 5, max = 200)
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "author", length = 150)
	private String author;

	@Column(name = "publisher", length = 150)
	private String publisher;

	@Column(name = "date_of_publication")
	private Date dateOfPublication;

	@Column(name = "binding_type")
	@Enumerated(EnumType.ORDINAL)
	private BindingType bindingType;

	@Column(name = "inventory_type")
	@Enumerated(EnumType.ORDINAL)
	private InventoryType inventoryType;

	@Column(name = "illustrator", length = 50)
	private String illustrator;

	@Column(name = "edition", length = 50)
	private String edition;

	@Column(name = "code_type")
	@Enumerated(EnumType.ORDINAL)
	private InventoryCodeType inventoryCodeType;

	@Column(name = "condition_type")
	@Enumerated(EnumType.ORDINAL)
	private InventoryConditionType inventoryConditionType;

	@Column(name = "fulfillment_center_id", length = 40)
	private String fulfillmentCenterId;

	@Column(name = "sku", length = 40)
	private String sku;

	@Column(name = "dust_jacket_type")
	@Enumerated(EnumType.ORDINAL)
	private DustJacketType dustJacketType;

	@Column(name = "signed_by", length = 40)
	private String signedBy;

  /**
   * Select the option corresponding to your choice: "y" if you are offering expedited shipping to your customers; "n" otherwise.
   * 
   * "n" = Expedited shipping not available	
   * "y" = Expedited shipping available"
   */
	@Column(name = "expedited_shipping", length=2)
	private String expeditedShipping;

  /**
   * Select the code corresponding to your choice: "1" or "n": Will ship within US only; "2" or "y": Will ship to international locations
   * 
   * "1" or "n" = Will ship in US only
   * "2" or "y" = Will ship to international locations
   */
	@Column(name = "will_ship_internationally", length=2)
	private String willShipInternationally;
  
	@Column(name = "notes", columnDefinition="TEXT")
	private String notes;

	@Column(name = "condition", columnDefinition="TEXT")
	private String condition;

	@Column(name = "info", columnDefinition="TEXT")
	private String info;

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "photo", columnDefinition="TEXT")
	private String photo;

	@ManyToOne
	@JoinColumn(name = "main_category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "sub_category_id")
	private Category subCategory;

	@ManyToOne
	@JoinColumn(name = "supplementary_category_id")
	private Category supplementaryCategory;

	@ManyToOne
	@JoinColumn(name = "extra_category_id")
	private Category extraCategory;

	@ManyToOne
	@JoinColumn(name = "business_group_id")
	private Department businessGroup;

	@Column(name = "online_series", length=20)
	private String 	onlineSeries;

	@Column(name = "sales_model_subscription", length=20)
	private String salesModelSubscription;

	@Column(name = "sales_model_one_time", length=20)
	private String salesModelOneTime;

	@Column(name = "capacity")
	private Double capacity;

	@Column(name = "online_availability_date")
	private Date onlineAvailabilityDate;

	@Column(name = "production_publication_date")
	private Date productionPublicationDate;

	@Column(name = "url_online_library", length=100)
	private String urlOnlineLibrary;

	@Column(name = "oclc", length=20)
	private String oclc;

	@ManyToOne
	@JoinColumn(name = "specialized_subject_area_id")
	private Department specializedSubjectArea;

	@ManyToOne
	@JoinColumn(name = "main_subject_category_id")
	private Category mainSubjectCategory;

	@Column(name = "isbn10", length=10)
	private String isbn10;

	@Column(name = "o_isbn10", length=10)
	private String oIsbn10;	

	@Column(name = "e_isbn10", length=10)
	private String eIsbn10;

	@Column(name = "isbn13", length=13)
	private String isbn13;

	@Column(name = "o_isbn13", length=13)
	private String oIsbn13;	

	@Column(name = "e_isbn13", length=13)
	private String eIsbn13;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private InventoryProfile parent;

	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinTable(
			name = "inventory_entry_vendor", 
			inverseJoinColumns = {@JoinColumn(name = "vendor_id")},
			joinColumns = {@JoinColumn(name = "inventory_entry_id")}
	)
	private Set<Contact> vendors;

	@ManyToOne
	@JoinColumn(name = "imprint_contact_id")
	private Contact imprint;

	@Column(name="concentration", length=150)
	private String concentration;

	@Column(name="active_principle", length=150)
	private String activePrinciple;	

	@Column(name="processing_type", length=50)
	private String processingType;

	@Column(name="packaging", length=50)
	private String packaging;

	@Column(name="standard", length=50)
	private String standard;

	@Column(name="expectation_of_life", length=50)
	private String expectationOfLife;

	@Column(name="production_company", length=150)
	private String productionCompany;

	@Column(name="production_country", length=50)
	private String productionCountry;

	@Column(name="production_address", length=250)
	private String productionAddress;

	@Column(name="registration_company", length=80)
	private String registrationCompany;

	@Column(name="registrationCountry", length=50)
	private String registrationCountry;

	@Column(name="registration_address", length=250)
	private String registrationAddress;

	@Column(name="circular_no")
	private String nationalCircularNo;

	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "manufacturing_date")
	private Date manufacturingDate;

	@Column(name="maintenance_level")
	private String maintenanceLevel;

	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "issue_date")
	private Date issueDate;

	@Column(name="reset_date")
	private Date resetDate;

	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "available_date")
	private Date availableDate;

	@Column(name="minimum_options")
	private Integer minimumOptions;

	@Column(name="maximum_options")
	private Integer maximumOptions;
	
	@ManyToOne(targetEntity=MeasureUnit.class, fetch=FetchType.EAGER)
	@JoinColumn(name = "measure_unit_id")
	private MeasureUnit measureUnit;

	@Column(name="unit_name")
	private String unitName;

	@Column(name="translated_name")
	private String translatedName;

	@Column(name="buy_price")
	private Double buyPrice;

	@Column(name="stock_amount")
	private Double stockAmount;

	@Column(name="price")
	private Double price;

	@Column(name="discount_rate")
	private Double discountRate;

	@Column(name="disable_when_stock_amount_is_zero")
	private Boolean disableWhenStockAmountIsZero;

	@Column(name="sort_order")
	private Integer sortOrder;

	@Column(name="image_data")
	private byte[] imageData;

	@Column(name="fractional_unit")
	private Boolean fractionalUnit;

	@Column(name="default_sell_portion")
	private Integer defaultSellPortion;

	@ManyToOne(targetEntity=TaxGroup.class, fetch=FetchType.EAGER)
	@JoinColumn(name = "tax_group_id")
	private TaxGroup taxGroup;

	@ManyToOne(targetEntity=PrinterGroup.class, fetch=FetchType.EAGER)
	@JoinColumn(name = "printer_group_id")
	private PrinterGroup printerGroup;
	
	public Short getVersion() {
		return version;
	}

	public void setVersion(Short version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Date getDateOfPublication() {
		return dateOfPublication;
	}

	public void setDateOfPublication(Date dateOfPublication) {
		this.dateOfPublication = dateOfPublication;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		InventoryProfile inventoryEntry = (InventoryProfile) o;

		if (!Objects.equals(getId(), inventoryEntry.getId()))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public BindingType getBindingType() {
		return bindingType;
	}

	public void setBindingType(BindingType bindingType) {
		this.bindingType = bindingType;
	}

	public String getIllustrator() {
		return illustrator;
	}

	public void setIllustrator(String illustrator) {
		this.illustrator = illustrator;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public InventoryCodeType getInventoryCodeType() {
		return inventoryCodeType;
	}

	public void setInventoryCodeType(InventoryCodeType inventoryCodeType) {
		this.inventoryCodeType = inventoryCodeType;
	}

	public InventoryConditionType getInventoryConditionType() {
		return inventoryConditionType;
	}

	public void setInventoryConditionType(InventoryConditionType inventoryConditionType) {
		this.inventoryConditionType = inventoryConditionType;
	}

	public String getFulfillmentCenterId() {
		return fulfillmentCenterId;
	}

	public void setFulfillmentCenterId(String fulfillmentCenterId) {
		this.fulfillmentCenterId = fulfillmentCenterId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public DustJacketType getDustJacketType() {
		return dustJacketType;
	}

	public void setDustJacketType(DustJacketType dustJacketType) {
		this.dustJacketType = dustJacketType;
	}

	public String getSignedBy() {
		return signedBy;
	}

	public void setSignedBy(String signedBy) {
		this.signedBy = signedBy;
	}

	public String getExpeditedShipping() {
		return expeditedShipping;
	}

	public void setExpeditedShipping(String expeditedShipping) {
		this.expeditedShipping = expeditedShipping;
	}

	public String getWillShipInternationally() {
		return willShipInternationally;
	}

	public void setWillShipInternationally(String willShipInternationally) {
		this.willShipInternationally = willShipInternationally;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(Category subCategory) {
		this.subCategory = subCategory;
	}

	public Category getSupplementaryCategory() {
		return supplementaryCategory;
	}

	public void setSupplementaryCategory(Category supplementaryCategory) {
		this.supplementaryCategory = supplementaryCategory;
	}

	public Category getExtraCategory() {
		return extraCategory;
	}

	public void setExtraCategory(Category extraCategory) {
		this.extraCategory = extraCategory;
	}

	public InventoryProfile getParent() {
		return parent;
	}

	public void setParent(InventoryProfile parent) {
		this.parent = parent;
	}

	public Set<Contact> getVendors() {
		return vendors;
	}

	public void setVendors(Set<Contact> vendors) {
		this.vendors = vendors;
	}

	public Department getBusinessGroup() {
		return businessGroup;
	}

	public void setBusinessGroup(Department businessGroup) {
		this.businessGroup = businessGroup;
	}

	public String getOnlineSeries() {
		return onlineSeries;
	}

	public void setOnlineSeries(String onlineSeries) {
		this.onlineSeries = onlineSeries;
	}

	public String getSalesModelSubscription() {
		return salesModelSubscription;
	}

	public void setSalesModelSubscription(String salesModelSubscription) {
		this.salesModelSubscription = salesModelSubscription;
	}

	public String getSalesModelOneTime() {
		return salesModelOneTime;
	}

	public void setSalesModelOneTime(String salesModelOneTime) {
		this.salesModelOneTime = salesModelOneTime;
	}

	public Double getCapacity() {
		return capacity;
	}

	public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}

	public Date getOnlineAvailabilityDate() {
		return onlineAvailabilityDate;
	}

	public void setOnlineAvailabilityDate(Date onlineAvailabilityDate) {
		this.onlineAvailabilityDate = onlineAvailabilityDate;
	}

	public Date getProductionPublicationDate() {
		return productionPublicationDate;
	}

	public void setProductionPublicationDate(Date productionPublicationDate) {
		this.productionPublicationDate = productionPublicationDate;
	}

	public String getUrlOnlineLibrary() {
		return urlOnlineLibrary;
	}

	public void setUrlOnlineLibrary(String urlOnlineLibrary) {
		this.urlOnlineLibrary = urlOnlineLibrary;
	}

	public String getOclc() {
		return oclc;
	}

	public void setOclc(String oclc) {
		this.oclc = oclc;
	}

	public Department getSpecializedSubjectArea() {
		return specializedSubjectArea;
	}

	public void setSpecializedSubjectArea(Department specializedSubjectArea) {
		this.specializedSubjectArea = specializedSubjectArea;
	}

	public Category getMainSubjectCategory() {
		return mainSubjectCategory;
	}

	public void setMainSubjectCategory(Category mainSubjectCategory) {
		this.mainSubjectCategory = mainSubjectCategory;
	}

	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}

	public String getoIsbn10() {
		return oIsbn10;
	}

	public void setoIsbn10(String oIsbn10) {
		this.oIsbn10 = oIsbn10;
	}

	public String geteIsbn10() {
		return eIsbn10;
	}

	public void seteIsbn10(String eIsbn10) {
		this.eIsbn10 = eIsbn10;
	}

	public String getIsbn13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public String getoIsbn13() {
		return oIsbn13;
	}

	public void setoIsbn13(String oIsbn13) {
		this.oIsbn13 = oIsbn13;
	}

	public String geteIsbn13() {
		return eIsbn13;
	}

	public void seteIsbn13(String eIsbn13) {
		this.eIsbn13 = eIsbn13;
	}

	public Contact getImprint() {
		return imprint;
	}

	public void setImprint(Contact imprint) {
		this.imprint = imprint;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public InventoryType getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(InventoryType inventoryType) {
		this.inventoryType = inventoryType;
	}

	public String getConcentration() {
		return concentration;
	}

	public void setConcentration(String concentration) {
		this.concentration = concentration;
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

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getExpectationOfLife() {
		return expectationOfLife;
	}

	public void setExpectationOfLife(String expectationOfLife) {
		this.expectationOfLife = expectationOfLife;
	}

	public String getProductionCompany() {
		return productionCompany;
	}

	public void setProductionCompany(String productionCompany) {
		this.productionCompany = productionCompany;
	}

	public String getProductionCountry() {
		return productionCountry;
	}

	public void setProductionCountry(String productionCountry) {
		this.productionCountry = productionCountry;
	}

	public String getProductionAddress() {
		return productionAddress;
	}

	public void setProductionAddress(String productionAddress) {
		this.productionAddress = productionAddress;
	}

	public String getRegistrationCompany() {
		return registrationCompany;
	}

	public void setRegistrationCompany(String registrationCompany) {
		this.registrationCompany = registrationCompany;
	}

	public String getRegistrationCountry() {
		return registrationCountry;
	}

	public void setRegistrationCountry(String registrationCountry) {
		this.registrationCountry = registrationCountry;
	}

	public String getRegistrationAddress() {
		return registrationAddress;
	}

	public void setRegistrationAddress(String registrationAddress) {
		this.registrationAddress = registrationAddress;
	}

	public String getNationalCircularNo() {
		return nationalCircularNo;
	}

	public void setNationalCircularNo(String nationalCircularNo) {
		this.nationalCircularNo = nationalCircularNo;
	}

	public Date getManufacturingDate() {
		return manufacturingDate;
	}

	public void setManufacturingDate(Date manufacturingDate) {
		this.manufacturingDate = manufacturingDate;
	}

	public String getMaintenanceLevel() {
		return maintenanceLevel;
	}

	public void setMaintenanceLevel(String maintenanceLevel) {
		this.maintenanceLevel = maintenanceLevel;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getResetDate() {
		return resetDate;
	}

	public void setResetDate(Date resetDate) {
		this.resetDate = resetDate;
	}

	public Date getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(Date availableDate) {
		this.availableDate = availableDate;
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

	public MeasureUnit getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(MeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getTranslatedName() {
		return translatedName;
	}

	public void setTranslatedName(String translatedName) {
		this.translatedName = translatedName;
	}

	public Double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Double getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(Double stockAmount) {
		this.stockAmount = stockAmount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public Boolean getDisableWhenStockAmountIsZero() {
		return disableWhenStockAmountIsZero;
	}

	public void setDisableWhenStockAmountIsZero(Boolean disableWhenStockAmountIsZero) {
		this.disableWhenStockAmountIsZero = disableWhenStockAmountIsZero;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public Boolean getFractionalUnit() {
		return fractionalUnit;
	}

	public void setFractionalUnit(Boolean fractionalUnit) {
		this.fractionalUnit = fractionalUnit;
	}

	public Integer getDefaultSellPortion() {
		return defaultSellPortion;
	}

	public void setDefaultSellPortion(Integer defaultSellPortion) {
		this.defaultSellPortion = defaultSellPortion;
	}

	public TaxGroup getTaxGroup() {
		return taxGroup;
	}

	public void setTaxGroup(TaxGroup taxGroup) {
		this.taxGroup = taxGroup;
	}

	public PrinterGroup getPrinterGroup() {
		return printerGroup;
	}

	public void setPrinterGroup(PrinterGroup printerGroup) {
		this.printerGroup = printerGroup;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
