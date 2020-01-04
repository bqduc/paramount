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
import net.paramount.css.service.general.ItemTypeService;
import net.paramount.entity.general.ItemType;
import net.paramount.model.Filter;
import net.paramount.msp.util.FacesUtilities;

/**
 * @author ducbq
 */
@Named(value = "itemTypeBrowser")
@ViewScoped
public class ItemTypeBrowseController implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6412581743995405359L;

	@Inject
  private ItemTypeService businessService;
	private List<ItemType> selectedObjects; 
	private List<ItemType> businessObjects; 
	private Filter<ItemType> bizFilter = new Filter<>(new ItemType());
	private List<ItemType> filteredObjects;// datatable filteredValue attribute (column filters)

    @Inject
    private FacesUtilities facesUtilities;

    private String instantSearch;
    Long id;

    Filter<ItemType> filter = new Filter<>(new ItemType());

    List<ItemType> filteredValue;// datatable filteredValue attribute (column filters)

    @PostConstruct
    public void initDataModel() {
    	try {
    		this.businessObjects = businessService.getObjects();
			} catch (Exception e) {
				e.printStackTrace();
			}
    }

    public void clear() {
        filter = new Filter<ItemType>(new ItemType());
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
      	for (ItemType removalItem :this.selectedObjects) {
      		System.out.println("#" + removalItem.getCode());
      		this.businessObjects.remove(removalItem);
      	}
        facesUtilities.addDetailMessage("Objects deleted successfully!");
        this.selectedObjects.clear();
    	}
    }

    public List<ItemType> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<ItemType> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public Filter<ItemType> getFilter() {
        return filter;
    }

    public void setFilter(Filter<ItemType> filter) {
        this.filter = filter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

  	public List<ItemType> getBusinessObjects() {
  		return businessObjects;
  	}

  	public void setBusinessObjects(List<ItemType> businessObjects) {
  		this.businessObjects = businessObjects;
  	}

		public List<ItemType> getSelectedObjects() {
			return selectedObjects;
		}

		public void setSelectedObjects(List<ItemType> selectedObjects) {
			this.selectedObjects = selectedObjects;
		}

		public Filter<ItemType> getBizFilter() {
			return bizFilter;
		}

		public void setBizFilter(Filter<ItemType> bizFilter) {
			this.bizFilter = bizFilter;
		}

		public List<ItemType> getFilteredObjects() {
			return filteredObjects;
		}

		public void setFilteredObjects(List<ItemType> filteredObjects) {
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
