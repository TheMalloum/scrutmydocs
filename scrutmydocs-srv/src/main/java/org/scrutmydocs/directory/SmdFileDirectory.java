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

import java.io.File;
import java.util.*;

/**
 * Created by dpilato on 12/05/15.
 */
public class SmdFileDirectory {



    public final Map<String, Long> children;

    public SmdFileDirectory(File dir) {


        if(dir.isFile()){
            throw new IllegalArgumentException(dir.getAbsolutePath() + " must be a directory");
        }

        children = genarate(dir,new HashMap<String, Long>());

    }

    private Map<String, Long> genarate(File file, Map<String, Long>  mychldren) {

        if(file.isFile()){
            mychldren.put(file.getAbsolutePath(),file.lastModified());
            return mychldren;
        }

        for(File fileTmp : file.listFiles()){
            genarate(fileTmp,mychldren);
        }

        return  mychldren;
    }


    public List<Difference> diff(SmdFileDirectory news) {
        List<Difference> differences = new ArrayList<>();



        for(String key : this.children.keySet()){

            if(news.children.get(key) == null){
                differences.add(new Difference(Difference.Type.DELETE,key));
            }


           else  if(news.children.get(key) != this.children.get(key)){
                differences.add(new Difference(Difference.Type.UPDATE,key));
            }
        }



        for(String key : news.children.keySet()){

            if(this.children.get(key) == null){
               differences.add(new Difference(Difference.Type.ADD,key));
            }
        }

        return differences;

    }
}
