/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.paramount.controller.stock;

import static com.github.adminfaces.template.util.Assert.has;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import com.github.adminfaces.template.exception.AccessDeniedException;

import net.paramount.css.service.contact.ContactService;
import net.paramount.entity.contact.Contact;
import net.paramount.entity.stock.Product;
import net.paramount.msp.model.Car;
import net.paramount.msp.service.CarService;
import net.paramount.msp.util.FacesUtilities;

/**
 * @author ducbq
 */
@Named(value = "productDetail")
@ViewScoped
public class ProductDetailController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5550266105589219010L;

	@Inject
	CarService carService;

	@Inject
	private ContactService businessService;

	@Inject
	private FacesUtilities utils;

	private Long id;
	private Car car;

	private Product businessObject;
	private Contact contact;

	public void init() {
		if (Faces.isAjaxRequest()) {
			return;
		}
		if (has(id)) {
			this.contact = businessService.getObject(Long.valueOf(id));
		} else {
			this.contact = Contact.builder().build();
		}
	}

	public void remove() throws IOException {
		if (!utils.isUserInRole("ROLE_ADMIN")) {
			throw new AccessDeniedException("User not authorized! Only role <b>admin</b> can remove cars.");
		}
		if (has(car) && has(car.getId())) {
			carService.remove(car);
			utils.addDetailMessage("Car " + car.getModel() + " removed successfully");
			Faces.getFlash().setKeepMessages(true);
			Faces.redirect("user/car-list.jsf");
		}
	}

	public void save() {
		businessService.saveOrUpdate(this.contact);
		String msg = "Contact " + this.contact.getCode() + " created successfully";
		utils.addDetailMessage(msg);
	}

	public void clear() {
		this.contact = Contact.builder().build();
		id = null;
	}

	public boolean isNew() {
		return this.contact == null || this.contact.getId() == null;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(Product businessObject) {
		this.businessObject = businessObject;
	}

}
