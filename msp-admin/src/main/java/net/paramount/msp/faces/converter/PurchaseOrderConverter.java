package net.paramount.msp.faces.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import net.paramount.entity.emx.PurchaseOrder;
import net.paramount.repository.PurchaseOrderFacade;
import net.paramount.utility.JsfUtil;

/**
 * 
 * @author MOHAMMED BOUNAGA
 * 
 * github.com/medbounaga
 */

@FacesConverter(value = "purchaseOrderConverter")
public class PurchaseOrderConverter implements Converter<PurchaseOrder> {

    @Inject
    private PurchaseOrderFacade ejbFacade;

    @Override
    public PurchaseOrder getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    java.lang.Integer getKey(String value) {
        java.lang.Integer key;
        key = Integer.valueOf(value);
        return key;
    }

    String getStringKey(java.lang.Integer value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, PurchaseOrder object) {
        if (object == null) {
            return null;
        }
        if (object instanceof PurchaseOrder) {
            PurchaseOrder o = (PurchaseOrder) object;
            return getStringKey(o.getId().intValue());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PurchaseOrder.class.getName()});
            return null;
        }
    }

}
