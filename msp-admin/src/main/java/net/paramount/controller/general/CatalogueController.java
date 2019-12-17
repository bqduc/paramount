package net.paramount.controller.general;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.FileCopyUtils;

import com.github.adminfaces.template.exception.BusinessException;

import net.paramount.component.helper.ResourcesServicesHelper;
import net.paramount.css.service.general.CatalogueService;
import net.paramount.dmx.repository.GlobalDmxRepositoryManager;
import net.paramount.entity.general.Catalogue;
import net.paramount.framework.async.Asynchronous;
import net.paramount.framework.controller.BaseController;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.msp.async.AsyncExtendedDataLoader;
import net.paramount.msp.faces.model.Entity;

@Named(value = "catalogueController")
@ViewScoped
public class CatalogueController extends BaseController {
	/**
	* 
	*/
	private static final long serialVersionUID = 7164206790864479558L;

	private List<String> allCities;
	private List<String> allTalks;
	private Entity entity;
	
	private Long bizObjectId;

	private List<Catalogue> listOfEntities;

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
	private CatalogueService businessService;

	@Override
  public void doPostConstruct() {
		allCities = Arrays.asList("São Paulo", "New York", "Tokyo", "Islamabad", "Chongqing", "Guayaquil", "Porto Alegre", "Hanoi",
				"Montevideo", "Shijiazhuang", "Guadalajara", "Stockholm", "Seville", "Moscow", "Glasgow", "Reykjavik", "Lyon", "Barcelona", "Kieve",
				"Vilnius", "Warsaw", "Budapest", "Prague", "Sofia", "Belgrade");
		allTalks = Arrays.asList("Designing for Modularity with Java 9", "Twelve Ways to Make Code Suck Less",
				"Confessions of a Java Educator: 10 Java Insights I Wish I’d Had Earlier", "Ten Simple Rules for Writing Great Test Cases",
				"No more mocks, only real tests with Arquillian", "Cloud native Java with JakartaEE and MicroProfile",
				"Jenkins Essentials: an evergreen version of Jenkins", "From Java EE to Jakarta EE: a user perspective",
				"Cloud Native Java with Open J9, Fast, Lean and Definitely Mean", "Service Mesh and Sidecars with Istio and Envoy");
		allCities.sort(Comparator.naturalOrder());
		allTalks.sort(Comparator.naturalOrder());
		entity = new Entity();

		listOfEntities = businessService.getObjects();
		System.out.println("Business entities: " + listOfEntities);
	}

	public void clear() {
		entity = new Entity();
		archiveData();
		// loadResourceData();
		// loadingAsyncData();
	}

	public void remove() {
		Messages.create("Info").detail("Entity removed successfully.").add();
		clear();
	}

	public void save() {
		BusinessException be = new BusinessException();
		if (entity.getFirstname().trim().length() < 5) {
			be.addException(new BusinessException("Firstname must have at least 5 characters", FacesMessage.SEVERITY_ERROR, "firstname"));
		}

		if (entity.getAge() < 18) {
			be.addException(new BusinessException("<b>Age</b> must be greater or equal to <b style=\"color:#fff\">18</b>",
					FacesMessage.SEVERITY_ERROR, "age"));
		}

		be.build(); // will throw exceptions if has any enqueued

		Messages.create("Info").detail(String.format("Entity %s successfully.", isNew() ? "created" : "updated")).add();
		if (isNew()) {
			entity.setId(new Random(Instant.now().getEpochSecond()).nextLong());
		}
	}

	public List<String> completeTalk(String query) {
		List<String> result = new ArrayList<>();
		allTalks.stream().filter(t -> t.toLowerCase().contains(query.toLowerCase())).forEach(result::add);
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

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Long getBizObjectId() {
		return bizObjectId;
	}

	public void setBizObjectId(Long bizObjectId) {
		this.bizObjectId = bizObjectId;
	}

	protected void loadResourceData() {
		try {
			Resource resource = this.resourceLoader.getResource("classpath:/data/marshall/develop_data.zip");
			InputStream inputStream = resource.getInputStream();

			byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
			// String data = new String(bdata, StandardCharsets.UTF_8);
			System.out.println(bdata);
		} catch (IOException e) {
			e.printStackTrace();
			// LOGGER.error("IOException", e);
		}
	}

	protected void loadingAsyncData() {
		ExecutionContext executionContext = null;
		Asynchronous asyncExtendedDataLoader = null;
		try {
			executionContext = ExecutionContext.builder().build();
			executionContext.context("AA", "xx").context("DD", "ss");

			asyncExtendedDataLoader = applicationContext.getBean(AsyncExtendedDataLoader.class, executionContext);
			this.asyncExecutor.execute(asyncExtendedDataLoader);
		} catch (Exception e) {
			// log.error(e.getMessage());
		}
	}

	public void archiveData() {
	}

  public List<Catalogue> getBusinessObjects() {
    return this.listOfEntities;
  }
}
