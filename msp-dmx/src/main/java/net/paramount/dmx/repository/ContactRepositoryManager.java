/**
 * 
 */
package net.paramount.dmx.repository;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import net.paramount.common.CommonConstants;
import net.paramount.common.CommonUtility;
import net.paramount.common.GenderTypeUtility;
import net.paramount.common.ListUtility;
import net.paramount.css.service.contact.ContactService;
import net.paramount.dmx.helper.DmxCollaborator;
import net.paramount.dmx.repository.base.DmxRepositoryBase;
import net.paramount.embeddable.Address;
import net.paramount.entity.contact.Contact;
import net.paramount.entity.general.Office;
import net.paramount.exceptions.DataLoadingException;
import net.paramount.framework.entity.Entity;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.osx.model.DataWorkbook;
import net.paramount.osx.model.DataWorksheet;
import net.paramount.osx.model.OSXConstants;
import net.paramount.osx.model.OsxBucketContainer;

/**
 * @author ducbui
 *
 */
@Component
public class ContactRepositoryManager extends DmxRepositoryBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5362325078265755318L;

	@Inject 
	private DmxCollaborator dmxCollaborator;
	
	@Inject 
	private ContactService contactService;

	@Override
	protected ExecutionContext doUnmarshallBusinessObjects(ExecutionContext executionContext) throws DataLoadingException {
		DataWorkbook dataWorkbook = null;
		OsxBucketContainer osxBucketContainer = (OsxBucketContainer)executionContext.get(OSXConstants.MARSHALLED_CONTAINER);
		if (CommonUtility.isEmpty(osxBucketContainer))
			throw new DataLoadingException("There is no data in OSX container!");

		if (osxBucketContainer.containsKey(dmxCollaborator.getConfiguredContactWorkbookId())){
			dataWorkbook = (DataWorkbook)osxBucketContainer.get(dmxCollaborator.getConfiguredContactWorkbookId());
		}

		List<Entity> marshalledObjects = unmarshallBusinessObjects(dataWorkbook, ListUtility.createDataList(dmxCollaborator.getConfiguredContactWorksheetIds()));
		if (CommonUtility.isNotEmpty(marshalledObjects)) {
			for (Entity entityBase :marshalledObjects) {
				contactService.save((Contact)entityBase);
			}
		}
		return executionContext;
	}

	@Override
	protected List<Entity> doUnmarshallBusinessObjects(DataWorkbook dataWorkbook, List<String> datasheetIds) throws DataLoadingException {
		List<Entity> results = ListUtility.createDataList();
		Contact currentContact = null;
		if (null != datasheetIds) {
			for (DataWorksheet dataWorksheet :dataWorkbook.datasheets()) {
				if (!datasheetIds.contains(dataWorksheet.getId()))
					continue;

				System.out.println("Processing sheet: " + dataWorksheet.getId());
				for (Integer key :dataWorksheet.getKeys()) {
					try {
						currentContact = (Contact)unmarshallBusinessObject(dataWorksheet.getDataRow(key));
					} catch (DataLoadingException e) {
						e.printStackTrace();
					}
					if (null != currentContact) {
						results.add(currentContact);
					}
				}
			}
		} else {
			for (DataWorksheet dataWorksheet :dataWorkbook.datasheets()) {
				System.out.println("Processing sheet: " + dataWorksheet.getId());
				for (Integer key :dataWorksheet.getKeys()) {
					try {
						currentContact = (Contact)unmarshallBusinessObject(dataWorksheet.getDataRow(key));
					} catch (DataLoadingException e) {
						e.printStackTrace();
					}
					results.add(currentContact);
				}
			}
		}
		return results;
	}

	@Override
	protected Entity doUnmarshallBusinessObject(List<?> marshallingDataRow) throws DataLoadingException {
		String firstName = "";
		String lastName = "";
		String fullName = (String)marshallingDataRow.get(1);
		int lastStringSpacePos = fullName.lastIndexOf(CommonConstants.STRING_SPACE);
		if (lastStringSpacePos != -1) {
			firstName = fullName.substring(lastStringSpacePos+1);
			lastName = fullName.substring(0, lastStringSpacePos);
		} else {
			firstName = fullName;
		}

		Contact marshalledObject = null;
		try {
			marshalledObject = Contact.builder()
					.code((String)marshallingDataRow.get(0))
					.firstName(firstName)
					.lastName(lastName)
					.birthdate((Date)marshallingDataRow.get(5))
					.email((String)marshallingDataRow.get(IDX_EMAIL_WORK))
					.businessUnit(getBusinessUnit(marshallingDataRow))
					.jobInfo(this.parseJobInfo(marshallingDataRow))
					.gender(GenderTypeUtility.getGenderType((String)marshallingDataRow.get(IDX_GENDER)))
					.phone(this.parsePhone(CommonConstants.STRING_BLANK, CommonConstants.STRING_BLANK, (String)marshallingDataRow.get(IDX_PHONE_OFFICE), CommonConstants.STRING_BLANK))
					.mobilePhone(this.parsePhone(CommonConstants.STRING_BLANK, CommonConstants.STRING_BLANK, (String)marshallingDataRow.get(IDX_PHONE_MOBILE), CommonConstants.STRING_BLANK))
					.homePhone(this.parsePhone(CommonConstants.STRING_BLANK, CommonConstants.STRING_BLANK, (String)marshallingDataRow.get(IDX_PHONE_HOME), CommonConstants.STRING_BLANK))
					.fax((String)marshallingDataRow.get(IDX_FAX))
					.build();
		} catch (Exception e) {
			log.error(e);
		}

		return marshalledObject;
	}
	
	public Address[] buildAddresses() {
		List<Address> addresses = ListUtility.createArrayList();
		Random randomGenerator = new Random();
		Faker faker = new Faker();
		for (int i = 0; i < NUMBER_TO_GENERATE; ++i) {
			addresses.add(
					Address.builder()
					.country(DEFAULT_COUNTRY)
					.city(DmxRepositoryConstants.cities[randomGenerator.nextInt(DmxRepositoryConstants.cities.length)])
					.address(faker.address().fullAddress())
					.state(faker.address().cityName())
					.postalCode(faker.address().zipCode()).build());
		}
		return addresses.toArray(new Address[0]);
	}

	public List<Office> generateFakeOfficeData(){
		List<Office> results = ListUtility.createDataList();
		Office currentObject = null;
		Faker faker = new Faker();
		Address[] addresses = this.buildAddresses();
		for (int i = 0; i < NUMBER_TO_GENERATE; i++) {
			try {
				currentObject = Office.builder()
						.code(faker.code().ean13())
						.name(CommonUtility.stringTruncate(faker.company().name(), 200))
						.phones(faker.phoneNumber().phoneNumber())
						.info(faker.company().industry() + "\n" + faker.commerce().department() + "\n" + faker.company().profession())
						.address(addresses[i])
						.build();
				results.add(currentObject);
				//bizServiceManager.saveOrUpdate(currentObject);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return results;
	}

	public List<Contact> generateFakeContactProfiles(){
		List<Contact> results = ListUtility.createDataList();
		Contact currentObject = null;
		Faker faker = new Faker();
		for (int i = 0; i < NUMBER_TO_GENERATE; i++) {
			try {
				currentObject = Contact.builder()
						.code(faker.code().ean13())
						.firstName(CommonUtility.stringTruncate(faker.name().firstName(), 50))
						.lastName(CommonUtility.stringTruncate(faker.name().lastName(), 150))
						.code(CommonUtility.stringTruncate(faker.code().ean13(), 200))
						.info(faker.company().industry() + "\n" + faker.commerce().department() + "\n" + faker.company().profession())
						.birthdate(faker.date().birthday())
						.build();
				currentObject.setId(i+28192L);
				results.add(currentObject);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return results;
	}	
}
