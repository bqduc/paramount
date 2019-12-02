/**
 * 
 */
package net.paramount.osx.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.stereotype.Component;

import lombok.Builder;
import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.exceptions.EcosysException;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.osx.model.OsxBucketContainer;
import net.paramount.osx.model.DataWorkbook;
import net.paramount.osx.model.DataWorksheet;
import net.paramount.osx.model.OSXConstants;
import net.paramount.osx.model.OfficeDocumentType;
import net.paramount.osx.model.OfficeMarshalType;

/**
 * @author bqduc
 *
 */
@Component
@Builder
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OfficeSuiteServiceProvider {
	protected OfficeDocumentType detectOfficeDocumentType(InputStream inputStream) throws EcosysException {
		OfficeDocumentType excelSheetType = OfficeDocumentType.INVALID;
		InputStream checkInputStream = null;
		checkInputStream = FileMagic.prepareToCheckMagic(inputStream);
		try {
			if (FileMagic.OOXML.equals(FileMagic.valueOf(checkInputStream))) {
				excelSheetType = OfficeDocumentType.XSSF_WORKBOOK;
			} else {
				excelSheetType = OfficeDocumentType.HSSF_WORKBOOK;
			}
		} catch (IOException e) {
			throw new EcosysException(e);
		}
		return excelSheetType;
	}

	protected DataWorkbook readXlsxByEventHandler(final Map<?, ?> params) throws EcosysException {
		return XSSFEventDataHelper.instance(params).readXlsx();
	}

	protected DataWorkbook readXlsxByStreaming(final Map<?, ?> params) throws EcosysException {
		return OfficeStreamingReaderHealper.builder().build().readXlsx(params);
	}

	public DataWorkbook readExcelFile(final Map<?, ?> parameters) throws EcosysException {
		DataWorkbook workbookContainer = null;
		OfficeMarshalType officeMarshalType = OfficeMarshalType.STREAMING;
		if (parameters.containsKey(OSXConstants.PARAM_EXCEL_MARSHALLING_TYPE)) {
			officeMarshalType = (OfficeMarshalType) parameters.get(OSXConstants.PARAM_EXCEL_MARSHALLING_TYPE);
		}

		if (OfficeMarshalType.EVENT_HANDLER.equals(officeMarshalType)) {
			workbookContainer = this.readXlsxByEventHandler(parameters);
		} else if (OfficeMarshalType.STREAMING.equals(officeMarshalType)) {
			workbookContainer = this.readXlsxByStreaming(parameters);
		} else {
			// Not implemented yet
		}
		return workbookContainer;
	}

	public OsxBucketContainer readOfficeDataInZip(final ExecutionContext executionContextParams) throws EcosysException {
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

	public OsxBucketContainer extractOfficeDataFromZip(final ExecutionContext executionContextParams) throws EcosysException {
		OsxBucketContainer bucketContainer = OsxBucketContainer.instance();
		File zipFile = null;
		Map<String, InputStream> zipInputStreams = null;
		Map<String, Object> processingParameters = ListUtility.createMap();
		OfficeDocumentType officeDocumentType = OfficeDocumentType.INVALID;
		DataWorkbook workbookContainer = null;
		InputStream zipInputStream = null;
		Map<String, List<String>> sheetIdsMap = null;
		List<String> workbookIds = null;
		Map<String, String> passwordMap = null;
		try {
			if (executionContextParams.containKey(OSXConstants.PARAM_MASTER_BUFFER) && executionContextParams.containKey(OSXConstants.PARAM_MASTER_FILE_NAME)) {
				zipFile = CommonUtility.createDataFile((String)executionContextParams.get(OSXConstants.PARAM_MASTER_FILE_NAME), (byte[])executionContextParams.get(OSXConstants.PARAM_MASTER_BUFFER));
			}

			if (null==zipFile) {
				return bucketContainer;
			}

			if (executionContextParams.containKey(OSXConstants.PARAM_ENCRYPTION_KEY)) {
				passwordMap = (Map) executionContextParams.get(OSXConstants.PARAM_ENCRYPTION_KEY);
			}

			if (executionContextParams.containKey(OSXConstants.PARAM_DATA_SHEET_IDS)) {
				sheetIdsMap = (Map) executionContextParams.get(OSXConstants.PARAM_DATA_SHEET_IDS);
			} else if (executionContextParams.containKey(OSXConstants.PARAM_DATA_SHEETS_MAP)) {
				sheetIdsMap = (Map) executionContextParams.get(OSXConstants.PARAM_DATA_SHEETS_MAP);
			}

			if (executionContextParams.containKey(OSXConstants.PARAM_DATA_BOOK_IDS)) {
				workbookIds = (List<String>)executionContextParams.get(OSXConstants.PARAM_DATA_BOOK_IDS);
			}
			zipInputStreams = CommonUtility.extractZipInputStreams(zipFile, workbookIds);
			
			for (String zipEntry : zipInputStreams.keySet()) {
				zipInputStream = zipInputStreams.get(zipEntry);
				officeDocumentType = detectOfficeDocumentType(zipInputStream);
				if (!OfficeDocumentType.isExcelDocument(officeDocumentType)) {
					continue;
				}

				if (null != sheetIdsMap) {
					processingParameters.put(OSXConstants.PARAM_DATA_SHEET_IDS, sheetIdsMap.get(zipEntry));
				}
				processingParameters.putAll(executionContextParams.getContext());
				processingParameters.remove(OSXConstants.PARAM_COMPRESSED_FILE);
				processingParameters.put(OSXConstants.PARAM_INPUT_STREAM, zipInputStream);
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

	public static void main(String[] args) throws Exception {
		String zipFileName = "D:\\git\\paramount\\msp-osx\\src\\main\\resources\\data\\develop_data.zip";
		OsxBucketContainer bucketContainer = OfficeSuiteServicesHelper.builder().build().loadDefaultZipConfiguredData(new File(zipFileName));
		DataWorkbook workbookContainer = null;
		Set<Object> keys = bucketContainer.getKeys();
		for (Object key : keys) {
			workbookContainer = (DataWorkbook) bucketContainer.get(key);
			System.out.println("############################### " + key + " ###############################");
			displayWorkbookContainer(workbookContainer);
		}

		if (null != keys)
			return;
	}

	protected static void displayWorkbookContainer(DataWorkbook workbookContainer) {
		for (DataWorksheet worksheetContainer : workbookContainer.datasheets()) {
			System.out.println("Sheet: " + worksheetContainer.getId());
			for (List<?> dataRow : worksheetContainer.getValues()) {
				System.out.println(dataRow);
			}
			System.out.println("============================DONE==============================");
		}
	}

	protected static void testReadXlsxDocuments() {

	}
}
