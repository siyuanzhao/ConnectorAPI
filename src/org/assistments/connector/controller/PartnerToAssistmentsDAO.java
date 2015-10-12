package org.assistments.connector.controller;

import java.sql.SQLException;
import java.util.List;

import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.exception.ReferenceNotFoundException;

/**
 * This interface contains the operations on table {@value #TABLE_NAME}.
 * @author szhao
 *
 */
public interface PartnerToAssistmentsDAO {
	
	static final String TABLE_NAME = "partner_external_references";
	
//	static final String TABLE_NAME = "partner_to_assistments_links";
	
	/**
	 * Check if certain entry already exists in the table based on column name and value
	 * @param cn -- column name in table {@value #TABLE_NAME}
	 * @param value -- value for the column
	 * @return true -- if the entry exists
	 * @return false -- if the entry doesn't exist
	 * @throws SQLException
	 */
	public boolean isExists(PartnerToAssistments.ColumnNames cn, String value);
	
	/**
	 * Add a new entry to table {@value #TABLE_NAME}
	 * @param pta -- object needs to be added into table
	 * @throws SQLException
	 */
	public void add(PartnerToAssistments pta);
	
	/**
	 * Find one or many certain entries from table {@value #TABLE_NAME}
	 * @param cn -- column name
	 * @param value -- value for this column
	 * @return a list of entries
	 * @throws SQLException
	 * @throws ReferenceNotFoundException
	 */
	public List<PartnerToAssistments> find(PartnerToAssistments.ColumnNames cn, String value) 
			throws ReferenceNotFoundException;
	
	/**
	 * Update certain entry in table {@value #TABLE_NAME}
	 * @param pta -- object needs to be updated
	 * @throws SQLException
	 */
	public void update(PartnerToAssistments pta);

}
