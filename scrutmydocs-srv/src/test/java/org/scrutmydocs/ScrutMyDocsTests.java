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

package org.scrutmydocs;

import com.google.common.base.Predicate;
import org.junit.After;
import org.junit.Before;
import org.scrutmydocs.services.SMDDocumentService;
import org.scrutmydocs.services.SMDRepositoriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class ScrutMyDocsTests {

    private static final Logger logger = LoggerFactory.getLogger(ScrutMyDocsTests.class);

    public static final String TEST_FILENAME = "LICENSE.txt";
    public static final String TEST_FILE = "integration/docs/" + TEST_FILENAME;
    protected final SMDRepositoriesService repositoriesService;
    protected final SMDDocumentService searchService;

    @Inject
    public ScrutMyDocsTests(SMDRepositoriesService repositoriesService,
                            SMDDocumentService searchService) {
        this.repositoriesService = repositoriesService;
        this.searchService = searchService;
    }

    @Before @After
    public void wipeIndices() {
        logger.info("  --> Cleaning existing data");
        // TODO When we uncomment the following line, tests are failing.
        // Strange. We should not have to wait here
//        ElasticsearchFactory.esClient().admin().indices().prepareDelete("_all").get();
    }

    public static boolean awaitBusy(Predicate<?> breakPredicate) throws InterruptedException {
        return awaitBusy(breakPredicate, 10, TimeUnit.SECONDS);
    }

    public static boolean awaitBusy(Predicate<?> breakPredicate, long maxWaitTime, TimeUnit unit) throws InterruptedException {
        long maxTimeInMillis = TimeUnit.MILLISECONDS.convert(maxWaitTime, unit);
        long iterations = Math.max(Math.round(Math.log10(maxTimeInMillis) / Math.log10(2)), 1);
        long timeInMillis = 1;
        long sum = 0;
        for (int i = 0; i < iterations; i++) {
            if (breakPredicate.apply(null)) {
                return true;
            }
            sum += timeInMillis;
            Thread.sleep(timeInMillis);
            timeInMillis *= 2;
        }
        timeInMillis = maxTimeInMillis - sum;
        Thread.sleep(Math.max(timeInMillis, 0));
        return breakPredicate.apply(null);
    }
}
