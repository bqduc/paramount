/**
 * 
 */
package net.paramount.framework.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author bqduc
 *
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
/*
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
*/
public abstract class ObjectAudit extends ObjectBase {
  /**
	 * 
	 */
	private static final long serialVersionUID = -5548899991281118480L;

	@CreatedDate
  @Column(updatable = false, name="created_at")
  private Instant createdAt;

  @LastModifiedDate
  @Column(updatable = false, name="updated_at")
  private Instant updatedAt;

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
