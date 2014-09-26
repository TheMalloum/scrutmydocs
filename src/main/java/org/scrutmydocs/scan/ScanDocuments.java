package org.scrutmydocs.scan;

import java.util.List;
import java.util.Set;

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
import org.reflections.Reflections;
import org.scrutmydocs.repositories.SMDRegisterRepositoryScan;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;
import org.scrutmydocs.repositories.SMDRepositoryData;
import org.scrutmydocs.repositories.SMDRepositoryScan;

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

			Reflections reflections = new Reflections("org.scrutmydocs");

			Set<Class<?>> annotated = reflections
					.getTypesAnnotatedWith(SMDRegisterRepositoryScan.class);

			for (Class<?> class1 : annotated) {
				try {

					if (smdRepositoryData.getClass().isAssignableFrom(class1)) {
						SMDRepositoryScan register = (SMDRepositoryScan) class1
								.newInstance();

						register.scrut(smdRepositoryData);
					}

				} catch (Exception e) {
					logger.error(class1.getName()
							+ " doesn't have default constructor");
					throw new RuntimeException(
							"doesn't have default constructor : " + e);
				}

			}

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
									.withIntervalInMinutes(5).repeatForever())
					.build();

			// schedule the job
			StdSchedulerFactory.getDefaultScheduler().scheduleJob(job, trigger);
			StdSchedulerFactory.getDefaultScheduler().start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
