package org.assistments.connector.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.assistments.connector.domain.ExternalUser;
import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistmentsLinks;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.utility.Constants;
import org.assistments.dao.ConnectionFactory;

public class ExternalUserDAO extends AbstractPartnerToAssistmentsDAO {
	
	String partnerRef;
	
	public ExternalUserDAO(String partnerRef) {
		this.partnerRef = partnerRef;
	}
	
	@Override
	public boolean isExists(ColumnNames cn, String value) {
		boolean b = false;
		
		switch(cn) {
		case PARTNER_EXTERNAL_REFERENCE:
			try {
				b = isUserExist(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		default:
			throw new UnsupportedOperationException();
		}
		
		return b;
		
	}

	@Override
	public void add(PartnerToAssistments pta) {
		ExternalUser eu = (ExternalUser) pta;
		if(pta.getExternalRefernceTypeId() == Constants.EXTERNAL_USER_TYPE_ID) {
			addNewUser(eu);
		} else {
			throw new UnsupportedOperationException("You only allow to add ExternalUser!");
		}
	}

	@Override
	public List<PartnerToAssistments> find(ColumnNames cn, String value) 
			throws ReferenceNotFoundException {
		List<PartnerToAssistments> l = new ArrayList<PartnerToAssistments>();
		
		PartnerToAssistments pta = new ExternalUser(partnerRef);
		switch(cn) {
		case PARTNER_EXTERNAL_REFERENCE:
			try {
				pta = findByPartnerExternalRef(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		case ASSISTMENTS_EXTERNAL_REFERENCE:
			try {
				pta = findByExternalRef(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		case ASSISTMENTS_ACCESS_TOKEN:
			try {
				pta = findByAccessToken(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		default:
			throw new UnsupportedOperationException("API doesn't this at this moment!");
		}
		
		l.add(pta);
		return l;
	}

	/**
	 * Check if user already has an account inside ASSISTments
	 * @param partnerExternalRef -- user external reference outside ASSISTments
	 * @return true -- if user exists; otherwise, it returns false
	 */
	boolean isUserExist(String partnerExternalRef) throws SQLException {
		boolean hasData = false;
		//SELECT * FROM partner_to_assistments_links WHERE partner_external_reference=? 
		//		AND external_reference_type_id=? AND api_partner_reference = ?
		String queryString = "SELECT * FROM "+PartnerToAssistmentsLinks.TABLE_NAME+" WHERE "
				+PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE+"=?"
				+ " AND "+PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistmentsLinks.API_PARTNER_REFERENCE+"= ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, partnerExternalRef);
		pstm.setInt(2, Constants.EXTERNAL_USER_TYPE_ID);
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
	 * 	Find external user by external reference outside ASSISTments
	 * @param partnerExternalRef -  user external reference outside ASSISTments
	 * @return ExternalUser object -- if user exists; otherwise, it should return null
	 */
	ExternalUser findByPartnerExternalRef(String partnerExternalRef) throws SQLException, ReferenceNotFoundException {
		ExternalUser user = null;
		//SELECT * FROM partner_to_assistments_links WHERE partner_external_reference=? 
		//		AND external_reference_type_id=? AND api_partner_reference = ?
		String queryString = "SELECT * FROM "+PartnerToAssistmentsLinks.TABLE_NAME+" WHERE "
				+PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE+"=?"
				+ " AND "+PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistmentsLinks.API_PARTNER_REFERENCE+"= ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, partnerExternalRef);
		pstm.setInt(2, Constants.EXTERNAL_USER_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();

		if (rs.next()) {
			user = new ExternalUser(partnerRef);
			user.setApiPartnerReference(rs.getString(PartnerToAssistmentsLinks.API_PARTNER_REFERENCE));
			user.setExternalRefernceTypeId(rs.getInt(PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID));
			user.setAssistmentsExternalRefernce(rs.getString(PartnerToAssistmentsLinks.ASSISTMENTS_EXTERNAL_REFERENCE));
			user.setAssistmentsAccessToken(rs.getString(PartnerToAssistmentsLinks.ASSISTMENTS_ACCESS_TOKEN));
			user.setPartnerExternalReference(rs.getString(PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE));
			user.setPartnerAccessToken(rs.getString(PartnerToAssistmentsLinks.PARTNER_ACCESS_TOKEN));
			user.setNote(rs.getString(PartnerToAssistmentsLinks.NOTE));
		} else {
			throw new ReferenceNotFoundException("Cannot find ExternalUser with partner_external_reference = " + partnerExternalRef);
		}
		if (pstm != null) {
			pstm.close();
		}
		if (conn != null) {
			conn.close();
		} 
		if(rs != null) {
			rs.close();
		}
		return user;
	}
	
	/**
	 * Find external user by external reference frorm ASSISTments
	 * @param externalRef -- user external reference from ASSISTments
	 * @return ExternalUser object if user exists; otherwise, it throws an exception
	 */
	ExternalUser findByExternalRef(String externalRef) throws SQLException, ReferenceNotFoundException {
		ExternalUser user = null;
		
		//SELECT * FROM partner_to_assistments_links WHERE assistments_external_reference=? 
		//		AND external_reference_type_id=? AND api_partner_reference = ?
		String queryString = "SELECT * FROM "+PartnerToAssistmentsLinks.TABLE_NAME+" WHERE "
				+PartnerToAssistmentsLinks.ASSISTMENTS_EXTERNAL_REFERENCE+"=?"
				+ " AND "+PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistmentsLinks.API_PARTNER_REFERENCE+"= ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, externalRef);
		pstm.setInt(2, Constants.EXTERNAL_USER_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();

		if (rs.next()) {
				user = new ExternalUser(partnerRef);
				user.setApiPartnerReference(rs.getString(PartnerToAssistmentsLinks.API_PARTNER_REFERENCE));
				user.setExternalRefernceTypeId(rs.getInt(PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID));
				user.setAssistmentsExternalRefernce(rs.getString(PartnerToAssistmentsLinks.ASSISTMENTS_EXTERNAL_REFERENCE));
				user.setAssistmentsAccessToken(rs.getString(PartnerToAssistmentsLinks.ASSISTMENTS_ACCESS_TOKEN));
				user.setPartnerExternalReference(rs.getString(PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE));
				user.setPartnerAccessToken(rs.getString(PartnerToAssistmentsLinks.PARTNER_ACCESS_TOKEN));
				user.setNote(rs.getString(PartnerToAssistmentsLinks.NOTE));
		} else {
			throw new ReferenceNotFoundException("Cannot find ExternalUser with assistments_external_reference = " + externalRef);
		}
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(rs != null) {
			rs.close();
		}
		return user;	
	}
	
	ExternalUser findByAccessToken (String accessToken) throws SQLException, ReferenceNotFoundException {
		ExternalUser user = null;

		//SELECT * FROM partner_to_assistments_links WHERE assistments_access_token=? 
		//		AND external_reference_type_id=? AND api_partner_reference = ?
		String queryString = "SELECT * FROM "+PartnerToAssistmentsLinks.TABLE_NAME+" WHERE "
				+PartnerToAssistmentsLinks.ASSISTMENTS_ACCESS_TOKEN+"=?"
				+ " AND "+PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
				+PartnerToAssistmentsLinks.API_PARTNER_REFERENCE+"= ?";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(queryString);
		pstm.setString(1, accessToken);
		pstm.setInt(2, Constants.EXTERNAL_USER_TYPE_ID);
		pstm.setString(3, partnerRef);
		ResultSet rs = pstm.executeQuery();

		if (rs.next()) {
				user = new ExternalUser(partnerRef);
				user.setApiPartnerReference(rs.getString(PartnerToAssistmentsLinks.API_PARTNER_REFERENCE));
				user.setExternalRefernceTypeId(rs.getInt(PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID));
				user.setAssistmentsExternalRefernce(rs.getString(PartnerToAssistmentsLinks.ASSISTMENTS_EXTERNAL_REFERENCE));
				user.setAssistmentsAccessToken(rs.getString(PartnerToAssistmentsLinks.ASSISTMENTS_ACCESS_TOKEN));
				user.setPartnerExternalReference(rs.getString(PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE));
				user.setPartnerAccessToken(rs.getString(PartnerToAssistmentsLinks.PARTNER_ACCESS_TOKEN));
				user.setNote(rs.getString(PartnerToAssistmentsLinks.NOTE));
		} else {
			throw new ReferenceNotFoundException("Cannot find ExternalUser with "
					+ "assistments_access_token = " + accessToken);
		}
		
		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(rs != null) {
			rs.close();
		}
		return user;	
	}
	
	@Deprecated
	public String getUserRef(String partnerExternalRef) {
		String userRef = "";
		
		try {
			//SELECT * FROM partner_to_assistments_links WHERE partner_external_reference=? 
			//		AND external_reference_type_id=? AND api_partner_reference = ?
			String queryString = "SELECT * FROM "+PartnerToAssistmentsLinks.TABLE_NAME+" WHERE "+
					PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE+"=?"
					+ " AND "+PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
					+PartnerToAssistmentsLinks.API_PARTNER_REFERENCE+" = ?";
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement pstm = conn.prepareStatement(queryString);
			pstm.setString(1, partnerExternalRef);
			pstm.setInt(2, Constants.EXTERNAL_USER_TYPE_ID);
			pstm.setString(3, partnerRef);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				userRef = rs.getString("external_reference");
				break;
			}
			try {
				if (pstm != null)
					pstm.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return userRef;
	}
	
	@Deprecated
	public String getUserAccessToken(String externalRef) {
		String assist_user_token = "";
		
		try {
			//SELECT * FROM partner_to_assistments_links WHERE assistments_external_reference=? 
			//		AND external_reference_type_id=? AND api_partner_reference = ?
			String queryString = "SELECT * FROM "+PartnerToAssistmentsLinks.TABLE_NAME+" WHERE "+
					PartnerToAssistmentsLinks.ASSISTMENTS_EXTERNAL_REFERENCE+"=?"
					+ " AND "+PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID+"=? AND "
					+PartnerToAssistmentsLinks.API_PARTNER_REFERENCE+" = ?";
			
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement pstm = conn.prepareStatement(queryString);
			pstm.setString(1, externalRef);
			pstm.setInt(2, Constants.EXTERNAL_USER_TYPE_ID);
			pstm.setString(3, partnerRef);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				assist_user_token = rs.getString("user_access_token");
				break;
			}
			try {
				if (pstm != null)
					pstm.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return assist_user_token;
	}
	
	/**
	 * Add a new external user into the table.
	 * @param externalUser -- ExternalUser object (Make sure every field is not null)
	 */
	void addNewUser(ExternalUser externalUser) {
		try {		
			//INSERT INTO partner_to_assistments_links(api_partner_reference, 
			//		external_reference_type_id, assistments_external_reference, partner_external_reference,
			//		assistments_access_token) VALUES(?,?,?,?,?)
			String queryString = "INSERT INTO "+PartnerToAssistmentsLinks.TABLE_NAME
					+"("+PartnerToAssistmentsLinks.API_PARTNER_REFERENCE+", "+PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID
					+", "+PartnerToAssistmentsLinks.ASSISTMENTS_EXTERNAL_REFERENCE+", "+
					PartnerToAssistmentsLinks.ASSISTMENTS_ACCESS_TOKEN+", "+
					PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE+", "+
					PartnerToAssistmentsLinks.PARTNER_ACCESS_TOKEN+", "+
					PartnerToAssistmentsLinks.NOTE+") VALUES(?,?,?,?,?,?,?)";
			Connection conn = ConnectionFactory.getInstance().getConnection();

			PreparedStatement pstm = conn.prepareStatement(queryString);
			pstm.setString(1, externalUser.getApiPartnerReference());
			pstm.setInt(2, externalUser.getExternalRefernceTypeId());
			pstm.setString(3, externalUser.getAssistmentsExternalRefernce());
			pstm.setString(4, externalUser.getAssistmentsAccessToken());
			pstm.setString(5, externalUser.getPartnerExternalReference());
			pstm.setString(6, externalUser.getPartnerAccessToken());
			pstm.setString(7, externalUser.getNote());
			pstm.executeUpdate();

			try {
				if (pstm != null)
					pstm.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Update access token for the user
	 * @param accessToken -- access token from ASSISTments
	 * @param partnerExternalRef -- user external reference outside ASSISTments
	 */
	public void updateAccessToken(String accessToken, String partnerExternalRef) {
		try {
			//UPDATE partner_to_assistments_links SET assistments_access_token=? WHERE partner_external_reference=? 
			//		AND external_reference_type_id=? AND api_partner_reference = ?
			String queryString = "UPDATE "+PartnerToAssistmentsLinks.TABLE_NAME+" SET "+
					PartnerToAssistmentsLinks.ASSISTMENTS_ACCESS_TOKEN+"=? WHERE "+
					PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE+"=? "
					+ "AND "+PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID+
					"=? AND "+PartnerToAssistmentsLinks.API_PARTNER_REFERENCE+" = ?";
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement pstm = conn.prepareStatement(queryString);
			pstm.setString(1, accessToken);
			pstm.setString(2, partnerExternalRef);
			pstm.setInt(3, Constants.EXTERNAL_USER_TYPE_ID);
			pstm.setString(4, partnerRef );
			pstm.executeUpdate();

			try {
				if (pstm != null)
					pstm.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
