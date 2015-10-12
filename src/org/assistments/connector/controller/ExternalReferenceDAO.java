package org.assistments.connector.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.Random;

import org.assistments.dao.ConnectionFactory;

public class ExternalReferenceDAO {

	public static String createExternalReference(String table, String partnerUid,
			long dbid) {
		byte[] reference = new byte[16];
		Random random = new Random(); 
		random.nextBytes(reference);
		String hexReference = byteArrayToHexadecimalString(reference);
		Connection conn = null;
		PreparedStatement pstm = null;

		String query = "select id from external_reference_types where table_name = ?";
		long typeId = 0;
		long partnerId = 0;
		try  {
			conn = ConnectionFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(query);
			
			pstm.setString(1, table);
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) {
				typeId = rs.getLong("id");
			}
			rs.close();
			pstm.close();
			
			query = "select id from api_partners where partner_reference = ?";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, partnerUid);
			rs = pstm.executeQuery();
			if(rs.next()) {
				partnerId = rs.getLong("id");
			}
			rs.close();
			pstm.close();
			
			query = "insert into external_references "
					+ "(external_reference, partner_id, type_id, db_id) "
					+ "values (?,?,?,?)";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, hexReference);
			pstm.setLong(2, partnerId);
			pstm.setLong(3, typeId);
			pstm.setLong(4, dbid);
			rs.close();
			pstm.close();
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstm != null) {
				try {
					pstm.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
				if(conn != null) {
					try {
						conn.close();
					} catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return hexReference;

	}
	
	public static String byteArrayToHexadecimalString(byte[] data) {
		Formatter formatter = new Formatter();
		for (byte b : data) {
			formatter.format("%02x", b);
		}
		String hex = formatter.toString();
		formatter.close();
		return hex;
	}
}
