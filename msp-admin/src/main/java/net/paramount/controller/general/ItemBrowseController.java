package net.paramount.controller.general;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.github.adminfaces.template.exception.BusinessException;

import net.paramount.css.service.config.ItemService;
import net.paramount.entity.general.Item;
import net.paramount.msp.infra.model.Filter;
import net.paramount.msp.model.Car;
import net.paramount.msp.service.CarService;
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
	private Filter<Car> bizFilter = new Filter<>(new Car());
	private List<Item> filteredObjects;// datatable filteredValue attribute (column filters)

	@Inject
    CarService carService;

    @Inject
    private Utils utils;

    Integer id;

    LazyDataModel<Car> cars;

    Filter<Car> filter = new Filter<>(new Car());

    List<Car> selectedCars; //cars selected in checkbox column

    List<Car> filteredValue;// datatable filteredValue attribute (column filters)

    @PostConstruct
    public void initDataModel() {
    	try {
    		this.businessObjects = businessService.getObjects();
			} catch (Exception e) {
				e.printStackTrace();
			}
        cars = new LazyDataModel<Car>() {
            @Override
            public List<Car> load(int first, int pageSize,
                                  String sortField, SortOrder sortOrder,
                                  Map<String, Object> filters) {
                net.paramount.msp.infra.model.SortOrder order = null;
                if (sortOrder != null) {
                    order = sortOrder.equals(SortOrder.ASCENDING) ? net.paramount.msp.infra.model.SortOrder.ASCENDING
                            : sortOrder.equals(SortOrder.DESCENDING) ? net.paramount.msp.infra.model.SortOrder.DESCENDING
                            : net.paramount.msp.infra.model.SortOrder.UNSORTED;
                }
                filter.setFirst(first).setPageSize(pageSize)
                        .setSortField(sortField).setSortOrder(order)
                        .setParams(filters);
                List<Car> list = carService.paginate(filter);
                setRowCount((int) carService.count(filter));
                return list;
            }

            @Override
            public int getRowCount() {
                return super.getRowCount();
            }

            @Override
            public Car getRowData(String key) {
                return carService.findById(new Integer(key));
            }
        };
    }

    public void clear() {
        filter = new Filter<Car>(new Car());
    }

    public List<String> completeModel(String query) {
        List<String> result = carService.getModels(query);
        return result;
    }

    public void findCarById(Integer id) {
        if (id == null) {
            throw new BusinessException("Provide Car ID to load");
        }
        selectedCars.add(carService.findById(id));
    }

    public void delete() {
        int numCars = 0;
        for (Car selectedCar : selectedCars) {
            numCars++;
            //carService.remove(selectedCar);
        }
        selectedCars.clear();
        utils.addDetailMessage(numCars + " cars deleted successfully!");
    }

    public List<Car> getSelectedCars() {
        return selectedCars;
    }

    public List<Car> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Car> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public void setSelectedCars(List<Car> selectedCars) {
        this.selectedCars = selectedCars;
    }

    public LazyDataModel<Car> getCars() {
        return cars;
    }

    public void setCars(LazyDataModel<Car> cars) {
        this.cars = cars;
    }

    public Filter<Car> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Car> filter) {
        this.filter = filter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

		public Filter<Car> getBizFilter() {
			return bizFilter;
		}

		public void setBizFilter(Filter<Car> bizFilter) {
			this.bizFilter = bizFilter;
		}

		public List<Item> getFilteredObjects() {
			return filteredObjects;
		}

		public void setFilteredObjects(List<Item> filteredObjects) {
			this.filteredObjects = filteredObjects;
		}
}
