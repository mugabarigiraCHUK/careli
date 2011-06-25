/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.bpd.hr.bank.referrals.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 * A referral is an entry
 * @author Administrator
 */


class Referral implements Serializable{
    private long id = 0;

    private Date referralDate = null;
    private Date creationDate = null;
    private String referrerId = "";
    private String customerNtlId = "";
    private String customerInternalId = "";
    private String customerName = "";
    private String customerFullAddress = "";
    private String sourceIp = "";
    private String[] customerPhones = {};
    private List<ReferralNote> referralNotes = null;
    private String trackId = "";
    private String referralType = "";
    private String status = null;

    public Referral() {
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
    
    public String getCustomerInternalId() {
        return customerInternalId;
    }

    public void setCustomerInternalId(String customerInternalId) {
        this.customerInternalId = customerInternalId;
    }

    public String getCustomerNtlId() {
        return customerNtlId;
    }

    public void setCustomerNtlId(String customerNtlId) {
        this.customerNtlId = customerNtlId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(Date referralDate) {
        this.referralDate = referralDate;
    }

    public List<ReferralNote> getReferralNotes() {
        return referralNotes;
    }

    public void setReferralNotes(List<ReferralNote> referralNotes) {
        this.referralNotes = referralNotes;
    }

    public String getReferralType() {
        return referralType;
    }

    public void setReferralType(String referralType) {
        this.referralType = referralType;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String[] getCustomerPhones() {
        return customerPhones;
    }

    public void setCustomerPhones(String[] customerPhones) {
        this.customerPhones = customerPhones;
    }

    public String getCustomerFullAddress() {
        return customerFullAddress;
    }

    public void setCustomerFullAddress(String customerFullAddress) {
        this.customerFullAddress = customerFullAddress;
    }
    
}
