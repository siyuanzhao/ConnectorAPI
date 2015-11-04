package org.assistments.connector.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.assistments.connector.domain.ExternalStudentClassSection;
import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistmentsLinks;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.utility.Constants;
import org.assistments.dao.ConnectionFactory;

public class ExternalStudentClassSectionDAO extends AbstractPartnerToAssistmentsDAO {

	String partnerRef;
	
	
	public ExternalStudentClassSectionDAO(String partnerRef) {
		this.partnerRef = partnerRef;
	}
	
	@Override
	public boolean isExists(ColumnNames cn, String value) {
		boolean b = false;
		
		switch(cn) {
		case PARTNER_EXTERNAL_REFERENCE:
			try {
				b = isClassExist(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		default:
			throw new UnsupportedOperationException("API doesn't support this at this moment!");
		}
		
		return b;
	}


	@Override
	public void add(PartnerToAssistments pta) {
		ExternalStudentClassSection stuClass = (ExternalStudentClassSection)pta;
		
		try {
			addNewClass(stuClass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}


	@Override
	public List<PartnerToAssistments> find(ColumnNames cn, String value)
			throws ReferenceNotFoundException {
		List<PartnerToAssistments> l = new ArrayList<PartnerToAssistments>();
		
		switch(cn) {
		case PARTNER_EXTERNAL_REFERENCE:
			try {
				l = findByPartnerExternalRef(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		case ASSISTMENTS_EXTERNAL_REFERENCE:
			try {
				l = findByExternalRef(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		case ASSISTMENTS_ACCESS_TOKEN:
			try {
				l = findByAccessToken(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		default:
			throw new UnsupportedOperationException("API doesn't support this at this moment!");
		}
		
		return l;
	}
	
	
	/**
	 * Check if this class exist based on class external reference from outside ASSISTments
	 * @param partnerExternalRef -- class external reference outside ASSISTments
	 * @return true -- if the class exists; otherwise it returns false
	 */
	public boolean isClassExist(String partnerExternalRef) throws SQLException {
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
		pstm.setInt(2, Constants.EXTERNAL_CLASS_SECTION_TYPE_ID);
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
	
	@Deprecated
	public String getClassRef(String partnerExternalRef) {
		String classRef = "";
		
		try {
			//SELECT * FROM partner_to_assistments_links WHERE partner_external_reference=? 
			//		AND external_reference_type_id=? AND api_partner_reference = ?
			String queryString = "SELECT * FROM "+PartnerToAssistments.TABLE_NAME+" WHERE "+
					PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE+"=?"
					+ " AND "+PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
					+PartnerToAssistments.API_PARTNER_REFERENCE+" = ?";
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement pstm = conn.prepareStatement(queryString);
			pstm.setString(1, partnerExternalRef);
			pstm.setInt(2, Constants.EXTERNAL_CLASS_SECTION_TYPE_ID);
			pstm.setString(3,  partnerRef);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				classRef = rs.getString("external_reference");
				break;
			}
			try {
				if (pstm != null)
					pstm.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return classRef;
	}
	
	/**
	 * Add a new external class into the table. Before calling this function, make sure the class doesn't exist
	 * @param externalClass -- ExternalClass object for this class
	 */
	public void addNewClass(ExternalStudentClassSection externalClass) throws SQLException {
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
		pstm.setString(1, externalClass.getApiPartnerReference());
		pstm.setInt(2, externalClass.getExternalRefernceTypeId());
		pstm.setString(3, externalClass.getAssistmentsExternalRefernce());
		pstm.setString(4,	externalClass.getAssistmentsAccessToken());
		pstm.setString(5,	externalClass.getPartnerExternalReference());
		pstm.setString(6, externalClass.getPartnerAccessToken());
		pstm.setString(7, externalClass.getNote());
		int affectedRows = pstm.executeUpdate();
		
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		
		if(affectedRows <= 0) {
			throw new SQLException(String.format("Cannot insert ExternalStudentClass "
					+ "with assistments_external_reference = %s "
					+ "and partner_external_referenec = %s", externalClass.getAssistmentsExternalRefernce(), externalClass.getPartnerExternalReference()));
		}
	}
	
	public List<PartnerToAssistments> findByAccessToken(String accessToken) throws SQLException, ReferenceNotFoundException {
		List<PartnerToAssistments> list = new ArrayList<PartnerToAssistments>();

		//SELECT * FROM partner_to_assistments_links WHERE assistments_access_token=? 
		//		AND external_reference_type_id=? AND api_partner_reference = ?
		String queryString = "SELECT * FROM "+PartnerToAssistments.TABLE_NAME+" WHERE "+
				PartnerToAssistments.ASSISTMENTS_ACCESS_TOKEN+"=?"
				+ " AND "+PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistments.API_PARTNER_REFERENCE+" = ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, accessToken);
		pstm.setInt(2, Constants.EXTERNAL_CLASS_SECTION_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();

		while(rs.next()) {
			PartnerToAssistments studentClass = new ExternalStudentClassSection(partnerRef);
			studentClass.setId(rs.getInt("id"));
			studentClass.setApiPartnerReference(rs.getString(PartnerToAssistments.API_PARTNER_REFERENCE));
			studentClass.setExternalRefernceTypeId(rs.getInt(PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID));
			studentClass.setAssistmentsExternalRefernce(rs.getString(PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE));
			studentClass.setAssistmentsAccessToken(rs.getString(PartnerToAssistments.ASSISTMENTS_ACCESS_TOKEN));
			studentClass.setPartnerExternalReference(rs.getString(PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE));
			studentClass.setPartnerAccessToken(rs.getString(PartnerToAssistments.PARTNER_ACCESS_TOKEN));
			studentClass.setNote(rs.getString(PartnerToAssistments.NOTE));
			list.add(studentClass);
		} 
		if(list.size() == 0) {
			throw new ReferenceNotFoundException(String.format("Cannot find ExternalStudentClass with %s = %s", 
					PartnerToAssistments.ASSISTMENTS_ACCESS_TOKEN, accessToken));
		}
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(rs != null) 
			rs.close();
		
		return list;
	}
	public List<PartnerToAssistments> findByPartnerExternalRef(String partnerExternalRef) throws SQLException, ReferenceNotFoundException {
		List<PartnerToAssistments> list = new ArrayList<PartnerToAssistments>();
		//SELECT * FROM partner_to_assistments_links WHERE partner_external_reference=?"
		//		+ " AND external_reference_type_id=? AND api_partner_reference = ?
		String queryString = "SELECT * FROM "+PartnerToAssistments.TABLE_NAME+" WHERE "+
				PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE+"=?"
				+ " AND "+PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistments.API_PARTNER_REFERENCE+" = ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, partnerExternalRef);
		pstm.setInt(2, Constants.EXTERNAL_CLASS_SECTION_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();

		while (rs.next()) {
			PartnerToAssistments studentClass = new ExternalStudentClassSection(partnerRef);
			studentClass.setId(rs.getInt("id"));
			studentClass.setApiPartnerReference(rs.getString(PartnerToAssistments.API_PARTNER_REFERENCE));
			studentClass.setExternalRefernceTypeId(rs.getInt(PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID));
			studentClass.setAssistmentsExternalRefernce(rs.getString(PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE));
			studentClass.setAssistmentsAccessToken(rs.getString(PartnerToAssistments.ASSISTMENTS_ACCESS_TOKEN));
			studentClass.setPartnerExternalReference(rs.getString(PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE));
			studentClass.setPartnerAccessToken(rs.getString(PartnerToAssistments.PARTNER_ACCESS_TOKEN));
			studentClass.setNote(rs.getString(PartnerToAssistments.NOTE));
			list.add(studentClass);
		} 
		if(list.size() == 0){
			throw new ReferenceNotFoundException(String.format("Cannot find ExternalStudentClass with %s = %s",
					PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE, partnerExternalRef));
		}
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(rs != null) 
			rs.close();
		return list;
	}
	
	public List<PartnerToAssistments> findByExternalRef(String externalRef) throws SQLException, ReferenceNotFoundException {
		List<PartnerToAssistments> list = new ArrayList<PartnerToAssistments>();

		//SELECT * FROM partner_to_assistments_links WHERE assistments_external_reference=?"
		//		+ " AND external_reference_type_id=? AND api_partner_reference = ?
		String queryString = "SELECT * FROM "+PartnerToAssistments.TABLE_NAME+" WHERE "+
				PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE+"=?"
				+ " AND "+PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistments.API_PARTNER_REFERENCE+" = ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, externalRef);
		pstm.setInt(2, Constants.EXTERNAL_CLASS_SECTION_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();
		while (rs.next()) {
			PartnerToAssistments studentClass = new ExternalStudentClassSection(partnerRef);
			studentClass.setId(rs.getInt("id"));
			studentClass.setApiPartnerReference(rs.getString(PartnerToAssistments.API_PARTNER_REFERENCE));
			studentClass.setExternalRefernceTypeId(rs.getInt(PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID));
			studentClass.setAssistmentsExternalRefernce(rs.getString(PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE));
			studentClass.setAssistmentsAccessToken(rs.getString(PartnerToAssistments.ASSISTMENTS_ACCESS_TOKEN));
			studentClass.setPartnerExternalReference(rs.getString(PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE));
			studentClass.setPartnerAccessToken(rs.getString(PartnerToAssistments.PARTNER_ACCESS_TOKEN));
			studentClass.setNote(rs.getString(PartnerToAssistments.NOTE));
			list.add(studentClass);
		} 
		if(list.size() == 0) {
			throw new ReferenceNotFoundException(String.format("Cannot find ExternalStudentClass with %s = %s",
					PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE, externalRef));
		}
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(rs != null)
			rs.close();
		
		return list;
	}
}
