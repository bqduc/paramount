package net.paramount.css.service.contact;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import net.paramount.css.repository.contact.ContactAddressRepository;
import net.paramount.css.specification.ContactAddressSpecification;
import net.paramount.entity.contact.Contact;
import net.paramount.entity.contact.ContactAddress;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;


@Service
public class ContactAddressServiceImpl extends GenericServiceImpl<ContactAddress, Long> implements ContactAddressService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7761477574156308888L;

	@Inject 
	private ContactAddressRepository repository;
	
	protected BaseRepository<ContactAddress, Long> getRepository() {
		return this.repository;
	}

	@Override
	public List<ContactAddress> getByContact(Contact contact) throws ObjectNotFoundException {
		return repository.findByContact(contact);
	}

	@Override
	public Page<ContactAddress> getObjects(SearchParameter searchParameter) {
		Page<ContactAddress> fetchedObjects = this.repository.findAll(ContactAddressSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
		//Perform additional operations here
		return fetchedObjects;
	}
}
