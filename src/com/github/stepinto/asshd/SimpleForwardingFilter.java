package com.github.stepinto.asshd;

import java.net.InetSocketAddress;

import org.apache.sshd.server.ForwardingFilter;
import org.apache.sshd.server.session.ServerSession;

public class SimpleForwardingFilter implements ForwardingFilter {

	@Override
	public boolean canConnect(InetSocketAddress arg0, ServerSession arg1) {
		return true;
	}

	@Override
	public boolean canForwardAgent(ServerSession arg0) {
		return true;
	}

	@Override
	public boolean canForwardX11(ServerSession arg0) {
		return true;
	}

	@Override
	public boolean canListen(InetSocketAddress arg0, ServerSession arg1) {
		return true;
	}
}
