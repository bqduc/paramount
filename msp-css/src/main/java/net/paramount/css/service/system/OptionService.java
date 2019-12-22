package net.paramount.css.service.system;

import org.springframework.data.domain.Page;

import net.paramount.entity.options.OptionKey;
import net.paramount.entity.system.Option;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface OptionService extends GenericService<Option, Long>{
  /**
   * Get one option with the provided code.
   * 
   * @param user The option user
   * @param key The option key
   * @return The option
   * @throws ObjectNotFoundException If no such option exists.
   */
	Option getOne(OptionKey optionKey) throws ObjectNotFoundException;

	/**
   * Get one option with the provided code.
   * 
   * @param user The option user
   * @param key The option key
   * @return The option
   * @throws ObjectNotFoundException If no such option exists.
   */
	Option getOne(String key) throws ObjectNotFoundException;

  /**
   * Get one option with the provided code.
   * 
   * @param user The option user
   * @param key The option key
   * @return The option
   * @throws ObjectNotFoundException If no such option exists.
   */
	Option getOne(String user, String key) throws ObjectNotFoundException;

  /**
   * Get one option with the provided code.
   * 
   * @param user The option user
   * @param key The option key
   * @return The option
   * @throws ObjectNotFoundException If no such option exists.
   */
	Option getOption(String key, String defaultValue) throws ObjectNotFoundException;

  /**
   * Get one option with the provided code.
   * 
   * @param user The option user
   * @param key The option key
   * @return The option
   * @throws ObjectNotFoundException If no such option exists.
   */
	Option getOption(String key, String defaultValue, boolean forUser) throws ObjectNotFoundException;


  /**
   * Get one option with the provided code.
   * 
   * @param user The option user
   * @param key The option key
   * @return The option
   * @throws ObjectNotFoundException If no such option exists.
   */
	Option getOption(OptionKey optionKey, boolean create) throws ObjectNotFoundException;

	/**
   * Get one option with the provided search parameters.
   * 
   * @param searchParameter The search parameter
   * @return The pageable system sequences
   */
	Page<Option> getObjects(SearchParameter searchParameter);
}