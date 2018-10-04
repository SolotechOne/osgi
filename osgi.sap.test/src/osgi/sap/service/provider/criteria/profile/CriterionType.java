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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für criterionType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="criterionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="sign" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="I"/>
 *             &lt;enumeration value="E"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="opt" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="BT"/>
 *             &lt;enumeration value="NB"/>
 *             &lt;enumeration value="EQ"/>
 *             &lt;enumeration value="GE"/>
 *             &lt;enumeration value="GT"/>
 *             &lt;enumeration value="LE"/>
 *             &lt;enumeration value="LT"/>
 *             &lt;enumeration value="NE"/>
 *             &lt;enumeration value="CP"/>
 *             &lt;enumeration value="NP"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="low" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="high" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "criterionType")
public class CriterionType {

    @XmlAttribute(name = "sign", required = true)
    protected String sign;
    @XmlAttribute(name = "opt", required = true)
    protected String opt;
    @XmlAttribute(name = "low", required = true)
    protected String low;
    @XmlAttribute(name = "high")
    protected String high;

    /**
     * Ruft den Wert der sign-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSign() {
        return sign;
    }

    /**
     * Legt den Wert der sign-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSign(String value) {
        this.sign = value;
    }

    /**
     * Ruft den Wert der opt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpt() {
        return opt;
    }

    /**
     * Legt den Wert der opt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpt(String value) {
        this.opt = value;
    }

    /**
     * Ruft den Wert der low-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLow() {
        return low;
    }

    /**
     * Legt den Wert der low-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLow(String value) {
        this.low = value;
    }

    /**
     * Ruft den Wert der high-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHigh() {
        return high;
    }

    /**
     * Legt den Wert der high-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHigh(String value) {
        this.high = value;
    }

}
