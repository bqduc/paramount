package net.paramount.controller.paux;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.paramount.auth.service.UserAuthenticationService;
import net.paramount.common.CommonUtility;
import net.paramount.css.service.contact.ContactService;
import net.paramount.dmx.repository.ContactRepositoryManager;
import net.paramount.entity.contact.Contact;
import net.paramount.framework.controller.BaseController;
import net.paramount.msp.faces.model.FacesCar;
import net.paramount.msp.faces.model.FacesTeamFacade;
import net.paramount.msp.faces.model.Stats;
import net.paramount.msp.faces.service.FacesCarService;
import net.paramount.msp.model.Car;

/**
 * @author ducbq
 */
@Named(value="userAccountBrowse")
@ViewScoped
public class UserAccountBrowseController extends BaseController {
		/**
	 * 
	 */
	private static final long serialVersionUID = -2825754765498119385L;

		private List<FacesTeamFacade> teams;
    private List<FacesCar> cars;
    private FacesCar selectedCar;
    private List<String> selectedColors;

    private List<FacesCar> filteredCars;

    List<Car> selectedCars; //cars selected in checkbox column

    @Inject
    private UserAuthenticationService userAuthenticationService;
    
    @Inject
    private FacesCarService carService;

    @Inject
    private ContactService contactService;

    @Inject
    private ContactRepositoryManager globalDmxRepository;

    private List<Contact> businessObjects;
    private List<Contact> selectedBusinessObjects;

    @Override
    public void doPostConstruct() {
    	this.businessObjects = contactService.getObjects();
    	if (this.businessObjects.isEmpty()) {
    		this.businessObjects = globalDmxRepository.generateFakeContactProfiles();
    		contactService.saveObjects(businessObjects);
    	}
    	//System.out.println("Business objects: " + this.businessObjects);
        teams = new ArrayList<FacesTeamFacade>();
        selectedColors = new ArrayList<>();
        FacesTeamFacade lakers = new FacesTeamFacade("Los Angeles Lakers");
        lakers.getStats().add(new Stats("2005-2006", 50, 32));
        lakers.getStats().add(new Stats("2006-2007", 44, 38));
        lakers.getStats().add(new Stats("2007-2008", 40, 42));
        lakers.getStats().add(new Stats("2008-2009", 45, 37));
        lakers.getStats().add(new Stats("2009-2010", 48, 34));
        lakers.getStats().add(new Stats("2010-2011", 42, 42));
        teams.add(lakers);

        FacesTeamFacade celtics = new FacesTeamFacade("Boston Celtics");
        celtics.getStats().add(new Stats("2005-2006", 46, 36));
        celtics.getStats().add(new Stats("2006-2007", 50, 32));
        celtics.getStats().add(new Stats("2007-2008", 41, 41));
        celtics.getStats().add(new Stats("2008-2009", 45, 37));
        celtics.getStats().add(new Stats("2009-2010", 38, 44));
        celtics.getStats().add(new Stats("2010-2011", 35, 47));
        teams.add(celtics);

        cars = carService.createCars(230);
    }

    protected void initData() {
    	List<Contact> bizObjects = this.contactService.getObjects();
    	if (CommonUtility.isEmpty(bizObjects)) {
    		
    	}
    }

    public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }

        if(value == null) {
            return false;
        }

        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
    }

    public int getRandomPrice() {
        return (int) (Math.random() * 100000);
    }

    public boolean filterByColor(Object value, Object filter, Locale locale) {

        if(filter == null || filter.toString().equals("")) {
            return true;
        }

        if(value == null) {
            return false;
        }

        if(selectedColors.isEmpty()) {
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
        return cars.subList(0,8);
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

		public List<Contact> getBusinessObjects() {
			return businessObjects;
		}

		public void setBusinessObjects(List<Contact> businessObjects) {
			this.businessObjects = businessObjects;
		}

		public List<Contact> getSelectedBusinessObjects() {
			return selectedBusinessObjects;
		}

		public void setSelectedBusinessObjects(List<Contact> selectedBusinessObjects) {
			this.selectedBusinessObjects = selectedBusinessObjects;
		}
}
