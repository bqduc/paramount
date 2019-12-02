package net.paramount.css.service.org;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.css.entity.org.BusinessUnit;
import net.paramount.css.repository.org.BusinessUnitRepository;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;
import net.paramount.specification.BusinessUnitSpecification;

@Service
public class BusinessUnitServiceImpl extends GenericServiceImpl<BusinessUnit, Long> implements BusinessUnitService{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7067548144456556095L;

	@Inject 
	private BusinessUnitRepository repository;
	
	protected BaseRepository<BusinessUnit, Long> getRepository() {
		return this.repository;
	}

	@Override
	public BusinessUnit getOne(String code) throws ObjectNotFoundException {
		return super.getOptionalObject(repository.findByCode(code));
	}

	@Override
	protected Page<BusinessUnit> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	private BusinessUnit parseEntity(List<String> data){
		BusinessUnit.buildObject(data);
		return new BusinessUnit();
		/*return BusinessUnit.getInstance(
				(String)ListUtility.getEntry(data, 0), //Code
				(String)ListUtility.getEntry(data, 2), //First name
				(String)ListUtility.getEntry(data, 1)) //Last name
				.setDateOfBirth(DateTimeUtility.createFreeDate((String)ListUtility.getEntry(data, 4)))
				.setPlaceOfBirth((String)ListUtility.getEntry(data, 5))
				.setNationalId((String)ListUtility.getEntry(data, 6))
				.setNationalIdIssuedDate(DateTimeUtility.createFreeDate((String)ListUtility.getEntry(data, 7)))
				.setNationalIdIssuedPlace((String)ListUtility.getEntry(data, 8))
				.setGender(GenderTypeUtility.getGenderType((String)ListUtility.getEntry(data, 21)))
				.setAddress((String)ListUtility.getEntry(data, 14))
				.setPresentAddress((String)ListUtility.getEntry(data, 14), (String)ListUtility.getEntry(data, 22))
				.setBillingAddress((String)ListUtility.getEntry(data, 15), (String)ListUtility.getEntry(data, 22))
				.setPhones(CommonUtility.safeSubString((String)ListUtility.getEntry(data, 18), 0, 50))
				.setCellPhones(CommonUtility.safeSubString((String)ListUtility.getEntry(data, 19), 0, 50))
				.setOverallExpectation((String)ListUtility.getEntry(data, 28))
				.setOverallExperience((String)ListUtility.getEntry(data, 27))
				.setEmail((String)ListUtility.getEntry(data, 20))
				.setNotes((String)ListUtility.getEntry(data, 29))
			;*/
	}

	@Override
	public Page<BusinessUnit> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(BusinessUnitSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}
}
