/**
 * 
 */
package net.paramount.css.repository.general;

import org.springframework.stereotype.Repository;

import net.paramount.css.entity.general.Attachment;
import net.paramount.framework.repository.BaseRepository;

/**
 * @author bqduc
 *
 */
@Repository
public interface AttachmentRepository extends BaseRepository<Attachment, Long> {
}
