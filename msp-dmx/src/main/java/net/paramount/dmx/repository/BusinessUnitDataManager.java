/**
 * 
 */
package net.paramount.dmx.repository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.css.service.org.BusinessUnitService;
import net.paramount.dmx.helper.DmxCollaborator;
import net.paramount.dmx.helper.DmxConfigurationHelper;
import net.paramount.dmx.repository.base.DmxRepositoryBase;
import net.paramount.entity.config.ConfigurationDetail;
import net.paramount.entity.general.BusinessUnit;
import net.paramount.exceptions.DataLoadingException;
import net.paramount.framework.entity.Entity;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.osx.model.ConfigureUnmarshallObjects;
import net.paramount.osx.model.DataWorkbook;
import net.paramount.osx.model.DataWorksheet;
import net.paramount.osx.model.OSXConstants;
import net.paramount.osx.model.OsxBucketContainer;

/**
 * @author ducbui
 *
 */
@Component
public class BusinessUnitDataManager extends DmxRepositoryBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8410155453232492561L;

	@Inject 
	private DmxCollaborator dmxCollaborator;
	
	@Inject 
	private BusinessUnitService businessService;
	
	@Inject 
	private DmxConfigurationHelper dmxConfigurationHelper;

	private Map<String, Byte> configDetailIndexMap = ListUtility.createMap();

	private Map<String, BusinessUnit> businessObjectsMap = ListUtility.createMap();

	@Override
	protected ExecutionContext doUnmarshallBusinessObjects(ExecutionContext executionContext) throws DataLoadingException {
		DataWorkbook dataWorkbook = null;
		OsxBucketContainer osxBucketContainer = (OsxBucketContainer)executionContext.get(OSXConstants.MARSHALLED_CONTAINER);
		if (CommonUtility.isEmpty(osxBucketContainer))
			throw new DataLoadingException("There is no business unit data in OSX container!");

		String workingDatabookId = dmxCollaborator.getConfiguredDataCatalogueWorkbookId();
		if (osxBucketContainer.containsKey(workingDatabookId)){
			dataWorkbook = (DataWorkbook)osxBucketContainer.get(workingDatabookId);
		}

		unmarshallBusinessObjects(dataWorkbook, ListUtility.createDataList(ConfigureUnmarshallObjects.BUSINESS_UNITS.getDataSheetId()));
		/*
		List<Entity> marshalledObjects = 
		if (CommonUtility.isNotEmpty(marshalledObjects)) {
			for (Entity entityBase :marshalledObjects) {
				businessService.saveOrUpdate((BusinessUnit)entityBase);
			}
		}*/
		return executionContext;
	}

	@Override
	protected List<Entity> doUnmarshallBusinessObjects(DataWorkbook dataWorkbook, List<String> datasheetIds) throws DataLoadingException {
		Map<String, ConfigurationDetail> configDetailMap = null;
		if (CommonUtility.isEmpty(configDetailIndexMap)) {
			configDetailMap = dmxConfigurationHelper.fetchInventoryItemConfig(ConfigureUnmarshallObjects.BUSINESS_UNITS.getConfiguredEntryName());
			for (String key :configDetailMap.keySet()) {
				configDetailIndexMap.put(key, Byte.valueOf(configDetailMap.get(key).getValue()));
			}
		}

		List<Entity> marshallingObjects = ListUtility.createDataList();
		BusinessUnit unmarshalledObject = null;
		DataWorksheet dataWorksheet = dataWorkbook.getDatasheet(ConfigureUnmarshallObjects.BUSINESS_UNITS.getDataSheetId());
		if (CommonUtility.isNotEmpty(dataWorksheet)) {
			for (Integer key :dataWorksheet.getKeys()) {
				try {
					unmarshalledObject = (BusinessUnit)unmarshallBusinessObject(dataWorksheet.getDataRow(key));
				} catch (DataLoadingException e) {
					log.error(e);
				}

				if (null != unmarshalledObject) {
					this.businessService.saveOrUpdate(unmarshalledObject);
					marshallingObjects.add(unmarshalledObject);
					this.businessObjectsMap.put(unmarshalledObject.getCode(), unmarshalledObject);
				}
			}
		}
		return marshallingObjects;
	}

	@Override
	protected Entity doUnmarshallBusinessObject(List<?> marshallingDataRow) throws DataLoadingException {
		BusinessUnit marshalledObject = null;
		BusinessUnit parentObject = null;
		try {
			if (1 > businessService.count("code", marshallingDataRow.get(this.configDetailIndexMap.get("idxCode")))) {
				if (CommonUtility.isNotEmpty(marshallingDataRow.get(this.configDetailIndexMap.get("idxParentCode")))) {
					parentObject = this.businessObjectsMap.get((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxParentCode")));
					if (null==parentObject) {
						parentObject = this.businessService.getOne((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxParentCode")));
					}

					if (null != parentObject && !this.businessObjectsMap.containsKey(parentObject.getCode())) {
						this.businessObjectsMap.put(parentObject.getCode(), parentObject);
					}
				}

				marshalledObject = BusinessUnit.builder()
						.code(String.valueOf(marshallingDataRow.get(this.configDetailIndexMap.get("idxCode"))))
						.name((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxName")))
						.supportLevel((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxSupportLevel")))
						.supportCategory((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxSupportCategory")))
						.managementModel((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxManagementModel")))
						.parent(parentObject)
						.address((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxAddress")))
						.operatingModel((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxOperatingModel")))
						.activityStatus((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxActivityStatus")))
						.organizationalModel((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxOrganizationalModel")))
						.licenseNo((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxLicense")))
						.info((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxInfo")))
						.build();
			}
		} catch (Exception e) {
			log.error(e);
		}

		return marshalledObject;
	}

}