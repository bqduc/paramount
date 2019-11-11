package net.paramount.controller.contacts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.paramount.common.CommonUtility;
import net.paramount.crs.entity.base.ContactClass;
import net.paramount.crs.service.ContactService;
import net.paramount.dmx.repository.GlobalDmxRepository;
import net.paramount.msp.faces.model.FacesCar;
import net.paramount.msp.faces.model.Stats;
import net.paramount.msp.faces.model.Team;
import net.paramount.msp.faces.service.FacesCarService;

/**
 * Created by rmpestano on 18/01/17.
 */
@Named
@ViewScoped
public class ContactBrowse implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 265858846501330811L;

		private List<Team> teams;
    private List<FacesCar> cars;
    private FacesCar selectedCar;
    private List<String> selectedColors;

    private List<FacesCar> filteredCars;

    @Inject
    private FacesCarService carService;

    @Inject
    private ContactService contactService;

    @Inject
    private GlobalDmxRepository globalDmxRepository;

    @PostConstruct
    public void init() {
    	
    	System.out.println(globalDmxRepository.generateFakeOfficeData());
        teams = new ArrayList<Team>();
        selectedColors = new ArrayList<>();
        Team lakers = new Team("Los Angeles Lakers");
        lakers.getStats().add(new Stats("2005-2006", 50, 32));
        lakers.getStats().add(new Stats("2006-2007", 44, 38));
        lakers.getStats().add(new Stats("2007-2008", 40, 42));
        lakers.getStats().add(new Stats("2008-2009", 45, 37));
        lakers.getStats().add(new Stats("2009-2010", 48, 34));
        lakers.getStats().add(new Stats("2010-2011", 42, 42));
        teams.add(lakers);

        Team celtics = new Team("Boston Celtics");
        celtics.getStats().add(new Stats("2005-2006", 46, 36));
        celtics.getStats().add(new Stats("2006-2007", 50, 32));
        celtics.getStats().add(new Stats("2007-2008", 41, 41));
        celtics.getStats().add(new Stats("2008-2009", 45, 37));
        celtics.getStats().add(new Stats("2009-2010", 38, 44));
        celtics.getStats().add(new Stats("2010-2011", 35, 47));
        teams.add(celtics);

        cars = carService.createCars(30);
    }

    protected void initData() {
    	List<ContactClass> bizObjects = this.contactService.getObjects();
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


    public List<Team> getTeams() {
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
}
