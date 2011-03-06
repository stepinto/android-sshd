package com.github.stepinto.asshd;

import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.session.ServerSession;

public class SimplePasswordAuthenticator implements PasswordAuthenticator {

	public String getPassword() {
		return password;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	@Override
	public boolean authenticate(String user, String password,
			ServerSession session) {
		return user.equals(this.user) && password.equals(this.password);
	}

	private String user;
	private String password;

}
