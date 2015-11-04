package org.assistments.connector.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.assistments.connector.domain.ExternalSchool;
import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistmentsLinks;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.utility.Constants;
import org.assistments.dao.ConnectionFactory;

public class ExternalSchoolDAO extends AbstractPartnerToAssistmentsDAO {

	String partnerRef;
	
	public ExternalSchoolDAO(String partnerRef) {
		this.partnerRef = partnerRef;
	}
	
	
	@Override
	public boolean isExists(ColumnNames cn, String value) {
		boolean b = false;
		
		switch(cn) {
		case PARTNER_EXTERNAL_REFERENCE:
			try {
				b = isSchoolExist(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		default:
			throw new UnsupportedOperationException("API doesn't support it at this moment!");
		}
		
		return b;
	}

	@Override
	public void add(PartnerToAssistments pta) {
		try {
			addNewSchool(pta);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public List<PartnerToAssistments> find(ColumnNames cn, String value)
			throws ReferenceNotFoundException {
		List<PartnerToAssistments> l = new ArrayList<PartnerToAssistments>();
		PartnerToAssistments pta = new ExternalSchool(partnerRef);
		
		switch(cn) {
		case PARTNER_EXTERNAL_REFERENCE:
			try {
				pta = findByExternalPartnerRef(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		default:
			throw new UnsupportedOperationException("API doesn't support it at this moment!");
		}
		
		l.add(pta);
		return l;		
	}
	
	/**
	 * Check if school already exists
	 * @param partnerExternalRef -- school external referencec outside ASSISTments
	 * @return true -- if school exists; otherwise it returns false
	 */
	public boolean isSchoolExist(String partnerExternalRef) throws SQLException {
		boolean hasData = false;
		
		//SELECT * FROM partner_to_assistments_links WHERE partner_external_reference=? 
		//		AND external_reference_type_id=? AND api_partner_reference = ?
		String queryString = "SELECT * FROM "+PartnerToAssistments.TABLE_NAME+" WHERE "+
				PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE+"=?"
				+ " AND "+PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistments.API_PARTNER_REFERENCE+" = ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, partnerExternalRef);
		pstm.setInt(2, Constants.EXTERNAL_SCHOOL_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();

		if (rs.next()) {
				hasData = true;
		}
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(rs != null)
			rs.close();
		
		return hasData;
		
	}
	
	/**
	 * Add a new school into the table -- before calling this function, make sure the school doesn't exist
	 * @param externalSchool -- ExternalSchool object for this school
	 */
	public void addNewSchool(PartnerToAssistments externalSchool) throws SQLException {
		//INSERT INTO partner_to_assistments_links(api_partner_reference, 
		//		external_reference_type_id, assistments_external_reference, partner_external_reference,
		//		assistments_access_token) VALUES(?,?,?,?,?)
		String queryString = "INSERT INTO "+PartnerToAssistments.TABLE_NAME
				+"("+PartnerToAssistments.API_PARTNER_REFERENCE+", "+PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID
				+", "+PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE+", "+
				PartnerToAssistments.ASSISTMENTS_ACCESS_TOKEN+", "+
				PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE+", "+
				PartnerToAssistments.PARTNER_ACCESS_TOKEN+", "+
				PartnerToAssistments.NOTE+") VALUES(?,?,?,?,?,?,?)";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, externalSchool.getApiPartnerReference());
		pstm.setInt(2, externalSchool.getExternalRefernceTypeId());
		pstm.setString(3, externalSchool.getAssistmentsExternalRefernce());
		pstm.setString(4,	externalSchool.getAssistmentsAccessToken());
		pstm.setString(5,	externalSchool.getPartnerExternalReference());
		pstm.setString(6, externalSchool.getPartnerAccessToken());
		pstm.setString(7, externalSchool.getNote());
		int affectedRows = pstm.executeUpdate();
		
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(affectedRows <= 0) {
			throw new SQLException(String.format("Cannot insert ExternalSchool with %s = %s and %s = %s",
					PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE, externalSchool.getAssistmentsExternalRefernce(),
					PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE, externalSchool.getPartnerExternalReference()));
		}
	}
	
	public PartnerToAssistments findByExternalPartnerRef(String externalPartnerRef) 
			throws SQLException, ReferenceNotFoundException{
		PartnerToAssistments school = new ExternalSchool(partnerRef);
		
		String queryString = "SELECT * FROM "+PartnerToAssistments.TABLE_NAME+" WHERE "+
				PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE+"=?"
				+ " AND "+PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistments.API_PARTNER_REFERENCE+" = ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, externalPartnerRef);
		pstm.setInt(2, Constants.EXTERNAL_SCHOOL_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();

		if (rs.next()) {
			school.setApiPartnerReference(rs.getString(PartnerToAssistments.API_PARTNER_REFERENCE));
			school.setExternalRefernceTypeId(rs.getInt(PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID));
			school.setAssistmentsExternalRefernce(rs.getString(PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE));
			school.setAssistmentsAccessToken(rs.getString(PartnerToAssistments.ASSISTMENTS_ACCESS_TOKEN));
			school.setPartnerExternalReference(rs.getString(PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE));
			school.setPartnerAccessToken(rs.getString(PartnerToAssistments.PARTNER_ACCESS_TOKEN));
			school.setNote(rs.getString(PartnerToAssistments.NOTE));
		} else {
			throw new ReferenceNotFoundException(String.format("Cannot find ExternalSchool with %s = %s",
					PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE, externalPartnerRef));
		}
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(rs != null)
			rs.close();
		
		return school;
	}
}
