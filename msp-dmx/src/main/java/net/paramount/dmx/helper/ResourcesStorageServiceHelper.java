/**
 * 
 */
package net.paramount.dmx.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Component;

import net.paramount.common.CommonConstants;
import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.common.MimeTypes;
import net.paramount.common.SimpleEncryptionEngine;
import net.paramount.component.helper.ResourcesServicesHelper;
import net.paramount.css.entity.config.Configuration;
import net.paramount.css.entity.config.ConfigurationDetail;
import net.paramount.css.service.config.ConfigurationService;
import net.paramount.css.service.general.AttachmentService;
import net.paramount.entity.Attachment;
import net.paramount.exceptions.MspDataException;
import net.paramount.exceptions.MspRuntimeException;
import net.paramount.exceptions.ResourcesException;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.osx.model.OSXConstants;
import net.paramount.osx.model.OfficeMarshalType;

/**
 * @author ducbq
 *
 */
@Component
public class ResourcesStorageServiceHelper {
	@Inject
	private ResourcesServicesHelper resourcesServicesHelper;
	
	@Inject
	private AttachmentService attachmentService;
	
	@Inject
	private ConfigurationService configurationService;

	public ExecutionContext buildExecutionContext(Configuration config, byte[] dataBytes) throws ResourcesException {
		if (null == config) {
			return null;
		}

		ExecutionContext executionContext = ExecutionContext.builder().build();

		String masterFileName = config.getValue();
		Map<String, String> secretKeyMap = ListUtility.createMap();
		for (ConfigurationDetail configDetail :config.getConfigurationDetails()) {
			if (OSXConstants.PARAM_ENCRYPTION_KEY.equalsIgnoreCase(configDetail.getValueExtended())) {
				secretKeyMap.put(configDetail.getName(), SimpleEncryptionEngine.decode(configDetail.getValue()));
			}
		}

		executionContext.put(OSXConstants.PARAM_MASTER_BUFFER, dataBytes);
		executionContext.put(OSXConstants.PARAM_MASTER_FILE_NAME, masterFileName);
		executionContext.put(OSXConstants.PARAM_ENCRYPTION_KEY, secretKeyMap);
		executionContext.put(OSXConstants.PARAM_EXCEL_MARSHALLING_TYPE, OfficeMarshalType.STREAMING);
		return executionContext;
	}

	public ExecutionContext buildDefaultDataExecutionContext() throws ResourcesException {
		ExecutionContext executionContext = ExecutionContext.builder().build();

		String defaultContactsData = "Vietbank_14.000.xlsx", defaultCataloguesData = "data-catalog.xlsx";
		//File zipFile = resourcesServicesHelper.loadClasspathResourceFile("data/marshall/develop_data.zip");
		Map<String, String> secretKeyMap = ListUtility.createMap(defaultContactsData, "thanhcong");
		Map<String, List<String>> sheetIdMap = ListUtility.createMap();
		sheetIdMap.put(defaultContactsData, ListUtility.arraysAsList(new String[] {"File Tổng hợp", "Các trưởng phó phòng", "9"}));

		executionContext.put(OSXConstants.PARAM_MASTER_BUFFER, resourcesServicesHelper.loadClasspathResourceBytes("data/marshall/develop_data.zip"));
		executionContext.put(OSXConstants.PARAM_MASTER_FILE_NAME, "data/marshall/develop_data.zip");
		executionContext.put(OSXConstants.PARAM_ENCRYPTION_KEY, secretKeyMap);
		executionContext.put(OSXConstants.PARAM_ZIP_ENTRY, ListUtility.arraysAsList(new String[] {defaultContactsData, defaultCataloguesData}));
		executionContext.put(OSXConstants.PARAM_EXCEL_MARSHALLING_TYPE, OfficeMarshalType.STREAMING);
		executionContext.put(OSXConstants.PARAM_DATA_SHEET_IDS, sheetIdMap);
		return executionContext;
	}

