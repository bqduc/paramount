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

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.paramount.entity.general.BusinessUnit;
import net.paramount.entity.general.Catalogue;
import net.paramount.entity.general.Item;
import net.paramount.entity.general.MeasureUnit;
import net.paramount.entity.general.Money;
import net.paramount.entity.general.MoneySet;
import net.paramount.entity.general.Quantity;
import net.paramount.entity.trade.DiscountOrExpense;
import net.paramount.entity.trade.ExpenseType;
import net.paramount.entity.trade.Foundation;
import net.paramount.entity.trade.PaymentActionType;
import net.paramount.entity.trade.Tax;
import net.paramount.entity.trade.TaxType;
import net.paramount.entity.trade.UnitPriceMoneySet;
import net.paramount.framework.entity.AuditBase;
import net.paramount.framework.validation.StrictlyPositiveNumber;
import net.paramount.global.GlobalConstants;

/**
 * Entity class Product
 * 
 * @author ducbq
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product extends AuditBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2929178651788000055L;

	@Builder.Default
	@Column(name = "PRODUCT_TYPE")
	@Enumerated(EnumType.ORDINAL)
	private ProductType productType = ProductType.Product;

	@Column(name = "CODE", length = GlobalConstants.SIZE_CODE)
	private String code;

	@Column(name = "barcode", length = GlobalConstants.SIZE_BARCODE)
	private String barcode;

	@Column(name = "NAME", length = GlobalConstants.SIZE_NAME)
	private String name;

	@Builder.Default
	@Column(name = "OPEN_DATE")
	@Temporal(TemporalType.DATE)
	private Date openDate = new Date();

	@ManyToOne
	@JoinColumn(name = "product_category_id")
	private ProductCategory productCategory;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Catalogue category;

	@Column(name = "SYSTEM")
	private Boolean system;

	@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(name = "image_default")
	@Type(type = "org.hibernate.type.ImageType")
	private byte[] imageDefault;

	/*@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(name = "image_medium")
	private byte[] imageMedium;*/

	@ManyToOne
	@JoinColumn(name = "ExpenseType_ID")
	private ExpenseType expenseType;

	// KDV - VAT
	@ManyToOne
	@JoinColumn(name = "BUY_TAX_ID")
	private Tax buyTax;

	@ManyToOne
	@JoinColumn(name = "SELL_TAX_ID")
	private Tax sellTax;

	@Builder.Default
	@Column(name = "TAX_INCLUDED")
	private Boolean taxIncluded = Boolean.TRUE;

	// OIV / OTV
	@ManyToOne
	@JoinColumn(name = "BUY_TAX2_ID")
	private Tax buyTax2;

	@ManyToOne
	@JoinColumn(name = "SELL_TAX2_ID")
	private Tax sellTax2;

	@Builder.Default
	@Column(name = "TAX2_INCLUDED")
	private Boolean tax2Included = Boolean.TRUE;

	@Builder.Default
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductUnit> productUnitList = new ArrayList<ProductUnit>();

	@Builder.Default
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductDetail> detailList = new ArrayList<ProductDetail>();

	/**
	 * Eğer ürün masraf veya indirim olarak işaretlenmişse indirim/masraf bilgileri tutar.
	 */
	@Embedded
	@Valid
	@AttributeOverrides({ 
		@AttributeOverride(name = "percentage", column = @Column(name = "DISCOUNT_EXPENSE_PERCENTAGE")),
		@AttributeOverride(name = "rate", column = @Column(name = "DISCOUNT_EXPENSE_RATE")),
		@AttributeOverride(name = "currency", column = @Column(name = "DISCOUNT_EXPENSE_CCY")),
		@AttributeOverride(name = "value", column = @Column(name = "DISCOUNT_EXPENSE_VALUE")),
		@AttributeOverride(name = "localAmount", column = @Column(name = "DISCOUNT_EXPENSE_LCYVAL")) })
	private DiscountOrExpense discountOrExpense;

	/**
	 * Son alış fiyat bilgileridir.
	 */
	@Builder.Default
	@Embedded
	@Valid
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "LAST_PURCHASE_PRICE_CCY")),
		@AttributeOverride(name = "value", column = @Column(name = "LAST_PURCHASE_PRICE_VALUE")),
		@AttributeOverride(name = "localAmount", column = @Column(name = "LAST_PURCHASE_PRICE_LCYVAL")) })
	private MoneySet lastPurchasePrice = new UnitPriceMoneySet();

	/**
	 * Son satış fiyat bilgileridir.
	 */
	@Builder.Default
	@Embedded
	@Valid
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "LAST_SALE_PRICE_CCY")),
		@AttributeOverride(name = "value", column = @Column(name = "LAST_SALE_PRICE_VALUE")),
		@AttributeOverride(name = "localAmount", column = @Column(name = "LAST_SALE_PRICE_LCYVAL")) })
	private MoneySet lastSalePrice = new UnitPriceMoneySet();

	/**
	 * Etiketin üzerine basılacak olan yazı bilgisini tutar.
	 */
	@Column(name = "LABEL_NAME")
	private String labelName;

	/**
	 * Marka bilgisini tutar.
	 */
	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	private ProductGroup group;

	/**
	 * Kurum bilgisini tutar.
	 */
	@ManyToOne
	@JoinColumn(name = "FOUNDATION_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_FOUNDATIONID"))
	private Foundation foundation;

	@ManyToOne
	@JoinColumn(name = "PAYMENT_ACTION_TYPE_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_PAYMENTACTIONTYPEID"))
	private PaymentActionType paymentActionType;

	/**
	 * Katkı masraf veya indirimlerinin yansıtılacağı hizmeti tutar.
	 */
	@ManyToOne
	@JoinColumn(name = "REF_PRODUCT_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_REFERENCEPRODUCTID"))
	private Product referenceProduct;

	@ManyToOne
	@JoinColumn(name = "ref_active_ingredient_id", foreignKey = @ForeignKey(name = "FK_active_ingredient"))
	private Item activeIngredient;

	@ManyToOne
	@JoinColumn(name = "ref_usage_direction_id", foreignKey = @ForeignKey(name = "fk_usage_direction"))
	private Item usageDirection;

	@ManyToOne
	@JoinColumn(name = "ref_servicing_business_unit_id", foreignKey = @ForeignKey(name = "fk_servicing_business_unit_id"))
	private BusinessUnit servicingBusinessUnit;

	/**
	 * Ürün veya hizmetin birim fiyatının scale(virgülden sonrası) bilgisini tutar.
	 */
	@Builder.Default
	@Column(name = "UNIT_PRICE_SCALE")
	@Enumerated(EnumType.ORDINAL)
	private UnitPriceScale unitPriceScale = UnitPriceScale.Low;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Size(max = GlobalConstants.SIZE_NAME)
	@Column(name = "translated_name")
	private String translatedName;

	@Column(name = "composition", length = 150)
	private String composition; // Content also

	@Column(name = "processing_type", length = 100)
	private String processingType;

	@Column(name = "packaging", length = 150)
	private String packaging;

	@Column(name = "info", columnDefinition = "TEXT")
	private String info;

	@Column(name = "manufacturing_date")
	private Date manufacturingDate;

	@Column(name = "available_date")
	private Date availableDate;

	@Column(name = "master_vendor_id")
	private Long masterVendorId;

	@Column(name = "vendor_part_number", length = GlobalConstants.SIZE_CODE)
	private String vendorPartNumber;

	@Column(name = "manufacturer", length=GlobalConstants.SIZE_NAME)
	private String manufacturer;

	@Column(name = "manufacturer_country", length=GlobalConstants.SIZE_COUNTRY)
	private String manufacturerCountry;

	@Column(name = "manufacturer_part_number", length = GlobalConstants.SIZE_CODE)
	private String manufacturerPartNumber;

	@Column(name = "manufacturer_address", length = 250)
	private String manufacturerAddress;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Product parent;

	@Column(name = "minimum_options")
	private Integer minimumOptions;

	@Column(name = "maximum_options")
	private Integer maximumOptions;

	@Column(name = "servicing_code", length = GlobalConstants.SIZE_SERIAL)
	private String servicingCode;

	@Column(name = "cross_servicing_code", length = GlobalConstants.SIZE_SERIAL)
	private String crossServicingCode;

	@Column(name = "government_decision_no", length = GlobalConstants.SIZE_SERIAL)
	private String governmentDecisionNo;

	@Column(name = "published_code", length = GlobalConstants.SIZE_SERIAL)
	private String publishedCode;

	@Column(name = "standard", length = 50)
	private String standard;

	@Column(name = "expectation_of_life", length = 50)
	private String expectationOfLife;

	@Column(name = "registration_company", length = 80)
	private String registrationCompany;

	@Column(name = "registrationCountry", length = 50)
	private String registrationCountry;

	@Column(name = "registration_address", length = 250)
	private String registrationAddress;

	@Column(name = "root")
	private String root;

	@Column(name = "circular_no")
	private String nationalCircularNo;

	@Column(name = "maintenance_level")
	private String maintenanceLevel;

	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "issue_date")
	private Date issueDate;

	@Builder.Default
	@Column(name = "reset_date")
	private ZonedDateTime resetDate = null;

	@Builder.Default
	@Column(name = "sale_price")
	@StrictlyPositiveNumber(message = "{PositiveSalePrice}")
	private Double salePrice = 2d;

	@Builder.Default
	@Column(name = "purchase_price")
	@StrictlyPositiveNumber(message = "{PositiveCost}")
	private Double purchasePrice = 1d;

	@Builder.Default
	@Column(name = "weight")
	private Double weight = 0d;

	@Builder.Default
	@Column(name = "volume")
	private Double volume = 0d;

	@Builder.Default
	@Column(name = "lenght")
	private Double length = 0d;

	@Column(name = "sale_ok")
	private Boolean saleOk;

	@Column(name = "purchase_ok")
	private Boolean purchaseOk;

	@ManyToOne
	@JoinColumn(name = "measure_unit_id")
	private MeasureUnit measureUnit;
	
	@Builder.Default
	@Embedded
  @Valid
  @AttributeOverrides( {
      @AttributeOverride(name="currency", column=@Column(name="unit_price_ccy")),
      @AttributeOverride(name="value", column=@Column(name="unit_price_value"))
  })
  private Money unitPrice = new Money(); 

	@Builder.Default
  @Embedded
  @Valid
  @AttributeOverrides( {
      @AttributeOverride(name="currency", column=@Column(name="unit_price_market_ccy")),
      @AttributeOverride(name="value", column=@Column(name="unit_price_market_value"))
  })
  private Money unitPriceMarket = new Money(); 

	@Builder.Default
  @Embedded
  @Valid
  @AttributeOverrides( {
      @AttributeOverride(name="currency", column=@Column(name="cost_price_ccy")),
      @AttributeOverride(name="value", column=@Column(name="cost_price_value"))
  })
  private Money costPrice = new Money(); 

	@Builder.Default
  @Embedded
  @Valid
  @AttributeOverrides( {
      @AttributeOverride(name="currency", column=@Column(name="selling_price_ccy")),
      @AttributeOverride(name="value", column=@Column(name="selling_price_value"))
  })
  private Money sellingPrice = new Money(); 

	@Builder.Default
  @Embedded
  @Valid
  @AttributeOverrides( {
      @AttributeOverride(name="currency", column=@Column(name="prog_selling_price_ccy")),
      @AttributeOverride(name="value", column=@Column(name="prog_selling_price_value"))
  })
  private Money progSellingPrice = new Money(); 

	@Builder.Default
  @Embedded
  @Valid
  @AttributeOverrides( {
    @AttributeOverride(name="unitId", column=@Column(name="prog_selling_unit_id")),
    @AttributeOverride(name="value", column=@Column(name="prog_selling_value"))
  })
  private Quantity progSellingQuantity = new Quantity();

	@Builder.Default
  @Embedded
  @Valid
  @AttributeOverrides( {
    @AttributeOverride(name="unitId", column=@Column(name="balance_unit_id")),
    @AttributeOverride(name="value", column=@Column(name="balance_value"))
  })
  private Quantity balanceQuantity = new Quantity();

	@Column(name = "contractor", length=GlobalConstants.SIZE_NAME)
	private String contractor;

	@Column(name = "contractor_category", length=GlobalConstants.SIZE_NAME_SHORT)
	private String contracorCategory;
  
	@Column(name = "contractor_group", length=GlobalConstants.SIZE_NAME_SHORT)
	private String contractorGroup;

	@Column(name = "notification_no", length=GlobalConstants.SIZE_SERIAL)
	private String notificationNo;

	@ManyToOne
	@JoinColumn(name = "vendor_business_unit_id")
	private BusinessUnit vendor;

	@Column(name = "external_code", length=GlobalConstants.SIZE_CODE)
	private String externalCode;

	@Column(name = "external_type", length=GlobalConstants.SIZE_STRING_TINY)
	private String externalType;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Tax getOTVTax() {
		try {
			Method taxGetter = null;
			Tax tax = null;

			for (int i = 1; i <= 5; i++) {
				taxGetter = getClass().getMethod("getSellTax" + i);
				tax = (Tax) taxGetter.invoke(this);

				if (tax != null && tax.getType().equals(TaxType.OTV)) {
					return tax;
				}
			}
		} catch (Exception e) {
			System.out.println("Hata" + e.getMessage());
		}
		return null;
	}

	public boolean isHasLowerPriceThanCent() {
		if (unitPriceScale.equals(UnitPriceScale.High))
			return true;
		return false;
	}

	public void setHasLowerPriceThanCent(boolean aValue) {
		if (aValue) {
			unitPriceScale = UnitPriceScale.High;
		} else {
			unitPriceScale = UnitPriceScale.Low;
		}
	}

	/**
	 * vergileri cachelemek için kullanacak.
	 */
	@Transient
	private List<ProductTax> taxList;

	public List<ProductTax> taxesAsList() {
		if (taxList == null) {
			taxList = new ArrayList<ProductTax>();

			taxList.add(new ProductTax(getBuyTax1(), getTax1Included()));
			taxList.add(new ProductTax(getBuyTax2(), getTax2Included()));
		}
		return taxList;
	}

	public ProductTax getWitholdingTax() {
		for (ProductTax productTax : taxesAsList()) {
			if (productTax.getTax() != null && productTax.getTax().getType().equals(TaxType.STOPAJ))
				return productTax;
		}
		return null;
	}

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

	public List<ProductDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<ProductDetail> detailList) {
		this.detailList = detailList;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Boolean getSaleOk() {
		return saleOk;
	}

	public void setSaleOk(Boolean saleOk) {
		this.saleOk = saleOk;
	}

	public Boolean getPurchaseOk() {
		return purchaseOk;
	}

	public void setPurchaseOk(Boolean purchaseOk) {
		this.purchaseOk = purchaseOk;
	}

	public Boolean getSystem() {
		return system;
	}

	public void setSystem(Boolean system) {
		this.system = system;
	}

	public Tax getBuyTax() {
		return buyTax;
	}

	public Tax getBuyTax1() {
		return buyTax;
	}

	public void setBuyTax(Tax buyTax) {
		this.buyTax = buyTax;
	}

	public Tax getSellTax() {
		return sellTax;
	}

	public Tax getSellTax1() {
		return sellTax;
	}

	public void setSellTax(Tax sellTax) {
		this.sellTax = sellTax;
	}

	public Boolean getTaxIncluded() {
		return taxIncluded;
	}

	public Boolean getTax1Included() {
		return taxIncluded;
	}

	public void setTaxIncluded(Boolean taxIncluded) {
		this.taxIncluded = taxIncluded;
	}

	@Transient
	public String getCaption() {
		return "[" + getCode() + "] " + getName();
	}

	@Override
	public String toString() {
		return getName();
	}

	public DiscountOrExpense getDiscountOrExpense() {
		if (discountOrExpense == null) {
			discountOrExpense = new DiscountOrExpense();
		}
		return discountOrExpense;
	}

	public void setDiscountOrExpense(DiscountOrExpense discountOrExpense) {
		this.discountOrExpense = discountOrExpense;
	}

	public ExpenseType getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}

	/**
	 * @return the buyTax2
	 */
	public Tax getBuyTax2() {
		return buyTax2;
	}

	/**
	 * @param buyTax2
	 *          the buyTax2 to set
	 */
	public void setBuyTax2(Tax buyTax2) {
		this.buyTax2 = buyTax2;
	}

	/**
	 * @return the sellTax2
	 */
	public Tax getSellTax2() {
		return sellTax2;
	}

	/**
	 * @param sellTax2
	 *          the sellTax2 to set
	 */
	public void setSellTax2(Tax sellTax2) {
		this.sellTax2 = sellTax2;
	}

	/**
	 * @return the tax2Included
	 */
	public Boolean getTax2Included() {
		return tax2Included;
	}

	/**
	 * @param tax2Included
	 *          the tax2Included to set
	 */
	public void setTax2Included(Boolean tax2Included) {
		this.tax2Included = tax2Included;
	}

	public void setProductUnitList(List<ProductUnit> productUnitList) {
		this.productUnitList = productUnitList;
	}

	public List<ProductUnit> getProductUnitList() {
		return productUnitList;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public MoneySet getLastPurchasePrice() {
		if (lastPurchasePrice == null) {
			lastPurchasePrice = new MoneySet();
		}
		return lastPurchasePrice;
	}

	public void setLastPurchasePrice(MoneySet lastPurchasePrice) {
		this.lastPurchasePrice = lastPurchasePrice;
	}

	public MoneySet getLastSalePrice() {
		if (lastSalePrice == null) {
			lastSalePrice = new MoneySet();
		}
		return lastSalePrice;
	}

	public void setLastSalePrice(MoneySet lastSalePrice) {
		this.lastSalePrice = lastSalePrice;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public ProductGroup getGroup() {
		return group;
	}

	public void setGroup(ProductGroup group) {
		this.group = group;
	}

	public BigDecimal getLastPurchasePriceValue() {
		return lastPurchasePrice.getValue();
	}

	public void setLastPurchasePriceValue(BigDecimal lastPurchasePriceValue) {
		this.lastPurchasePrice.setValue(lastPurchasePriceValue);
	}

	public BigDecimal getLastSalePriceValue() {
		return lastSalePrice.getValue();
	}

	public void setLastSalePriceValue(BigDecimal lastSalePriceValue) {
		this.lastSalePrice.setValue(lastSalePriceValue);
	}

	public Foundation getFoundation() {
		return foundation;
	}

	public void setFoundation(Foundation foundation) {
		this.foundation = foundation;
	}

	public PaymentActionType getPaymentActionType() {
		return paymentActionType;
	}

	public void setPaymentActionType(PaymentActionType paymentActionType) {
		this.paymentActionType = paymentActionType;
	}

	public Product getReferenceProduct() {
		return referenceProduct;
	}

	public void setReferenceProduct(Product referenceProduct) {
		this.referenceProduct = referenceProduct;
	}

	public UnitPriceScale getUnitPriceScale() {
		return unitPriceScale;
	}

	public void setUnitPriceScale(UnitPriceScale unitPriceScale) {
		this.unitPriceScale = unitPriceScale;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public Long getMasterVendorId() {
		return masterVendorId;
	}

	public void setMasterVendorId(Long masterVendorId) {
		this.masterVendorId = masterVendorId;
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

	public String getServicingCode() {
		return servicingCode;
	}

	public void setServicingCode(String servicingCode) {
		this.servicingCode = servicingCode;
	}

	public String getCrossServicingCode() {
		return crossServicingCode;
	}

	public void setCrossServicingCode(String crossServicingCode) {
		this.crossServicingCode = crossServicingCode;
	}

	public String getPublishedCode() {
		return publishedCode;
	}

	public void setPublishedCode(String publishedCode) {
		this.publishedCode = publishedCode;
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

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getNationalCircularNo() {
		return nationalCircularNo;
	}

	public void setNationalCircularNo(String nationalCircularNo) {
		this.nationalCircularNo = nationalCircularNo;
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

	public ZonedDateTime getResetDate() {
		return resetDate;
	}

	public void setResetDate(ZonedDateTime resetDate) {
		this.resetDate = resetDate;
	}

	public List<ProductTax> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<ProductTax> taxList) {
		this.taxList = taxList;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public Catalogue getCategory() {
		return category;
	}

	public void setCategory(Catalogue category) {
		this.category = category;
	}

	public byte[] getImageDefault() {
		return imageDefault;
	}

	public void setImageDefault(byte[] imageDefault) {
		this.imageDefault = imageDefault;
	}

	public Item getActiveIngredient() {
		return activeIngredient;
	}

	public void setActiveIngredient(Item activeIngredient) {
		this.activeIngredient = activeIngredient;
	}

	public Item getUsageDirection() {
		return usageDirection;
	}

	public void setUsageDirection(Item usageDirection) {
		this.usageDirection = usageDirection;
	}

	public BusinessUnit getServicingBusinessUnit() {
		return servicingBusinessUnit;
	}

	public void setServicingBusinessUnit(BusinessUnit servicingBusinessUnit) {
		this.servicingBusinessUnit = servicingBusinessUnit;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getManufacturerCountry() {
		return manufacturerCountry;
	}

	public void setManufacturerCountry(String manufacturerCountry) {
		this.manufacturerCountry = manufacturerCountry;
	}

	public String getGovernmentDecisionNo() {
		return governmentDecisionNo;
	}

	public void setGovernmentDecisionNo(String governmentDecisionNo) {
		this.governmentDecisionNo = governmentDecisionNo;
	}

	public MeasureUnit getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(MeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Money getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Money unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Money getUnitPriceMarket() {
		return unitPriceMarket;
	}

	public void setUnitPriceMarket(Money unitPriceMarket) {
		this.unitPriceMarket = unitPriceMarket;
	}

	public Money getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Money costPrice) {
		this.costPrice = costPrice;
	}

	public Money getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Money sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Money getProgSellingPrice() {
		return progSellingPrice;
	}

	public void setProgSellingPrice(Money progSellingPrice) {
		this.progSellingPrice = progSellingPrice;
	}

	public Quantity getProgSellingQuantity() {
		return progSellingQuantity;
	}

	public void setProgSellingQuantity(Quantity progSellingQuantity) {
		this.progSellingQuantity = progSellingQuantity;
	}

	public Quantity getBalanceQuantity() {
		return balanceQuantity;
	}

	public void setBalanceQuantity(Quantity balanceQuantity) {
		this.balanceQuantity = balanceQuantity;
	}

	public String getContractor() {
		return contractor;
	}

	public void setContractor(String contractor) {
		this.contractor = contractor;
	}

	public String getContracorCategory() {
		return contracorCategory;
	}

	public void setContracorCategory(String contracorCategory) {
		this.contracorCategory = contracorCategory;
	}

	public String getContractorGroup() {
		return contractorGroup;
	}

	public void setContractorGroup(String contractorGroup) {
		this.contractorGroup = contractorGroup;
	}

	public String getNotificationNo() {
		return notificationNo;
	}

	public void setNotificationNo(String notificationNo) {
		this.notificationNo = notificationNo;
	}

	public BusinessUnit getVendor() {
		return vendor;
	}

	public void setVendor(BusinessUnit vendor) {
		this.vendor = vendor;
	}

	public String getExternalCode() {
		return externalCode;
	}

	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}

	public String getManufacturerAddress() {
		return manufacturerAddress;
	}

	public void setManufacturerAddress(String manufacturerAddress) {
		this.manufacturerAddress = manufacturerAddress;
	}

	public Product getParent() {
		return parent;
	}

	public void setParent(Product parent) {
		this.parent = parent;
	}
}
