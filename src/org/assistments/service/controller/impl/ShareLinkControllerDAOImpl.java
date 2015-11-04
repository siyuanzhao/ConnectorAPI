package org.assistments.service.controller.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.dao.ConnectionFactory;
import org.assistments.service.controller.ShareLinkController;
import org.assistments.service.dao.ExternalReferenceDao;
import org.assistments.service.dao.jdbc.JdbcExternalReferenceDao;
import org.assistments.service.domain.ProblemSet;
import org.assistments.service.domain.ShareLink;
import org.assistments.service.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class ShareLinkControllerDAOImpl implements ShareLinkController {
	
	String partnerRef;
	JdbcTemplate jdbcTemplate;
	DataSource dataSource;
	
	ExternalReferenceDao externalRefDao;
	
	{
		dataSource = ConnectionFactory.getDataSource();
		jdbcTemplate = new JdbcTemplate(dataSource);
		externalRefDao = new JdbcExternalReferenceDao(dataSource);
	}
	
	public ShareLinkControllerDAOImpl(String partnerRef) {
		this.partnerRef = partnerRef;
		
	}
	
	@Override
	public String create(String userId, String problemSetId, String recipient, boolean reShareable) {
		String shareLinkRef = new String();
		final long userIdLong = Long.valueOf(userId);
		final long problemSetIdLong = Long.valueOf(problemSetId);
		
		if(ShareLink.GENERIC.equals(recipient)) {
			//first check if the generic share link exist
			if(isGenericShareLinkExists(problemSetIdLong, userIdLong)) {
				//get share link id from table
				shareLinkRef = getExternalRefForGenericLink(problemSetIdLong, userIdLong);
				return shareLinkRef;
			}
		}
		final String sql = "INSERT INTO share_links (distributor_id, problem_set_id, recipient, reShareable, url, form) VALUES (?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		final String email = recipient;
		final boolean b = reShareable;

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, new String[] { "id" });
				ps.setLong(1, userIdLong);
				ps.setLong(2, problemSetIdLong);
				ps.setString(3, email);
				ps.setBoolean(4, b);
				ps.setString(5, "");
				ps.setString(6, "");
				return ps;
			}
		}, keyHolder);

		long dbid = keyHolder.getKey().longValue();
		shareLinkRef = externalRefDao.createExternalReference("share_links", partnerRef, dbid);
		
		
		return shareLinkRef;
	}
	
	@Override
	public String create(String userId, String problemSetId, String recipient,
			boolean reShareable, String url, String form, boolean isAssistmentsVerified) {
		String shareLinkRef = new String();
		final long userIdLong = Long.valueOf(userId);
		final long problemSetIdLong = Long.valueOf(problemSetId);
		final String urlFinal = url;
		final String formFinal = form;
		if(ShareLink.GENERIC.equals(recipient)){
			//first check if the generic share link exists
			if(isGenericShareLinkExists(problemSetIdLong, userIdLong, url, form, isAssistmentsVerified)){
				//get share link id from table
				shareLinkRef = getExternalRefForGenericLink(problemSetIdLong, userIdLong, url, form, isAssistmentsVerified);
				return shareLinkRef;
			}
		}
		final String sql = "INSERT INTO share_links (distributor_id, problem_set_id, recipient, reShareable, url, form, assistments_verified) VALUES (?, ?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		final String email = recipient;
		final boolean b = reShareable;
		final boolean v = isAssistmentsVerified;
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
				ps.setLong(1, userIdLong);
				ps.setLong(2, problemSetIdLong);
				ps.setString(3, email);
				ps.setBoolean(4, b);
				ps.setString(5, urlFinal);
				ps.setString(6, formFinal);
				ps.setBoolean(7, v);
				return ps;
			}
		}, keyHolder);
		
		long dbid = keyHolder.getKey().longValue();
		shareLinkRef = externalRefDao.createExternalReference("share_links", partnerRef, dbid);
		return shareLinkRef;
	}

	@Override
	public ShareLink find(String shareLinkRef)
			throws ReferenceNotFoundException {
		ShareLink sl = null;
		try {
			sl = getShareLinkByRef(shareLinkRef);
		} catch (SQLException e) {
			throw new ReferenceNotFoundException(e);
		}
		return sl;
	}

	public ShareLink getShareLinkByRef(String shareLinkRef) 
			throws SQLException, ReferenceNotFoundException {
		ShareLink shareLink = new ShareLink();

		ProblemSet problemSet = new ProblemSet();
		User distributor = new User();
		String query = "SELECT s.id, s.name, u.email, ud.first_name, ud.last_name, ud.display_name, sl.url, sl.form, sl.recipient, sl.assistments_verified "
				+ "FROM external_references er "
				+ "JOIN external_reference_types ert ON ert.id = er.type_id "
				+ "JOIN api_partners ap ON ap.id = er.partner_id "
				+ "JOIN share_links sl ON sl.id = er.db_id "
				+ "JOIN sequences s ON s.id = sl.problem_set_id "
				+ "JOIN users u ON u.id = sl.distributor_id "
				+ "JOIN user_details ud ON ud.user_id = u.id "
				+ "WHERE er.external_reference = ? and ap.partner_reference = ? and ert.table_name = 'share_links'";
		// access to database to gather all information for this share link
		Connection conn = null;
		PreparedStatement pstm = null;
		conn = ConnectionFactory.getInstance().getConnection();
		pstm = conn.prepareStatement(query);
		pstm.setString(1, shareLinkRef);
		pstm.setString(2, partnerRef);
		ResultSet rs = pstm.executeQuery();

		if (rs.next()) {
			problemSet.setDecodedID(rs.getInt(1));
			problemSet.setName(rs.getString(2));

			distributor.setEmail(rs.getString(3));
			distributor.setFirstName(rs.getString(4));
			distributor.setLastName(rs.getString(5));
			distributor.setDisplayName(rs.getString(6));

			shareLink.setDistributor(distributor);
			shareLink.setProblemSet(problemSet);
			shareLink.setUrl(rs.getString(7));
			shareLink.setForm(rs.getString(8));
			shareLink.setRecipient(rs.getString(9));
			shareLink.setAssistmentsVerified(rs.getBoolean(10));
		} else {
			throw new ReferenceNotFoundException("Cannot find ShareLink with external reference = " + shareLinkRef);
		}
		if(rs != null)
			rs.close();
		if(pstm != null)
			pstm.close();
		if(conn != null)
			conn.close();
		return shareLink;
	}
	
	protected String getExternalRefForGenericLink(long problemSetId, long distributorId) {
		String query = "SELECT id FROM share_links WHERE problem_set_id = ? AND distributor_id = ? AND recipient = ? AND url = ? AND form = ? AND assistments_verified = ?";
		
		Long dbid = jdbcTemplate.queryForObject(query, Long.class, problemSetId, distributorId, ShareLink.GENERIC, "", "", false);
		
		String ref = externalRefDao.getReferenceForEntity("share_links", partnerRef, dbid);
		return ref;
	}
	
	protected String getExternalRefForGenericLink(long problemSetId, long distributorId, String url, String form, boolean isAssistmentsVerified) {
		String query = "SELECT id FROM share_links WHERE problem_set_id = ? AND distributor_id = ? AND recipient = ? AND url = ? AND form = ? AND assistments_verified = ?";
		
		Long dbid = jdbcTemplate.queryForObject(query, Long.class, problemSetId, distributorId, ShareLink.GENERIC, url, form, isAssistmentsVerified);
		
		String ref = externalRefDao.getReferenceForEntity("share_links", partnerRef, dbid);
		return ref;
	}
	
	public static String getShareLinkRef(long problemSetId, long distributorId, long typeId, String partnerRef) 
			throws SQLException, ReferenceNotFoundException {
		String ref = new String();
		Connection conn = null;
		PreparedStatement pstm = null;
		
		String query = "SELECT * FROM share_links WHERE problem_set_id = ? AND distributor_id = ? AND type_id = ?";
		
		conn = ConnectionFactory.getInstance().getConnection();
		pstm = conn.prepareStatement(query);
			
		pstm.setLong(1, problemSetId);
		pstm.setLong(2, distributorId);
		pstm.setLong(3, typeId);
			
		ResultSet rs = pstm.executeQuery();
		if(rs.next()) {
			long dbId = rs.getLong("id");
			ref = getShareLinkRef(dbId, partnerRef);
		} else {
			throw new ReferenceNotFoundException(String.format("Cannot find share link with problem set id = %s and distributor id = %s and typeId = %s",
					String.valueOf(problemSetId), String.valueOf(distributorId), String.valueOf(typeId)));
		}
		if(pstm != null)
			pstm.close();
		if(conn != null)
			conn.close();
		if(rs != null)
			rs.close();
		
		return ref;
	}
	
	public static String getShareLinkRef(long dbId, String partnerRef) {
		String ref = new String();
		Connection conn = null;
		PreparedStatement pstm = null;

		String query = "select id from external_reference_types where table_name = ?";
		long typeId = 0;
		long partnerId = 0;
		try  {
			conn = ConnectionFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(query);
			
			pstm.setString(1, "share_links");
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) {
				typeId = rs.getLong("id");
			}
			rs.close();
			pstm.close();
			
			query = "select id from api_partners where partner_reference = ?";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, partnerRef);
			rs = pstm.executeQuery();
			if(rs.next()) {
				partnerId = rs.getLong("id");
			}
			rs.close();
			pstm.close();
			
			query = "SELECT * FROM external_references WHERE db_id = ? AND partner_id = ? AND type_id = ?";
			pstm = conn.prepareStatement(query);
			pstm.setLong(1, dbId);
			pstm.setLong(2, partnerId);
			pstm.setLong(3, typeId);
			
			rs = pstm.executeQuery();
			if(rs.next()) {
				ref = rs.getString("external_reference");
			}
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
			}
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return ref;
	}
	
	public boolean isGenericShareLinkExists(long problemSetId, long distributorId) {
		String sql = "SELECT id FROM share_links WHERE problem_set_id = ? AND distributor_id = ? AND recipient = ? AND url = ? AND form = ? AND assistments_verified = ?";
		try {
			Integer id = jdbcTemplate.
					queryForObject(sql, Integer.class, problemSetId, distributorId, "generic", "", "", false);
			System.out.println(id);
		} catch(EmptyResultDataAccessException e1) {
			return false;
		}
		return true;
	}
	
	public boolean isGenericShareLinkExists(long problemSetId, long distributorId, String url, String form, boolean isAssistmentsVerified) {
		String sql = "SELECT id FROM share_links WHERE problem_set_id = ? AND distributor_id = ? AND recipient = ? AND url = ? AND form = ? AND assistments_verified = ?";
		try {
			Integer id = jdbcTemplate.
					queryForObject(sql, Integer.class, problemSetId, distributorId, "generic", url, form, isAssistmentsVerified);
		} catch(EmptyResultDataAccessException e) {
			return false;
		}
		return true;
	}
	
	/*
	public static String createShareLink(long problemSetId, long distributorId, String partnerRef, long typeId, String recipient) {
		String sql = "INSERT INTO share_links (problem_set_id, distributor_id, type_id, recipient) VALUES (?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement pstm = null;
		long linkId = 0;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setLong(1, problemSetId);
			pstm.setLong(2, distributorId);
			pstm.setLong(3, typeId);
			pstm.setString(4, recipient);
			int affectedRows = pstm.executeUpdate();
			if(affectedRows > 0) {
				ResultSet rs = pstm.getGeneratedKeys();
				if(rs.next()) {
					linkId = rs.getLong(1);
					String ref = ExternalReferenceDAO.createExternalReference("share_links", partnerRef, linkId);
					rs.close();
					pstm.close();
					conn.close();
					return ref;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstm != null) {
				try {
					pstm.close();
				} catch(SQLException e ) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e ) {
					e.printStackTrace();
				}
			}
		}
		return null;
		
	}*/
	
	//This method is only temporary for the convenience of modifying test 1 database
	public static void addCols(){
		Connection conn;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			String sql = "ALTER TABLE share_links ADD COLUMN reshareable BOOLEAN";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
			sql = "ALTER TABLE share_links ADD COLUMN url VARCHAR(256)";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
			sql = "ALTER TABLE share_links ADD COLUMN form VARCHAR(256)";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
			sql = "ALTER TABLE share_links ADD COLUMN recipient VARCHAR(64)";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
			sql = "UPDATE share_links SET recipient = 'generic'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
			sql = "UPDATE share_links SET reshareable = true";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
