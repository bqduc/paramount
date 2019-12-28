package net.paramount.controller.mvp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;

import com.github.adminfaces.template.exception.BusinessException;

import net.paramount.common.CommonConstants;
import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.common.SimpleEncryptionEngine;
import net.paramount.component.helper.ResourcesServicesHelper;
import net.paramount.controller.GlobalWebConstants;
import net.paramount.css.service.config.ConfigurationService;
import net.paramount.dmx.helper.DmxCollaborator;
import net.paramount.dmx.helper.ResourcesStorageServiceHelper;
import net.paramount.dmx.repository.GlobalDmxRepositoryManager;
import net.paramount.entity.config.Configuration;
import net.paramount.entity.config.ConfigurationDetail;
import net.paramount.framework.async.Asynchronous;
import net.paramount.framework.controller.BaseController;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.msp.async.AsyncExtendedDataLoader;
import net.paramount.msp.faces.model.Entity;
import net.paramount.msp.i18n.CustomResourceBundle;
import net.paramount.osx.model.DataWorkbook;
import net.paramount.osx.model.DataWorksheet;
import net.paramount.osx.model.MarshallingObjects;
import net.paramount.osx.model.OSXConstants;
import net.paramount.osx.model.OsxBucketContainer;

@Named(value="virtualSimulator")
@ViewScoped
public class VirtualSimulatorPageController extends BaseController {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4917742028352793276L;

		private List<String> allCities;
    private List<String> allTalks;
    private Entity entity;

    @Inject
    private ResourceLoader resourceLoader;

    @Inject
  	private ApplicationContext applicationContext;

  	@Inject
  	@Qualifier("taskExecutor")
  	private TaskExecutor taskExecutor;
    
  	@Inject
  	private GlobalDmxRepositoryManager globalDmxRepository;

  	@Inject
  	private ResourcesServicesHelper resourcesServicesHelper;
  	
  	@Inject
  	private ResourcesStorageServiceHelper resourcesStorageServiceHelper;
  	
  	@Inject
  	private CustomResourceBundle customResourceBundle;

  	@Inject 
  	private DmxCollaborator dmxCollaborator;

  	@Inject
  	private ConfigurationService configurationService;

  	@Override
    public void doPostConstruct() {
        allCities = Arrays.asList("São Paulo", "New York", "Tokyo", "Islamabad", "Chongqing", "Guayaquil", "Porto Alegre", "Hanoi", "Montevideo", "Shijiazhuang", "Guadalajara","Stockholm",
                "Seville", "Moscow", "Glasgow", "Reykjavik", "Lyon", "Barcelona", "Kieve", "Vilnius", "Warsaw", "Budapest", "Prague", "Sofia", "Belgrade");
        allTalks = Arrays.asList("Designing for Modularity with Java 9", "Twelve Ways to Make Code Suck Less", "Confessions of a Java Educator: 10 Java Insights I Wish I’d Had Earlier",
                "Ten Simple Rules for Writing Great Test Cases", "No more mocks, only real tests with Arquillian", "Cloud native Java with JakartaEE and MicroProfile","Jenkins Essentials: an evergreen version of Jenkins",
                "From Java EE to Jakarta EE: a user perspective", "Cloud Native Java with Open J9, Fast, Lean and Definitely Mean", "Service Mesh and Sidecars with Istio and Envoy");
        allCities.sort(Comparator.naturalOrder());
        allTalks.sort(Comparator.naturalOrder());
        entity = new Entity();
    }

  	public String configureMasterData() {
  		return GlobalWebConstants.ACTION_SUCCESS;
  	}

