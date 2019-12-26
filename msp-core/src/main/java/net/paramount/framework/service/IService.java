package net.paramount.framework.service;

import java.io.Serializable;

import net.paramount.framework.entity.ObjectBase;

public interface IService<T extends ObjectBase, K extends Serializable> extends Serializable {
  /**
   * Get object with the provided key.
   * 
   * @param id The object key
   * @return The Object
   */
	T getObject(K id);
}