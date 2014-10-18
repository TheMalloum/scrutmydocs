package org.scrutmydocs.security;

import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.scrutmydocs.annotations.SMDRegisterRepositoryData;
import org.scrutmydocs.annotations.SMDRegisterRepositoryScan;
import org.scrutmydocs.annotations.SMDRegisterSecurityPolicy;
import org.scrutmydocs.contract.SMDRepositoryData;
import org.scrutmydocs.contract.SMDRepositoryScan;
import org.scrutmydocs.repositories.ElasticRepositoryImpl;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;

public class SMDSecurityFactory {

	protected static Logger logger = LogManager
			.getLogger(SMDSecurityFactory.class);

	public static SMDSecurity securityPolicy;

	public static SMDSecurity getInstance() {

		if (securityPolicy == null) {

			Reflections reflections = new Reflections("org.scrutmydocs");

			Set<Class<?>> annotated = reflections
					.getTypesAnnotatedWith(SMDRegisterSecurityPolicy.class);

			for (Class<?> annotatedSecurityPolicy : annotated) {

				if (SMDSecurity.class.isAssignableFrom(annotatedSecurityPolicy)) {
					
					if (annotatedSecurityPolicy.equals(
									SMDDefaultSecurityPolicy.class)) {
						continue;
					}
						
					logger.info(annotatedSecurityPolicy.getName()
							+ " class detect for the security policy");

					try {
						securityPolicy = (SMDSecurity) annotatedSecurityPolicy
								.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						
						logger.fatal(e);
						throw new RuntimeException(e);
					}

					return securityPolicy;

				} else {
					logger.warn(annotatedSecurityPolicy.getName()
							+ " class must extends"
							+ SMDSecurity.class.getName());
				}
			}

			securityPolicy = new SMDDefaultSecurityPolicy();
		}

		logger.info("no  class detect for the security policy, we use the default policy all is public");

		return securityPolicy;
	}

}
