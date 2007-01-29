/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.handler;

import java.io.IOException;

import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;
import org.apache.solr.update.CommitUpdateCommand;

public class CommitRequestHandler extends RequestHandlerBase
{
  @Override
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws IOException 
  {
    SolrParams params = req.getParams();
        
    boolean optimize = params.getBool( UpdateParams.OPTIMIZE, false );
    CommitUpdateCommand cmd = new CommitUpdateCommand( optimize );
    cmd.waitFlush = params.getBool( UpdateParams.WAIT_FLUSH, cmd.waitFlush );
    cmd.waitSearcher = params.getBool( UpdateParams.WAIT_SEARCHER, cmd.waitSearcher );

    SolrCore.getSolrCore().getUpdateHandler().commit( cmd );
    
    if( optimize ) {
      rsp.add( "optimize", "true" );
    }
    else {
      rsp.add( "commit", "true" );
    }
  }

  //////////////////////// SolrInfoMBeans methods //////////////////////

  @Override
  public String getDescription() {
    return "Commit all pending documents";
  }

  @Override
  public String getVersion() {
      return "$Revision:$";
  }

  @Override
  public String getSourceId() {
    return "$Id:$";
  }

  @Override
  public String getSource() {
    return "$URL:$";
  }
}
