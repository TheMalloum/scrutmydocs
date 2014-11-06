package org.scrutmydocs.jobs;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.scrutmydocs.services.SMDRepositoriesService;
import org.scrutmydocs.domain.SMDRepository;
import org.scrutmydocs.domain.SMDRepositoryScan;
import org.scrutmydocs.services.SMDRepositoryScanReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class ScanDocuments implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ScanDocuments.class);
    private final SMDRepositoriesService repositoriesService;
    private final SMDRepositoryScanReflectionService repositoryScanReflectionService;

    @Inject
    public ScanDocuments(SMDRepositoriesService repositoriesService,
                         SMDRepositoryScanReflectionService repositoryScanReflectionService) {
        this.repositoriesService = repositoriesService;
        this.repositoryScanReflectionService = repositoryScanReflectionService;
    }

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("start scan()");

		List<SMDRepository> repositories = repositoriesService.getRepositories();

		if (repositories == null) {
			logger.info("No repositories found. Skipping...");
			return;
		}

		for (SMDRepository smdRepository : repositories) {
            SMDRepositoryScan smdRepositoryScan= repositoryScanReflectionService.getListScan(smdRepository.type);

			if (smdRepositoryScan == null ) {
				logger.error("we don't have repository scan with type {}", smdRepository.type);
				continue;
			}
			
			if (!smdRepositoryScan.check(smdRepository)) {
				logger.error("the repository {} is not available", smdRepository.id);
				continue;
			}
			
			if ( !smdRepository.start) {
				continue;
			}
				
			
			Date startScan = new Date();
			try {
			    smdRepositoryScan.scrut(smdRepository);
			} catch(Exception e){
				logger.error("error scan directory {} have repository scan with type", smdRepository.url, e);
			}
			smdRepository.lastScan = startScan;
            repositoriesService.save(smdRepository);
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