	public void archiveResourceData(final ExecutionContext executionContextParams) throws MspDataException {
		Attachment attachment = null;
		Optional<Attachment> attachmentChecker = null;
		Configuration archivedConfig = null;
		Map<String, String> secretKeyMap = null;
		byte[] masterDataBuffer = null;
		String masterDataFileName = null;
		try {
			if (!(executionContextParams.containKey(OSXConstants.PARAM_MASTER_BUFFER) || executionContextParams.containKey(OSXConstants.PARAM_MASTER_FILE_NAME)))
				throw new MspDataException("There is no archiving file!");

			masterDataBuffer = (byte[]) executionContextParams.get(OSXConstants.PARAM_MASTER_BUFFER);
			masterDataFileName = (String)executionContextParams.get(OSXConstants.PARAM_MASTER_FILE_NAME);
			attachmentChecker = this.attachmentService.getByName(masterDataFileName);
			if (!attachmentChecker.isPresent()) {
				attachment = this.buidAttachment(masterDataFileName, masterDataBuffer, (String)executionContextParams.get(OSXConstants.PARAM_MASTER_FILE_ENCRYPTION_KEY));
				this.attachmentService.save(attachment);
				//Build configuration & dependencies accordingly
				archivedConfig = Configuration.builder()
						.name(masterDataFileName)
						.value(masterDataFileName)
						.build();

				secretKeyMap = (Map)executionContextParams.get(OSXConstants.PARAM_ENCRYPTION_KEY);
				for (String key :secretKeyMap.keySet()) {
					archivedConfig.addConfigurationDetail(ConfigurationDetail.builder()
							.name(OSXConstants.PARAM_ENCRYPTION_KEY)
							.value(SimpleEncryptionEngine.encode(secretKeyMap.get(key)))
							.valueExtended(key)
							.build())
					;
				}
				this.configurationService.save(archivedConfig);
			}
		} catch (Exception e) {
			throw new MspDataException(e);
		}
	}

	public static Attachment buidAttachment(final File file) throws MspRuntimeException {
		Attachment attachment = null;
		int lastDot = file.getName().lastIndexOf(CommonConstants.FILE_EXTENSION_SEPARATOR);
		String fileExtension = file.getName().substring(lastDot+1);
		try {
			attachment = Attachment.builder()
					.name(file.getName())
					.data(IOUtils.toByteArray(new FileInputStream(file)))
					.mimetype(MimeTypes.getMimeType(fileExtension))
					.build();
		} catch (IOException e) {
			throw new MspRuntimeException(e);
		}
		return attachment;
	}

	public Attachment buidAttachment(final String fileName, final InputStream inputStream, String encryptionKey) throws MspRuntimeException {
		Attachment attachment = null;
		int lastDot = fileName.lastIndexOf(CommonConstants.FILE_EXTENSION_SEPARATOR);
		String fileExtension = fileName.substring(lastDot+1);
		String procEncyptionKey = null;
		try {
			if (CommonUtility.isNotEmpty(encryptionKey))
				procEncyptionKey = SimpleEncryptionEngine.encode(encryptionKey);

			attachment = Attachment.builder()
					.name(fileName)
					.data(IOUtils.toByteArray(inputStream))
					.mimetype(MimeTypes.getMimeType(fileExtension))
					.encryptionKey(procEncyptionKey)
					.build();
		} catch (IOException e) {
			throw new MspRuntimeException(e);
		}
		return attachment;
	}

	public Attachment buidAttachment(final String fileName, final byte[] bytes, String encryptionKey) throws MspRuntimeException {
		Attachment attachment = null;
		int lastDot = fileName.lastIndexOf(CommonConstants.FILE_EXTENSION_SEPARATOR);
		String fileExtension = fileName.substring(lastDot+1);
		String procEncyptionKey = null;
		try {
			if (CommonUtility.isNotEmpty(encryptionKey))
				procEncyptionKey = SimpleEncryptionEngine.encode(encryptionKey);

			attachment = Attachment.builder()
					.name(fileName)
					.data(bytes)
					.mimetype(MimeTypes.getMimeType(fileExtension))
					.encryptionKey(procEncyptionKey)
					.build();
		} catch (Exception e) {
			throw new MspRuntimeException(e);
		}
		return attachment;
	}

	public static InputStream buidInputStreamFromAttachment(final Attachment attachment) throws MspRuntimeException {
		InputStream inputStream = null;
		try {
			inputStream = CommonUtility.createInputStream(attachment.getName(), attachment.getData());
		} catch (Exception e) {
			throw new MspRuntimeException(e);
		}
		return inputStream;
	}	
}
