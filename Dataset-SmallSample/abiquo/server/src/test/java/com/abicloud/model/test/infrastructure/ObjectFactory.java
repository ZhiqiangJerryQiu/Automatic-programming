/**
 * Abiquo community edition
 * cloud management application for hybrid clouds
 * Copyright (C) 2008-2010 - Abiquo Holdings S.L.
 *
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the GNU LESSER GENERAL PUBLIC
 * LICENSE as published by the Free Software Foundation under
 * version 3 of the License
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * LESSER GENERAL PUBLIC LICENSE v.3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.01 at 11:03:17 AM CET 
//

package com.abicloud.model.test.infrastructure;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.abicloud.model.test.infrastructure package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation
 * for XML content. The Java representation of XML content can consist of schema derived interfaces
 * and classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory
{

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes
     * for package: com.abicloud.model.test.infrastructure
     */
    public ObjectFactory()
    {
    }

    /**
     * Create an instance of {@link Machine }
     */
    public Machine createMachine()
    {
        return new Machine();
    }

    /**
     * Create an instance of {@link VirtualAppliance }
     */
    public VirtualAppliance createVirtualAppliance()
    {
        return new VirtualAppliance();
    }

    /**
     * Create an instance of {@link VirtualDatacenter }
     */
    public VirtualDatacenter createVirtualDatacenter()
    {
        return new VirtualDatacenter();
    }

    /**
     * Create an instance of {@link Infrastructure }
     */
    public Infrastructure createInfrastructure()
    {
        return new Infrastructure();
    }

    /**
     * Create an instance of {@link Datacenter }
     */
    public Datacenter createDatacenter()
    {
        return new Datacenter();
    }

}