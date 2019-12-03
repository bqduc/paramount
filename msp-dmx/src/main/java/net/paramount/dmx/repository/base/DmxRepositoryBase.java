/**
 * 
 */
package net.paramount.dmx.repository.base;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import net.paramount.common.CommonConstants;
import net.paramount.common.CommonUtility;
import net.paramount.common.GUUISequenceGenerator;
import net.paramount.common.ListUtility;
import net.paramount.css.entity.general.Item;
import net.paramount.css.entity.org.BusinessUnit;
import net.paramount.css.service.config.ConfigurationService;
import net.paramount.css.service.config.ItemService;
import net.paramount.css.service.org.BusinessUnitService;
import net.paramount.dmx.helper.ResourcesStorageServiceHelper;
import net.paramount.embeddable.Phone;
import net.paramount.exceptions.DataLoadingException;
import net.paramount.framework.component.ComponentBase;
import net.paramount.framework.entity.BizObjectBase;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SequenceType;
import net.paramount.osx.helper.OfficeSuiteServiceProvider;
import net.paramount.osx.helper.OfficeSuiteServicesHelper;
import net.paramount.osx.model.DataWorkbook;

/**
 * @author ducbui
 *
 */
public abstract class DmxRepositoryBase extends ComponentBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5074736014633924681L;

	public final static int IDX_BUSINESS_DIVISION_NAME = 2;
	public final static int IDX_BUSINESS_UNIT_CODE = 3;
	public final static int IDX_BUSINESS_UNIT_NAME = 4;
	public final static int IDX_GENDER = 6;
	public final static int IDX_STATUS = 7;
	public final static int IDX_JOB_CODE = 8;
	public final static int IDX_JOB_NAME = 9;
	public final static int IDX_PHONE_PRIORITY = 11;
	public final static int IDX_PHONE_OFFICE = 12;
	public final static int IDX_PHONE_HOME = 13;
	public final static int IDX_PHONE_MOBILE = 14;
	public final static int IDX_FAX = 19;
	public final static int IDX_PHONE_OTHER = 20;
	public final static int IDX_EMAIL_WORK = 21;
	public final static int IDX_EMAIL_PERSONAL = 23;
	public final static int NUMBER_OF_CATALOGUE_SUBTYPES_GENERATE = 500;
	public final static int NUMBER_TO_GENERATE = 15000;
	public final static String DEFAULT_COUNTRY = "Việt Nam";

	@Inject
	protected OfficeSuiteServiceProvider officeSuiteServiceProvider;

	@Inject
	protected OfficeSuiteServicesHelper officeSuiteServicesHelper;
	
	@Inject
	protected BusinessUnitService businessUnitService;

	@Inject
	protected ConfigurationService configurationService;
	
	@Inject
	protected ItemService itemService;

	@Inject
	protected ResourcesStorageServiceHelper resourcesStorageServiceHelper;

	protected Map<String, BusinessUnit> businessUnitMap = ListUtility.createMap();

	protected Map<String, Item> itemMap = ListUtility.createMap();

	protected BusinessUnit getBusinessUnit(List<?> contactDataRow) {
		if (this.businessUnitMap.containsKey(contactDataRow.get(IDX_BUSINESS_UNIT_CODE))) {
			return this.businessUnitMap.get(contactDataRow.get(IDX_BUSINESS_UNIT_CODE));
		}

		BusinessUnit businessUnit = this.businessUnitService.getOne((String)contactDataRow.get(IDX_BUSINESS_UNIT_CODE));
		if (null != businessUnit) {
			this.businessUnitMap.put(businessUnit.getCode(), businessUnit);
			return businessUnit;
		}

		SearchParameter searchParameter = SearchParameter.builder()
				.pageable(PageRequest.of(CommonConstants.DEFAULT_PAGE_BEGIN, CommonConstants.DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "id"))
				.build()
				.put("name", (String)contactDataRow.get(IDX_BUSINESS_UNIT_NAME));
		Page<BusinessUnit> fetchedObjects = this.businessUnitService.getObjects(searchParameter);
		if (fetchedObjects.hasContent()) {
			businessUnit = fetchedObjects.getContent().get(0);
			this.businessUnitMap.put(businessUnit.getCode(), businessUnit);
			return businessUnit;
		}
		
		BusinessUnit businessDivision = getBusinessDivision(contactDataRow);
		businessUnit = BusinessUnit.builder()
				.parent(businessDivision)
				.code((String)contactDataRow.get(IDX_BUSINESS_UNIT_CODE))
				.name((String)contactDataRow.get(IDX_BUSINESS_UNIT_NAME))
				.nameLocal((String)contactDataRow.get(IDX_BUSINESS_UNIT_NAME))
				.build();

		this.businessUnitService.save(businessUnit);
		this.businessUnitMap.put(businessUnit.getCode(), businessUnit);
		return businessUnit;
	}

	protected BusinessUnit getBusinessDivision(List<?> contactDataRow) {
		if (CommonUtility.isEmpty(contactDataRow.get(IDX_BUSINESS_DIVISION_NAME))) 
			return null;

		BusinessUnit businessDivision = null;
		for (BusinessUnit businessUnit :this.businessUnitMap.values()) {
			if (businessUnit.getName().equals(contactDataRow.get(IDX_BUSINESS_DIVISION_NAME))) {
				return businessUnit;
			}
		}

		SearchParameter searchParameter = SearchParameter.builder()
				.pageable(PageRequest.of(CommonConstants.DEFAULT_PAGE_BEGIN, CommonConstants.DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "id"))
				.build()
				.put("name", (String)contactDataRow.get(IDX_BUSINESS_DIVISION_NAME));
		Page<BusinessUnit> fetchedObjects = this.businessUnitService.getObjects(searchParameter);
		if (fetchedObjects.hasContent()) {
			businessDivision = fetchedObjects.getContent().get(0);
			this.businessUnitMap.put(businessDivision.getCode(), businessDivision);
			return businessDivision;
		}

		String guuId = GUUISequenceGenerator.getInstance().nextGUUIdString(SequenceType.BUSINESS_DIVISION.getType());
		businessDivision = BusinessUnit.builder()
				.code(guuId)
				.name((String)contactDataRow.get(IDX_BUSINESS_DIVISION_NAME))
				.nameLocal((String)contactDataRow.get(IDX_BUSINESS_DIVISION_NAME))
				.build();
		this.businessUnitService.save(businessDivision);
		this.businessUnitMap.put(businessDivision.getCode(), businessDivision);
		return businessDivision;
	}

	protected Item parseJobInfo(List<?> contactDataRow) {
		return marshallItem((String)contactDataRow.get(IDX_JOB_CODE), (String)contactDataRow.get(IDX_JOB_NAME));
	}

	protected Item marshallItem(String code, String name) {
		if (CommonUtility.isNotEmpty(code) && itemMap.containsKey(code))
			return itemMap.get(code);

		for (Item object :this.itemMap.values()) {
			if (object.getName().equals(name)) {
				return object;
			}
		}

		Item fetchedObject = null;
		SearchParameter searchParameter = SearchParameter.builder()
				.pageable(PageRequest.of(CommonConstants.DEFAULT_PAGE_BEGIN, CommonConstants.DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "id"))
				.build()
				.put("name", name);
		Page<Item> fetchedObjects = this.itemService.getObjects(searchParameter);
		if (fetchedObjects.hasContent()) {
			fetchedObject = fetchedObjects.getContent().get(0);
			this.itemMap.put(fetchedObject.getCode(), fetchedObject);
			return fetchedObject;
		}

		fetchedObject = Item.builder()
				.code(code)
				.name(name)
				.build();
		this.itemService.save(fetchedObject);
		this.itemMap.put(fetchedObject.getCode(), fetchedObject);
		return fetchedObject;
	}

	protected Phone parsePhone(List<?> contactDataRow) {
		Phone phone = new Phone();
		phone.setMobile((String)contactDataRow.get(IDX_PHONE_MOBILE));
		phone.setOffice((String)contactDataRow.get(IDX_PHONE_OFFICE));
		phone.setHome((String)contactDataRow.get(IDX_PHONE_HOME));
		return phone;
	}

	public List<?> marshallingBusinessObjects(DataWorkbook dataWorkbook, List<String> datasheetIds) throws DataLoadingException {
		return doMarshallingBusinessObjects(dataWorkbook, datasheetIds);
	}

	protected List<?> doMarshallingBusinessObjects(DataWorkbook dataWorkbook, List<String> datasheetIds) throws DataLoadingException {
		throw new DataLoadingException("Not implemented yet");
	}

	protected BizObjectBase marshallBusinessObject(List<?> marshallingDataRow) throws DataLoadingException {
		throw new DataLoadingException("Not implemented yet");
	}

	protected ExecutionContext doMarshallingBusinessObjects(ExecutionContext executionContext) throws DataLoadingException {
		throw new DataLoadingException("Not implemented yet");
	}

	public ExecutionContext marshallingBusinessObjects(ExecutionContext executionContext) throws DataLoadingException {
		return doMarshallingBusinessObjects(executionContext);
	}
}
