package net.paramount.css.service.general;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.paramount.css.entity.general.Attachment;
import net.paramount.css.repository.general.AttachmentRepository;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;

@Service
public class AttachmentServiceImpl extends GenericServiceImpl<Attachment, Long> implements AttachmentService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7761477574156308888L;

	@Inject 
	private AttachmentRepository repository;
	
	protected BaseRepository<Attachment, Long> getRepository() {
		return this.repository;
	}
}
