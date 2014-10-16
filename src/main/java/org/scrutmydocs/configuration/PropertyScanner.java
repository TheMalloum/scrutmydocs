package org.scrutmydocs.configuration;

import com.google.common.io.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.Strings;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyScanner {
    private static Logger logger = LogManager.getLogger();

    public static SMDConfiguration scanPropertyFile() {
        SMDConfiguration smdProps = new SMDConfiguration();

        String userHome = System.getProperty("user.home");
        logger.debug("[user.home] set to [%s]", userHome);
        Path home = Paths.get(userHome);
        Path smdHome = home.resolve(".scrutmydocs/config/");
        Path homeProperties = smdHome.resolve("scrutmydocs.properties");

        Properties props = null;
        try (InputStream is = new FileInputStream(homeProperties.toFile())) {
            Properties homeProps = new Properties();
            homeProps.load(is);
            props = homeProps;
        } catch (FileNotFoundException e) {
            // File does not exists ? Who cares ?
        } catch (IOException e) {
            logger.error(e);
        }

        if (props == null) {
            try {
                // Build the file from the Classpath
                URL resource = PropertyScanner.class.getResource("/scrutmydocs/config/scrutmydocs.properties");
                File from = Paths.get(resource.toURI()).toFile();
                File dest = new File(smdHome.toFile(), from.getName());
                Files.createParentDirs(dest);
                Files.copy(from, dest);

                try (InputStream is = new FileInputStream(homeProperties.toFile())) {
                    props.load(is);
                } catch (FileNotFoundException e) {
                    // File does not exists ? Who cares ?
                }
            } catch (IOException e) {
                logger.error(e);
            } catch (URISyntaxException e) {
                logger.error(e);
            }
        }

        if (props == null) {
            throw new RuntimeException("Can not build ~/.scrutmydocs/config/scrutmydocs.properties file. Check that current user have a write access");
        }

        smdProps.setNodeEmbedded(getProperty(props, "node.embedded", true));
        smdProps.setClusterName(getProperty(props, "cluster.name", "scrutmydocs"));
        Path smdData = smdHome.resolve("esdata");
        smdProps.setPathData(getProperty(props, "path.data", smdData.toString()));
        smdProps.setNodeAdresses(getPropertyAsArray(props, "node.addresses", "localhost:9300,localhost:9301"));

        // We check some rules here :
        // if node.embedded = false, we must have an array of node.adresses
        if (!smdProps.isNodeEmbedded() && (smdProps.getNodeAdresses() == null || smdProps.getNodeAdresses().length < 1)) {
            throw new RuntimeException("If you don't want embedded node, you MUST set node.addresses property.");
        }

        return smdProps;
    }

    private static boolean getProperty(Properties props, String path,
                                       boolean defaultValue) {
        String sValue = getProperty(props, path, Boolean.toString(defaultValue));
        if (sValue == null)
            return false;
        return Boolean.parseBoolean(sValue);
    }

    private static String getProperty(Properties props, String path,
                                      String defaultValue) {
        return props.getProperty(path, defaultValue);
    }

    private static String[] getPropertyAsArray(Properties props, String path,
                                               String defaultValue) {
        String sValue = getProperty(props, path, defaultValue);
        if (sValue == null)
            return null;

        // Remove spaces and split results with comma
        String[] arrStr = Strings.commaDelimitedListToStringArray(sValue);

        return arrStr;
    }
}
