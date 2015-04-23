/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.scrutmydocs.app;


import com.google.common.base.Optional;
import restx.server.JettyWebServer;
import restx.server.WebServer;

public class ScrutmydocsApp {
    public static final String WEB_APP_LOCATION = "scrutmydocs-srv/src/main/webapp/";

    public static void main(String[] args) throws Exception {
        int port = Integer.valueOf(Optional.fromNullable(System.getenv("PORT")).or("8080"));
        String webappLocation = Optional.fromNullable(System.getenv("WEBAPP_LOCATION")).or(WEB_APP_LOCATION);
        WebServer server = new JettyWebServer(webappLocation, port);

        /*
         * load mode from system property if defined, or default to dev
         * be careful with that setting, if you use this class to launch your server in production, make sure to launch
         * it with -Drestx.mode=prod or change the default here         */
        System.setProperty("restx.mode", System.getProperty("restx.mode", "dev"));
        System.setProperty("restx.app.package", "org.scrutmydocs");
        server.startAndAwait();
    }
}
