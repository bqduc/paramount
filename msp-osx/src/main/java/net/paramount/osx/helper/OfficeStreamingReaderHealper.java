/**
 * 
 */
package net.paramount.osx.helper;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.monitorjbl.xlsx.StreamingReader;

import lombok.Builder;
import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.exceptions.EcosysException;
import net.paramount.osx.model.BucketContainer;
import net.paramount.osx.model.DataWorkbook;
import net.paramount.osx.model.DataWorksheet;
import net.paramount.osx.model.OfficeDataPackage;

/**
 * @author ducbq
 *
 */
@Builder
public class OfficeStreamingReaderHealper {
	/**
	 * 
	 */
	public OfficeDataPackage readXlsxDocuments(Map<?, ?> parameters) throws EcosysException {
		if (parameters.containsKey(BucketContainer.PARAM_COMPRESSED_FILE)) {
			throw new EcosysException("There is no zip file in parameters!!");
		}

		OfficeDataPackage result = OfficeDataPackage.builder().build();
		Map<Object, Object> clonedParameters = (Map<Object, Object>) ListUtility.cloneMap(parameters);
		DataWorkbook dataWorkbook = null;
		InputStream zipInputStream = null;
		File compressedFile = (File)parameters.get(BucketContainer.PARAM_COMPRESSED_FILE);
		Map<String, InputStream> inputStreamMap = CommonUtility.extractZipInputStreams(compressedFile);
		for (String zipEntry :inputStreamMap.keySet()) {
			zipInputStream = inputStreamMap.get(zipEntry);
			clonedParameters.put(BucketContainer.PARAM_INPUT_STREAM, zipInputStream);
			dataWorkbook = readXlsx(clonedParameters);
			if (null != dataWorkbook) {
				result.put(zipEntry, dataWorkbook);
			}
		}
		return result;
	}

	/**
	 * 
	 */
	public DataWorkbook readXlsx(Map<?, ?> parameters) throws EcosysException {
		InputStream inputStream = null;
		Workbook workbook = null;

		DataWorksheet worksheet = null;
		DataWorkbook dataWorkbook = DataWorkbook.builder().build();
		List<Object> dataRow = null;
		try {
			inputStream = (InputStream)parameters.get(BucketContainer.PARAM_INPUT_STREAM);
			if (parameters.containsKey(BucketContainer.PARAM_ENCRYPTION_KEY)) {
				workbook = StreamingReader.builder()
						.rowCacheSize(100)
						.bufferSize(4096)
						.password((String)parameters.get(BucketContainer.PARAM_ENCRYPTION_KEY))
						.open(inputStream);
			} else {
				workbook = StreamingReader.builder()
						.rowCacheSize(100)
						.bufferSize(4096)
						.open(inputStream);
			}

			for (Sheet sheet : workbook) {
				if (!isValidSheet(sheet, parameters))
					continue;

				worksheet = DataWorksheet.builder()
						.id(sheet.getSheetName())
						.build();
				for (Row currentRow : sheet) {
					dataRow = ListUtility.createArrayList();
					for (Cell currentCell : currentRow) {
						if (null==currentCell || CellType._NONE.equals(currentCell.getCellType()) || CellType.BLANK.equals(currentCell.getCellType())) {
							dataRow.add("");
						} else if (CellType.BOOLEAN.equals(currentCell.getCellType())) {
							dataRow.add(currentCell.getBooleanCellValue());
						} else if (CellType.FORMULA.equals(currentCell.getCellType())) {
							
						} else if (CellType.NUMERIC.equals(currentCell.getCellType())) {
							dataRow.add(currentCell.getNumericCellValue());
						} else if (CellType.STRING.equals(currentCell.getCellType())) {
							dataRow.add(currentCell.getStringCellValue());
						}
					}
					worksheet.addDataRows(Integer.valueOf(currentRow.getRowNum()), dataRow);
				}
				dataWorkbook.put(worksheet.getId(), worksheet);
			}
		} catch (Exception e) {
			throw new EcosysException(e);
		}
		return dataWorkbook;
	}

	/**
	 * True if no sheet id list otherwise check matched sheet id
	 */
	private boolean isValidSheet(Sheet sheet, Map<?, ?> parameters) {
		if (!parameters.containsKey(BucketContainer.PARAM_DATA_SHEET_IDS) || CommonUtility.isEmpty(parameters.get(BucketContainer.PARAM_DATA_SHEET_IDS)))
			return true;

		List<String> sheetIds = (List<String>)parameters.get(BucketContainer.PARAM_DATA_SHEET_IDS);
		return sheetIds.contains(sheet.getSheetName());
	}
}