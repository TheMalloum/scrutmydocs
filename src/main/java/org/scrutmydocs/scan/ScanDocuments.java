package org.scrutmydocs.scan;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.scrutmydocs.repositories.SMDAbstractRepository;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;

public class ScanDocuments implements Job{
	private Logger logger = LogManager.getLogger(ScanDocuments.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
        logger.info("start scan()");

        List<SMDAbstractRepository> repositories = SMDRepositoriesFactory.getInstance().getRepositories();
        if (repositories == null) {
            logger.info("No repositories found. Skipping...");
            return;
        }

        for (SMDAbstractRepository repositorie : repositories) {

        	
        		logger.info("checking plugin " + repositorie.name +" active "+ repositorie.start );
				if(repositorie.start)
				{
        		repositorie.scrut();
				}
        }

        logger.info("end scan()");
	}
    
    
    
    public static  void init() {

		try {

			// specify the job' s details..
			JobDetail job = JobBuilder.newJob(ScanDocuments.class)
					.withIdentity("ScanDocuments").build();
			// specify the running period of the job
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withSchedule(
							SimpleScheduleBuilder.simpleSchedule()
									.withIntervalInSeconds(30).repeatForever())
					.build();

			// schedule the job
			StdSchedulerFactory.getDefaultScheduler().scheduleJob(job, trigger);
			StdSchedulerFactory.getDefaultScheduler().start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}




}
