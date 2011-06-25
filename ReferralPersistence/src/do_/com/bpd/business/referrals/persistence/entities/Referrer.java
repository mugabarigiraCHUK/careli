/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "referrer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Referrer.findAll", query = "SELECT r FROM Referrer r"),
    @NamedQuery(name = "Referrer.findById", query = "SELECT r FROM Referrer r WHERE r.id = :id"),
    @NamedQuery(name = "Referrer.findByReferrerId", query = "SELECT r FROM Referrer r WHERE r.referrerId = :referrerId"),
    @NamedQuery(name = "Referrer.findByReferralDate", query = "SELECT r FROM Referrer r WHERE r.referralDate = :referralDate"),
    @NamedQuery(name = "Referrer.findByCreationDate", query = "SELECT r FROM Referrer r WHERE r.creationDate = :creationDate"),
    @NamedQuery(name = "Referrer.findByCustomerNtlId", query = "SELECT r FROM Referrer r WHERE r.customerNtlId = :customerNtlId"),
    @NamedQuery(name = "Referrer.findByCustomerInternalId", query = "SELECT r FROM Referrer r WHERE r.customerInternalId = :customerInternalId"),
    @NamedQuery(name = "Referrer.findByCustomerName", query = "SELECT r FROM Referrer r WHERE r.customerName = :customerName"),
    @NamedQuery(name = "Referrer.findBySourceIp", query = "SELECT r FROM Referrer r WHERE r.sourceIp = :sourceIp")})
public class Referrer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "referrer_id")
    private String referrerId;
    @Column(name = "referral_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date referralDate;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "customer_ntl_id")
    private String customerNtlId;
    @Column(name = "customer_internal_id")
    private String customerInternalId;
    @Column(name = "customer_name")
    private String customerName;
    @Lob
    @Column(name = "customer_full_address")
    private String customerFullAddress;
    @Column(name = "source_ip")
    private String sourceIp;

    public Referrer() {
    }

    public Referrer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }

    public Date getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(Date referralDate) {
        this.referralDate = referralDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCustomerNtlId() {
        return customerNtlId;
    }

    public void setCustomerNtlId(String customerNtlId) {
        this.customerNtlId = customerNtlId;
    }

    public String getCustomerInternalId() {
        return customerInternalId;
    }

    public void setCustomerInternalId(String customerInternalId) {
        this.customerInternalId = customerInternalId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerFullAddress() {
        return customerFullAddress;
    }

    public void setCustomerFullAddress(String customerFullAddress) {
        this.customerFullAddress = customerFullAddress;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Referrer)) {
            return false;
        }
        Referrer other = (Referrer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "do_.com.bpd.business.referrals.entities.Referrer[ id=" + id + " ]";
    }
    
}
