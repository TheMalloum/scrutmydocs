package org.scrutmydocs.scan;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.scrutmydocs.contract.SMDRepositoryData;
import org.scrutmydocs.contract.SMDRepositoryScan;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;

import java.util.Date;
import java.util.List;

public class ScanDocuments implements Job {
	private Logger logger = LogManager.getLogger(ScanDocuments.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("start scan()");

		List<SMDRepositoryData> repositories = SMDRepositoriesFactory
				.getInstance().getRepositories();

		if (repositories == null) {
			logger.info("No repositories found. Skipping...");
			return;
		}

		for (SMDRepositoryData smdRepositoryData : repositories) {
            SMDRepositoryScan smdRepositoryScan= SMDRepositoriesFactory.getScanInstance(smdRepositoryData.type);

			if (smdRepositoryScan == null ) {
				logger.fatal("we don't have repository scan with type = "+smdRepositoryData.type);
				continue;
			}
			
			if (!smdRepositoryScan.check(smdRepositoryData)) {
				logger.error("the repository {} is not available",
						smdRepositoryData.id);
				continue;
			}
			
			if ( !smdRepositoryData.start) {
				continue;
			}
				
			
			Date startScan = new Date();
			try{
			smdRepositoryScan.scrut(smdRepositoryData);
			}catch(Exception e){
				logger.fatal("error scan directory {} have repository scan with type = ",smdRepositoryData.url,e);
			}
			smdRepositoryData.lastScan = startScan;
			SMDRepositoriesFactory.getInstance().save(
					smdRepositoryData);
			
		}

		logger.info("end scan()");
	}


	public static void init() {

		try {

			// specify the job' s details..
			JobDetail job = JobBuilder.newJob(ScanDocuments.class)
					.withIdentity("ScanDocuments").build();
			// specify the running period of the job
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withSchedule(
							SimpleScheduleBuilder.simpleSchedule()
									.withIntervalInMinutes(1).repeatForever())
					.build();

			// schedule the job
			StdSchedulerFactory.getDefaultScheduler().scheduleJob(job, trigger);
			StdSchedulerFactory.getDefaultScheduler().start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
