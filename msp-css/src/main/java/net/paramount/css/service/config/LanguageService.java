package net.paramount.css.service.config;

import net.paramount.entity.general.Language;
import net.paramount.framework.service.GenericService;

public interface LanguageService extends GenericService<Language, Long>{
	Language getByCode(String code);
	Language getByName(String name);
}
