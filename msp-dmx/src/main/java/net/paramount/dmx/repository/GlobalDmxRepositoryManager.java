/**
 * 
 */
package net.paramount.dmx.repository;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.css.entity.config.Configuration;
import net.paramount.css.entity.general.Item;
import net.paramount.css.service.config.ConfigurationService;
import net.paramount.css.service.general.AttachmentService;
import net.paramount.dmx.helper.ResourcesStorageServiceHelper;
import net.paramount.entity.Attachment;
import net.paramount.exceptions.DataLoadingException;
import net.paramount.exceptions.MspDataException;
import net.paramount.framework.component.ComponentBase;
import net.paramount.framework.entity.EntityBase;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.osx.helper.OfficeSuiteServiceProvider;
import net.paramount.osx.model.DataWorkbook;
import net.paramount.osx.model.MarshallingObjects;
import net.paramount.osx.model.OSXConstants;
import net.paramount.osx.model.OsxBucketContainer;

/**
 * @author ducbui
 *
 */
@Component
public class GlobalDmxRepositoryManager extends ComponentBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -759495846609992244L;

	public final static int NUMBER_OF_CATALOGUE_SUBTYPES_GENERATE = 500;
	public final static int NUMBER_TO_GENERATE = 15000;
	public final static String DEFAULT_COUNTRY = "Việt Nam";

	@Inject
	private InentoryItemRepositoryManager itemDmxRepository;

	@Inject
	private ContactRepositoryManager contactDmxRepository;

	@Inject
	protected AttachmentService attachmentService;

	@Inject
	protected ResourcesStorageServiceHelper resourcesStorageServiceHelper;

	@Inject
	protected ConfigurationService configurationService;
	
	@Inject
	protected OfficeSuiteServiceProvider officeSuiteServiceProvider;

	@SuppressWarnings("unchecked")
	public ExecutionContext marshallData(ExecutionContext executionContext) throws DataLoadingException {
		OsxBucketContainer osxBucketContainer = null;
		List<String> databookIdList = null;
		Map<String, List<String>> datasheetIdMap = null;
		String archivedResourceName = null;
		List<String> marshallingObjects = null;
		try {
			if (!executionContext.containKey(OSXConstants.PARAM_MARSHALLING_OBJECTS))
				return executionContext;

			databookIdList = (List<String>)executionContext.get(OSXConstants.PARAM_DATA_BOOK_IDS);
			datasheetIdMap = (Map<String, List<String>>)executionContext.get(OSXConstants.PARAM_DATA_SHEETS_MAP);

			if (Boolean.TRUE.equals(executionContext.get(OSXConstants.PARAM_FROM_ATTACHMENT))) {
				archivedResourceName = (String)executionContext.get(OSXConstants.PARAM_INPUT_RESOURCE_NAME);
				osxBucketContainer = this.marshallDataFromArchived(archivedResourceName, databookIdList, datasheetIdMap);
			} else {
				marshallDataExt(executionContext);
			}
			marshallingObjects = (List)executionContext.get(OSXConstants.PARAM_MARSHALLING_OBJECTS);
			if (null != executionContext.get(OSXConstants.PARAM_MARSHALLED_CONTAINER) && marshallingObjects.contains(MarshallingObjects.LANGUAGES)){
				//Should be a thread
				itemDmxRepository.marshallingBusinessObjects(executionContext);
			}

			if (null != executionContext.get(OSXConstants.PARAM_MARSHALLED_CONTAINER) && marshallingObjects.contains(MarshallingObjects.CONTACTS)){
				//Should be a thread
				contactDmxRepository.marshallingBusinessObjects(executionContext);
			}
		} catch (Exception e) {
			 throw new DataLoadingException (e);
		}
		return executionContext;
	}



	/**
	 * Archive resource data to database unit
	 */
	public void archiveResourceData(final String archivedFileName, final InputStream inputStream, String encryptionKey) throws MspDataException {
		Attachment attachment = null;
		Optional<Attachment> optAttachment = null;
		try {
			optAttachment = this.attachmentService.getByName(archivedFileName);
			if (!optAttachment.isPresent()) {
				attachment = resourcesStorageServiceHelper.buidAttachment(archivedFileName, inputStream, encryptionKey);
				this.attachmentService.save(attachment);
			}
		} catch (Exception e) {
			throw new MspDataException(e);
		}
	}

	public OsxBucketContainer marshallDataFromArchived(String archivedName, List<String> databookIds, Map<String, List<String>> datasheetIds) throws MspDataException {
		Optional<Attachment> optAttachment = this.attachmentService.getByName(archivedName);
		if (!optAttachment.isPresent())
			return null;

		Optional<Configuration> optConfig = null;
		OsxBucketContainer osxBucketContainer = null;
		InputStream inputStream = null;
		ExecutionContext defaultExecutionContext = null;
		try {
			inputStream = CommonUtility.createInputStream(archivedName, optAttachment.get().getData());
			if (null==inputStream)
				return null;

			optConfig = configurationService.getOne(archivedName);
			if (optConfig.isPresent()) {
				defaultExecutionContext = resourcesStorageServiceHelper.buildExecutionContext(optConfig.get(), optAttachment.get().getData());
			}

			defaultExecutionContext.put(OSXConstants.PARAM_DATA_BOOK_IDS, databookIds);
			if (CommonUtility.isNotEmpty(datasheetIds)) {
				defaultExecutionContext.put(OSXConstants.PARAM_DATA_SHEET_IDS, datasheetIds);
			}
			osxBucketContainer = officeSuiteServiceProvider.extractOfficeDataFromZip(defaultExecutionContext);
		} catch (Exception e) {
			 throw new MspDataException(e);
		}
		return osxBucketContainer;
	}

	public ExecutionContext marshallDataExt(ExecutionContext executionContext) throws MspDataException {
		Optional<Configuration> optConfig = null;
		InputStream inputStream;
		OsxBucketContainer osxBucketContainer = null;
		ExecutionContext workingExecutionContext = null;
		byte[] bytesData = null;
		try {
			inputStream = (InputStream)executionContext.get(OSXConstants.PARAM_INPUT_STREAM);
			bytesData = FileCopyUtils.copyToByteArray(inputStream);
			optConfig = configurationService.getOne((String)executionContext.get(OSXConstants.PARAM_CONFIGURATION_ENTRY));
			if (optConfig.isPresent()) {
				workingExecutionContext = resourcesStorageServiceHelper.buildExecutionContext(optConfig.get(), bytesData);
			} else {
				workingExecutionContext = ExecutionContext.builder().build();
			}

			workingExecutionContext.putAll(executionContext);
			osxBucketContainer = officeSuiteServiceProvider.extractOfficeDataFromZip(workingExecutionContext);
			executionContext.put(OSXConstants.PARAM_MARSHALLED_CONTAINER, osxBucketContainer);
		} catch (Exception e) {
			 throw new MspDataException(e);
		}
		return executionContext;
	}

	public List<EntityBase> marshallContacts(String archivedResourceName, String dataWorkbookId, List<String> datasheetIdList) throws MspDataException {
		List<EntityBase> contacts = null;
		DataWorkbook dataWorkbook = null;
		OsxBucketContainer osxBucketContainer = null;
		List<String> databookIdList = null;
		Map<String, List<String>> datasheetIdMap = null;
		try {
			databookIdList = ListUtility.createDataList(dataWorkbookId);
			datasheetIdMap = ListUtility.createMap(dataWorkbookId, datasheetIdList);
			osxBucketContainer = this.marshallDataFromArchived(archivedResourceName, databookIdList, datasheetIdMap);
			if (null != osxBucketContainer && osxBucketContainer.containsKey(dataWorkbookId)){
				dataWorkbook = (DataWorkbook)osxBucketContainer.get(dataWorkbookId);
			}

			contacts = contactDmxRepository.marshallingBusinessObjects(dataWorkbook, datasheetIdList);
		} catch (Exception e) {
			 throw new MspDataException (e);
		}
		return contacts;
	}

	protected List<Item> marshallItems(){
		List<Item> marshalledList = ListUtility.createDataList();
		
		return marshalledList;
	}
}
