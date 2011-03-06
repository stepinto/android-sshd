package com.github.stepinto.asshd;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;

public class SimplePublicKeyAuthenticator implements PublickeyAuthenticator {
	
	private List<PublicKey> keys = new ArrayList<PublicKey>();
	
	public void addKey(PublicKey key) {
		keys.add(key);
	}

	@Override
	public boolean authenticate(String user, PublicKey key, ServerSession session) {
		for (PublicKey k : keys) {
			if (key.equals(k)) {
				return true;
			}
		}
		return false;
	}

}
