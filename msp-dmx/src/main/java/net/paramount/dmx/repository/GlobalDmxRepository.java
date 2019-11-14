/**
 * 
 */
package net.paramount.dmx.repository;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.css.entity.contact.Contact;
import net.paramount.css.entity.general.Office;
import net.paramount.css.service.general.AttachmentService;
import net.paramount.embeddable.Address;
import net.paramount.exceptions.MspDataException;
import net.paramount.framework.component.ComponentBase;
import net.paramount.framework.model.CEContext;
import net.paramount.osx.helper.OfficeSuiteServiceProvider;
import net.paramount.osx.model.DataWorkbook;

/**
 * @author ducbui
 *
 */
@Component
public class GlobalDmxRepository extends ComponentBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -759495846609992244L;

	public final static int NUMBER_OF_CATALOGUE_SUBTYPES_GENERATE = 500;
	public final static int NUMBER_TO_GENERATE = 5000;
	public final static String DEFAULT_COUNTRY = "Việt Nam";

	public static final Byte[] CATALOGUE_SUBTYPE_LEVELS = new Byte[] {10, 11, 12, 20, 21, 22, 30, 31, 32, 40, 41, 42};
	public static final String[] cities = new String[] { "Sài Gòn", "Biên Hòa", "Đồng Xoài", "Tây Ninh", "Lái Thiêu", "Đà Lạt", "Bảo Lộc", "Phan Thiết", "Nha Trang", "Sông Cầu", "Quy Nhơn",
			"Quảng Ngãi", "Đà Nẵng", "Hội An", "Huế", "Hà Nội", "Móng Cái", "Cẩm Phả", "Thác Bản Giốc", "Tuy Hòa", "Cam Lộc", "Bến Tre", "Cần Thơ", "Bạc Liêu", "Mỹ Tho", "Sa Đéc" };

	@Inject
	private AttachmentService attachmentService;
	
	@Inject
	private OfficeSuiteServiceProvider officeSuiteServiceProvider;

	public Address[] buildAddresses() {
		List<Address> addresses = ListUtility.createArrayList();
		Random randomGenerator = new Random();
		Faker faker = new Faker();
		for (int i = 0; i < NUMBER_TO_GENERATE; ++i) {
			addresses.add(
					Address.builder()
					.country(DEFAULT_COUNTRY)
					.city(cities[randomGenerator.nextInt(cities.length)])
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
						.description(faker.company().industry() + "\n" + faker.commerce().department() + "\n" + faker.company().profession())
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
						.description(faker.company().industry() + "\n" + faker.commerce().department() + "\n" + faker.company().profession())
						.birthdate(faker.date().birthday())
						.build();
				currentObject.setId(i+28192L);
				results.add(currentObject);
				//bizServiceManager.saveOrUpdate(currentObject);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return results;
	}

	/**
	 * Archive resource data to database unit
	 */
	public void archiveResourceData(final File resourceFile) throws MspDataException {
		String contentType;
		InputStream zipInputStream = null;
		Map<String, InputStream> zipInputStreams = null;
		File newFile = null;
		try {
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			if (resourceFile.getPath().endsWith("zip")) {
				zipInputStreams = CommonUtility.extractAllZipInputStreams(resourceFile);
				for (String zipEntry :zipInputStreams.keySet()) {
					zipInputStream  = zipInputStreams.get(zipEntry);
					contentType = officeSuiteServiceProvider.detectMineType(zipInputStream, zipEntry);
					newFile = new File(zipEntry);
					contentType = officeSuiteServiceProvider.detectMineType(newFile);
					contentType = mimeTypesMap.getContentType(newFile);
					if (CommonUtility.isEmpty(contentType)) {
						contentType = URLConnection.guessContentTypeFromStream(new BufferedInputStream(zipInputStream));
					}

					if (CommonUtility.isEmpty(contentType)) {
						contentType = newFile.toURI().toURL().openConnection().getContentType();
					}

					if (CommonUtility.isEmpty(contentType)) {
						contentType = Files.probeContentType(newFile.toPath());
					}
					System.out.println("Mine type: " + contentType);
				}
			}
		} catch (Exception e) {
			throw new MspDataException(e);
		}
	}

	public DataWorkbook marshallDataFromZip(CEContext contextParameters) {
		return DataWorkbook.builder().build();
	}
}
