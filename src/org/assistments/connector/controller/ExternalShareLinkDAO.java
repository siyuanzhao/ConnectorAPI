package org.assistments.connector.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.assistments.connector.domain.ExternalShareLink;
import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistmentsLinks;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.dao.ConnectionFactory;

public class ExternalShareLinkDAO extends AbstractPartnerToAssistmentsDAO {

	String partnerRef;
	
	public ExternalShareLinkDAO(String partnerRef) {
		this.partnerRef = partnerRef;
	}
	
	@Override
	public boolean isExists(ColumnNames cn, String value) {
		throw new UnsupportedOperationException("API doesn't support this method at this moment!");
	}

	@Override
	public void add(PartnerToAssistments pta) {
		ExternalShareLink esl = (ExternalShareLink)pta;
		
		try {
			save(esl);
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
				l = getExternalShareLinksByUser(value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			break;
		default:
			throw new UnsupportedOperationException("API doesn't support this at this moment!");
		}
		return l;
	}
	
	public void save(ExternalShareLink shareLink) throws SQLException {

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
		pstm.setString(1, shareLink.getApiPartnerReference());
		pstm.setInt(2, shareLink.getExternalRefernceTypeId());
		pstm.setString(3, shareLink.getAssistmentsExternalRefernce());
		pstm.setString(4, shareLink.getAssistmentsAccessToken());
		pstm.setString(5, shareLink.getPartnerExternalReference());
		pstm.setString(6, shareLink.getPartnerAccessToken());
		pstm.setString(7, shareLink.getNote());
		int affectedRows = pstm.executeUpdate();

		if (pstm != null)
			pstm.close();
		if (conn != null)
			conn.close();
		if(affectedRows <= 0) {
			throw new SQLException(String.format("Cannot insert ExternalShareLink with %s = %s and %s = %s", 
					PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE, shareLink.getAssistmentsExternalRefernce(),
					PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE, shareLink.getPartnerExternalReference()));
		}

	}
	
	/**
	 * 	Get all share links which one user have created assignment from
	 * @param thirdPartyID -- partner external reference for one user
	 * @return list of ExternalShareLink objects
	 */
	public List<PartnerToAssistments> getExternalShareLinksByUser(String thirdPartyID) 
			throws SQLException, ReferenceNotFoundException {
		List<PartnerToAssistments> shareLinks = new ArrayList<PartnerToAssistments>();
		//SELECT * FROM partner_to_assistments_links WHERE partner_external_reference=? 
		//		AND external_reference_type_id=? AND api_partner_reference = ?
		String query = "SELECT * FROM "+PartnerToAssistmentsLinks.TABLE_NAME+" WHERE "+
			PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE+"=?"
			+ " AND "+PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID+"=7 AND "
			+PartnerToAssistmentsLinks.API_PARTNER_REFERENCE+" = ?";

		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(query);
		pstm.setString(1, thirdPartyID);
		pstm.setString(2, partnerRef);
		ResultSet rs = pstm.executeQuery();
		int count = 0;
		while(rs.next()) {
			count++;
			PartnerToAssistments shareLink = new ExternalShareLink(partnerRef);
			shareLink.setId(rs.getInt("id"));
			shareLink.setApiPartnerReference(rs.getString(PartnerToAssistmentsLinks.API_PARTNER_REFERENCE));
			shareLink.setExternalRefernceTypeId(rs.getInt(PartnerToAssistmentsLinks.EXTERNAL_REFERENCE_TYPE_ID));
			shareLink.setAssistmentsExternalRefernce(rs.getString(PartnerToAssistmentsLinks.ASSISTMENTS_EXTERNAL_REFERENCE));
			shareLink.setAssistmentsAccessToken(rs.getString(PartnerToAssistmentsLinks.ASSISTMENTS_ACCESS_TOKEN));
			shareLink.setPartnerExternalReference(rs.getString(PartnerToAssistmentsLinks.PARTNER_EXTERNAL_REFERENCE));
			shareLink.setPartnerAccessToken(rs.getString(PartnerToAssistmentsLinks.PARTNER_ACCESS_TOKEN));
			shareLink.setNote(rs.getString(PartnerToAssistmentsLinks.NOTE));				
			shareLinks.add(shareLink);
		}
		if(count == 0) {
			throw new ReferenceNotFoundException(String.format("Cannot find any ExternalShareLink for %s = %s", 
					PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE, thirdPartyID));
		} else {
			return shareLinks;
		}

	}
}
