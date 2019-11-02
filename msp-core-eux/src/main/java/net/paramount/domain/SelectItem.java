/**
 * 
 */
package net.paramount.domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.paramount.common.CommonConstants;

/**
 * @author bqduc
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectItem {
	private Long id;
	private String code;
	private String name;
	private String nameLocal;

	public String getCode() {
		return code;
	}

	public SelectItem setCode(String code) {
		this.code = code;
		return this;
	}

	public SelectItem(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public SelectItem(long id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public SelectItem setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public SelectItem setName(String name) {
		this.name = name;
		return this;
	}

	public String getNameLocal() {
		return nameLocal;
	}

	public void setNameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
	}
	public SelectItem instance(Long key, String code, String name){
		return SelectItem.builder()
				.id(key)
				.code(code)
				.name(name)
				.build();
	}

	public SelectItem instance(Long key, Map<String, Object> properties){
		return SelectItem.builder()
				.id(key)
				.code((String)properties.get(CommonConstants.PROPERTY_CODE))
				.name(properties.containsKey(CommonConstants.PROPERTY_NAME)?(String)properties.get(CommonConstants.PROPERTY_NAME):"")
				.nameLocal(properties.containsKey(CommonConstants.PROPERTY_NAME_LOCAL)?(String)properties.get(CommonConstants.PROPERTY_NAME_LOCAL):"")
				.build();
	}
}