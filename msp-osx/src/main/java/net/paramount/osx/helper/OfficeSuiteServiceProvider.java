/**
 * 
 */
package net.paramount.osx.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import lombok.Builder;
import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.exceptions.EcosysException;
import net.paramount.osx.model.BucketContainer;
import net.paramount.osx.model.DataWorkbook;
import net.paramount.osx.model.DataWorksheet;
import net.paramount.osx.model.OfficeDocumentType;
import net.paramount.osx.model.OfficeMarshalType;

/**
 * @author bqduc
 *
 */
@Component
@Builder
public class OfficeSuiteServiceProvider {
	private static TikaConfig tikaConfig = null;
	private static Tika tika = new Tika();

	static {
		try {
			tikaConfig = new TikaConfig();
		} catch (MimeTypeException | IOException e) {
			e.printStackTrace();
		}
	}
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
		if (parameters.containsKey(BucketContainer.PARAM_EXCEL_MARSHALLING_TYPE)) {
			officeMarshalType = (OfficeMarshalType) parameters.get(BucketContainer.PARAM_EXCEL_MARSHALLING_TYPE);
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

	public BucketContainer readOfficeDataInZip(final Map<String, Object> parameters) throws EcosysException {
		BucketContainer bucketContainer = BucketContainer.instance();
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
			zipFile = (File) parameters.get(BucketContainer.PARAM_COMPRESSED_FILE);
			zipInputStreams = CommonUtility.extractZipInputStreams(zipFile, (List<String>) parameters.get(BucketContainer.PARAM_ZIP_ENTRY));
			if (zipInputStreams.isEmpty()) {
				return bucketContainer;
			}

			passwordMap = (Map) parameters.get(BucketContainer.PARAM_ENCRYPTION_KEY);
			sheetIdsMap = (Map) parameters.get(BucketContainer.PARAM_DATA_SHEET_IDS);
			for (String zipEntry : zipInputStreams.keySet()) {
				zipInputStream = zipInputStreams.get(zipEntry);
				officeDocumentType = detectOfficeDocumentType(zipInputStream);
				if (!OfficeDocumentType.isExcelDocument(officeDocumentType)) {
					continue;
				}

				worksheetIds = (List<String>) sheetIdsMap.get(zipEntry);
				processingParameters.putAll(parameters);
				processingParameters.remove(BucketContainer.PARAM_COMPRESSED_FILE);
				processingParameters.put(BucketContainer.PARAM_INPUT_STREAM, zipInputStream);
				processingParameters.put(BucketContainer.PARAM_DATA_SHEET_IDS, worksheetIds);
				processingParameters.put(BucketContainer.PARAM_ENCRYPTION_KEY, (String) passwordMap.get(zipEntry));
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

	public String detectMineType(final InputStream inputStream, final String fileName) {
		String mineType = "Unknown";
		try {
			InputStream checkInputStream = FileMagic.prepareToCheckMagic(inputStream);
			System.out.println(FileMagic.valueOf(checkInputStream));

      Metadata metadata = new Metadata();
			
			mineType = tika.detect(inputStream);
			System.out.println("Mine type of #1: " + mineType);

			//Metadata metadata = new Metadata();
			//metadata.set(Metadata.RESOURCE_NAME_KEY, fileName);

			MediaType mediaType = tikaConfig.getMimeRepository().detect(inputStream, metadata);
			mineType = mediaType.getType();
			System.out.println(mediaType.getSubtype());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mineType;
	}

	public String detectMineType(final File file) {
		String mineType = "Unknown";
		try {
			mineType = tika.detect(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mineType;
	}

	public static void main(String[] args) throws Exception {
		String zipFileName = "D:\\git\\paramount\\msp-osx\\src\\main\\resources\\data\\develop_data.zip";
		BucketContainer bucketContainer = OfficeSuiteServicesHelper.builder().build().loadDefaultZipConfiguredData(new File(zipFileName));
		/*
		 * String encryptKey = "thanhcong"; Map<String, Object> params = ListUtility.createMap();
		 * 
		 * Map<String, String> secretKeyMap = ListUtility.createMap("Vietbank_14.000.xlsx", encryptKey); Map<String, List<String>> sheetIdMap =
		 * ListUtility.createMap(); sheetIdMap.put("Bieu thue XNK 2019.07.11.xlsx", ListUtility.arraysAsList(new String[] {"BIEU THUE 2019"}));
		 * sheetIdMap.put("Vietbank_14.000.xlsx", ListUtility.arraysAsList(new String[] {"File Tổng hợp", "Các trưởng phó phòng", "9"}));
		 * 
		 * params.put(BucketContainer.PARAM_COMPRESSED_FILE, new File(zipFileName)); params.put(BucketContainer.PARAM_ENCRYPTION_KEY,
		 * secretKeyMap); params.put(BucketContainer.PARAM_ZIP_ENTRY, ListUtility.arraysAsList(new String[] {"Bieu thue XNK 2019.07.11.xlsx",
		 * "Final_PL5_Thuoc tan duoc.xlsx", "Vietbank_14.000.xlsx", "data-catalog.xlsx"}));
		 * params.put(BucketContainer.PARAM_EXCEL_MARSHALLING_TYPE, OfficeMarshalType.STREAMING);
		 * params.put(BucketContainer.PARAM_DATA_SHEET_IDS, sheetIdMap); BucketContainer bucketContainer = OfficeSuiteServiceProvider .builder()
		 * .build() .readOfficeDataInZip(params);
		 */
		DataWorkbook workbookContainer = null;
		Set<Object> keys = bucketContainer.getKeys();
		for (Object key : keys) {
			workbookContainer = (DataWorkbook) bucketContainer.get(key);
			// System.out.println(workbookContainer.getValues().size());
			System.out.println("############################### " + key + " ###############################");
			displayWorkbookContainer(workbookContainer);
		}

		if (null != keys)
			return;

		/*
		 * long started = System.currentTimeMillis(); String fileName =
		 * "C:/Users/ducbq/Downloads/data_sheets/[FILE GỐC] 100.000 Khách hàng PG Bank.xlsx";/
		 * /"C:/Users/ducbq/Downloads/data_sheets/Danh bạ toàn hệ thống Ngân hàng Vietinbank_Khoảng 14.000.xlsx";/
		 * /"C:\\Users\\ducbq\\Downloads\\data_sheets\\Bieu thue XNK 2019.07.11 (SongAnhLogs).xlsx";/
		 * /"C:\\Users\\ducbq\\Downloads\\data_sheets/Danh bạ toàn hệ thống Ngân hàng LIENVIET POST BANK_Khoảng 3.000.xlsx";
		 * //params.put(BucketContainer.PARAM_ENCRYPTION_KEY, encryptKey); //params.put("BIEU THUE 2019" +
		 * BucketContainer.PARAM_STARTED_ROW_INDEX, 0); params.put(BucketContainer.PARAM_INPUT_STREAM, new FileInputStream(zipFileName));
		 * workbookContainer = OfficeSuiteServiceProvider.builder() .build() .readXlsxByStreaming(params); long duration =
		 * System.currentTimeMillis()-started;
		 * 
		 * keys = bucketContainer.getKeys(); for (Object key :keys) { workbookContainer = (DataWorkbook)bucketContainer.get(key);
		 * System.out.println(workbookContainer.getValues().size()); //displayWorkbookContainer(workbookContainer); }
		 * System.out.println("Event: " + duration);
		 * 
		 * params.put(BucketContainer.PARAM_INPUT_STREAM, new FileInputStream(fileName)); started = System.currentTimeMillis();
		 * workbookContainer = OfficeSuiteServiceProvider.builder() .build() .readXlsxByEventHandler(params); duration =
		 * System.currentTimeMillis()-started; System.out.println("---------------------------------------------------------------------");
		 * System.out.println("Streaming: " + duration); workbookContainer = null; for (Object key :bucketContainer.getKeys()) {
		 * workbookContainer = (DataWorkbook)bucketContainer.get(key); System.out.println(workbookContainer.getValues().size());
		 * displayWorkbookContainer(workbookContainer); }
		 */
		/*
		 * String file = "C:\\Users\\ducbq\\Downloads\\data_sheets.zip"; List<InputStream> inputStreams =
		 * CommonUtility.getZipFileInputStreams(new File(file)); DataBucket dataBucket = null; Map<Object, Object> params =
		 * ListUtility.createMap(); for (InputStream inputStream :inputStreams) { params.put(DataBucket.PARAM_INPUT_STREAM, inputStream);
		 * //params.put(DataBucket.PARAM_DATA_SHEETS, sheetIds); params.put(DataBucket.PARAM_STARTED_ROW_INDEX, new Integer[] {1, 1, 1});
		 * dataBucket = OfficeSuiteServiceProvider.builder() .build() .readXlsxData(params); System.out.println(dataBucket); }
		 */
	}

	protected static void displayWorkbookContainer(DataWorkbook workbookContainer) {
		for (DataWorksheet worksheetContainer : workbookContainer.getValues()) {
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
