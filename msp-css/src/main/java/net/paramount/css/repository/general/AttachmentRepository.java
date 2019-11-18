/**
 * 
 */
package net.paramount.css.repository.general;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import net.paramount.entity.Attachment;
import net.paramount.framework.repository.BaseRepository;

/**
 * @author bqduc
 *
 */
@Repository
public interface AttachmentRepository extends BaseRepository<Attachment, Long> {
	Optional<Attachment> findByName(String name);
}
