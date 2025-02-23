package org.devgraft.sc.schedule;

import org.devgraft.sc.SshConnection;
import org.devgraft.sc.config.SshSessionProperties;
import org.devgraft.sc.config.SshSessionProperty;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SshKeepAliveJobBean extends QuartzJobBean {
    private final SshSessionProperties sshSessionProperties;
    private final SshConnection sshConnection;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            for (SshSessionProperty sshSessionProperty : sshSessionProperties.getList()) {
                sshConnection.sendAlive(sshSessionProperty.getSessionName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
