package net.paramount.controller.mvp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;

import com.github.adminfaces.template.exception.BusinessException;

import net.paramount.auth.domain.UserAccountProfile;
import net.paramount.auth.helper.AuxServiceHelper;
import net.paramount.component.helper.ResourcesServicesHelper;
import net.paramount.dmx.helper.ResourcesStorageServiceHelper;
import net.paramount.dmx.repository.GlobalDmxRepositoryManager;
import net.paramount.framework.controller.BaseController;
import net.paramount.msp.faces.model.Entity;

@Named(value="virtualWebProfile")
@ViewScoped
public class VirtualWebProfileController extends BaseController {
		/**
	 * 
	 */
	private static final long serialVersionUID = -4054830380339740986L;
		private List<String> allCities;
    private List<String> allTalks;
    private Entity entity;

    @Inject
    private ResourceLoader resourceLoader;

    @Inject
  	private ApplicationContext applicationContext;

  	@Inject
  	private TaskExecutor asyncExecutor;
    
  	@Inject
  	private GlobalDmxRepositoryManager globalDmxRepository;

  	@Inject
  	private ResourcesServicesHelper resourcesServicesHelper;
  	
  	@Inject
  	private ResourcesStorageServiceHelper resourcesStorageServiceHelper;

  	@Inject
  	private AuxServiceHelper auxServiceHelper;

  	private UserAccountProfile  userAccountProfile;

  	@Override
    public void doPostConstruct() {
        allCities = Arrays.asList("São Paulo", "New York", "Tokyo", "Islamabad", "Chongqing", "Guayaquil", "Porto Alegre", "Hanoi", "Montevideo", "Shijiazhuang", "Guadalajara","Stockholm",
                "Seville", "Moscow", "Glasgow", "Reykjavik", "Lyon", "Barcelona", "Kieve", "Vilnius", "Warsaw", "Budapest", "Prague", "Sofia", "Belgrade");
        allTalks = Arrays.asList("Designing for Modularity with Java 9", "Twelve Ways to Make Code Suck Less", "Confessions of a Java Educator: 10 Java Insights I Wish I’d Had Earlier",
                "Ten Simple Rules for Writing Great Test Cases", "No more mocks, only real tests with Arquillian", "Cloud native Java with JakartaEE and MicroProfile","Jenkins Essentials: an evergreen version of Jenkins",
                "From Java EE to Jakarta EE: a user perspective", "Cloud Native Java with Open J9, Fast, Lean and Definitely Mean", "Service Mesh and Sidecars with Istio and Envoy");
        allCities.sort(Comparator.naturalOrder());
        allTalks.sort(Comparator.naturalOrder());
        
        //Build information of user's profile
        String remoteUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        this.userAccountProfile = this.auxServiceHelper.getUserAccountProfile(remoteUser);
  	}

    public void clear() {
    	System.out.println("Remote user: " + FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
    }

    public void remove() {
        Messages.create("Info").detail("Entity removed successfully.").add();
        clear();
    }

    public void save() {
        BusinessException be = new BusinessException();
        if(entity.getFirstname().trim().length() < 5) {
            be.addException(new BusinessException("Firstname must have at least 5 characters", FacesMessage.SEVERITY_ERROR, "firstname"));
        }
        
        if(entity.getAge() < 18) {
            be.addException(new BusinessException("<b>Age</b> must be greater or equal to <b style=\"color:#fff\">18</b>", FacesMessage.SEVERITY_ERROR, "age"));
        }
        
        be.build(); //will throw exceptions if has any enqueued
        
        Messages.create("Info").detail(String.format("Entity %s successfully.",isNew() ? "created":"updated")).add();
        if(isNew()) {
            entity.setId(new Random(Instant.now().getEpochSecond()).nextLong());
        }
    }

    public List<String> completeTalk(String query) {
        List<String> result = new ArrayList<>();
        allTalks.stream().filter(t -> t.toLowerCase().contains(query.toLowerCase()))
                       .forEach(result::add);
        return result;
    }

    public Boolean isNew() {
        return entity.getId() == null;
    }

    public List<String> getCities() {
        return allCities;
    }

    public void setCities(List<String> cities) {
        this.allCities = cities;
    }

    public List<String> getTalks() {
        return allTalks;
    }

    public void setTalks(List<String> talks) {
        this.allTalks = talks;
    }


}
