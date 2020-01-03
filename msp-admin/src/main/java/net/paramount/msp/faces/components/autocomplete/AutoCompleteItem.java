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
package net.paramount.msp.faces.components.autocomplete;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import net.paramount.common.ListUtility;
import net.paramount.css.service.config.ItemService;
import net.paramount.entity.general.Item;

@Named(value="autoCompleteItem")
@ViewScoped
public class AutoCompleteItem implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1180855013001665329L;

	private Item item;
	private List<Item> selectedItems;

	@Inject
	private ItemService businessService;

	public List<String> completeText(String query) {
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			results.add(query + i);
		}

		return results;
	}

	public List<Item> completeItem(String query) {
		List<Item> allItems = businessService.getObjects();
		List<Item> filteredItems = ListUtility.createDataList();

		for (int i = 0; i < allItems.size(); i++) {
			Item skin = allItems.get(i);
			if (skin.getName().toLowerCase().contains(query.toLowerCase())) {
				filteredItems.add(skin);
			}
		}

		return filteredItems;
	}

	public List<Item> completeItemContains(String query) {
		List<Item> allItems = businessService.getObjects();
		List<Item> filteredItems = ListUtility.createDataList();

		for (int i = 0; i < allItems.size(); i++) {
			Item skin = allItems.get(i);
			if (skin.getName().toLowerCase().contains(query.toLowerCase())) {
				filteredItems.add(skin);
			}
		}

		return filteredItems;
	}

	public void onItemSelect(SelectEvent event) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", event.getObject().toString()));
	}

	public List<Item> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<Item> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public char getItemGroup(Item item) {
		return item.getName().charAt(0);
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ItemService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(ItemService businessService) {
		this.businessService = businessService;
	}

}
