/**
 * 
 */
package net.paramount.dmx.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.paramount.common.CommonConstants;
import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.css.entity.config.ConfigurationDetail;
import net.paramount.css.entity.contact.Contact;
import net.paramount.css.entity.general.Catalogue;
import net.paramount.css.entity.general.Item;
import net.paramount.css.entity.stock.InventoryItem;
import net.paramount.css.service.general.CatalogueService;
import net.paramount.css.service.stock.InventoryItemService;
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
public class InentoryItemRepositoryManager extends DmxRepositoryBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4990550616110685770L;

	@Inject
	private CatalogueService catalogueService;
	
	@Inject 
	private DmxCollaborator dmxCollaborator;

	@Inject
	private InventoryItemService inventoryItemService; 
	
	@Inject 
	private DmxConfigurationHelper dmxConfigurationHelper;

	private Map<String, Byte> configDetailIndexMap = ListUtility.createMap();
	
	private Map<String, Catalogue> catalogueMap = ListUtility.createMap();

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
				inventoryItemService.saveOrUpdate((InventoryItem)entityBase);
			}
		}
		return executionContext;
	}

	@Override
	protected List<Entity> doMarshallingBusinessObjects(DataWorkbook dataWorkbook, List<String> datasheetIds) throws DataLoadingException {
		Map<String, ConfigurationDetail> configDetailMap = null;
		if (CommonUtility.isEmpty(configDetailIndexMap)) {
			configDetailMap = dmxConfigurationHelper.fetchInventoryItemConfig("load-inventory-items");
			for (String key :configDetailMap.keySet()) {
				configDetailIndexMap.put(key, Byte.valueOf(configDetailMap.get(key).getValue()));
			}
		}

		List<Entity> results = ListUtility.createDataList();
		Contact currentContact = null;
		if (null != datasheetIds) {
			for (DataWorksheet dataWorksheet :dataWorkbook.datasheets()) {
				if (!datasheetIds.contains(dataWorksheet.getId()))
					continue;

				System.out.println("==================" + dataWorksheet.getId() + "==================");
				for (Integer key :dataWorksheet.getKeys()) {
					try {
						currentContact = (Contact)marshallBusinessObject(dataWorksheet.getDataRow(key));
					} catch (DataLoadingException e) {
						e.printStackTrace();
					}
					if (null != currentContact) {
						results.add(currentContact);
					}
				}
			}
		} else {
			for (DataWorksheet dataWorksheet :dataWorkbook.datasheets()) {
				System.out.println("==================" + dataWorksheet.getId() + "==================");
				for (Integer key :dataWorksheet.getKeys()) {
					try {
						currentContact = (Contact)marshallBusinessObject(dataWorksheet.getDataRow(key));
					} catch (DataLoadingException e) {
						e.printStackTrace();
					}
					results.add(currentContact);
				}
			}
		}
		return results;
	}

	@Override
	protected Entity doMarshallBusinessObject(List<?> marshallingDataRow) throws DataLoadingException {
		String fullName = (String)marshallingDataRow.get(1);
		int lastStringSpacePos = fullName.lastIndexOf(CommonConstants.STRING_SPACE);

		Item masterUsageDirection = null, masterGenericDrug = null;
		InventoryItem marshalledObject = null;
		Catalogue bindingCategory = null;
		try {
			masterUsageDirection = this.marshallItem((String)marshallingDataRow.get(DmxConfigurationHelper.idxUsageDirectionCode), 
					(String)marshallingDataRow.get(DmxConfigurationHelper.idxUsageDirectionName), 
					null, null);

			masterGenericDrug = this.marshallItem((String)marshallingDataRow.get(DmxConfigurationHelper.idxGenericDrugCode), 
					(String)marshallingDataRow.get(DmxConfigurationHelper.idxGenericDrugName), 
					(String)marshallingDataRow.get(DmxConfigurationHelper.idxGenericDrugNameRegistered), 
					null);

			bindingCategory = this.marshallingCatalogue((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxGroupPath")));
	
			marshalledObject = InventoryItem.builder()
					.masterCategory(bindingCategory)
					.code((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxCode")))
					.barcode((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxBarcode")))
					.name((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxName")))
					.composition((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxComposition"))) //Hàm lượng
					.packaging((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxPackaging")))
					.masterGenericDrug(masterGenericDrug)
					.masterUsageDirection(masterUsageDirection)
					.build();
		} catch (Exception e) {
			log.error(e);
		}

		return marshalledObject;
	}

	/**
	 * Example data 11.TUOI SONG>111.RAU CU, QUA>11111.RAU AN LA
	 */
	private Catalogue marshallingCatalogue(String inventoryGroupInfo) {
		int sepPos;
		String processingPart = null;
		String [] parts = null;
		String groupSep = ">";
		String partSep = ".";
		String processingInfo = inventoryGroupInfo;
		Catalogue parentCatalogue = null;
		Catalogue currentCatalogue = null;
		Optional<Catalogue> optCatalogue = null;
		while (processingInfo.length() > 0 && processingInfo.contains(groupSep)) {
			sepPos = processingInfo.indexOf(groupSep);
			processingPart = processingInfo.substring(0, sepPos);
			parts = processingPart.split(partSep);
			optCatalogue = this.catalogueService.getByCode(parts[0]);
			if (!optCatalogue.isPresent()) {
				currentCatalogue = Catalogue.builder()
						.parent(parentCatalogue)
						.code(parts[0])
						.name(parts[1])
						.build();
				parentCatalogue = this.catalogueService.saveOrUpdate(currentCatalogue);
				catalogueMap.put(parts[0], currentCatalogue);
			} else {
				parentCatalogue = optCatalogue.get();
				catalogueMap.put(optCatalogue.get().getCode(), optCatalogue.get());
			}
		}
		return currentCatalogue;
	}
}
