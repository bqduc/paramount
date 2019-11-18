package net.paramount.css.service.general;

import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import net.paramount.css.repository.general.AttachmentRepository;
import net.paramount.entity.Attachment;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;

@Service
@Transactional
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

	@Override
	public Optional<Attachment> getByName(String name) {
		return this.repository.findByName(name);
	}
}
