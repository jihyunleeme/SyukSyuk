package org.devgraft.sc;

import org.devgraft.sc.config.SshSessionProperty;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
public class SshConnection implements ISshConnection {
    private final Properties config;
    private final Map<String, Session> sessions;

    public SshConnection() {
        config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        this.sessions = new HashMap<>();
    }

    @Override
    public void connect(SshSessionProperty property) throws JSchException {
        JSch jSch = new JSch();
        if (StringUtils.hasText(property.getPrivateKey())) {
            jSch.addIdentity(property.getPrivateKey());
        }
        Session session = jSch.getSession(property.getUserName(),
                property.getConnectionHost(),
                property.getConnectionPort());
        if (StringUtils.hasText(property.getPassword())) {
            session.setPassword(property.getPassword());
        }
        session.setConfig(config);
        session.setServerAliveCountMax(5);
        session.setServerAliveInterval(0);
        session.connect();
        session.setPortForwardingL(property.getLPort(), property.getHost(), property.getRPort());
        sessions.put(property.getSessionName(), session);
        log.info("session: {} connection Complete!", property.getSessionName());
    }

    @Override
    public void sendAlive(String sessionName) throws Exception {
        if (sessions.containsKey(sessionName)) {
            Session session = sessions.get(sessionName);
            session.sendKeepAliveMsg();
        }
    }

    @Override
    public boolean isConnect(String sessionName) {
        if (sessions.containsKey(sessionName)) {
            Session session = sessions.get(sessionName);
            return session.isConnected();
        }
        return false;
    }

    @Override
    public void close(String sessionName) {
        if (sessions.containsKey(sessionName)) {
            Session session = sessions.get(sessionName);
            session.disconnect();
            sessions.remove(sessionName);
        }
    }
}