    public void unmarshallingData() {
      System.out.println("Remote user: " + FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
      this.marshallContacts();
    }

    public void marshallingData() {
      System.out.println("Remote user: " + FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
      //this.marshallContacts();
      this.marshallMasterData();
    }

    public void clear() {
        entity = new Entity();
        System.out.println("Remote user: " + FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        this.marshallContacts();
        //loadResourceData();
        //loadingAsyncData();
    }

    public void persistArchivedData() {
    	try {
        archiveData("data/marshall/develop_data.zip");
			} catch (Exception e) {
				log.error(e);
			}
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

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    protected void loadResourceData() {
      try
      {
      	/*DataWorkbook dataWorkbook = globalDmxRepository.marshallDataFromArchived("data/marshall/develop_data.zip", "Vietbank_14.000.xlsx", "thanhcong");
      	System.out.println(dataWorkbook);
      	Resource resource = this.resourceLoader.getResource("classpath:/data/marshall/develop_data.zip");
        InputStream inputStream = resource.getInputStream();

        byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
          //String data = new String(bdata, StandardCharsets.UTF_8);
        System.out.println(bdata);*/
      } 
      catch (Exception e) 
      {
      	e.printStackTrace();
          //LOGGER.error("IOException", e);
      }
    }

  	protected void loadingAsyncData() {
  		ExecutionContext executionContext = null;
  		Asynchronous asyncExtendedDataLoader = null;
  		try {
  			executionContext = ExecutionContext.builder().build();
  			executionContext.context("AA", "xx").context("DD", "ss");

  			asyncExtendedDataLoader = applicationContext.getBean(AsyncExtendedDataLoader.class, executionContext);
  			this.taskExecutor.execute(asyncExtendedDataLoader);
  		} catch (Exception e) {
  			//log.error(e.getMessage());
  		}
  	}

  	private void archiveData(final String resourceName) {
  		try {
  			ExecutionContext executionContext = resourcesStorageServiceHelper.buildDefaultDataExecutionContext();
  			resourcesStorageServiceHelper.archiveResourceData(executionContext);
  			//resourceFile = resourcesServicesHelper.loadClasspathResourceStream(resourceName);
				//globalDmxRepository.archiveResourceData(resourceName, resourceFile, "");
			} catch (Exception e) {
				log.error(e);
			}
  	}

  	protected void marshallContacts() {
  		DataWorksheet dataWorksheet = null;
  		ExecutionContext executionContext = null;
  		String contactWorkBookId = dmxCollaborator.getConfiguredContactWorkbookId(), itemWorkbookId = dmxCollaborator.getConfiguredDataCatalogueWorkbookId();
  		try {
  			Resource resource = this.resourceLoader.getResource("classpath:" + dmxCollaborator.getConfiguredDataPackage());

  			executionContext = ExecutionContext.builder().build()
  	  			.set(OSXConstants.CONFIGURATION_ENTRY, dmxCollaborator.getConfiguredDataLoadingEntry())
  	  			.set(OSXConstants.MARSHALLING_OBJECTS, ListUtility.createDataList(
  	  					MarshallingObjects.CONTACTS, 
  	  					MarshallingObjects.ITEMS, 
  	  					MarshallingObjects.LOCALIZED_ITEMS, 
  	  					MarshallingObjects.LANGUAGES))

  	  			.set(OSXConstants.PROCESSING_DATABOOK_IDS, ListUtility.createDataList(contactWorkBookId, itemWorkbookId))
  	  			.set(OSXConstants.MAPPING_DATABOOKS_DATASHEETS, ListUtility.createMap(
  	  					contactWorkBookId, ListUtility.createDataList(dmxCollaborator.getConfiguredContactWorksheetIds()), 
  	  					itemWorkbookId, ListUtility.createDataList(dmxCollaborator.getConfiguredDataCatalogueWorksheetIds())))
  	  			.set(OSXConstants.DATABOOKS_DATASHEETS_MAP, ListUtility.createMap(
  	  					MarshallingObjects.CONTACTS, contactWorkBookId, 
  	  					MarshallingObjects.ITEMS, itemWorkbookId, 
  	  					MarshallingObjects.LANGUAGES, itemWorkbookId,
  	  					MarshallingObjects.LOCALIZED_ITEMS, itemWorkbookId))

  	  			//.set(OSXConstants.PARAM_INPUT_RESOURCE_NAME, "data/marshall/develop_data.zip")
  	  			//.set(OSXConstants.PARAM_FROM_ATTACHMENT, Boolean.TRUE)

  	  			.set(OSXConstants.INPUT_STREAM, resource.getInputStream())
  	  			.set(OSXConstants.FROM_ATTACHMENT, Boolean.FALSE)
  			
  			;
  			globalDmxRepository.marshallData(executionContext);

  			/*List<Contact> contacts = globalDmxRepository.marshallContacts(resourceName, "Vietbank_14.000.xlsx", ListUtility.createDataList("File Tổng hợp"));
  			System.out.println(contacts);*/
  			/*dataWorkbook = extractContacts(resourceName, "Vietbank_14.000.xlsx");
				System.out.println(dataWorkbook); 
				for (Object sheetId :dataWorkbook.getKeys()) {
					dataWorksheet = dataWorkbook.getWorksheet(sheetId);
					System.out.println(dataWorksheet);
				}*/
			} catch (Exception e) {
				log.error(e);
			}
  	}

  	protected DataWorkbook extractContacts(String archivedResourceName, String dataWorkbookId){
			DataWorkbook dataWorkbook = null;
  		try {
  			OsxBucketContainer osxBucketContainer = globalDmxRepository.marshallDataFromArchivedInAttachment(archivedResourceName, ListUtility.createDataList(dataWorkbookId), null);
				if (null != osxBucketContainer){
					dataWorkbook = (DataWorkbook)osxBucketContainer.get(dataWorkbookId);
				}
			} catch (Exception e) {
				log.error(e);
			}
  		return dataWorkbook;
  	}

  	protected void marshallMasterData() {
  		ExecutionContext executionContext = null;
  		List<String> marshallingObjects = null;
  		try {
  			Optional<Configuration> optConfigDataWorkbooks = configurationService.getOne("dataWokbooks");
  			if (!optConfigDataWorkbooks.isPresent()) {
  				log.info("There is no configuration of data workbooks!");
  				return;
  			}

  			Resource resource = this.resourceLoader.getResource("classpath:" + optConfigDataWorkbooks.get().getValueExtended());

  			List<String> marshallingDatasheetIds = null;
  			String[] parts = null;
  			String[] databookIds = null;

  			if (CommonUtility.isNotEmpty(optConfigDataWorkbooks.get().getValue())) {
    			databookIds = optConfigDataWorkbooks.get().getValue().split(CommonConstants.SEPARATOR_PIPELINE);
  			}

  			Map<String, String> encryptionKeyMap = ListUtility.createMap();
  			Map<String, List<?>> databooksDatasheetsMap = ListUtility.createMap();
  			marshallingObjects = ListUtility.createDataList();
  			for (String databookId :databookIds) {
  				for (ConfigurationDetail configDetail :optConfigDataWorkbooks.get().getConfigurationDetails()) {
  					if (databookId.equalsIgnoreCase(configDetail.getName())) {
  						parts = configDetail.getValue().split(CommonConstants.SEPARATOR_PIPELINE);
  						marshallingDatasheetIds = ListUtility.createDataList(parts);
  						databooksDatasheetsMap.put(databookId, marshallingDatasheetIds);
  						marshallingObjects.addAll(marshallingDatasheetIds);
  						if (CommonUtility.isNotEmpty(configDetail.getValueExtended())) {
    						encryptionKeyMap.put(databookId, SimpleEncryptionEngine.decode(configDetail.getValueExtended()));
  						}
  					}
  				}
  			}

  			executionContext = ExecutionContext.builder().build()
  					.set(OSXConstants.MASTER_ARCHIVED_FILE_NAME, optConfigDataWorkbooks.get().getValueExtended()) // For creating working data file
  	  			.set(OSXConstants.INPUT_STREAM, resource.getInputStream())
  	  			.set(OSXConstants.FROM_ATTACHMENT, Boolean.FALSE)
  	  			.set(OSXConstants.ENCRYPTION_KEYS, encryptionKeyMap)

  	  			.set(OSXConstants.CONFIGURATION_ENTRY, dmxCollaborator.getConfiguredDataLoadingEntry())
  	  			.set(OSXConstants.MARSHALLING_OBJECTS, marshallingObjects)

  	  			.set(OSXConstants.PROCESSING_DATABOOK_IDS, ListUtility.createDataList(databookIds))
  	  			.set(OSXConstants.MAPPING_DATABOOKS_DATASHEETS, databooksDatasheetsMap)

  	  			/*.set(OSXConstants.MAPPING_DATABOOKS_DATASHEETS, ListUtility.createMap(
  	  					contactWorkBookId, ListUtility.createDataList(dmxCollaborator.getConfiguredContactWorksheetIds()), 
  	  					itemWorkbookId, marshallingObjects))
  	  			.set(OSXConstants.DATABOOKS_DATASHEETS_MAP, ListUtility.createMap(
  	  					MarshallingObjects.CONTACTS, contactWorkBookId, 
  	  					MarshallingObjects.ITEMS, itemWorkbookId, 
  	  					MarshallingObjects.LANGUAGES, itemWorkbookId,
  	  					MarshallingObjects.LOCALIZED_ITEMS, itemWorkbookId, 
  	  					MarshallingObjects.INVENTORY_ITEMS, itemWorkbookId, 
  	  					MarshallingObjects.MEASURE_UNITS, itemWorkbookId))*/

  	  			//.set(OSXConstants.PARAM_INPUT_RESOURCE_NAME, "data/marshall/develop_data.zip")
  	  			//.set(OSXConstants.PARAM_FROM_ATTACHMENT, Boolean.TRUE)

  			
  			;
  			globalDmxRepository.marshallData(executionContext);

  			/*List<Contact> contacts = globalDmxRepository.marshallContacts(resourceName, "Vietbank_14.000.xlsx", ListUtility.createDataList("File Tổng hợp"));
  			System.out.println(contacts);*/
  			/*dataWorkbook = extractContacts(resourceName, "Vietbank_14.000.xlsx");
				System.out.println(dataWorkbook); 
				for (Object sheetId :dataWorkbook.getKeys()) {
					dataWorksheet = dataWorkbook.getWorksheet(sheetId);
					System.out.println(dataWorksheet);
				}*/
			} catch (Exception e) {
				log.error(e);
			}
  	}

}
