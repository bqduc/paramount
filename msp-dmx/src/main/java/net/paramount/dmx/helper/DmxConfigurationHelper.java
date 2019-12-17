/**
 * 
 */
package net.paramount.dmx.helper;

import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.paramount.common.ListUtility;
import net.paramount.css.service.config.ConfigurationService;
import net.paramount.entity.config.Configuration;
import net.paramount.entity.config.ConfigurationDetail;

/**
 * @author ducbq
 *
 */
@Component
public class DmxConfigurationHelper {
	//private final static String configInvtrItem = "load-inventory-items";

	public static byte idxInvtrItemCode = 1;
	public static byte idxInvtrItemName = 2;
	public static byte idxInvtrItemContent = 8; // Hàm lượng
	public static byte idxInvtrItemPackaging = 9;

	public static byte idxUsageDirectionCode = 6;
	public static byte idxUsageDirectionName = 7;

	public static byte idxGenericDrugCode = 3;
	public static byte idxGenericDrugName = 4;
	public static byte idxGenericDrugNameRegistered = 5;

	@Inject
	private ConfigurationService configurationSerice;

	public Map<String, ConfigurationDetail> fetchInventoryItemConfig(String configName) {
		Map<String, ConfigurationDetail> configDetailMap = ListUtility.createMap();
		Optional<Configuration> optInventoryItemConfig = configurationSerice.getOne(configName);
		if (optInventoryItemConfig.isPresent()) {
			for (ConfigurationDetail configDetail :optInventoryItemConfig.get().getConfigurationDetails()) {
				configDetailMap.put(configDetail.getName(), configDetail);
			}
		}

		return configDetailMap;
	}
}
