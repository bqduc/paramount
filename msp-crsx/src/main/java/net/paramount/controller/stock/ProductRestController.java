/**
 * 
 */
package net.paramount.controller.stock;

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

import net.paramount.css.service.stock.ProductService;
import net.paramount.entity.stock.Product;
import net.paramount.framework.controller.BaseRestController;
import net.paramount.framework.service.IService;

/**
 * @author ducbui
 *
 */
@RestController
@RequestMapping("/api/product")
public class ProductRestController extends BaseRestController<Product, Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -753628349833341962L;

	@Inject
	private ProductService businessService;

	@Override
	protected void doCreateBusinessObject(Product businessObject) {
		log.info("Account Rest::CreateBusinessObject: " + businessObject.getCode());
		businessService.saveOrUpdate(businessObject);
		log.info("Account Rest::CreateBusinessObject is done");
	}

	@RequestMapping(value = "/listAll/", method = RequestMethod.GET)
	public ResponseEntity<List<Product>> listAll() {
		List<Product> userObjects = businessService.getObjects();
		if (userObjects.isEmpty()) {
			return new ResponseEntity<List<Product>>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Product>>(userObjects, HttpStatus.OK);
	}

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public List<Product> list(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Product> objects = businessService.getObjects();
		System.out.println("COME !");
		return objects;
	}

	@Override
	protected Product doFetchBusinessObject(Long id) {
		Product fetchedObject = this.businessService.getObject(id);
		return fetchedObject;
	}

	@Override
	protected IService<Product, Long> getBusinessService() {
		return this.businessService;
	}
}
