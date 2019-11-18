/**
 * 
 */
package net.paramount.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.auth.entity.Module;
import net.paramount.framework.repository.NameBaseRepository;

/**
 * @author bqduc
 *
 */
@Repository
public interface ModuleRepository extends NameBaseRepository<Module, Long> {

	@Query("SELECT entity FROM #{#entityName} entity WHERE LOWER(entity.name) like LOWER(:keyword) ")
	List<Module> search(@Param("keyword") String keyword);
}
