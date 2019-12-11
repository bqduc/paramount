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
import net.paramount.css.entity.config.ConfigurationDetail;
import net.paramount.css.entity.contact.Contact;
import net.paramount.css.entity.general.MeasureUnit;
import net.paramount.css.service.contact.ContactService;
import net.paramount.css.service.general.MeasureUnitService;
import net.paramount.dmx.helper.DmxCollaborator;
import net.paramount.dmx.helper.DmxConfigurationHelper;
import net.paramount.dmx.repository.base.DmxRepositoryBase;
import net.paramount.exceptions.DataLoadingException;
import net.paramount.framework.entity.Entity;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.osx.model.DataWorkbook;
import net.paramount.osx.model.DataWorksheet;
import net.paramount.osx.model.OSXConstants;
import net.paramount.osx.model.OsxBucketContainer;

/**
 * @author ducbui
 *
 */
@Component
public class MeasureUnitRepositoryManager extends DmxRepositoryBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5094772767804079070L;

	@Inject 
	private DmxCollaborator dmxCollaborator;
	
	@Inject 
	private ContactService contactService;

	@Inject 
	private MeasureUnitService measureUnitService;
	
	@Inject 
	private DmxConfigurationHelper dmxConfigurationHelper;

	private Map<String, Byte> configDetailIndexMap = ListUtility.createMap();

	@Override
	protected ExecutionContext doMarshallingBusinessObjects(ExecutionContext executionContext) throws DataLoadingException {
		DataWorkbook dataWorkbook = null;
		OsxBucketContainer osxBucketContainer = (OsxBucketContainer)executionContext.get(OSXConstants.PARAM_MARSHALLED_CONTAINER);
		if (CommonUtility.isEmpty(osxBucketContainer))
			throw new DataLoadingException("There is no data in OSX container!");

		if (osxBucketContainer.containsKey(dmxCollaborator.getConfiguredContactWorkbookId())){
			dataWorkbook = (DataWorkbook)osxBucketContainer.get(dmxCollaborator.getConfiguredContactWorkbookId());
		}

		List<Entity> marshalledObjects = marshallingBusinessObjects(dataWorkbook, ListUtility.createDataList(dmxCollaborator.getConfiguredContactWorksheetIds()));
		if (CommonUtility.isNotEmpty(marshalledObjects)) {
			for (Entity entityBase :marshalledObjects) {
				contactService.save((Contact)entityBase);
			}
		}
		return executionContext;
	}

	@Override
	protected List<Entity> doMarshallingBusinessObjects(DataWorkbook dataWorkbook, List<String> datasheetIds) throws DataLoadingException {
		Map<String, ConfigurationDetail> configDetailMap = null;
		if (CommonUtility.isEmpty(configDetailIndexMap)) {
			configDetailMap = dmxConfigurationHelper.fetchInventoryItemConfig("load-measure-units");
			for (String key :configDetailMap.keySet()) {
				configDetailIndexMap.put(key, Byte.valueOf(configDetailMap.get(key).getValue()));
			}
		}

		List<Entity> marshallingObjects = ListUtility.createDataList();
		MeasureUnit marshallingObject = null;
		if (null != datasheetIds) {
			for (DataWorksheet dataWorksheet :dataWorkbook.datasheets()) {
				if (!datasheetIds.contains(dataWorksheet.getId()))
					continue;

				System.out.println("==================" + dataWorksheet.getId() + "==================");
				for (Integer key :dataWorksheet.getKeys()) {
					try {
						marshallingObject = (MeasureUnit)marshallBusinessObject(dataWorksheet.getDataRow(key));
					} catch (DataLoadingException e) {
						log.error(e);
					}
					if (null != marshallingObject) {
						marshallingObjects.add(marshallingObject);
					}
				}
			}
		} else {
			for (DataWorksheet dataWorksheet :dataWorkbook.datasheets()) {
				System.out.println("==================" + dataWorksheet.getId() + "==================");
				for (Integer key :dataWorksheet.getKeys()) {
					try {
						marshallingObject = (MeasureUnit)marshallBusinessObject(dataWorksheet.getDataRow(key));
					} catch (DataLoadingException e) {
						log.error(e);
					}
					if (null != marshallingObject) {
						marshallingObjects.add(marshallingObject);
					}
				}
			}
		}

		return marshallingObjects;
	}

	@Override
	protected Entity doMarshallBusinessObject(List<?> marshallingDataRow) throws DataLoadingException {
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