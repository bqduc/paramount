/**
 * 
 */
package net.paramount.osx.model;

/**
 * @author ducbq
 *
 */
public enum MarshallingObjects {
	CONTACTS ("contacts"), 
	ITEMS("items"),
	LOCALIZED_ITEMS("localizedItems"),
	LANGUAGES("languages"),
	INVENTORY_ITEMS("inventoryItems"),

	;
	private String object;

	public String getObjectName() {
		return object;
	}

	private MarshallingObjects(String object) {
		this.object = object;
	}
}
