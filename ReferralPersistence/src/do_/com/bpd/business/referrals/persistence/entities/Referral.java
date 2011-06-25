/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;

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
@Table(name = "referral")
@XmlRootElement
@Cacheable(false)
@NamedQueries({
    @NamedQuery(name = "Referral.findAll", query = "SELECT r FROM Referral r"),
    @NamedQuery(name = "Referral.findById", query = "SELECT r FROM Referral r WHERE r.id = :id"),
    @NamedQuery(name = "Referral.findByReferrerId", query = "SELECT r FROM Referral r WHERE r.status <> 'deleted' AND r.referrerId = :referrer_id"),
    @NamedQuery(name = "Referral.findByReferralDate", query = "SELECT r FROM Referral r WHERE r.referralDate = :referralDate"),
    @NamedQuery(name = "Referral.findByReferralCreationDate", query = "SELECT r FROM Referral r WHERE r.referralCreationDate = :referralCreationDate"),
    @NamedQuery(name = "Referral.findByCustomerNtlId", query = "SELECT r FROM Referral r WHERE r.customerNtlId = :customerNtlId"),
    @NamedQuery(name = "Referral.findByCustomerInternalId", query = "SELECT r FROM Referral r WHERE r.customerInternalId = :customerInternalId"),
    @NamedQuery(name = "Referral.findByCustomerName", query = "SELECT r FROM Referral r WHERE r.customerName = :customerName"),
    @NamedQuery(name = "Referral.findBySourceIp", query = "SELECT r FROM Referral r WHERE r.sourceIp = :sourceIp"),
    @NamedQuery(name = "Referral.findByTrackId", query = "SELECT r FROM Referral r WHERE r.trackId = :trackId"),
    @NamedQuery(name = "Referral.findByStatus", query = "SELECT r FROM Referral r WHERE r.status = :status"),
    @NamedQuery(name = "Referral.findByLoggedUser", query = "SELECT r FROM Referral r WHERE r.loggedUser = :loggedUser")})
public class Referral implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "referralDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date referralDate;
    @Column(name = "referral_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date referralCreationDate;
    @Lob
    @Column(name = "referrer_id")
    private String referrerId;
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
    @Lob
    @Column(name = "phones")
    private String phones;
    @Column(name = "track_id")
    private String trackId;
    @Column(name = "status")
    private String status;
    @Column(name = "logged_user")
    private String loggedUser;

    public Referral() {
    }

    public Referral(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(Date referralDate) {
        this.referralDate = referralDate;
    }

    public Date getReferralCreationDate() {
        return referralCreationDate;
    }

    public void setReferralCreationDate(Date referralCreationDate) {
        this.referralCreationDate = referralCreationDate;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
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

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
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
        if (!(object instanceof Referral)) {
            return false;
        }
        Referral other = (Referral) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "do_.com.bpd.business.referrals.persistence.entities.Referral[ id=" + id + " ]";
    }
    
}
