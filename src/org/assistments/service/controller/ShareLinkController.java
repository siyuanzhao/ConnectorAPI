package org.assistments.service.controller;

import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.service.domain.ShareLink;

/**
 * It represents Share Link Management in ASSISTments Service
 * @author szhao
 *
 */
public interface ShareLinkController {
	
	
	/**
	 * Create a share link
	 * @param userId -- identifier for the user from ASSISTments. It could be db id or external reference
	 * @param problemSetId -- problem set id
	 * @param recipient -- email address of the recipient or "generic"
	 * @param reShareable -- if this share link can be re-shared by recipient
	 * @return external reference for the share link
	 */
	public String create(String userId, String problemSetId, String recipient, boolean reShareable);

	/**
	 * Create a share link
	 * @param userId -- identifier for the user from ASSISTments
	 * @param problemSetId -- problem set id
	 * @param recipient -- email address of the recipient
	 * @param reShareable -- if the share link can be  re-shared by the recipient
	 * @param url -- url of Google spreadsheet containing all verified teachers' email
	 * @param form -- url of Google form for teachers whose email address is not listed on Google spreadsheet
	 * @param isAssistmentsVerified -- if the distributor select assistments verified
	 * @return external reference for the share link
	 */
	public String create(String userId, String problemSetId, String recipient, boolean reShareable, String url, String form, boolean isAssistmentsVerified);
	
	/**
	 * Find Share Link information
	 * @param shareLinkRef -- identifier for the share link
	 * @return {@link ShareLink}
	 * @throws ReferenceNotFoundException -- if nothing is found, this exception is thrown out.
	 */
	public ShareLink find(String shareLinkRef) throws ReferenceNotFoundException;
}
