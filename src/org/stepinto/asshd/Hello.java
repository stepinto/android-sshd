package org.stepinto.asshd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.PublicKey;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.ForwardingFilter;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.os.Bundle;

public class Hello extends Activity {

	private static final int PORT = 8023;
	private static final String USERNAME = "stepinto";
	private static final String PASSWORD = "orange";

	private final Logger log = LoggerFactory.getLogger(Hello.class);
	private final SshServer sshd = SshServer.setUpDefaultServer();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		System.out.println("hello, world!");
		log.debug("Test debug");
		log.info("Test info");
		log.warn("Test warn");

		try {
			sshd.setPort(PORT);
			sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(
					"key.ser"));
			sshd.setShellFactory(new PseudoTerminalFactory("/system/bin/sh", "-i"));
			sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
				@Override
				public boolean authenticate(String username, String password,
						ServerSession arg2) {
					return username.equals(USERNAME)
							&& password.equals(PASSWORD);
				}
			});
			sshd.setPublickeyAuthenticator(new PublickeyAuthenticator() {
				@Override
				public boolean authenticate(String arg0, PublicKey arg1,
						ServerSession arg2) {
					return false;
				}
			});
			sshd.setForwardingFilter(new ForwardingFilter() {
				@Override
				public boolean canForwardAgent(ServerSession session) {
					return true;
				}
				@Override
				public boolean canForwardX11(ServerSession session) {
					return true;
				}
				@Override
				public boolean canListen(InetSocketAddress address,
						ServerSession session) {
					return true;
				}
				@Override
				public boolean canConnect(InetSocketAddress address,
						ServerSession session) {
					return true;
				}
			});

			sshd.start();
			log.info("SSHD is started.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}