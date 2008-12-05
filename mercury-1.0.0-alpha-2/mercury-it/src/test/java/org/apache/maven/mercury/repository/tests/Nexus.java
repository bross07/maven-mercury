/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.maven.mercury.repository.tests;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.sonatype.appbooter.ForkedAppBooter;
import org.sonatype.appbooter.ctl.AppBooterServiceException;
import org.sonatype.nexus.client.NexusClient;
import org.sonatype.nexus.client.rest.NexusRestClient;

/**
 *
 *
 * @author Oleg Gusakov
 * @version $Id$
 *
 */
public class Nexus
{
  public static final String TEST_NEXUS_ROLE = ForkedAppBooter.ROLE;
  public static final String TEST_NEXUS_HINT = "NexusForkedAppBooter";

  public static String nexusTestUrl  = "http://localhost:8091/nexus";
  public static String nexusTestUser = "admin";
  public static String nexusTestPass = "admin123";
  
  private static ForkedAppBooter nexusForkedAppBooter;
  //------------------------------------------------------------------------------
  private static void checkAppBooter( final PlexusContainer plexus )
  throws AppBooterServiceException, ComponentLookupException
  {
    if( nexusForkedAppBooter != null )
      return;
    
    nexusForkedAppBooter = (ForkedAppBooter)plexus.lookup( TEST_NEXUS_ROLE, TEST_NEXUS_HINT  );
      
    nexusForkedAppBooter.start();

    Runtime.getRuntime().addShutdownHook
    (
        new Thread()
        {
          PlexusContainer plx = plexus;
          private ForkedAppBooter nexusFAB;
          
          public void run()
          {
            try
            {
              nexusFAB = (ForkedAppBooter)plx.lookup( TEST_NEXUS_ROLE, TEST_NEXUS_HINT  );
              nexusFAB.shutdown();
            }
            catch( Exception e )
            {
              e.printStackTrace();
              return;
            }
          }
        }
    );
  }
  //------------------------------------------------------------------------------
  public static void start( PlexusContainer plexus )
  throws Exception
  {
    checkAppBooter( plexus );

    NexusClient client = new NexusRestClient();
    
    client.connect( nexusTestUrl, nexusTestUser, nexusTestPass );
    
    try
    {
      client.startNexus();
      Thread.sleep( 3000L );
    }
    catch( Exception any ) {}
    
    if( !client.isNexusStarted( true ) )
    {
      throw new Exception("Cannot start Nexus");
    }
    
    client.disconnect();
  }
  //------------------------------------------------------------------------------
  public static void stop()
  throws Exception
  {
    NexusClient client = new NexusRestClient();
    
    client.connect( nexusTestUrl, nexusTestUser, nexusTestPass );

    try
    {
      if( client.isNexusStarted( false ) )
      {
        client.stopNexus();
      }
    }
    catch( Exception any ) {}

    client.disconnect();
    
    Thread.sleep( 3000L );
  }
  //------------------------------------------------------------------------------
  public static void shutdown( PlexusContainer plexus )
  throws Exception
  {
    
    if( nexusForkedAppBooter == null )
      nexusForkedAppBooter = (ForkedAppBooter)plexus.lookup( TEST_NEXUS_ROLE, TEST_NEXUS_HINT  );
      
    try // if it was running before
    {
      nexusForkedAppBooter.shutdown();
      Thread.sleep( 3000L );
    }
    catch( Exception e ) {}
    
  }
  //------------------------------------------------------------------------------
  //------------------------------------------------------------------------------
}