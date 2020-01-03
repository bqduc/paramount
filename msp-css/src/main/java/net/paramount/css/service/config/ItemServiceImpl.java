package net.paramount.css.service.config;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.common.CommonUtility;
import net.paramount.css.repository.config.LocalizedItemRepository;
import net.paramount.css.repository.general.ItemRepository;
import net.paramount.entity.general.Item;
import net.paramount.entity.general.Language;
import net.paramount.entity.general.LocalizedItem;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;

@Service
public class ItemServiceImpl extends GenericServiceImpl<Item, Long> implements ItemService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8419327405445078475L;

	@Inject
	private ItemRepository repository;

	@Inject
	private LocalizedItemRepository localizedRepository;

	@Override
	protected BaseRepository<Item, Long> getRepository() {
		return this.repository;
	}

	@Override
	protected Page<Item> performSearch(String keyword, Pageable pageable) {
		return this.repository.search(keyword, pageable);
	}

	@Override
	public Item getOne(String code) throws ObjectNotFoundException {
		return this.repository.findByCode(code);
	}

	@Override
	public Item getByName(String name) throws ObjectNotFoundException {
		return this.repository.findByName(name);
	}

	@Override
	public LocalizedItem getLocalizedItem(Item item, Language language) {
		EntityManager em = this.getEntityManager();
		List results = em.createQuery("select li from LocalizedItem li where li.item = :item and li.language = :language")
		.setParameter("item", item)
		.setParameter("language", language)
		.getResultList();
		return (results.size() > 0)?(LocalizedItem)results.get(0):null;
	}

	@Override
	public LocalizedItem saveLocalizedItem(LocalizedItem localizedItem) {
		return this.localizedRepository.saveAndFlush(localizedItem);
	}

	@Override
	public List<LocalizedItem> getLocalizedItems(String subtype, Language language) {
		EntityManager em = this.getEntityManager();
		return em.createQuery("select li from LocalizedItem li where li.item.subtype = :itemSubtype and li.language = :language")
		.setParameter("itemSubtype", subtype)
		.setParameter("language", language)
		.getResultList();
	}

	@Override
	public Page<LocalizedItem> searchLocalizedItems(String keyword, String languageCode, Pageable pageable) {
		StringBuilder jql = new StringBuilder("select localizedItem from LocalizedItem localizedItem where localizedItem.value = :keyword");
		if (CommonUtility.isNotEmpty(languageCode)) {
			jql.append(" and localizedItem.language.code = :languageCode");
		}

		Query query = this.getEntityManager()
  		.createQuery(jql.toString())
  		.setParameter("keyword", keyword);

		if (CommonUtility.isNotEmpty(languageCode)) {
			query.setParameter("languageCode", languageCode);
		}
		List<LocalizedItem> foundItems = query.getResultList();
		return new PageImpl<>(foundItems, pageable, foundItems.size());
		/*SearchParameter searchParameter = SearchParameter.builder()
				.keyword(keyword)
				.build();

		return this.localizedRepository.findAll(LocalizedItemSpecification.buildSpecification(searchParameter), searchParameter.getPageable());*/
	}
}
