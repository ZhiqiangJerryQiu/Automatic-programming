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
// Generated on: 2012.02.23 at 01:07:44 PM CET 
//


package org.dmtf.schemas.ovf.envelope._1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


/**
 * Enumeration of discrete deployment options
 *             
 * 
 * <p>Java class for DeploymentOptionSection_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeploymentOptionSection_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.dmtf.org/ovf/envelope/1}Section_Type">
 *       &lt;sequence>
 *         &lt;element name="Configuration" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Label" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type"/>
 *                   &lt;element name="Description" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="default" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                 &lt;anyAttribute processContents='lax'/>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeploymentOptionSection_Type", propOrder = {
    "configuration",
    "any"
})
public class DeploymentOptionSectionType
    extends SectionType
{

    @XmlElement(name = "Configuration", required = true)
    protected List<DeploymentOptionSectionType.Configuration> configuration;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the configuration property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the configuration property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfiguration().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeploymentOptionSectionType.Configuration }
     * 
     * 
     */
    public List<DeploymentOptionSectionType.Configuration> getConfiguration() {
        if (configuration == null) {
            configuration = new ArrayList<DeploymentOptionSectionType.Configuration>();
        }
        return this.configuration;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Label" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type"/>
     *         &lt;element name="Description" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type"/>
     *       &lt;/sequence>
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="default" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;anyAttribute processContents='lax'/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "label",
        "description"
    })
    public static class Configuration {

        @XmlElement(name = "Label", required = true)
        protected MsgType label;
        @XmlElement(name = "Description", required = true)
        protected MsgType description;
        @XmlAttribute(namespace = "http://schemas.dmtf.org/ovf/envelope/1", required = true)
        protected String id;
        @XmlAttribute(name = "default", namespace = "http://schemas.dmtf.org/ovf/envelope/1")
        protected Boolean _default;
        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();

        /**
         * Gets the value of the label property.
         * 
         * @return
         *     possible object is
         *     {@link MsgType }
         *     
         */
        public MsgType getLabel() {
            return label;
        }

        /**
         * Sets the value of the label property.
         * 
         * @param value
         *     allowed object is
         *     {@link MsgType }
         *     
         */
        public void setLabel(MsgType value) {
            this.label = value;
        }

        /**
         * Gets the value of the description property.
         * 
         * @return
         *     possible object is
         *     {@link MsgType }
         *     
         */
        public MsgType getDescription() {
            return description;
        }

        /**
         * Sets the value of the description property.
         * 
         * @param value
         *     allowed object is
         *     {@link MsgType }
         *     
         */
        public void setDescription(MsgType value) {
            this.description = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the default property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isDefault() {
            if (_default == null) {
                return false;
            } else {
                return _default;
            }
        }

        /**
         * Sets the value of the default property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setDefault(Boolean value) {
            this._default = value;
        }

        /**
         * Gets a map that contains attributes that aren't bound to any typed property on this class.
         * 
         * <p>
         * the map is keyed by the name of the attribute and 
         * the value is the string value of the attribute.
         * 
         * the map returned by this method is live, and you can add new attribute
         * by updating the map directly. Because of this design, there's no setter.
         * 
         * 
         * @return
         *     always non-null
         */
        public Map<QName, String> getOtherAttributes() {
            return otherAttributes;
        }

    }

}