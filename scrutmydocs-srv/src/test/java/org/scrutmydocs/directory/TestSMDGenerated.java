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

package org.scrutmydocs.directory;


import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by dpilato on 12/05/15.
 */
public class TestSMDGenerated {


    @Test
    public void testCreateWitheFile(){


        try {

            new SmdFileDirectory(File.createTempFile("test","test"));
        } catch (IOException e) {}
        catch (IllegalArgumentException ex) {
            return;
        }

        Assert.fail();
    }


    @Test
    public void testNotDiffDirectory() throws IOException {

        Path dir = Files.createTempDirectory("scrut");

        SmdFileDirectory old = new SmdFileDirectory(dir.toFile());
        SmdFileDirectory news =  new SmdFileDirectory(dir.toFile());

        Assert.assertEquals("you must not have diffenrents elements", 0, old.diff(news).size());

    }


    @Test
    public void testOneADD() throws IOException {

        Path dir = Files.createTempDirectory("scrut");

        SmdFileDirectory old = new SmdFileDirectory(dir.toFile());
        Path toto1 = dir.resolve("toto1");
        Files.createFile(toto1);


        SmdFileDirectory news =  new SmdFileDirectory(dir.toFile());

        Assert.assertEquals("you must  have 1 diffenrent element", 1, old.diff(news).size());
        Assert.assertEquals("you must  have 1 ADD element", Difference.Type.ADD, old.diff(news).get(0).type);

    }


    @Test
    public void testOneDELETE() throws IOException {

        Path dir = Files.createTempDirectory("scrut");
        Path toto1 = dir.resolve("toto1");
        Files.createFile(toto1);



        SmdFileDirectory old = new SmdFileDirectory(dir.toFile());

        Files.delete(toto1);

        SmdFileDirectory news =  new SmdFileDirectory(dir.toFile());

        Assert.assertEquals("you must  have 1 diffenrent element", 1, old.diff(news).size());
        Assert.assertEquals("you must  have 1 DELETE element", Difference.Type.DELETE, old.diff(news).get(0).type);

    }


    @Test
    public void testOneUPDATE() throws IOException {

        Path dir = Files.createTempDirectory("scrut");
        Path toto1 = dir.resolve("toto1");
        Files.createFile(toto1);



        SmdFileDirectory old = new SmdFileDirectory(dir.toFile());

        Files.delete(toto1);
        toto1 = dir.resolve("toto1");
        Files.createFile(toto1);

        SmdFileDirectory news =  new SmdFileDirectory(dir.toFile());

        Assert.assertEquals("you must  have 1 diffenrent element", 1, old.diff(news).size());
        Assert.assertEquals("you must  have 1 UPDATE element", Difference.Type.UPDATE, old.diff(news).get(0).type);

    }
}
