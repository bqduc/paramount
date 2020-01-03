package net.paramount.css.repository.general;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.entity.general.Item;
import net.paramount.framework.repository.BaseRepository;

@Repository
public interface ItemRepository extends BaseRepository<Item, Long>{
	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<Item> search(@Param("keyword") String keyword, Pageable pageable);

	Item findByCode(String code);
	Item findByName(String name);
}
