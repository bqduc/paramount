/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.paramount.controller.general;

import static com.github.adminfaces.template.util.Assert.has;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import com.github.adminfaces.template.exception.AccessDeniedException;

import net.paramount.css.service.config.ItemService;
import net.paramount.entity.general.Item;
import net.paramount.msp.util.Utils;

/**
 * @author ducbq
 */
@Named(value = "itemDetailController")
@ViewScoped
public class ItemDetailController implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1951559129683497779L;

	@Inject
	private ItemService businessService;

	@Inject
	private Utils utils;

	private Long id;
	private Item businessObject;

	public void init() {
		if (Faces.isAjaxRequest()) {
			return;
		}
		if (has(id)) {
			businessObject = businessService.getObject(id);
		} else {
			businessObject = new Item();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Item getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(Item businessObject) {
		this.businessObject = businessObject;
	}

	public void remove() throws IOException {
		if (!utils.isUserInRole("ROLE_ADMIN")) {
			throw new AccessDeniedException("User not authorized! Only role <b>admin</b> can remove cars.");
		}
		if (has(businessObject) && has(businessObject.getId())) {
			businessService.remove(businessObject);
			utils.addDetailMessage("Business object " + businessObject.getName() + " removed successfully");
			Faces.getFlash().setKeepMessages(true);
			Faces.redirect("user/car-list.jsf");
		}
	}

	public void save() {
		String msg;
		if (businessObject.getId() == null) {
			businessService.saveOrUpdate(businessObject);
			msg = "Business object " + businessObject.getName() + " created successfully";
		} else {
			businessService.saveOrUpdate(businessObject);
			msg = "Business object " + businessObject.getName() + " updated successfully";
		}
		utils.addDetailMessage(msg);
	}

	public void clear() {
		businessObject = new Item();
		id = null;
	}

	public boolean isNew() {
		return businessObject == null || businessObject.getId() == null;
	}

}
