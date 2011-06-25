package do_.com.bpd.business.referrals.referralservermanager.impl;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import do_.bpd.hr.bank.referrals.api.NonExistentReferral;
import do_.com.bpd.business.referrals.persistence.entities.Referral;
import do_.com.bpd.business.referrals.persistence.exceptions.InvalidReferral;
import do_.com.bpd.business.referrals.persistence.exceptions.ReferralExistsAndActive;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public interface IReferralManager {

    /**
     * Cancels a given referral
     * @return
     */
    boolean cancelReferral(int referralId);

    /**
     * Creates a new referral
     * @param options
     * @return
     */
    Referral createReferral(Map<String, Object> options) throws InvalidReferral, ReferralExistsAndActive, Exception;

    /**
     * Deletes a referral
     * @param referral
     * @param comment
     * @return 
     */
     public boolean deleteReferral(Referral referral, String comment);
     
     /**
      * Deletes a referral
      * @param referralId
      * @param comment
      * @return 
      */
     public boolean deleteReferral(int referralId, String comment);

    /**
     * Gets the referrals of an employee
     * @return
     */
    List<Referral> getEmployeeReferrals(String empId);

    /**
     * Gets a given referral
     * @param referralId
     * @return
     */
    Referral getReferral(int referralId) throws NonExistentReferral;

    /**
     * Returns true if a client is in a referral that is currently active
     * @return
     */
    boolean isClientReferralActive(String clientNtlId) throws Exception;

    /**
     * Normalizes the id of a referral
     * @param natlId
     * @return
     */
    String normalizeNatlIdNumber(String natlId);

    /**
     * Creates a new referral based on a passed model
     * @param referral
     * @return 
     */
    public String createReferral(String referral);

    /**
     * Saves a given referral
     * @param referral
     * @return 
     */
    public Referral saveReferral(Referral referral, Map<String, Object> options)throws Exception;

    /**
     * Creates a new referral from a prototype
     * @param m
     * @return 
     */
    public Referral createReferral(Referral referral);
    
    /**
     * Transfers referral from one referrer to another
     * @param referralId
     * @param transferTo
     * @param comment
     * @return
     * @throws Exception 
     */
    public boolean transferReferral(int referralId, String transferTo, String comment) throws Exception;
    
    public List<Referral> findActiveReferrals();
}
