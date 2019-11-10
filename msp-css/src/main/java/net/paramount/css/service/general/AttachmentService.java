package net.paramount.css.service.general;

import org.springframework.data.domain.Page;

import net.paramount.css.entity.general.Attachment;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface AttachmentService extends GenericService<Attachment, Long> {
	/**
	 * Get one Attachments with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Attachments
	 */
	Page<Attachment> getObjects(SearchParameter searchParameter);
}
