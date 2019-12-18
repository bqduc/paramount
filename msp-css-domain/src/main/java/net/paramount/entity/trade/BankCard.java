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

package net.paramount.entity.trade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.paramount.entity.general.Country;
import net.paramount.framework.entity.AuditBase;


/**
 * Banka kard kodları
 * BIN database
 *
 * @author volkan
 *
 */
@Entity
@Table(name="BANK_CARD")
public class BankCard extends AuditBase{

	private static final long serialVersionUID = 1L;

    /**
     * BIN kodu, 6 hanedir. Ilk kod kart sistemini belirtir. Geri kalan 5 hane banka+urun kodudur.
     * 4- visa
     * 5- master
     *
     */
    @Column(name="CODE", length=6, unique=true, nullable=false)
    @Size(max=6, min=1)
    @NotNull
	private String code;

    @Column(name="NAME", length=50)
    @Size(max=50)
    private String name;
    
    @Column(name="INFO")
	private String info;
    
    @Column(name="ISACTIVE")
	private Boolean active = Boolean.TRUE;

    @Column(name="FUNDING_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private CardFundingType fundingType;

    @Column(name="CARD_TYPE")
    @Enumerated(EnumType.ORDINAL)
	private CardType cardType;

    @ManyToOne
    @JoinColumn(name="BANK_ID", foreignKey = @ForeignKey(name = "FK_BANKCARD_BANK_ID"))
    private Bank bank;
    
    @Column(name="BANK_NAME", length=50)
    @Size(max=50)
    private String bankName;

    @ManyToOne
	@JoinColumn(name="COUNTRY_ID", foreignKey = @ForeignKey(name = "FK_BANKCARD_COUNTRY_ID"))
	private Country country;

    @Transient
    private boolean selected = true;
    
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

  public CardFundingType getFundingType() {
        return fundingType;
    }

    public void setFundingType(CardFundingType fundingType) {
        this.fundingType = fundingType;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

	@Override
    public String toString() {
        return "com.ut.tekir.entities.BankCard[id=" + getId() + "]";
    }

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

    public String getCaption(){
        return "[" + getCode() + "] " + getName();
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
