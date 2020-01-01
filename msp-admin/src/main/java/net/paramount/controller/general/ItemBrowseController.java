package net.paramount.controller.general;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.css.service.config.ItemService;
import net.paramount.entity.general.Item;
import net.paramount.msp.infra.model.Filter;
import net.paramount.msp.util.Utils;

/**
 * Created by rmpestano on 12/02/17.
 */
@Named(value = "itemBrowser")
@ViewScoped
public class ItemBrowseController implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6412581743995405359L;

	@Inject
  private ItemService businessService;
	private List<Item> selectedObjects; 
	private List<Item> businessObjects; 
	private Filter<Item> bizFilter = new Filter<>(new Item());
	private List<Item> filteredObjects;// datatable filteredValue attribute (column filters)

    @Inject
    private Utils utils;

    private String instantSearch;
    Long id;

    Filter<Item> filter = new Filter<>(new Item());

    List<Item> filteredValue;// datatable filteredValue attribute (column filters)

    @PostConstruct
    public void initDataModel() {
    	try {
    		this.businessObjects = businessService.getObjects();
			} catch (Exception e) {
				e.printStackTrace();
			}
    }

    public void clear() {
        filter = new Filter<Item>(new Item());
    }

    public List<String> completeModel(String query) {
        List<String> result = ListUtility.createDataList();//carService.getModels(query);
        return result;
    }

    public void search(String parameter) {
    	System.out.println("Searching parameter: " + parameter);
    	/*if (id == null) {
            throw new BusinessException("Provide Car ID to load");
        }
        selectedCars.add(carService.findById(id));*/
    }

    public void delete() {
    	if (CommonUtility.isNotEmpty(this.selectedObjects)) {
      	for (Item removalItem :this.selectedObjects) {
      		System.out.println("#" + removalItem.getCode());
      		this.businessObjects.remove(removalItem);
      	}
        utils.addDetailMessage("Objects deleted successfully!");
        this.selectedObjects.clear();
    	}
    }

    public List<Item> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Item> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public Filter<Item> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Item> filter) {
        this.filter = filter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

  	public List<Item> getBusinessObjects() {
  		return businessObjects;
  	}

  	public void setBusinessObjects(List<Item> businessObjects) {
  		this.businessObjects = businessObjects;
  	}

		public List<Item> getSelectedObjects() {
			return selectedObjects;
		}

		public void setSelectedObjects(List<Item> selectedObjects) {
			this.selectedObjects = selectedObjects;
		}

		public Filter<Item> getBizFilter() {
			return bizFilter;
		}

		public void setBizFilter(Filter<Item> bizFilter) {
			this.bizFilter = bizFilter;
		}

		public List<Item> getFilteredObjects() {
			return filteredObjects;
		}

		public void setFilteredObjects(List<Item> filteredObjects) {
			this.filteredObjects = filteredObjects;
		}

		public String getInstantSearch() {
			return instantSearch;
		}

		public void setInstantSearch(String instantSearch) {
			this.instantSearch = instantSearch;
		}

		public void recordsRowSelected(AjaxBehaviorEvent e) {
			System.out.println("recordsRowSelected");
		}
}
