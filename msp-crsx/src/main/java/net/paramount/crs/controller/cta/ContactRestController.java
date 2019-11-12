/**
 * 
 */
package net.paramount.crs.controller.cta;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.paramount.common.CommonUtility;
import net.paramount.crs.entity.cta.ContactProfile;
import net.paramount.crs.service.ContactProfileService;
import net.paramount.css.entity.contact.Contact;
import net.paramount.css.service.contact.ContactService;
import net.paramount.framework.controller.BaseRestController;

/**
 * @author ducbui
 *
 */
@RestController
@RequestMapping("/api/contact")
public class ContactRestController extends BaseRestController<Contact> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3865623310245311419L;

	@Inject
	private ContactService businessService;

	@Inject
	private ContactProfileService businessServiceProfile;

	@Override
	protected void doCreateBusinessObject(Contact businessObject) {
		log.info("Account Rest::CreateBusinessObject: " + businessObject.getCode());
		businessService.saveOrUpdate(businessObject);
		log.info("Account Rest::CreateBusinessObject is done");
	}

	@RequestMapping(value = "/listAll/", method = RequestMethod.GET)
	public ResponseEntity<List<Contact>> listAll() {
		List<Contact> userObjects = businessService.getObjects();
		if (userObjects.isEmpty()) {
			return new ResponseEntity<List<Contact>>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Contact>>(userObjects, HttpStatus.OK);
	}

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public List<Contact> list(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Contact> objects = businessService.getObjects();
		if (CommonUtility.isEmpty(objects)) {
			initDummyData();
			objects = businessService.getObjects();
		}
		System.out.println("COME !");
		return objects;
	}

	private void initDummyData() {
		Contact account = Contact.builder()
				.code("CC0191019")
				.accountName("Dummy Contact Clazz")
				.title("Dummy Class")
				.description("This is a dummy entity. ")
				.build();
		doCreateBusinessObject(account);
	}

	@RequestMapping(path = "/listProfiles", method = RequestMethod.GET)
	public List<ContactProfile> listObjects(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ContactProfile> objects = businessServiceProfile.getObjects();
		if (CommonUtility.isEmpty(objects)) {
			initDummyProfilesData();
			objects = businessServiceProfile.getObjects();
		}
		System.out.println("COME !");
		return objects;
	}

	private void initDummyProfilesData() {
		ContactProfile account = new ContactProfile();
		account.setCode("CP0191019");
		account.setFirstName("Duc");
		account.setLastName("Bui Quy");
		account.setTitle("Application Developer");
		this.businessServiceProfile.saveOrUpdate(account);
	}
}
