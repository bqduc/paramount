/**
 * 
 */
package net.paramount.manager;

import javax.inject.Inject;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import net.paramount.common.ListUtility;
import net.paramount.controller.GlobalWebConstants;
import net.paramount.css.service.config.ConfigurationService;
import net.paramount.dmx.helper.DmxCollaborator;
import net.paramount.dmx.repository.GlobalDmxRepositoryManager;
import net.paramount.framework.component.ComponentBase;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.osx.model.MarshallingObjects;
import net.paramount.osx.model.OSXConstants;

/**
 * @author ducbq
 *
 */
@Component
public class MvpDataManager extends ComponentBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7930754988017731873L;

	@Inject 
	private DmxCollaborator dmxCollaborator;

	@Inject
	private GlobalDmxRepositoryManager globalDmxRepository;

	@Inject 
	private ConfigurationService configurationService;

	public String marshallingData(final Resource resource) {
		ExecutionContext executionContext = null;
		String masterWorkBookId = dmxCollaborator.getConfiguredDataCatalogueWorkbookId();
		try {
			//Query marshaling objects
			executionContext = ExecutionContext.builder().build()
	  			.set(OSXConstants.PARAM_CONFIGURATION_ENTRY, dmxCollaborator.getConfiguredDataLoadingEntry())
	  			.set(OSXConstants.PARAM_MARSHALLING_OBJECTS, ListUtility.createDataList(
	  					MarshallingObjects.INVENTORY_ITEMS))

	  			.set(OSXConstants.PARAM_DATA_BOOK_IDS, ListUtility.createDataList(masterWorkBookId))
	  			.set(OSXConstants.PARAM_DATA_SHEETS_MAP, ListUtility.createMap(
	  						masterWorkBookId, ListUtility.createDataList(dmxCollaborator.getConfiguredDataCatalogueWorksheetIds()))
	  			)
	  			.set(OSXConstants.PARAM_DATA_BOOKS_SHEETS_MAP, ListUtility.createMap(MarshallingObjects.INVENTORY_ITEMS, masterWorkBookId))
	  			.set(OSXConstants.PARAM_INPUT_STREAM, resource.getInputStream())
	  			.set(OSXConstants.PARAM_FROM_ATTACHMENT, Boolean.FALSE)
			
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
		return GlobalWebConstants.ACTION_SUCCESS;
	}
}
