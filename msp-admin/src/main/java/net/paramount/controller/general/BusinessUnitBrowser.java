package net.paramount.controller.general;

import java.util.List;
import java.util.Locale;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.paramount.css.service.org.BusinessUnitService;
import net.paramount.entity.general.BusinessUnit;
import net.paramount.framework.controller.BaseController;
import net.paramount.msp.faces.model.FacesCar;
import net.paramount.msp.faces.model.FacesTeamFacade;
import net.paramount.msp.faces.service.FacesCarService;
import net.paramount.msp.model.Car;

/**
 * @author ducbq
 */
@Named(value = "businessUnitBrowser")
@ViewScoped
public class BusinessUnitBrowser extends BaseController {
	/**
	* 
	*/
	private static final long serialVersionUID = -2573073499742929139L;

	private List<FacesTeamFacade> teams;
	private List<FacesCar> cars;
	private FacesCar selectedCar;
	private List<String> selectedColors;

	private List<FacesCar> filteredCars;

	List<Car> selectedCars; // cars selected in checkbox column

	@Inject
	private FacesCarService carService;

	@Inject
	private BusinessUnitService businessService;

	private List<BusinessUnit> businessObjects;
	private List<BusinessUnit> selectedBusinessObjects;

	@Override
	public void doPostConstruct() {
		this.businessObjects = businessService.getObjects();
		System.out.println("Business objects: " + this.businessObjects);
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

	public List<BusinessUnit> getBusinessObjects() {
		return businessObjects;
	}

	public void setBusinessObjects(List<BusinessUnit> businessObjects) {
		this.businessObjects = businessObjects;
	}

	public List<BusinessUnit> getSelectedBusinessObjects() {
		return selectedBusinessObjects;
	}

	public void setSelectedBusinessObjects(List<BusinessUnit> selectedBusinessObjects) {
		this.selectedBusinessObjects = selectedBusinessObjects;
	}
}
