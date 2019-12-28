package net.paramount.css.service.general;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.css.repository.general.CurrencyRepository;
import net.paramount.css.service.system.SequenceManager;
import net.paramount.css.specification.CurrencySpecification;
import net.paramount.entity.general.Currency;
import net.paramount.exceptions.MspDataException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;
import net.paramount.global.GlobalConstants;

@Service
public class CurrencyServiceImpl extends GenericServiceImpl<Currency, Long> implements CurrencyService{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3677680060876281817L;

	@Inject 
	private CurrencyRepository repository;

	@Inject
	private SequenceManager sequenceManager;

	protected BaseRepository<Currency, Long> getRepository() {
		return this.repository;
	}

	@Override
	public Optional<Currency> getByAlphabeticCode(String alphabeticCode) throws ObjectNotFoundException {
		return repository.findByAlphabeticCode(alphabeticCode);
	}

	@Override
	protected Page<Currency> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Currency> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(CurrencySpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}

	@Override
	public Optional<Currency> getByNumericCode(String numericCode) throws ObjectNotFoundException {
		return repository.findByNumericCode(numericCode);
	}

	@Override
	public String nextSerial(String prefix) throws MspDataException {
		String newSerialNo = this.sequenceManager.getNewNumber(prefix, Integer.valueOf(GlobalConstants.SIZE_CODE));
		newSerialNo = prefix + newSerialNo.substring(prefix.length());
		return newSerialNo;
	}
}
