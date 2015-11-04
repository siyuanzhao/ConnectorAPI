package org.assistments.connector.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistmentsLinks;
import org.assistments.dao.ConnectionFactory;

public abstract class AbstractPartnerToAssistmentsDAO implements PartnerToAssistmentsDAO {
	
	@Override
	public void update(PartnerToAssistments pta) {
		// UPDATE partner_to_assistments_links SET assistments_access_token=?,
		// partner_access_token = ? WHERE partner_external_reference=?
		// AND external_reference_type_id=? AND api_partner_reference = ? AND
		// assistments_external_reference = ?
		String queryString = "UPDATE " + PartnerToAssistments.TABLE_NAME + " SET "
				+ PartnerToAssistments.ASSISTMENTS_ACCESS_TOKEN + "=?,  "
				+ PartnerToAssistments.PARTNER_ACCESS_TOKEN + " = ?, " + PartnerToAssistments.NOTE + " = ?, "
						+ PartnerToAssistments.PARTNER_EXTERNAL_REFERENCE + "=?" + " WHERE "
				+ PartnerToAssistments.EXTERNAL_REFERENCE_TYPE_ID + "=? AND "
				+ PartnerToAssistments.API_PARTNER_REFERENCE + " = ? AND "
				+ PartnerToAssistments.ASSISTMENTS_EXTERNAL_REFERENCE + " = ?";
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement pstm = conn.prepareStatement(queryString);
			pstm.setString(1, pta.getAssistmentsAccessToken());
			pstm.setString(2, pta.getPartnerAccessToken());
			pstm.setString(3, pta.getNote());
			pstm.setString(4, pta.getPartnerExternalReference());
			pstm.setInt(5, pta.getExternalRefernceTypeId());
			pstm.setString(6, pta.getApiPartnerReference());
			pstm.setString(7, pta.getAssistmentsExternalRefernce());

			pstm.executeUpdate();

			if (pstm != null)
				pstm.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
