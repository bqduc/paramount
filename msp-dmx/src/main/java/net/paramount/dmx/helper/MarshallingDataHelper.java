/**
 * 
 */
package net.paramount.dmx.helper;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import net.paramount.common.CommonConstants;
import net.paramount.css.service.config.ItemService;
import net.paramount.entity.general.Item;
import net.paramount.entity.general.LocalizedItem;

/**
 * @author ducbq
 *
 */
@Component
public class MarshallingDataHelper {
	@Inject 
	private ItemService itemService;

	public Item searchLocalizedItem(String localizedItem, String languageCode) {
		PageRequest pageRequest = PageRequest.of(CommonConstants.DEFAULT_PAGE_BEGIN, CommonConstants.DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "id");
		Page<LocalizedItem> pagedItems = itemService.searchLocalizedItems(localizedItem, languageCode, pageRequest);
		return pagedItems.getContent().get(0).getItem();
	}
}
