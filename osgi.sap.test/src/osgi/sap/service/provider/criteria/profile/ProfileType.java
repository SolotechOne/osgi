//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.10.04 um 11:39:17 AM CEST 
//


package osgi.sap.service.provider.criteria.profile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für profileType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="profileType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="root" type="{}rootType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="state" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lastchuser" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lastchtmstmp" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="createuser" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "profileType", propOrder = {
    "root"
})
@XmlRootElement(name = "profile")
public class ProfileType {

    @XmlElement(required = true)
    protected RootType root;
    @XmlAttribute(name = "type", required = true)
    protected String type;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "description", required = true)
    protected String description;
    @XmlAttribute(name = "state")
    protected String state;
    @XmlAttribute(name = "lastchuser")
    protected String lastchuser;
    @XmlAttribute(name = "lastchtmstmp")
    protected String lastchtmstmp;
    @XmlAttribute(name = "createuser")
    protected String createuser;

    /**
     * Ruft den Wert der root-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RootType }
     *     
     */
    public RootType getRoot() {
        return root;
    }

    /**
     * Legt den Wert der root-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RootType }
     *     
     */
    public void setRoot(RootType value) {
        this.root = value;
    }

    /**
     * Ruft den Wert der type-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Legt den Wert der type-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
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
     * Legt den Wert der id-Eigenschaft fest.
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
     * Ruft den Wert der description-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Legt den Wert der description-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Ruft den Wert der state-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Legt den Wert der state-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Ruft den Wert der lastchuser-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastchuser() {
        return lastchuser;
    }

    /**
     * Legt den Wert der lastchuser-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastchuser(String value) {
        this.lastchuser = value;
    }

    /**
     * Ruft den Wert der lastchtmstmp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastchtmstmp() {
        return lastchtmstmp;
    }

    /**
     * Legt den Wert der lastchtmstmp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastchtmstmp(String value) {
        this.lastchtmstmp = value;
    }

    /**
     * Ruft den Wert der createuser-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateuser() {
        return createuser;
    }

    /**
     * Legt den Wert der createuser-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateuser(String value) {
        this.createuser = value;
    }

}
