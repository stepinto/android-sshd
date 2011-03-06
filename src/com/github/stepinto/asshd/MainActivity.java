package com.github.stepinto.asshd;

import java.io.IOException;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stepinto.asshd.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private enum Status {
		STOPPED, STARTING, STARTED, STOPPING
	}

	private static final int PORT = 8022;

	private final Logger log = LoggerFactory.getLogger(MainActivity.class);
	private final SshServer sshd = SshServer.setUpDefaultServer();
	private final SimplePasswordAuthenticator passwordAuth = new SimplePasswordAuthenticator();
	private final SimplePublicKeyAuthenticator publicKeyAuth = new SimplePublicKeyAuthenticator();
	private final SimpleForwardingFilter forwardingFilter = new SimpleForwardingFilter();

	// UI components
	private Button startButton = null;
	private Status status = Status.STOPPED;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onStartButtonClicked();
			}
		});

		// Temporarily we add user for test
		passwordAuth.setUser("test");
		passwordAuth.setPassword("test");
	}

	private void onStartButtonClicked() {
		try {
			if (status == Status.STOPPED) {
				startButton.setEnabled(false);
				startButton.setText("Starting");
				status = Status.STARTING;

				sshd.setPort(PORT);
				sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(
						"key.ser"));
				sshd.setShellFactory(new PseudoTerminalFactory(
						"/system/bin/sh", "-i"));
				sshd.setPasswordAuthenticator(passwordAuth);
				sshd.setPublickeyAuthenticator(publicKeyAuth);
				sshd.setForwardingFilter(forwardingFilter);

				sshd.start();
				log.info("SSHD is started.");
				
				startButton.setEnabled(true);
				startButton.setText("Stop");
				status = Status.STARTED;
			}
			else if (status == Status.STARTED) {
				startButton.setEnabled(false);
				startButton.setText("Stopping");
				status = Status.STOPPING;
				
				sshd.stop();
				log.info("SSHD is stopped.");
				
				startButton.setEnabled(true);
				startButton.setText("Start");
				status = Status.STOPPED;
			}
		} catch (IOException e) {
			log.error(e.toString());
		} catch (InterruptedException e) {
			log.error(e.toString());
		}
	}
}