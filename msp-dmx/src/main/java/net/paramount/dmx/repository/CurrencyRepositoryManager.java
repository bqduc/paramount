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
import net.paramount.css.service.general.MeasureUnitService;
import net.paramount.dmx.helper.DmxCollaborator;
import net.paramount.dmx.helper.DmxConfigurationHelper;
import net.paramount.dmx.repository.base.DmxRepositoryBase;
import net.paramount.entity.config.ConfigurationDetail;
import net.paramount.entity.general.MeasureUnit;
import net.paramount.exceptions.DataLoadingException;
import net.paramount.framework.entity.Entity;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.osx.model.ConfigureMarshallObjects;
import net.paramount.osx.model.DataWorkbook;
import net.paramount.osx.model.DataWorksheet;
import net.paramount.osx.model.MarshallingObjects;
import net.paramount.osx.model.OSXConstants;
import net.paramount.osx.model.OsxBucketContainer;

/**
 * @author ducbui
 *
 */
@Component
public class CurrencyRepositoryManager extends DmxRepositoryBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4238256116048008136L;

	@Inject 
	private DmxCollaborator dmxCollaborator;
	
	@Inject 
	private MeasureUnitService measureUnitService;
	
	@Inject 
	private DmxConfigurationHelper dmxConfigurationHelper;

	private Map<String, Byte> configDetailIndexMap = ListUtility.createMap();

	@Override
	protected ExecutionContext doUnmarshallBusinessObjects(ExecutionContext executionContext) throws DataLoadingException {
		DataWorkbook dataWorkbook = null;
		OsxBucketContainer osxBucketContainer = (OsxBucketContainer)executionContext.get(OSXConstants.MARSHALLED_CONTAINER);
		if (CommonUtility.isEmpty(osxBucketContainer))
			throw new DataLoadingException("There is no measure unit data in OSX container!");

		String workingDatabookId = dmxCollaborator.getConfiguredDataCatalogueWorkbookId();
		if (osxBucketContainer.containsKey(workingDatabookId)){
			dataWorkbook = (DataWorkbook)osxBucketContainer.get(workingDatabookId);
		}

		List<Entity> marshalledObjects = unmarshallBusinessObjects(dataWorkbook, ListUtility.createDataList(MarshallingObjects.MEASURE_UNITS.getName()));
		if (CommonUtility.isNotEmpty(marshalledObjects)) {
			for (Entity entityBase :marshalledObjects) {
				measureUnitService.saveOrUpdate((MeasureUnit)entityBase);
			}
		}
		return executionContext;
	}

	@Override
	protected List<Entity> doUnmarshallBusinessObjects(DataWorkbook dataWorkbook, List<String> datasheetIds) throws DataLoadingException {
		Map<String, ConfigurationDetail> configDetailMap = null;
		if (CommonUtility.isEmpty(configDetailIndexMap)) {
			configDetailMap = dmxConfigurationHelper.fetchInventoryItemConfig(ConfigureMarshallObjects.MEASURE_UNITS.getConfigName());
			for (String key :configDetailMap.keySet()) {
				configDetailIndexMap.put(key, Byte.valueOf(configDetailMap.get(key).getValue()));
			}
		}

		List<Entity> marshallingObjects = ListUtility.createDataList();
		MeasureUnit marshallingObject = null;
		DataWorksheet dataWorksheet = dataWorkbook.getDatasheet(ConfigureMarshallObjects.MEASURE_UNITS.getName());
		if (CommonUtility.isNotEmpty(dataWorksheet)) {
			for (Integer key :dataWorksheet.getKeys()) {
				try {
					marshallingObject = (MeasureUnit)unmarshallBusinessObject(dataWorksheet.getDataRow(key));
				} catch (DataLoadingException e) {
					log.error(e);
				}
				if (null != marshallingObject) {
					marshallingObjects.add(marshallingObject);
				}
			}
		}
		return marshallingObjects;
	}

	@Override
	protected Entity doUnmarshallBusinessObject(List<?> marshallingDataRow) throws DataLoadingException {
		MeasureUnit marshalledObject = null;
		try {
			if (1 > measureUnitService.count("code", marshallingDataRow.get(this.configDetailIndexMap.get("idxCode")))) {
				marshalledObject = MeasureUnit.builder()
						.code((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxCode")))
						.name((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxName")))
						.nameLocal((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxNameLocal")))
						.info((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxInfo")))
						.build();
			}
		} catch (Exception e) {
			log.error(e);
		}

		return marshalledObject;
	}

}