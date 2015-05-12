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


import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

/**
 * Created by dpilato on 12/05/15.
 */
public class TestSMDDiff {

    private int nbFile = 0;

    @Test
    public void generateDirectory() throws IOException {


        Path dir = Files.createTempDirectory("scrut");

        System.out.println("dir = " + dir);
        creatChildren(dir);


    }




    private void creatChildren(Path dir) throws IOException {

        if (dir.toFile().isFile()) {
            return;
        }

        int nbChildren = 0;

        while (nbChildren++ > 5) {
            if (new Random().nextBoolean()) {
                Files.createFile(dir.resolve(String.valueOf(nbFile++)));
            } else {
                Path dirtmp = Files.createDirectories(dir.resolve(String.valueOf(nbFile++)));
                addChildren(dirtmp);
            }

        }

    }
    private void addChildren(Path dir) throws IOException {


            File file = dir.toFile();
            if(file.isFile()){
                return;
            }


            for(File fileTmp : file.listFiles()) {
                creatChildren(fileTmp.toPath());
            }
        }


}
