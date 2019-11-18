/**
 * 
 */
package net.paramount.framework.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author bqduc
 *
 */
@NoRepositoryBean
public interface NameBaseRepository<T, PK extends Serializable> extends BaseRepository<T, PK> {
	Optional<T> findByName(String name);
}
