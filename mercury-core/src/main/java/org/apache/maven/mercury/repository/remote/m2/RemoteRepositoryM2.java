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
package org.apache.maven.mercury.repository.remote.m2;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.maven.mercury.builder.api.DependencyProcessor;
import org.apache.maven.mercury.repository.api.AbstractRepository;
import org.apache.maven.mercury.repository.api.NonExistentProtocolException;
import org.apache.maven.mercury.repository.api.RemoteRepository;
import org.apache.maven.mercury.repository.api.RepositoryException;
import org.apache.maven.mercury.repository.api.RepositoryReader;
import org.apache.maven.mercury.repository.api.RepositoryUpdateIntervalPolicy;
import org.apache.maven.mercury.repository.api.RepositoryUpdatePolicy;
import org.apache.maven.mercury.repository.api.RepositoryWriter;
import org.apache.maven.mercury.transport.api.Server;
import org.codehaus.plexus.lang.DefaultLanguage;
import org.codehaus.plexus.lang.Language;

public class RemoteRepositoryM2
    extends AbstractRepository
    implements RemoteRepository
{
    private static final Language LANG = new DefaultLanguage( RemoteRepositoryReaderM2.class );

    public static final String METADATA_FILE_NAME = "maven-metadata.xml";

    private Server _server;

    protected boolean _workAroundBadMetadata = true;

    /** default update policy */
    private RepositoryUpdatePolicy _updatePolicy = RepositoryUpdateIntervalPolicy.DEFAULT_UPDATE_POLICY;

    // ----------------------------------------------------------------------------------
    public RemoteRepositoryM2( String url, DependencyProcessor dependencyProcessor )
        throws MalformedURLException
    {
        this( new Server( new URL( url ) ), dependencyProcessor );
    }

    // ----------------------------------------------------------------------------------
    public RemoteRepositoryM2( Server server, DependencyProcessor dependencyProcessor )
    {
        this( server.getId(), server, dependencyProcessor );
    }

    // ----------------------------------------------------------------------------------
    public RemoteRepositoryM2( String id, Server server, DependencyProcessor dependencyProcessor )
    {
        super( id, DEFAULT_REPOSITORY_TYPE );
        this._server = server;
        setDependencyProcessor( dependencyProcessor );
    }

    // ----------------------------------------------------------------------------------
    public Server getServer()
    {
        return _server;
    }

    // ----------------------------------------------------------------------------------
    public RepositoryReader getReader()
        throws RepositoryException
    {
        return new RemoteRepositoryReaderM2( this, getDependencyProcessor() );
    }

    // ----------------------------------------------------------------------------------
    public RepositoryReader getReader( String protocol )
        throws RepositoryException
    {
        return getReader();
    }

    // ----------------------------------------------------------------------------------
    public RepositoryWriter getWriter()
        throws RepositoryException
    {
        return new RemoteRepositoryWriterM2( this );
    }

    // ----------------------------------------------------------------------------------
    public RepositoryWriter getWriter( String protocol )
        throws NonExistentProtocolException, RepositoryException
    {
        return getWriter();
    }

    // ----------------------------------------------------------------------------------
    public boolean isLocal()
    {
        return false;
    }

    // ----------------------------------------------------------------------------------
    public boolean isReadable()
    {
        return true;
    }

    // ----------------------------------------------------------------------------------
    public boolean isWriteable()
    {
        return true;
    }

    // ----------------------------------------------------------------------------------
    public boolean isWorkAroundBadMetadata()
    {
        return _workAroundBadMetadata;
    }

    // ----------------------------------------------------------------------------------
    public void setWorkAroundBadMetadata( boolean aroundBadMetadata )
    {
        _workAroundBadMetadata = aroundBadMetadata;
    }

    // ----------------------------------------------------------------------------------
    public String getType()
    {
        return DEFAULT_REPOSITORY_TYPE;
    }

    // ----------------------------------------------------------------------------------
    public RepositoryUpdatePolicy getUpdatePolicy()
    {
        return _updatePolicy == null ? RepositoryUpdateIntervalPolicy.DEFAULT_UPDATE_POLICY : _updatePolicy;
    }

    // ----------------------------------------------------------------------------------
    public void setUpdatePolicy( RepositoryUpdatePolicy updatePolicy )
    {
        if ( updatePolicy == null )
            throw new IllegalArgumentException( LANG.getMessage( "null.update.policy", getId(),
                                                                 _server == null ? "no URL" : _server.getURL() + "" ) );

        this._updatePolicy = updatePolicy;
    }

    // ----------------------------------------------------------------------------------
    public String getMetadataName()
    {
        return METADATA_FILE_NAME;
    }
    // ----------------------------------------------------------------------------------
}
