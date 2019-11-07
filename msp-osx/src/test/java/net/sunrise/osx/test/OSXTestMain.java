/**
 * 
 */
package net.sunrise.osx.test;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import net.paramount.common.ListUtility;
import net.paramount.osx.helper.OfficeSuiteServiceProvider;
import net.paramount.osx.model.BucketContainer;
import net.paramount.osx.model.DataWorkbook;

/**
 * @author bqduc
 *
 */
public class OSXTestMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		doTestReadXlsx();
	}

	protected static void doTestReadXlsx() {
		Map<Object, Object> params = ListUtility.createMap();
		String[] sheetIds = new String[]{/*"languages", "items", "localized-items"*/"online-books", "Forthcoming"}; 
		BucketContainer dataBucket = null;
		String dataSheet = "D:\\workspace\\aquariums.git\\aquarium\\aquarium-admin\\src\\main\\resources\\config\\data\\data-catalog.xlsx";
		try {
			params.put(BucketContainer.PARAM_INPUT_STREAM, new FileInputStream(dataSheet));
			params.put(BucketContainer.PARAM_DATA_SHEET_IDS, sheetIds);
			params.put(BucketContainer.PARAM_STARTED_ROW_INDEX, new Integer[] {1, 1, 1});
			DataWorkbook workbookContainer = OfficeSuiteServiceProvider.builder()
			.build()
			.readExcelFile(params);
			List<?> details = null;
			List<?> forthcomingBooks = (List<?>)dataBucket.getBucketData().get("Forthcoming");
			List<?> onlineBooks = (List<?>)dataBucket.getBucketData().get("online-books");
			for (Object currentItem :forthcomingBooks) {
				System.out.println(currentItem);
			}

			for (Object currentItem :onlineBooks) {
				details = (List<?>)currentItem;
				System.out.println(details.size());
			}
			//System.out.println(dataBucket.getBucketData().get("Forthcoming"));
			//System.out.println(dataBucket.getBucketData().get("online-books"));
		} catch (Exception e) {
			e.printStackTrace();;
		}

	}
}
