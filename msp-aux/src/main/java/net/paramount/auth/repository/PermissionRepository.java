/**
 * 
 */
package net.paramount.auth.repository;

import org.springframework.stereotype.Repository;

import net.paramount.auth.entity.Permission;
import net.paramount.framework.repository.BaseRepository;

/**
 * @author bqduc
 *
 */
@Repository
public interface PermissionRepository extends BaseRepository<Permission, Long> {
}
