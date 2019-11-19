package com.yumatechnical.konnectandroid.Model;

import androidx.annotation.NonNull;

import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

public class SMBConnection {

	private Connection connection;
	private AuthenticationContext authenticationContext;
	private Session session;
	private SMBClient client;
	private SmbConfig config;
	private DiskShare diskShare;
	private String server, domain, username, password, share, path;


	public Connection getConnection() {
		return connection;
	}

	public AuthenticationContext getAuthenticationContext() {
		return authenticationContext;
	}

	public Session getSession() {
		return session;
	}

	public SMBClient getClient() {
		return client;
	}

	public SmbConfig getConfig() {
		return config;
	}

	public DiskShare getDiskShare() {
		return diskShare;
	}

	public String getServer() {
		return server;
	}

	public String getDomain() {
		return domain;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getShare() {
		return share;
	}

	public String getPath() {
		return path;
	}


	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setAuthenticationContext(AuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void setClient(SMBClient client) {
		this.client = client;
	}

	public void setConfig(SmbConfig config) {
		this.config = config;
	}

	public void setDiskShare(DiskShare diskShare) {
		this.diskShare = diskShare;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setShare(String share) {
		this.share = share;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setServer(String server) {
		this.server = server;
	}

	@NonNull
	@Override
	public String toString() {
		return "SMBConnection {\n" +
				"\tConnection = "+ connection.toString()+ ";\n" +
				"\tAuthenticationContext = "+ authenticationContext+ ";\n" +
				"\tSession = "+ session+ ";\n" +
				"\tSMBClient = "+ client+ ";\n" +
				"\tSmbConfig = "+ config+ ";\n" +
				"\tDiskShare = "+ diskShare+ ";\n" +
				"\tserver = "+ server+ ", domain = "+ domain+ ", username = "+ username+
				", password = "+ password+ ", share = "+ share+ ", path = "+ path+ ";\n}";
	}
}
