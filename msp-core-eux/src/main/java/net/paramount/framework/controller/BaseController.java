/**
 * 
 */
package net.paramount.framework.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.paramount.common.CommonBeanUtils;
import net.paramount.common.CommonConstants;
import net.paramount.common.CommonUtility;
import net.paramount.common.ListUtility;
import net.paramount.domain.SelectItem;
import net.paramount.exceptions.ExecutionContextException;
import net.paramount.framework.constants.ControllerConstants;
import net.paramount.framework.model.SearchParameter;
import net.paramount.helper.WebServicingHelper;

/**
 * @author bqduc
 *
 */
public abstract class BaseController extends RootController {
	private static final long serialVersionUID = -6493003418945724947L;

	protected static final String PAGE_POSTFIX_BROWSE = "Browse";
	protected static final String PAGE_POSTFIX_SHOW = "Show";
	protected static final String PAGE_POSTFIX_EDIT = "Edit";
	protected static final String REDIRECT = "redirect:/";

	protected void doPostConstruct() throws ExecutionContextException {
		throw new ExecutionContextException("Not implemented yet!");
	}

	@PostConstruct
	public void onPostConstruct() throws ExecutionContextException {
		doPostConstruct();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		CustomDateEditor editor = new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true);
		binder.registerCustomEditor(Date.class, editor);
	}

	protected String getView(String module) {
		return null;
	}

	protected String performListing(Model model, HttpServletRequest request){
		return CommonConstants.STRING_BLANK;
	}

	protected String performSearch(SearchParameter params) {
		return null;
	}

	protected List<?> performSearchObjects(SearchParameter params) {
		return null;
	}

	protected String loadDefault(Model model, HttpServletRequest request) {
		return "";
	}

	protected void loadDependencies(Model model){
		//Load dependencies and put back to model
	}

	protected Map<String, Object> buildSearchParameters(Short page, Short pageSize, String... keywords) {
		return WebServicingHelper.createSearchParameters(keywords[0], page, pageSize);
	}

	protected List<SelectItem> suggestItems(String keyword) {
		return null;
	}

	protected List<SelectItem> buildSelectedItems(List<?> objects){
		List<SelectItem> selectItems = new ArrayList<>();
		for (Object object :objects){
			selectItems.add(this.buildSelectableObject(object));
		}
		return selectItems;
	}

	protected SelectItem buildSelectableObject(Object beanObject){
		return null;
	}

	protected List<SelectItem> buildCategorySelectedItems(List<?> objects) {
		Long objectId = null;
		String objectCode = null, objectName = null;
		List<SelectItem> selectItems = ListUtility.createArrayList();
		if (CommonUtility.isNotEmpty(objects)) {
			for (Object object : objects) {
				try {
					objectId = (Long) CommonBeanUtils.getBeanProperty(object, "id");
					objectCode = (String) CommonBeanUtils.getBeanProperty(object, "code");
					objectName = (String) CommonBeanUtils.getBeanProperty(object, "name");
					selectItems.add(SelectItem.builder().id(objectId).code(objectCode).name(objectName).build());
				} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
					log.error(e);
				}
			}
		}
		return selectItems;
	}

	protected List<SelectItem> buildCategorySelectedItems(List<?> objects, String idProperty, String displayCodeProperty,
			String displayNameProperty) {
		Long objectId = null;
		String objectCode = null, objectName = null;
		List<SelectItem> selectItems = new ArrayList<>();
		for (Object object : objects) {
			try {
				objectId = (Long) CommonBeanUtils.getBeanProperty(object, idProperty);
				objectCode = (String) CommonBeanUtils.getBeanProperty(object, displayCodeProperty);
				objectName = (String) CommonBeanUtils.getBeanProperty(object, displayNameProperty);
				selectItems.add(SelectItem.builder().id(objectId).code(objectCode).name(objectName).build());
			} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				log.error(e);
			}
		}
		return selectItems;
	}

	protected List<SelectItem> buildSelectItems(List<?> objects) {
		Long objectId = null;
		String objectCode = null, objectName = null;
		List<SelectItem> selectItems = new ArrayList<>();
		for (Object object : objects) {
			try {
				objectId = (Long) CommonBeanUtils.getBeanProperty(object, "id");
				objectCode = (String) CommonBeanUtils.getBeanProperty(object, "code");
				objectName = (String) CommonBeanUtils.getBeanProperty(object, "name");
				selectItems.add(SelectItem.builder().id(objectId).code(objectCode).name(objectName).build());
			} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				log.error(e);
			}
		}
		return selectItems;
	}

	protected List<SelectItem> buildSelectItems(List<?> objects, String keyProperty, String[] displayProperties) {
		Long objectId = null;
		List<SelectItem> selectItems = ListUtility.createArrayList();
		Map<String, Object> displayValueMap = ListUtility.createMap();
		for (Object object : objects) {
			try {
				objectId = (Long) CommonBeanUtils.getBeanProperty(object, keyProperty);
				for (String displayProperty : displayProperties) {
					displayValueMap.put(displayProperty, CommonBeanUtils.getBeanProperty(object, displayProperty));
				}

				selectItems.add(SelectItem.builder().build().instance(objectId, displayValueMap));
			} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				log.error(e);
			}
		}
		return selectItems;
	}

	protected String performImport(Model model, HttpServletRequest request){
		return ControllerConstants.DEAULT_PAGE_CONTEXT_PREFIX;
	}

	protected String performExport(Model model, HttpServletRequest request){
		return ControllerConstants.DEAULT_PAGE_CONTEXT_PREFIX;
	}

	protected String buildRedirectShowBizObjectRoute(String controllerId, Long businessObjectId){
		return new StringBuilder(ControllerConstants.REDIRECT_PREFIX)
				.append(controllerId).append("/")
				.append(businessObjectId)
				.toString();
	}

	protected HttpServletRequest getCurrentHttpRequest(){
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes) {
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        return request;
    }

    log.debug("Not called in the context of an HTTP request");
    return null;
	}	

	protected boolean isContinueOther(Model model, HttpServletRequest request){
		boolean isContinuedOther = false;
		isContinuedOther = "on".equalsIgnoreCase(request.getParameter(ControllerConstants.PARAM_CREATE_OTHER));
		if (true==isContinuedOther){
			request.getSession().setAttribute(ControllerConstants.PARAM_CREATE_OTHER, Boolean.TRUE);
		}
		return isContinuedOther;
	}

	/*@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, HttpServletRequest request) {
		return loadDefault(model, request);
	}*/

	@PostMapping(value = "/search/query", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String search(@RequestBody(required = false) SearchParameter params, Model model, Pageable pageable) {
		log.info("Enter search/query");
		return performSearch(params.setPageable(pageable).setModel(model));
	}

	@RequestMapping(path = "/searchex/query", method = RequestMethod.GET)
	public List<?> query(@RequestBody(required = false) SearchParameter params, Model model, Pageable pageable) {
		log.info("Enter search/query");
		return performSearchObjects(params.setPageable(pageable).setModel(model));
	}

	@RequestMapping(value = "/searchAction", method = RequestMethod.POST)
	public @ResponseBody String onSearch(@RequestBody(required = false) SearchParameter params, Model model, Pageable pageable){
		Gson gson = new GsonBuilder().serializeNulls().create();
		List<?> searchResults = performSearchObjects(
				params
				.setPageable(pageable)
				.setModel(model));
		String jsonBizObjects = gson.toJson(searchResults);
		//String contentJson = "{\"iTotalDisplayRecords\":" + searchResults.size() + "," + "\"data\":" + jsonBizObjects + "}";
		String contentJson = jsonBizObjects;
		return contentJson;
	}

	@RequestMapping(value = "/suggest", method = RequestMethod.GET)
	public @ResponseBody List<SelectItem> suggest(@RequestParam("term") String keyword, HttpServletRequest request) {
		log.info("Enter keyword: " + keyword);
		List<SelectItem> suggestedItems = suggestItems(keyword);
		if (CommonUtility.isNull(suggestedItems)) {
			suggestedItems = new ArrayList<>();
		}
		return suggestedItems;
	}

	@RequestMapping(value = "/suggestObjects", method = RequestMethod.GET)
	public @ResponseBody List<SelectItem> suggestObject(@RequestParam("keyword") String keyword, HttpServletRequest request) {
		log.info("Enter keyword: " + keyword);
		List<SelectItem> suggestedItems = suggestItems(keyword);
		if (CommonUtility.isNull(suggestedItems)){
			suggestedItems = new ArrayList<>();
		}
		return suggestedItems;
	}

	/**
	 * Import business objects.
   */
	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public String imports(Model model, HttpServletRequest request) {
		log.info("Importing business objects .....");
		String importResults = performImport(model, request);
		log.info("Leave importing business objects!");
		return importResults;
	}

	/**
	 * Export business objects.
   */
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public String exports(Model model, HttpServletRequest request) {
		log.info("Exporting business objects .....");
		String exportResults = performExport(model, request);
		log.info("Leaving exporting business objects .....");
		return exportResults;
	}
}
