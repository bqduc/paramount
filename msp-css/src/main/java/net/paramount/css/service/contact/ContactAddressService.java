package net.paramount.css.service.contact;

import java.util.List;

import org.springframework.data.domain.Page;

import net.paramount.entity.contact.Contact;
import net.paramount.entity.contact.ContactAddress;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface ContactAddressService extends GenericService<ContactAddress, Long> {

	/**
	 * Get one Enterprise with the provided code.
	 * 
	 * @param code
	 *            The Enterprise code
	 * @return The Enterprise
	 * @throws ObjectNotFoundException
	 *             If no such Enterprise exists.
	 */
	List<ContactAddress> getByContact(Contact contact) throws ObjectNotFoundException;

	/**
	 * Get one Enterprises with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Enterprises
	 */
	Page<ContactAddress> getObjects(SearchParameter searchParameter);
}
