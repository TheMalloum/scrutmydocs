package org.scrutmydocs.services;

import com.google.common.io.Files;
import org.elasticsearch.common.Strings;
import org.scrutmydocs.domain.SMDConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyScanner {
    private static final Logger logger = LoggerFactory.getLogger(PropertyScanner.class);

    public static SMDConfiguration scanPropertyFile() {
        SMDConfiguration smdProps = new SMDConfiguration();

        String userHome = System.getProperty("user.home");
        logger.debug("[user.home] set to [{}]", userHome);
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
            logger.error("error TODO fix message", e);
        }

        if (props == null) {
            try {
                // Build the file from the Classpath
                URL resource = PropertyScanner.class.getResource("config/scrutmydocs.properties");
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
                logger.error("error TODO fix message", e);
            } catch (URISyntaxException e) {
                logger.error("error TODO fix message", e);
            }
        }

        if (props == null) {
            throw new RuntimeException("Can not build ~/.scrutmydocs/config/scrutmydocs.properties file. Check that current user have a write access");
        }

        smdProps.setNodeEmbedded(getProperty(props, "elasticsearch.node.embedded", true));
        smdProps.setClusterName(getProperty(props, "elasticsearch.cluster.name", "scrutmydocs"));
        Path smdData = smdHome.resolve("esdata");
        smdProps.setPathData(getProperty(props, "elasticsearch.path.data", smdData.toString()));
        smdProps.setNodeAdresses(getPropertyAsArray(props, "elasticsearch.node.addresses", "localhost:9300,localhost:9301"));

        // We check some rules here :
        // if node.embedded = false, we must have an array of node.adresses
        if (!smdProps.isNodeEmbedded() && (smdProps.getNodeAdresses() == null || smdProps.getNodeAdresses().length < 1)) {
            throw new RuntimeException("If you don't want embedded node, you MUST set elasticsearch.node.addresses property.");
        }

        smdProps.setCleanOnStart(getProperty(props, "documents.clean_on_start", false));
        smdProps.setIndexedChars(getProperty(props, "documents.indexed_chars", SMDConfiguration.INDEXED_CHARS_DEFAULT));

        return smdProps;
    }

    private static boolean getProperty(Properties props, String path,
                                       boolean defaultValue) {
        String sValue = getProperty(props, path, Boolean.toString(defaultValue));
        if (sValue == null) {
            return false;
        }
        return Boolean.parseBoolean(sValue);
    }

    private static int getProperty(Properties props, String path,
                                       int defaultValue) {
        String sValue = getProperty(props, path, Integer.toString(defaultValue));
        if (sValue == null) {
            return 0;
        }
        return Integer.parseInt(sValue);
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
