/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.server.schema.registries;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.directory.shared.ldap.schema.MatchingRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A MatchingRuleRegistry service used to lookup matching rules by OID.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class DefaultMatchingRuleRegistry implements MatchingRuleRegistry
{
    /** static class logger */
    private static final Logger LOG = LoggerFactory.getLogger( DefaultMatchingRuleRegistry.class );
    /** a map using an OID for the key and a MatchingRule for the value */
    private final Map<String,MatchingRule> byOid;
    /** the registry used to resolve names to OIDs */
    private final OidRegistry oidRegistry;


    // ------------------------------------------------------------------------
    // C O N S T R U C T O R S
    // ------------------------------------------------------------------------

    
    /**
     * Creates a DefaultMatchingRuleRegistry using existing MatchingRulees
     * for lookups.
     *
     * @param oidRegistry used by this registry for OID to name resolution of
     * dependencies and to automatically register and unregister it's aliases and OIDs
     */
    public DefaultMatchingRuleRegistry( OidRegistry oidRegistry )
    {
        this.oidRegistry = oidRegistry;
        this.byOid = new HashMap<String,MatchingRule>();
    }


    // ------------------------------------------------------------------------
    // MatchingRuleRegistry interface methods
    // ------------------------------------------------------------------------

    /**
     * @see org.apache.directory.server.schema.registries.MatchingRuleRegistry#lookup(String)
     */
    public MatchingRule lookup( String id ) throws NamingException
    {
        id = oidRegistry.getOid( id );

        if ( byOid.containsKey( id ) )
        {
            MatchingRule matchingRule = byOid.get( id );
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "lookup with id '"+id+"' of matchingRule: " + matchingRule );
            }
            return matchingRule;
        }

        throw new NamingException( "Unknown MatchingRule OID " + id );
    }


    /**
     * @see MatchingRuleRegistry#register(MatchingRule)
     */
    public void register( MatchingRule matchingRule ) throws NamingException
    {
        if ( byOid.containsKey( matchingRule.getOid() ) )
        {
            throw new NamingException( "matchingRule w/ OID " + matchingRule.getOid()
                + " has already been registered!" );
        }

        String[] names = matchingRule.getNames();
        for ( String name : names )
        {
            oidRegistry.register( name, matchingRule.getOid() );
        }
        oidRegistry.register( matchingRule.getOid(), matchingRule.getOid() );

        byOid.put( matchingRule.getOid(), matchingRule );
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "registed matchingRule: " + matchingRule);
        }
    }


    /**
     * @see org.apache.directory.server.schema.registries.MatchingRuleRegistry#hasMatchingRule(String)
     */
    public boolean hasMatchingRule( String id )
    {
        if ( oidRegistry.hasOid( id ) )
        {
            try
            {
                return byOid.containsKey( oidRegistry.getOid( id ) );
            }
            catch ( NamingException e )
            {
                return false;
            }
        }

        return false;
    }


    public String getSchemaName( String id ) throws NamingException
    {
        id = oidRegistry.getOid( id );
        MatchingRule mr = byOid.get( id );
        if ( mr != null )
        {
            return mr.getSchema();
        }

        throw new NamingException( "OID " + id + " not found in oid to " + "MatchingRule name map!" );
    }


    public Iterator<MatchingRule> iterator()
    {
        return byOid.values().iterator();
    }
    
    
    public void unregister( String numericOid ) throws NamingException
    {
        if ( ! Character.isDigit( numericOid.charAt( 0 ) ) )
        {
            throw new NamingException( "Looks like the arg is not a numeric OID" );
        }

        byOid.remove( numericOid );
        oidRegistry.unregister( numericOid );
    }
}
