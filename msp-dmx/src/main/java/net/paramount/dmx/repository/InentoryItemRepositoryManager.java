/**
 * 
 */
package net.paramount.dmx.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.css.service.general.CatalogueService;
import net.paramount.css.service.general.MeasureUnitService;
import net.paramount.css.service.org.BusinessUnitService;
import net.paramount.css.service.stock.InventoryItemService;
import net.paramount.css.service.stock.ProductService;
import net.paramount.dmx.helper.DmxCollaborator;
import net.paramount.dmx.helper.DmxConfigurationHelper;
import net.paramount.dmx.repository.base.DmxRepositoryBase;
import net.paramount.entity.config.ConfigurationDetail;
import net.paramount.entity.contact.Contact;
import net.paramount.entity.general.BusinessUnit;
import net.paramount.entity.general.Catalogue;
import net.paramount.entity.general.Item;
import net.paramount.entity.general.MeasureUnit;
import net.paramount.entity.stock.InventoryItem;
import net.paramount.entity.stock.Product;
import net.paramount.exceptions.DataLoadingException;
import net.paramount.exceptions.MspDataException;
import net.paramount.framework.entity.Entity;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.osx.model.ConfigureMarshallObjects;
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
	private BusinessUnitService businessUnitService; 

	@Inject
	private ProductService productService; 

	@Inject
	private MeasureUnitService measureUnitService;

	@Inject 
	private DmxConfigurationHelper dmxConfigurationHelper;

	private Map<String, Byte> configDetailIndexMap = ListUtility.createMap();
	
	private Map<String, Catalogue> catalogueMap = ListUtility.createMap();

	private Map<String, MeasureUnit> measureUnitMap = ListUtility.createMap();

	@Override
	protected ExecutionContext doUnmarshallBusinessObjects(ExecutionContext executionContext) throws DataLoadingException {
		List<String> marshallingObjects = null;
		DataWorkbook dataWorkbook = null;
		OsxBucketContainer osxBucketContainer = null;
		try {
			marshallingObjects = (List<String>)executionContext.get(OSXConstants.MARSHALLING_OBJECTS);
			if (CommonUtility.isEmpty(executionContext.get(OSXConstants.MARSHALLED_CONTAINER)))
				return executionContext;

			String workingDatabookId = dmxCollaborator.getConfiguredDataCatalogueWorkbookId();
			osxBucketContainer = (OsxBucketContainer)executionContext.get(OSXConstants.MARSHALLED_CONTAINER);
			if (CommonUtility.isEmpty(osxBucketContainer))
				throw new DataLoadingException("There is no data in OSX container!");

			if (osxBucketContainer.containsKey(workingDatabookId)){
				dataWorkbook = (DataWorkbook)osxBucketContainer.get(workingDatabookId);
			}

			List<Entity> marshalledObjects = unmarshallBusinessObjects(dataWorkbook, ListUtility.createDataList(workingDatabookId));
			if (CommonUtility.isNotEmpty(marshalledObjects)) {
				for (Entity entityBase :marshalledObjects) {
					inventoryItemService.saveOrUpdate((InventoryItem)entityBase);
				}
			}
		} catch (Exception e) {
			throw new DataLoadingException(e);
		}

		return executionContext;
	}

	@Override
	protected List<Entity> doUnmarshallBusinessObjects(DataWorkbook dataWorkbook, List<String> datasheetIds) throws DataLoadingException {
		Map<String, ConfigurationDetail> configDetailMap = null;
		if (CommonUtility.isEmpty(configDetailIndexMap)) {
			configDetailMap = dmxConfigurationHelper.fetchInventoryItemConfig(ConfigureMarshallObjects.INVENTORY_ITEMS.getConfigName());
			for (String key :configDetailMap.keySet()) {
				configDetailIndexMap.put(key, Byte.valueOf(configDetailMap.get(key).getValue()));
			}
		}

		List<Entity> results = ListUtility.createDataList();
		Product currentBizObject = null;
		DataWorksheet dataWorksheet = dataWorkbook.getDatasheet(ConfigureMarshallObjects.INVENTORY_ITEMS.getName());
		if (CommonUtility.isNotEmpty(dataWorksheet)) {
			System.out.println("Processing sheet: " + dataWorksheet.getId());
			for (Integer key :dataWorksheet.getKeys()) {
				try {
					currentBizObject = (Product)unmarshallBusinessObject(dataWorksheet.getDataRow(key));
				} catch (DataLoadingException e) {
					e.printStackTrace();
				}
				if (null != currentBizObject) {
					results.add(currentBizObject);
				}
			}
		}
		/*if (null != datasheetIds) {
			for (DataWorksheet dataWorksheet :dataWorkbook.datasheets()) {
				if (!datasheetIds.contains(dataWorksheet.getId()))
					continue;

				System.out.println("Processing sheet: " + dataWorksheet.getId());
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
				System.out.println("Processing sheet: " + dataWorksheet.getId());
				for (Integer key :dataWorksheet.getKeys()) {
					try {
						currentContact = (Contact)marshallBusinessObject(dataWorksheet.getDataRow(key));
					} catch (DataLoadingException e) {
						e.printStackTrace();
					}
					results.add(currentContact);
				}
			}
		}*/
		return results;
	}

	@Override
	protected Entity doUnmarshallBusinessObject(List<?> marshallingDataRow) throws DataLoadingException {
		marshallProduct(marshallingDataRow);
		Item masterUsageDirection = null, masterGenericDrug = null;
		InventoryItem marshalledObject = null;
		Catalogue bindingCategory = null;
		String stringValueOfCode = "";
		Object dataObject = null;
		try {
			dataObject = marshallingDataRow.get(this.configDetailIndexMap.get("idxUsageDirectionCode"));
			if (null != dataObject) {
				stringValueOfCode = dataObject.toString();
			}

			masterUsageDirection = this.marshallItem(stringValueOfCode, (String)marshallingDataRow.get(this.configDetailIndexMap.get("idxUsageDirectionName")), 
					null, null);

			dataObject = marshallingDataRow.get(this.configDetailIndexMap.get("idxGenericDrugCode"));
			if (null != dataObject) {
				stringValueOfCode = dataObject.toString();
			}

			masterGenericDrug = this.marshallItem(stringValueOfCode, (String)marshallingDataRow.get(this.configDetailIndexMap.get("idxGenericDrugName")), 
					null, //(String)marshallingDataRow.get(this.configDetailIndexMap.get("idxGenericDrugNameRegistered")), 
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

	protected Entity marshallProduct(List<?> marshallingDataRow) throws DataLoadingException {
		Item usageDirection = null, activeIngredient = null;
		BusinessUnit servicingBusinessUnit = null;
		Product marshalledObject = null;
		Catalogue bindingCategory = null;
		String stringValueOfCode = "";
		Object dataObject = null;
		MeasureUnit measureUnit = null;
		try {
			measureUnit = this.fetchMeasureUnit((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxMeasureUnit")));

			dataObject = marshallingDataRow.get(this.configDetailIndexMap.get("idxUsageDirectionCode"));
			if (null != dataObject) {
				stringValueOfCode = dataObject.toString();
			}
			usageDirection = this.marshallItem(stringValueOfCode, 
					(String)marshallingDataRow.get(this.configDetailIndexMap.get("idxUsageDirectionName")), 
					null, null);


			dataObject = marshallingDataRow.get(this.configDetailIndexMap.get("idxActiveIngredientCode"));
			if (null != dataObject) {
				stringValueOfCode = dataObject.toString();
			}
			activeIngredient = this.marshallItem(stringValueOfCode, 
					(String)marshallingDataRow.get(this.configDetailIndexMap.get("idxActiveIngredientName")), 
					null, //(String)marshallingDataRow.get(DmxConfigurationHelper.idxGenericDrugNameRegistered), 
					null);

			servicingBusinessUnit = fetchServicingBusinessUnit((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxBusinessServicingCode")));

			bindingCategory = this.marshallingCatalogue((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxGroupPath")));

			marshalledObject = Product.builder()
					.code((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxCode")))
					.barcode((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxBarcode")))
					.servicingBusinessUnit(servicingBusinessUnit)
					.category(bindingCategory)
					.name((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxName")))
					.composition((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxComposition"))) //Hàm lượng
					.packaging((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxPackaging")))
					.activeIngredient(activeIngredient)
					.usageDirection(usageDirection)
					.registrationNo((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxRegistrationNo")))
					.governmentDecisionNo((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxDecisionNo")))
					.measureUnit(measureUnit)
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
		String partSep = "\\.";
		String processingInfo = inventoryGroupInfo;
		Catalogue parentCatalogue = null;
		Catalogue currentCatalogue = null;
		Optional<Catalogue> optCatalogue = null;
		while (processingInfo.length() > 0) {
			sepPos = processingInfo.indexOf(groupSep);
			if (sepPos != -1) {
				processingPart = processingInfo.substring(0, sepPos);
			} else {
				processingPart = processingInfo;
			}
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
			if (sepPos < 0) {
				break;
			}
			processingInfo = processingInfo.substring(sepPos+1);
		}
		return (null!=currentCatalogue)?currentCatalogue:parentCatalogue;
	}

	private BusinessUnit fetchServicingBusinessUnit(String code) {
		if (CommonUtility.isEmpty(code))
			return null;

		if (this.businessUnitMap.containsKey(code)) {
			return this.businessUnitMap.get(code);
		}

		BusinessUnit unmarshalledObject = businessUnitService.getOne(code);
		this.businessUnitMap.put(code, unmarshalledObject);
		return unmarshalledObject;
	}

	private MeasureUnit fetchMeasureUnit(String name) throws MspDataException {
		if (this.measureUnitMap.containsKey(name))
			return this.measureUnitMap.get(name);

		Optional<MeasureUnit> optMeasureUnit = this.measureUnitService.getOne(name);
		if (optMeasureUnit.isPresent()) {
			this.measureUnitMap.put(optMeasureUnit.get().getName(), optMeasureUnit.get());
			return optMeasureUnit.get();
		}

		String prefix = "UM"; //unit of measurement
		MeasureUnit fetchedObject = MeasureUnit.builder()
		.code(this.measureUnitService.nextSerial(prefix))
		.name(name)
		.build();

		this.measureUnitService.saveOrUpdate(fetchedObject);
		this.measureUnitMap.put(fetchedObject.getName(), fetchedObject);
		
		return fetchedObject;
	}
}
