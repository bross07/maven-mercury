/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file                                                                                            
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.maven.mercury.spi.http.client;

import org.apache.maven.mercury.transport.api.Binding;

/**
 * BatchException
 * <p/>
 * Exception that occurs whilst deploying or retrieving files asynchronously.
 */
@SuppressWarnings("serial")
public class HttpClientException
    extends Exception
{
    private Binding binding;

    public HttpClientException( Binding b, String s )
    {
        super( s );
        binding = b;
    }

    public HttpClientException( Binding b, String s, Throwable throwable )
    {
        super( s, throwable );
        binding = b;
    }

    public HttpClientException( Binding b, Throwable throwable )
    {
        super( throwable );
        binding = b;
    }

    public Binding getBinding()
    {
        return binding;
    }

    public String getMessage()
    {
        return super.getMessage() + " for " + binding;
    }

}
