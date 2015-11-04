package org.assistments.connector.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.assistments.connector.domain.ExternalAssignment;
import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistmentsLinks;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.utility.Constants;
import org.assistments.dao.ConnectionFactory;

public class ExternalAssignmentDAO extends AbstractPartnerToAssistmentsDAO {
	
	@Override
	public boolean isExists(ColumnNames cn, String value) {
		boolean b = false;
		
		switch(cn) {
		case PARTNER_EXTERNAL_REFERENCE:
			try {
				b = isAssignmentExist(value);
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
		ExternalAssignment ea = (ExternalAssignment) pta;
		if(ea.getExternalRefernceTypeId() == Constants.EXTERNAL_ASSIGNMENT_TYPE_ID)
			try {
				addNewAssignment(ea);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		else
			throw new UnsupportedOperationException("You only allow to add ExternalAssignment!");
		
	}
	
	@Override
	public List<PartnerToAssistments> find(ColumnNames cn, String value) throws ReferenceNotFoundException {
		List<PartnerToAssistments> l = new ArrayList<PartnerToAssistments>();
		
		PartnerToAssistments pta = new ExternalAssignment(partnerRef);
		
		switch(cn) {
		case ASSISTMENTS_EXTERNAL_REFERENCE:
			try {
				pta = findByExternalRef(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		case PARTNER_EXTERNAL_REFERENCE:
			try {
				pta = findByPartnerExternalRef(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		default:
			throw new UnsupportedOperationException("API doesn't support whatever you want to do at this moment");
		}
		
		l.add(pta);
		
		return l;
	}

	String partnerRef;

	public ExternalAssignmentDAO(String partnerRef) {
		this.partnerRef = partnerRef;
	}

	/**
	 * Check if one assignment exists based on partner_external_reference
	 * 
	 * @param partnerExternalRef
	 *            --
	 * @return true -- if assignment exists; otherwise it returns false
	 */
	public boolean isAssignmentExist(String partnerExternalRef) throws SQLException{
		boolean hasData = false;
			//SELECT * FROM partner_to_assistments_links WHERE partner_external_reference=?"
			//		+ " AND external_reference_type_id=? AND api_partner_reference = ?
		String queryString = "SELECT * FROM "+PartnerToAssistments.TABLE_NAME+" WHERE "+
				PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE+"=?"
				+ " AND "+PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistments.API_PARTNER_REFERENCE+" = ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, partnerExternalRef);
		pstm.setInt(2, Constants.EXTERNAL_ASSIGNMENT_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();

		if (rs.next()) {
			hasData = true;
		}
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(rs != null) {
			rs.close();
		}
		return hasData;
	}

	/**
	 * Add a new external assignment into the table -- before calling this function, make sure the assignment doesn't exist in the table
	 * @param externalAssignment -- ExternalAssignment object for this school
	 */
	public void addNewAssignment(ExternalAssignment externalAssignment) throws SQLException {
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
		pstm.setString(1, externalAssignment.getApiPartnerReference());
		pstm.setInt(2, externalAssignment.getExternalRefernceTypeId());
		pstm.setString(3, externalAssignment.getAssistmentsExternalRefernce());
		pstm.setString(4,	externalAssignment.getAssistmentsAccessToken());
		pstm.setString(5,	externalAssignment.getPartnerExternalReference());
		pstm.setString(6, externalAssignment.getPartnerAccessToken());
		pstm.setString(7, externalAssignment.getNote());
		int affectedRows = pstm.executeUpdate();
		if(affectedRows <= 0) {
			//it means insertion is not successful, throws an exception at this moment
			throw new SQLException(String.format("Cannot insert assignment with %s = %s",
					PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE, externalAssignment.getAssistmentsExternalRefernce()));
		}
		
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
	}

	/**
	 * Find external assignment based on assignment external reference outside ASSISTments
	 * @param partnerExternalRef -- assignment external reference outside ASSISTments
	 * @return ExternalAssignment object for this assignment; it returns null if the assignment doesn't exist
	 */
	public ExternalAssignment findByPartnerExternalRef(String partnerExternalRef) 
			throws ReferenceNotFoundException, SQLException{
		ExternalAssignment assignment = null;
			
		String queryString = "SELECT * FROM "+PartnerToAssistments.TABLE_NAME+" WHERE "+
				PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE+"=?"
				+ " AND "+PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistments.API_PARTNER_REFERENCE+" = ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, partnerExternalRef);
		pstm.setInt(2, Constants.EXTERNAL_ASSIGNMENT_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();
		if (rs.next()) {
			assignment = new ExternalAssignment(partnerRef);
			assignment.setApiPartnerReference(rs.getString(PartnerToAssistments.API_PARTNER_REFERENCE));
			assignment.setExternalRefernceTypeId(rs.getInt(PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID));
			assignment.setAssistmentsExternalRefernce(rs.getString(PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE));
			assignment.setAssistmentsAccessToken(rs.getString(PartnerToAssistments.ASSISTMENTS_ACCESS_TOKEN));
			assignment.setPartnerExternalReference(rs.getString(PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE));
			assignment.setPartnerAccessToken(rs.getString(PartnerToAssistments.PARTNER_ACCESS_TOKEN));
			assignment.setNote(rs.getString(PartnerToAssistments.NOTE));
		} else {
			String msg = String.format("Cannot find the assignment with %s = %s",
					PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE, partnerExternalRef);
			throw new ReferenceNotFoundException(msg);
		}
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(rs != null) {
			rs.close();
		}

		return assignment;
	}
	
	/**
	 * Find external assignment based on assignment external reference outside ASSISTments
	 * @param partnerExternalRef -- assignment external reference outside ASSISTments
	 * @return ExternalAssignment object for this assignment; it throws ReferenceNotFoundException if the assignment doesn't exist
	 */
	public ExternalAssignment findByExternalRef(String externalRef) throws ReferenceNotFoundException, SQLException {
		ExternalAssignment assignment = null;


		String queryString = "SELECT * FROM "+PartnerToAssistments.TABLE_NAME+" WHERE "+
				PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE+"=?"
				+ " AND "+PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistments.API_PARTNER_REFERENCE+" = ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, externalRef);
		pstm.setInt(2, Constants.EXTERNAL_ASSIGNMENT_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();
		if (rs.next()) {
			assignment = new ExternalAssignment(partnerRef);
			assignment.setApiPartnerReference(rs.getString(PartnerToAssistments.API_PARTNER_REFERENCE));
			assignment.setExternalRefernceTypeId(rs.getInt(PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID));
			assignment.setAssistmentsExternalRefernce(rs.getString(PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE));
			assignment.setAssistmentsAccessToken(rs.getString(PartnerToAssistments.ASSISTMENTS_ACCESS_TOKEN));
			assignment.setPartnerExternalReference(rs.getString(PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE));
			assignment.setPartnerAccessToken(rs.getString(PartnerToAssistments.PARTNER_ACCESS_TOKEN));
			assignment.setNote(rs.getString(PartnerToAssistments.NOTE));
		} else {
			String msg = String.format("Cannot find the assignment with %s = %s",
					PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE, externalRef);
			throw new ReferenceNotFoundException(msg);
		}
		if(rs != null) {
			rs.close();
		}
		if(pstm != null) {
			pstm.close();
		}
		if(conn != null) {
			conn.close();
		}
		
		return assignment;
	}
}
