/**
 * 
 */
package net.paramount.ase.model;

import javax.inject.Inject;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.paramount.ase.service.MemberService;

/**
 * @author ducbq
 *
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class MemberStatsJob implements Job {
	@Inject
	private MemberService memberService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
    //log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

    memberService.memberStats();

    //log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());

	}

}
