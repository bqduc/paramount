/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.paramount.msp.faces.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.paramount.css.service.config.ItemService;
import net.paramount.entity.general.Item;

//@FacesConverter("itemConverter")
@Service(value="itemConverter")
public class ItemConverter implements Converter<Item> {
	@Inject
	private ItemService service;

	public Item getAsObject(FacesContext fc, UIComponent uic, String value) {
		Item asObject = null;
		if (value != null && value.trim().length() > 0) {
			try {
				asObject = service.getObject(Long.parseLong(value));
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid generic item."));
			}
		} 
		return asObject;
	}

	public String getAsString(FacesContext fc, UIComponent uic, Item object) {
		if (object != null) {
			return String.valueOf(object.getId());//object.toString();
		} else {
			return null;
		}
	}
}
