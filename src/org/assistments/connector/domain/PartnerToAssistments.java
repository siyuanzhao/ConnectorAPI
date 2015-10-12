package org.assistments.connector.domain;

/**
 * This interface represents an entry in table {@value #TABLE_NAME}.
 * {@link ColumnNames} contains all columns in the table.
 * @author szhao
 *
 */
public interface PartnerToAssistments {
	
	public static final String TABLE_NAME = "partner_external_references";
	
	//Below are column names
	public static final String ID = "id";
	public static final String API_PARTNER_REFERENCE = "partner_reference";
	public static final String EXTERNAL_REFERENCE_TYPE_ID = "external_reference_type_id";
	public static final String ASSISTMENTS_EXTERNAL_REFERENCE = "external_reference";
	public static final String ASSISTMENTS_ACCESS_TOKEN = "user_access_token";
	public static final String PARTNER_EXTERNAL_REFERENCE = "partner_external_reference";
	public static final String PARTNER_ACCESS_TOKEN = "user_connector_token";
	public static final String NOTE = "note";
	
	/*
	public static final String TABLE_NAME = "partner_to_assistments_links";
	
	//Below are column names
	public static final String ID = "id";
	public static final String API_PARTNER_REFERENCE = "api_partner_reference";
	public static final String EXTERNAL_REFERENCE_TYPE_ID = "external_reference_type_id";
	public static final String ASSISTMENTS_EXTERNAL_REFERENCE = "assistments_external_reference";
	public static final String ASSISTMENTS_ACCESS_TOKEN = "assistments_access_token";
	public static final String PARTNER_EXTERNAL_REFERENCE = "partner_external_reference";
	public static final String PARTNER_ACCESS_TOKEN = "partner_access_token";
	public static final String NOTE = "note";
	*/

	public static enum ColumnNames {
		ID(PartnerToAssistmentsLinks.ID),
		API_PARTNER_REFERENCE(PartnerToAssistmentsLinks.API_PARTNER_REFERENCE),
		EXTERNAL_REFERENCE_TYPE_ID(PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID),
		ASSISTMENTS_EXTERNAL_REFERENCE(PartnerToAssistmentsLinks.ASSISTMENTS_EXTERNAL_REFERENCE),
		ASSISTMENTS_ACCESS_TOKEN(PartnerToAssistmentsLinks.ASSISTMENTS_ACCESS_TOKEN),
		PARTNER_EXTERNAL_REFERENCE(PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE),
		PARTNER_ACCESS_TOKEN(PartnerToAssistmentsLinks.PARTNER_ACCESS_TOKEN),
		NOTE(PartnerToAssistmentsLinks.NOTE);
		
		private final String columnName;
		
		ColumnNames(String columnName) {
			this.columnName = columnName;
		}
		
		public String columnName() {
			return columnName;
		}
	}

	public int getId();
	public void setId(int id);
	
	
	public String getApiPartnerReference();
	public void setApiPartnerReference(String apiPartnerReference);
	
	
	public int getExternalRefernceTypeId();
	public void setExternalRefernceTypeId(int externalRefernceTypeId);
	
	
	public String getAssistmentsExternalRefernce();
	public void setAssistmentsExternalRefernce(String assistmentsExternalRefernce);
	
	
	public String getAssistmentsAccessToken();
	public void setAssistmentsAccessToken(String assistmentsAccessToken);
	
	
	public String getPartnerExternalReference();
	public void setPartnerExternalReference(String partnerExternalReference);
	
	public String getPartnerAccessToken();
	public void setPartnerAccessToken(String partnerAccessToken);
	
	public String getNote();
	public void setNote(String note);
}
