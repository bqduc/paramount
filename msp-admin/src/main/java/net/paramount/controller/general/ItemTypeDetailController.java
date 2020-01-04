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
import org.primefaces.event.SelectEvent;

import com.github.adminfaces.template.exception.AccessDeniedException;

import net.paramount.css.service.general.ItemTypeService;
import net.paramount.entity.general.ItemType;
import net.paramount.msp.util.FacesUtilities;

/**
 * @author ducbq
 */
@Named(value = "itemTypeDetailController")
@ViewScoped
public class ItemTypeDetailController implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1951559129683497779L;

	@Inject
	private ItemTypeService businessService;

	@Inject
	private FacesUtilities facesUtilities;

	private Long id;
	private ItemType businessObject;
	private ItemType parent;

	public void init() {
		if (Faces.isAjaxRequest()) {
			return;
		}
		if (has(id)) {
			businessObject = businessService.getObject(id);
		} else {
			businessObject = new ItemType();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ItemType getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(ItemType businessObject) {
		this.businessObject = businessObject;
	}

	public void remove() throws IOException {
		if (!facesUtilities.isUserInRole("ROLE_ADMIN")) {
			throw new AccessDeniedException("User not authorized! Only role <b>admin</b> can remove cars.");
		}
		if (has(businessObject) && has(businessObject.getId())) {
			businessService.remove(businessObject);
			facesUtilities.addDetailMessage("Business object " + businessObject.getName() + " removed successfully");
			Faces.getFlash().setKeepMessages(true);
			Faces.redirect("user/car-list.jsf");
		}
	}

	public void save() {
		String msg;
		businessObject.setParent(parent);
		if (businessObject.getId() == null) {
			businessService.saveOrUpdate(businessObject);
			msg = "Business object " + businessObject.getName() + " created successfully";
		} else {
			businessService.saveOrUpdate(businessObject);
			msg = "Business object " + businessObject.getName() + " updated successfully";
		}
		facesUtilities.addDetailMessage(msg);
	}

	public void clear() {
		businessObject = new ItemType();
		id = null;
	}

	public boolean isNew() {
		return businessObject == null || businessObject.getId() == null;
	}

	public void handleParentSelect(SelectEvent event) { 
		Object item = event.getObject(); 
		if (item instanceof ItemType) {
			this.parent = (ItemType)item;
		}
		//FacesMessage msg = new FacesMessage("Selected", "Item:" + item); 
	}

}
