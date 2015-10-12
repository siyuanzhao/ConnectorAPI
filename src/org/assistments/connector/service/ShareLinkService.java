package org.assistments.connector.service;

import java.util.List;

import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.service.domain.ShareLink;

/**
 * This interface contains the operations on share link
 * and manages the association between Partner user and share link
 * @author szhao
 *
 */
public interface ShareLinkService {
	
	/**
	 * Create a share link
	 * @param userId -- identifier for the user from ASSISTments
	 * @param problemSetId -- problem set id
	 * @param recipient -- email address of the recipient
	 * @param reShareable -- if the share link can be  re-shared by the recipient
	 * @return external reference for the share link
	 */
	public String create(String userId, String problemSetId, String recipient, boolean reShareable);
	
	/**
	 * Create a share link
	 * @param userId -- identifier for the user from ASSISTments
	 * @param problemSetId -- problem set id
	 * @param recipient -- email address of the recipient
	 * @param reShareable -- if the share link can be  re-shared by the recipient
	 * @param url -- url of Google spreadsheet containing all verified teachers' emails
	 * @param form -- url of Google form for teachers whose email address is not listed on Google spreadsheet
	 * @param isAssistmentsVerified -- if the distributor select assistments verified
	 * @return external reference for the share link
	 */
	public String create(String userId, String problemSetId, String recipient, boolean reShareable, String url, String form, boolean isAssistmentsVerified);
	
	/**
	 * Store the association between Partner user and share link into database
	 * @param shareLinkRef -- external reference for the share link from ASSISTments
	 * @param userToken -- access token for the user from ASSISTments
	 * @param partnerExternalRef --partner external reference for the user from Partner
	 * @param partnerToken -- partner access token for the user from Partner
	 * @param note -- To keep extra information
	 */
	public void addExternalShareLink(String shareLinkRef, String userToken, 
			String partnerExternalRef, String partnerToken, String note);
	
	/**
	 * Find a share link based on external reference for the share link
	 * @param shareLinkRef -- external reference for the share link from ASSISTments
	 * @return ShareLink object -- It contains problem set information and distributor information
	 * @throws ReferenceNotFoundException -- if nothing is found, this exception will be thrown out.
	 */
	public ShareLink find(String shareLinkRef) throws ReferenceNotFoundException;
	
	/**
	 * Find a certain external share link based on column name and value 
	 * @param cn -- column name. It comes from table partner_to_assistments_links (partner_external_references)
	 * @param value -- value for the column
	 * @return a list of external share link
	 * @throws ReferenceNotFoundException -- If nothing is found, this exception will be thrown out.
	 */
	public List<PartnerToAssistments> find(ColumnNames cn, String value) throws ReferenceNotFoundException;

}
