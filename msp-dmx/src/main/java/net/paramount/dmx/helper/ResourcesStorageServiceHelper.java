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
import java.util.Set;

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
import net.paramount.framework.model.DefaultExecutionContext;
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

	public DefaultExecutionContext buildDefaultDataExecutionContext() throws ResourcesException {
		DefaultExecutionContext executionContext = DefaultExecutionContext.builder().build();

		String defaultContactsData = "Vietbank_14.000.xlsx", defaultCataloguesData = "data-catalog.xlsx";
		File zipFile = resourcesServicesHelper.loadClasspathResourceFile("data/marshall/develop_data.zip");
		Map<String, String> secretKeyMap = ListUtility.createMap(defaultContactsData, "thanhcong");
		Map<String, List<String>> sheetIdMap = ListUtility.createMap();
		sheetIdMap.put(defaultContactsData, ListUtility.arraysAsList(new String[] {"File Tổng hợp", "Các trưởng phó phòng", "9"}));

		executionContext.put(OSXConstants.PARAM_COMPRESSED_FILE, zipFile);
		executionContext.put(OSXConstants.PARAM_ENCRYPTION_KEY, secretKeyMap);
		executionContext.put(OSXConstants.PARAM_ZIP_ENTRY, ListUtility.arraysAsList(new String[] {defaultContactsData, defaultCataloguesData}));
		executionContext.put(OSXConstants.PARAM_EXCEL_MARSHALLING_TYPE, OfficeMarshalType.STREAMING);
		executionContext.put(OSXConstants.PARAM_DATA_SHEET_IDS, sheetIdMap);
		return executionContext;
	}

	public void archiveResourceData(final DefaultExecutionContext executionContextParams) throws MspDataException {
		File zipFile = null;
		Attachment attachment = null;
		Optional<Attachment> attachmentChecker = null;
		Configuration archivedConfig = null;
		Set<ConfigurationDetail> configDetails = ListUtility.newHashSet();
		Map<String, String> secretKeyMap = null;
		try {
			if (!executionContextParams.containKey(OSXConstants.PARAM_MASTER_FILE))
				throw new MspDataException("There is no archiving file!");

			zipFile = (File) executionContextParams.get(OSXConstants.PARAM_MASTER_FILE);
			attachmentChecker = this.attachmentService.getByName(zipFile.getPath());
			if (!attachmentChecker.isPresent()) {
				attachment = this.buidAttachment(zipFile.getName(), new FileInputStream(zipFile), (String)executionContextParams.get(OSXConstants.PARAM_MASTER_FILE_ENCRYPTION_KEY));
				this.attachmentService.save(attachment);
				//Build configuration & dependencies accordingly
				archivedConfig = Configuration.builder()
						.name(zipFile.getName())
						.build();

				secretKeyMap = (Map)executionContextParams.get(OSXConstants.PARAM_ENCRYPTION_KEY);
				for (String key :secretKeyMap.keySet()) {
					configDetails.add(ConfigurationDetail.builder()
							.name(key)
							.value(secretKeyMap.get(key))
							.build())
					;
				}
				this.configurationService.save(archivedConfig);
			}
		} catch (Exception e) {
			throw new MspDataException(e);
		}
	}

	/*
	public OsxBucketContainer readOfficeDataInZip(final DefaultExecutionContext executionContextParams) throws EcosysException {
		OsxBucketContainer bucketContainer = OsxBucketContainer.instance();
		File zipFile = null;
		Map<String, InputStream> zipInputStreams = null;
		Map<String, Object> processingParameters = ListUtility.createMap();
		OfficeDocumentType officeDocumentType = OfficeDocumentType.INVALID;
		DataWorkbook workbookContainer = null;
		InputStream zipInputStream = null;
		Map<String, List<String>> sheetIdsMap = null;
		List<String> worksheetIds = null;
		Map<String, String> passwordMap = null;
		try {
			zipFile = (File) executionContextParams.get(OSXConstants.PARAM_COMPRESSED_FILE);
			zipInputStreams = CommonUtility.extractZipInputStreams(zipFile, (List<String>) executionContextParams.get(OSXConstants.PARAM_ZIP_ENTRY));
			if (zipInputStreams.isEmpty()) {
				return bucketContainer;
			}

			passwordMap = (Map) executionContextParams.get(OSXConstants.PARAM_ENCRYPTION_KEY);
			sheetIdsMap = (Map) executionContextParams.get(OSXConstants.PARAM_DATA_SHEET_IDS);
			for (String zipEntry : zipInputStreams.keySet()) {
				zipInputStream = zipInputStreams.get(zipEntry);
				officeDocumentType = detectOfficeDocumentType(zipInputStream);
				if (!OfficeDocumentType.isExcelDocument(officeDocumentType)) {
					continue;
				}

				worksheetIds = (List<String>) sheetIdsMap.get(zipEntry);
				processingParameters.putAll(executionContextParams.getContext());
				processingParameters.remove(OSXConstants.PARAM_COMPRESSED_FILE);
				processingParameters.put(OSXConstants.PARAM_INPUT_STREAM, zipInputStream);
				processingParameters.put(OSXConstants.PARAM_DATA_SHEET_IDS, worksheetIds);
				processingParameters.put(OSXConstants.PARAM_ENCRYPTION_KEY, (String) passwordMap.get(zipEntry));
				workbookContainer = readExcelFile(processingParameters);
				if (null != workbookContainer) {
					bucketContainer.put(zipEntry, workbookContainer);
				}
			}
		} catch (Exception e) {
			throw new EcosysException(e);
		}
		return bucketContainer;
	}
	*/
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
