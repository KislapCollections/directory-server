/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.server.config.beans;

/**
 * A class used to store the Transport configuration.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class TransportBean extends AdsBaseBean
{
    /** The default backlog queue size */
    private static final int DEFAULT_BACKLOG_NB = 50;
    
    /** The default number of threads */
    private static final int DEFAULT_NB_THREADS = 3;

    /** The unique identifier for this transport */
    private String transportid;
    
    /** The transport address */
    private String transportaddress;
    
    /** The port number */
    private int systemport = -1;
    
    /** A flag set if SSL is enabled */
    private boolean transportenablessl = false;
    
    /** The number of threads to use for the IoAcceptor executor */
    private int transportnbthreads = DEFAULT_NB_THREADS;
    
    /** The backlog for the transport services */
    private int transportbacklog = DEFAULT_BACKLOG_NB;
    
    /**
     * Create a new TransportBean instance
     */
    public TransportBean()
    {
    }

    
    /**
     * @param systemPort the port to set
     */
    public void setSystemPort( int systemPort ) 
    {
        this.systemport = systemPort;
    }

    
    /**
     * @return the port
     */
    public int getSystemPort() 
    {
        return systemport;
    }

    
    /**
     * @param transportAddress the address to set
     */
    public void setTransportAddress( String transportAddress ) {
        this.transportaddress = transportAddress;
    }

    
    /**
     * @return the address
     */
    public String getTransportAddress() {
        return transportaddress;
    }
    
    
    /**
     * @return <code>true</code> id SSL is enabled for this transport
     */
    public boolean isTransportEnableSSL()
    {
        return transportenablessl;
    }
    
    
    /**
     * Enable or disable SSL
     * 
     * @param transportEnableSSL if <code>true</code>, SSL is enabled.
     */
    public void setTransportEnableSSL( boolean transportEnableSSL )
    {
        this.transportenablessl = transportEnableSSL;
    }
    
    
    /**
     * @return The number of threads used to handle the incoming requests
     */
    public int getTransportNbThreads() 
    {
        return transportnbthreads;
    }
    
    
    /**
     * Sets the number of thread to use to process incoming requests
     * 
     * @param The number of threads
     */
    public void setTransportNbThreads( int transportNbThreads )
    {
        this.transportnbthreads = transportNbThreads;
    }
    
    
    /**
     * @return the size of the incoming request waiting queue
     */
    public int getTransportBackLog()
    {
        return transportbacklog;
    }
    
    
    /**
     * Sets the size of the incoming requests waiting queue
     * 
     * @param The size of waiting request queue
     */
    public void setTransportBackLog( int transportBacklog )
    {
        this.transportbacklog = transportBacklog;
    }


    /**
     * @return the transportId
     */
    public String getTransportId()
    {
        return transportid;
    }


    /**
     * @param transportId the transportId to set
     */
    public void setTransportId( String transportId )
    {
        this.transportid = transportId;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString( String tabs )
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append( toString( tabs, "transport id", transportid ) );
        sb.append( tabs ).append( "transport address : " );
        
        if ( transportaddress == null )
        {
            sb.append( "localhost" ).append( '\n' );
        }
        else
        {
            sb.append( transportaddress ).append( '\n' );
        }

        sb.append( tabs ).append( "transport port : " ).append( systemport ).append( '\n' );
        sb.append( tabs ).append( "transport backlog : " ).append( transportbacklog ).append( '\n' );
        sb.append( tabs ).append( "transport nb threads : " ).append( transportnbthreads ).append( '\n' );
        sb.append( toString( tabs, "SSL enabled", transportenablessl ) );

        return sb.toString();
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return toString( "" );
    }
}
