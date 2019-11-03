/**
 * 
 */
package net.paramount.framework.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bqduc
 *
 */
@Embeddable 
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Data
public class AuditLog {
	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_user_id")
	private Long createdBy;

	@Column(name="last_modified_date")
	private Date lastModifiedDate;

	@Column(name="modified_user_id")
	private Long lastModifiedBy;
}
