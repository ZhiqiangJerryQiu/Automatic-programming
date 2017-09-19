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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.24 at 11:14:06 AM CET 
//

package com.abiquo.server.core.infrastructure;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.abiquo.model.transport.SingleResourceTransportDto;

/**
 */
@XmlRootElement(name = "logicServer")
public class LogicServerDto extends SingleResourceTransportDto
{
    private static final long serialVersionUID = -3508955420063569772L;

    public static final String BASE_MEDIA_TYPE = "application/vnd.abiquo.logicserver+xml";
    public static final String MEDIA_TYPE = BASE_MEDIA_TYPE + "; version=" + API_VERSION;
    
    protected String name;

    // It can be template (update or initial) or instance
    protected String type;

    protected String associated;

    protected String associatedTo;

    protected String description;

    protected List<LogicServerPolicyDto> policies;

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link String }
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value allowed object is {@link String }
     */
    public void setType(final String value)
    {
        this.type = value;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value allowed object is {@link String }
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * Gets the value of the associated property.
     * 
     * @return possible object is {@link String }
     */
    public String getAssociated()
    {
        return associated;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return possible object is {@link String }
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the value of the associated property.
     * 
     * @param value allowed object is {@link String }
     */
    public void setAssociated(final String value)
    {
        this.associated = value;
    }

    /**
     * Gets the value of the associatedTo property.
     * 
     * @return possible object is {@link String }
     */
    public String getAssociatedTo()
    {
        return associatedTo;
    }

    /**
     * Sets the value of the associatedTo property.
     * 
     * @param value allowed object is {@link String }
     */
    public void setAssociatedTo(final String value)
    {
        this.associatedTo = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return possible object is {@link String }
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value allowed object is {@link String }
     */
    public void setDescription(final String value)
    {
        this.description = value;
    }

    /**
     * Gets the value of the host property.
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the host property.
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getCollection().add(newItem);
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link LogicServerPolicyDto }
     */
    @XmlElements({@XmlElement(name = "policy", type = LogicServerPolicyDto.class)})
    @XmlElementWrapper(name = "policies")
    public List<LogicServerPolicyDto> getCollection()
    {
        if (policies == null)
        {
            policies = new ArrayList<LogicServerPolicyDto>();
        }
        return this.policies;
    }
    
    @Override
    public String getMediaType()
    {
        return LogicServerDto.MEDIA_TYPE;
    }
    
    @Override
    public String getBaseMediaType()
    {
        return BASE_MEDIA_TYPE;
    }
}
