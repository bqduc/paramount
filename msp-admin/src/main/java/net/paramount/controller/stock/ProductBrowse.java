package net.paramount.controller.stock;

import java.util.List;
import java.util.Locale;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.paramount.common.CommonUtility;
import net.paramount.css.service.stock.ProductService;
import net.paramount.entity.stock.Product;
import net.paramount.framework.controller.BaseController;
import net.paramount.msp.faces.model.FacesCar;
import net.paramount.msp.faces.model.FacesTeamFacade;
import net.paramount.msp.faces.service.FacesCarService;
import net.paramount.msp.model.Car;

/**
 * @author ducbq
 */
@Named(value = "productBrowse")
@ViewScoped
public class ProductBrowse extends BaseController {
	/**
	* 
	*/
	private static final long serialVersionUID = 2662262440970210081L;
	private List<FacesTeamFacade> teams;
	private List<FacesCar> cars;
	private FacesCar selectedCar;
	private List<String> selectedColors;

	private List<FacesCar> filteredCars;

	List<Car> selectedCars; // cars selected in checkbox column

	@Inject
	private FacesCarService carService;

	@Inject
	private ProductService businessService;

	private List<Product> businessObjects;
	private List<Product> selectedBusinessObjects;

	@Override
	public void doPostConstruct() {
		this.businessObjects = businessService.getObjects();
	}

	protected void initData() {
		List<Product> bizObjects = this.businessService.getObjects();
		if (CommonUtility.isEmpty(bizObjects)) {

		}
	}

	public boolean filterByPrice(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}

		return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
	}

	public int getRandomPrice() {
		return (int) (Math.random() * 100000);
	}

	public boolean filterByColor(Object value, Object filter, Locale locale) {

		if (filter == null || filter.toString().equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}

		if (selectedColors.isEmpty()) {
			return true;
		}

		return selectedColors.contains(value.toString());
	}

	public List<FacesTeamFacade> getTeams() {
		return teams;
	}

	public List<String> getBrands() {
		return carService.getBrands();
	}

	public List<String> getColors() {
		return carService.getColors();
	}

	public List<FacesCar> getCars() {
		return cars;
	}

	public List<FacesCar> getCarsCarousel() {
		return cars.subList(0, 8);
	}

	public List<FacesCar> getFilteredCars() {
		return filteredCars;
	}

	public void setFilteredCars(List<FacesCar> filteredCars) {
		this.filteredCars = filteredCars;
	}

	public List<String> getSelectedColors() {
		return selectedColors;
	}

	public void setSelectedColors(List<String> selectedColors) {
		this.selectedColors = selectedColors;
	}

	public FacesCar getSelectedCar() {
		return selectedCar;
	}

	public void setSelectedCar(FacesCar selectedCar) {
		this.selectedCar = selectedCar;
	}

	public List<Car> getSelectedCars() {
		return selectedCars;
	}

	public void setSelectedCars(List<Car> selectedCars) {
		this.selectedCars = selectedCars;
	}

	public List<Product> getBusinessObjects() {
		return businessObjects;
	}

	public void setBusinessObjects(List<Product> businessObjects) {
		this.businessObjects = businessObjects;
	}

	public List<Product> getSelectedBusinessObjects() {
		return selectedBusinessObjects;
	}

	public void setSelectedBusinessObjects(List<Product> selectedBusinessObjects) {
		this.selectedBusinessObjects = selectedBusinessObjects;
	}
}
