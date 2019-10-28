package com.yumatechnical.konnectandroid.Helper.Network;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * String filename = documents.get(0).getUNCPath();
 *             try (File f = Smb.open(filename)){
 *
 *                Process the file code...
 *
 *                 f.closeSilently();
 *             }
 *
 *
 * And:
 *
 * while(i.hasNext()){
 *         String filename =  (String)i.next();
 *         Log.info("FILENAME "+filename);
 *         try(File f = Smb.open(filename)){
 *
 *            Process the file stuff here
 *
 *         }
 *     }
 */
public class Smb {

	private static SMBClient client;
	private static String[] DFSMounts = {"DFS1", "dfs1"};
	private static final Logger Log = LoggerFactory.getLogger(Smb.class);
	private static HashMap<String, DiskShare> shares = new HashMap<>();
	private static HashMap<String, Connection> connections = new HashMap<>();
	private static HashMap<Connection, Session> sessions = new HashMap<>();

	private synchronized static SMBClient getClient() {
		if (client == null) {
			SmbConfig cfg = SmbConfig.builder().withDfsEnabled(true).build();
			client = new SMBClient(cfg);
		}
		return client;
	}

	private synchronized static Connection getConnection(String realDomainName) throws IOException {
		Log.info("DOMAIN NAME " + realDomainName);
		Connection connection = (connections.get(realDomainName) == null) ? client.connect(realDomainName) : connections.get(realDomainName);
		if (!connection.isConnected()) {
			connection.close();
			sessions.remove(connection);
			connection = client.connect(realDomainName);

		}
		// connection = client.connect(realDomainName);
		connections.put(realDomainName, connection);
		return connection;


	}
/*
	private synchronized static Session getSession(Connection connection, SMBClient client) {
		Session session = sessions.get(connection);
		if (session == null) {
			PropertiesCache props = PropertiesCache.getInstance();
			String sambaUsername = props.getProperty("smb.user");
			String sambaPass = props.getProperty("smb.password");
			String sambaDomain = props.getProperty("smb.domain");
			Log.info("CLIENT " + client);

			session = (sessions.get(connection) != null) ? sessions.get(connection) : connection.authenticate(new AuthenticationContext(sambaUsername, sambaPass.toCharArray(), sambaDomain));

			sessions.put(connection, session);
		}
		return session;
	}

	@SuppressWarnings("UnusedReturnValue")
	public synchronized static DiskShare getShare(String domainName, String shareName) throws SmbException {
		DiskShare share = shares.get(domainName + "/" + shareName);
		if ((share != null) && (!share.isConnected())) share = null;
		if (share == null) {
			try {
				PropertiesCache props = PropertiesCache.getInstance();
				String sambaUsername = props.getProperty("smb.user");
				String sambaPass = props.getProperty("smb.password");
				String sambaDomain = props.getProperty("smb.domain");
				String dfsIP = props.getProperty("smb.sambaIP");

				SMBClient client = getClient();

				String realDomainName = (Arrays.stream(DFSMounts).anyMatch(domainName::equals)) ? dfsIP : domainName;
				Connection connection = getConnection(realDomainName);
				Session session = getSession(connection, client);

				share = (DiskShare) session.connectShare(shareName);
				shares.put(domainName + "/" + shareName, share);
			} catch (Exception e) {
				Log.info("EXCEPTION E " + e);
				Log.info("EX " + e.getMessage());

				throw new SmbException();
			}
		}
		return (share);
	}

	public static String fixFilename(String filename) {
		String[] parts = filename.split("\\\\");
		ArrayList<String> partsList = new ArrayList<>(Arrays.asList(parts));
		partsList.remove(0);
		partsList.remove(0);
		partsList.remove(0);
		partsList.remove(0);
		return String.join("/", partsList);
	}


	public static File open(String filename) throws SmbException {
		String[] parts = filename.split("\\\\");
		String domainName = parts[2];
		String shareName = parts[3];
		DiskShare share = getShare(domainName, shareName);
		Set<SMB2ShareAccess> s = new HashSet<>();
		s.add(SMB2ShareAccess.ALL.iterator().next());
		filename = fixFilename(filename);
		return (share.openFile(filename, EnumSet.of(AccessMask.GENERIC_READ), null, s, SMB2CreateDisposition.FILE_OPEN, null));
	}

*/
}