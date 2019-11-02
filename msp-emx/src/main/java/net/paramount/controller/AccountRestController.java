/**
 * 
 */
package net.paramount.controller;

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
import net.paramount.entity.Account;
import net.paramount.framework.controller.BaseRestController;
import net.paramount.repository.AccountFacade;

/**
 * @author ducbui
 *
 */
@RestController
@RequestMapping("/api/account")
public class AccountRestController extends BaseRestController<Account> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3865623310245311419L;

	@Inject
	private AccountFacade businessService;

	@Override
	protected void doCreateBusinessObject(Account businessObject) {
		log.info("Account Rest::CreateBusinessObject: " + businessObject.getName());
		businessService.create(businessObject);
		log.info("Account Rest::CreateBusinessObject is done");
	}
	/*
	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public List<Account> list(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageRequest pageRequest = PageRequest.of(0, 500, Sort.Direction.ASC, "id");
		SearchParameter searchParameter = SearchParameter.getInstance().setPageable(pageRequest);
		Page<EnterpriseUnit> objects = businessService.getObjects(searchParameter);
		System.out.println("COME !");
		return objects.getContent();
	}
	*/
    @RequestMapping(value = "/listAll/", method = RequestMethod.GET)
    public ResponseEntity<List<Account>> listAll() {
        List<Account> userObjects = businessService.findAll();
        if (userObjects.isEmpty()) {
            return new ResponseEntity<List<Account>>(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Account>>(userObjects, HttpStatus.OK);
    }

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public List<Account> list(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Account> objects = businessService.findAll();
		if (CommonUtility.isEmpty(objects)) {
			initDummyData();
			objects = businessService.findAll();
		}
		System.out.println("COME !");
		return objects;
	}

	private void initDummyData() {
		Account account = new Account("DUMMX", "Dummy Account", "Dummy account for testing purpose. ", "DUMMY", Boolean.TRUE);
		doCreateBusinessObject(account);
	}
}
