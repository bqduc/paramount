package net.paramount.css.repository.config;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.paramount.entity.general.Item;
import net.paramount.entity.general.Language;
import net.paramount.entity.general.LocalizedItem;
import net.paramount.framework.repository.BaseRepository;

@Repository
public interface LocalizedItemRepository extends BaseRepository<LocalizedItem, Long>{

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " entity.item = :item and entity.language = :language"
			+ ")"
	)
	public LocalizedItem findByLocalizedItem(Language language, Item item);
}
