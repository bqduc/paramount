package net.paramount.ase.model;

import javax.inject.Inject;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.paramount.ase.service.MemberClassService;

@Slf4j
@Component
@DisallowConcurrentExecution
public class MemberClassStatsJob implements Job {
    @Inject
    private MemberClassService memberClassService;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
        memberClassService.classStats();
        log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}
