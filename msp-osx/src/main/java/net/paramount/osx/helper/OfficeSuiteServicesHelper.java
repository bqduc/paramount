/**
 * 
 */
package net.paramount.osx.helper;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Builder;
import net.paramount.common.ListUtility;
import net.paramount.exceptions.EcosysException;
import net.paramount.framework.component.ComponentBase;
import net.paramount.osx.model.BucketContainer;
import net.paramount.osx.model.OfficeMarshalType;

/**
 * @author ducbui
 *
 */
@Component
@Builder
public class OfficeSuiteServicesHelper extends ComponentBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1799685037252299770L;

	public BucketContainer loadDefaultZipConfiguredData(final File sourceZipFile) throws EcosysException {
		BucketContainer bucketContainer = null;
		try {
			Map<String, Object> params = ListUtility.createMap();
			
			Map<String, String> secretKeyMap = ListUtility.createMap("Vietbank_14.000.xlsx", "thanhcong");
			Map<String, List<String>> sheetIdMap = ListUtility.createMap();
			sheetIdMap.put("Bieu thue XNK 2019.07.11.xlsx", ListUtility.arraysAsList(new String[] {"BIEU THUE 2019"}));
			sheetIdMap.put("Vietbank_14.000.xlsx", ListUtility.arraysAsList(new String[] {"File Tổng hợp", "Các trưởng phó phòng", "9"}));
			
			params.put(BucketContainer.PARAM_COMPRESSED_FILE, sourceZipFile);
			params.put(BucketContainer.PARAM_ENCRYPTION_KEY, secretKeyMap);
			params.put(BucketContainer.PARAM_ZIP_ENTRY, ListUtility.arraysAsList(new String[] {"Bieu thue XNK 2019.07.11.xlsx", "Final_PL5_Thuoc tan duoc.xlsx", "Vietbank_14.000.xlsx", "data-catalog.xlsx"}));
			params.put(BucketContainer.PARAM_EXCEL_MARSHALLING_TYPE, OfficeMarshalType.STREAMING);
			params.put(BucketContainer.PARAM_DATA_SHEET_IDS, sheetIdMap);
			bucketContainer = OfficeSuiteServiceProvider
					.builder()
					.build()
					.readOfficeDataInZip(params);
		} catch (Exception e) {
			throw new EcosysException(e);
		}
		return bucketContainer;
	}

}
