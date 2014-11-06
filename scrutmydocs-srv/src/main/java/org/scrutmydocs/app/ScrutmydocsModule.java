package org.scrutmydocs.app;

import restx.config.ConfigLoader;
import restx.config.ConfigSupplier;
import restx.factory.Module;
import restx.factory.Provides;

import javax.inject.Named;

@Module
public class ScrutmydocsModule {

    @Provides
    @Named("restx.admin.password")
    public String restxAdminPassword() {
        return "admin";
    }

    @Provides
    public ConfigSupplier appConfigSupplier(ConfigLoader configLoader) {
        // Load scrutmydocs.properties in config package as a set of config entries
        return configLoader.fromResource("config/scrutmydocs");
    }
}
