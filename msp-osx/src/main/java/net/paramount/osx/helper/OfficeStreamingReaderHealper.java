/**
 * 
 */
package net.paramount.osx.helper;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.monitorjbl.xlsx.StreamingReader;

import lombok.Builder;
import net.paramount.common.CommonConstants;
import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.exceptions.EcosysException;
import net.paramount.osx.exceptions.OsxException;
import net.paramount.osx.model.DataWorkbook;
import net.paramount.osx.model.DataWorksheet;
import net.paramount.osx.model.OSXConstants;

/**
 * @author ducbq
 *
 */
@Builder
public class OfficeStreamingReaderHealper {
	/**
	 * 
	 */
	public DataWorkbook readXlsx(Map<?, ?> parameters) throws OsxException {
		InputStream inputStream = null;
		Workbook workbook = null;

		DataWorksheet worksheet = null;
		DataWorkbook dataWorkbook = DataWorkbook.builder().build();
		try {
			inputStream = (InputStream)parameters.get(OSXConstants.INPUT_STREAM);
			if (parameters.containsKey(OSXConstants.ENCRYPTION_KEYS)) {
				workbook = StreamingReader.builder()
						.rowCacheSize(100)
						.bufferSize(4096)
						.password((String)parameters.get(OSXConstants.ENCRYPTION_KEYS))
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

				worksheet = buildDataWorksheet(sheet);
				dataWorkbook.put(worksheet.getId(), worksheet);
			}
		} catch (Exception e) {
			throw new EcosysException(e);
		}
		return dataWorkbook;
	}

	private DataWorksheet buildDataWorksheet(Sheet sheet) throws OsxException {
		List<Object> dataRow = null;
		DataWorksheet dataWorksheet = DataWorksheet.builder()
				.id(sheet.getSheetName())
				.build();
		Cell currentCell = null;
		short firstCellNum = 0;
		short lastCellNum = 0;
		for (Row currentRow : sheet) {
			firstCellNum = currentRow.getFirstCellNum();
			lastCellNum = currentRow.getLastCellNum();
			break;
		}

		for (Row currentRow : sheet) {
			dataRow = ListUtility.createArrayList();
			for (short idx = firstCellNum; idx <= lastCellNum; idx++) {
				currentCell = currentRow.getCell(idx);
				if (null==currentCell || CellType._NONE.equals(currentCell.getCellType()) || CellType.BLANK.equals(currentCell.getCellType())) {
					dataRow.add(CommonConstants.STRING_BLANK);
					continue;
				} 

				if (CellType.BOOLEAN.equals(currentCell.getCellType())) {
					dataRow.add(currentCell.getBooleanCellValue());
				} else if (CellType.FORMULA.equals(currentCell.getCellType())) {
					
				} else if (CellType.NUMERIC.equals(currentCell.getCellType())) {
					if (DateUtil.isCellDateFormatted(currentCell)) {
						dataRow.add(currentCell.getDateCellValue());
					} else {
						dataRow.add(currentCell.getNumericCellValue());
					}
				} else if (CellType.STRING.equals(currentCell.getCellType())) {
					dataRow.add(currentCell.getStringCellValue());
				} else {
					dataRow.add(currentCell.toString());
				}
			}
			dataWorksheet.addDataRows(Integer.valueOf(currentRow.getRowNum()), dataRow);
		}

		return dataWorksheet;
	}

	/**
	 * True if no sheet id list otherwise check matched sheet id
	 */
	@SuppressWarnings("unchecked")
	private boolean isValidSheet(Sheet sheet, Map<?, ?> parameters) {
		if (!parameters.containsKey(OSXConstants.PROCESSING_DATASHEET_IDS) || CommonUtility.isEmpty(parameters.get(OSXConstants.PROCESSING_DATASHEET_IDS)))
			return true;

		//Map<String, List<String>> sheetIds = (Map<String, List<String>>)parameters.get(OSXConstants.PARAM_DATA_SHEET_IDS);
		List<String> sheetIds = (List<String>)parameters.get(OSXConstants.PROCESSING_DATASHEET_IDS);
		return sheetIds.contains(sheet.getSheetName());
	}
}