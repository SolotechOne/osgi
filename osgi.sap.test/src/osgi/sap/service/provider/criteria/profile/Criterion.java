//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.10.01 um 02:43:22 PM CEST 
//


package osgi.sap.service.provider.criteria.profile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "criterion")
public class Criterion {

    @XmlAttribute(name = "sign", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String sign;
    @XmlAttribute(name = "opt", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String opt;
    @XmlAttribute(name = "low", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String low;
    @XmlAttribute(name = "high")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
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
