/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.paramount.controller.paux;

import static com.github.adminfaces.template.util.Assert.has;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import com.github.adminfaces.template.exception.AccessDeniedException;

import net.paramount.auth.entity.Authority;
import net.paramount.auth.service.AuthorityService;
import net.paramount.msp.util.FacesUtilities;

/**
 * @author ducbq
 */
@Named(value = "roleDetail")
@ViewScoped
public class RoleDetailController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8331499270487789202L;

	@Inject
	private AuthorityService businessService;

	@Inject
	private FacesUtilities utils;

	private Long id;

	private Authority businessObject;

	public void init() {
		if (Faces.isAjaxRequest()) {
			return;
		}

		if (has(id)) {
			this.businessObject = businessService.getObject(Long.valueOf(id));
		} else {
			this.businessObject = Authority.builder().build();
		}
	}

	public void remove() throws IOException {
		if (!utils.isUserInRole("ROLE_ADMIN")) {
			throw new AccessDeniedException("User not authorized! Only role <b>admin</b> can remove businessObjects.");
		}

		if (has(businessObject) && has(businessObject.getId())) {
			businessService.remove(businessObject);
			utils.addDetailMessage("Authority " + businessObject.getDisplayName() + " removed successfully");
			Faces.getFlash().setKeepMessages(true);
			Faces.redirect("user/car-list.jsf");
		}
	}

	public void save() {
		businessService.saveOrUpdate(this.businessObject);
		String msg = "Authority " + this.businessObject.getName() + " created successfully";
		utils.addDetailMessage(msg);
	}

	public void clear() {
		this.businessObject = Authority.builder().build();
		id = null;
	}

	public boolean isNew() {
		return this.businessObject == null || this.businessObject.getId() == null;
	}

	public Authority getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(Authority BusinessObject) {
		this.businessObject = BusinessObject;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
