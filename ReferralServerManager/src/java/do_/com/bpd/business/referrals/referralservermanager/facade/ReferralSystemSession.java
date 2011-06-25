/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.facade;

import com.google.gson.Gson;
import do_.bpd.hr.bank.referrals.api.NonExistentReferral;
import do_.com.bpd.business.referrals.persistence.entities.Referral;

import do_.com.bpd.business.referrals.referralservermanager.impl.ReferralManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;

/**
 *
 * @author Administrator
 */
@Stateful
public class ReferralSystemSession implements ReferralSystemSessionRemote {

    private String loggedUser = "";

    @Override
    public void loggin(String lLoggedUser) {
        loggedUser = lLoggedUser;
    }

    @Override
    public String createReferral(String obj) {
        String createReferral = ReferralManager.getDefault(loggedUser).createReferral(obj);
        return createReferral;
    }

    /**
     * Generates a list of all non deleted referrals for the current user
     * @return 
     */
    @Override
    public String listOwnReferrals() {
        String referrerId = loggedUser;
        List<Referral> createReferral = ReferralManager.getDefault(loggedUser).getEmployeeReferrals(referrerId);
        return "";
    }
    Gson gson = null;

    private Gson getGson() {
        if (null == gson) {
            gson = new Gson();
        }
        return gson;
    }

    /**
     * Returns a list of a given employee's referrals.
     * If none is found, an empty list is returned
     * @param employeeId
     * @return 
     */
    @Override
    public String listEmployeeReferrals(String employeeId) {
        List<Referral> createReferral = ReferralManager.getDefault(loggedUser).getEmployeeReferrals(employeeId);
        for (Referral referral : createReferral) {
            System.out.println("-> REFERRAL : " + referral.getTrackId());
        }
        return getGson().toJson(createReferral);
    }

    @Override
    public String saveReferral(String referral) {
        Referral r = getGson().fromJson(referral, Referral.class);
        Referral saveReferral = null;
        try {
            saveReferral = ReferralManager.getDefault(loggedUser).saveReferral(r, null);
        } catch (Exception ex) {
            Logger.getLogger(ReferralSystemSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getGson().toJson(saveReferral);
    }

    /**
     * Returns a given referral based on the id passed
     * @param referralId
     * @return 
     */
    @Override
    public String getReferral(String referralId) throws NonExistentReferral {
        String refToStr = "null";
        refToStr = ReferralManager.getDefault(loggedUser).getReferral(referralId);
        return refToStr;
    }

    /**
     * Returns all the non-deleted referrals
     * @param filter
     * @return 
     */
    @Override
    public String getAllReferrals(String filter) {
        return ReferralManager.getDefault(loggedUser).getAllReferrals(filter);
    }

    /**
     * Creates a blank referral only with the basic information
     * @param newReferralData
     * @return 
     */
    @Override
    public String createNewReferral(String newReferralData) {
        return "";
    }

    /**
     * Marks as deleted a given referral
     * @param referralId
     * @param Comment
     * @return 
     */
    @Override
    public String deleteReferral(String referralId, String comment) {
        System.out.println("REFERRAL ID RECEIVED: " + referralId);
        boolean deleteReferral = ReferralManager.getDefault(loggedUser).deleteReferral(Integer.parseInt(referralId), comment);
        return String.valueOf(deleteReferral);
    }

    /**
     * Marks as deleted a bunch of referrals based on a list of comma separated ids
     * returns the number of referrals marked as deleted
     * @param referralIds
     * @param comment
     * @return 
     */
    @Override
    public String deleteReferrals(String referralIds, String comment) {
        return "";
    }

    /**
     * Returns a list of deleted referrals
     * @return 
     */
    @Override
    public String getDeletedReferrals() {
        return "";
    }

    /**
     * Changes the status of all the referrals specified in the referralIds collection
     * 
     * @param referralIds : May be a single id or a comma separated list of ids
     * @param newStatus : The status that will be attached to the referrals. Some status might not be able to be assigned
     * @return 
     */
    @Override
    public String changeReferralStatus(String referralIds, String newStatus) {
        return "";
    }

    /**
     * Reassigns a referral to a different user and returns true if successful
     * @param referralId
     * @param newReferrerId
     * @return 
     */
    @Override
    public String reassignReferral(String referralId, String newReferrerId) {
        return "";
    }

    /**
     * Returns all the referrers in the system
     * @return 
     */
    @Override
    public String getReferrers() {
        return "";
    }

    @Override
    public String createReferrer(String referrerInfo) {
        return "";
    }

    /**
     * Suspends a referrer. While suspended he/she cannot create new referrals
     * @param referrerId
     * @param comment
     * @return 
     */
    @Override
    public String suspendReferrer(String referrerId, String comment) {
        return "";
    }

    /**
     * Activates a previously suspended referrer
     * @param referrerId
     * @param comment
     * @return 
     */
    @Override
    public String activateReferrer(String referrerId, String comment) {
        return "";
    }

    /**
     * Deletes a referrer or marks one as deleted. By default everyone can be a referrer. A deleted referrer is banned from the system
     * and his/her entrance will not be allowed
     * @param referrerId
     * @return 
     */
    @Override
    public String deleteReferrer(String referrerId) {
        return "";
    }
}
