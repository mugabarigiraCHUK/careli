/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.facade;

import do_.bpd.hr.bank.referrals.api.NonExistentReferral;
import javax.ejb.Remote;

/**
 *
 * @author Administrator
 */
@Remote
public interface ReferralSystemSessionRemote {

    public void loggin(String lLoggedUser);

    public String createReferral(String obj);

    public java.lang.String listEmployeeReferrals(java.lang.String employeeId);

    public java.lang.String getReferral(java.lang.String referralId) throws NonExistentReferral;

    public java.lang.String getAllReferrals(java.lang.String filter);

    public java.lang.String deleteReferrer(java.lang.String referrerId);

    public java.lang.String activateReferrer(java.lang.String referrerId, java.lang.String comment);

    public java.lang.String suspendReferrer(java.lang.String referrerId, java.lang.String comment);

    public java.lang.String createReferrer(java.lang.String referrerInfo);

    public java.lang.String getReferrers();

    public java.lang.String reassignReferral(java.lang.String referralId, java.lang.String newReferrerId);

    public java.lang.String changeReferralStatus(java.lang.String referralIds, java.lang.String newStatus);

    public java.lang.String getDeletedReferrals();

    public java.lang.String deleteReferrals(java.lang.String referralIds, java.lang.String comment);

    public java.lang.String deleteReferral(java.lang.String referralId, java.lang.String comment);

    public java.lang.String createNewReferral(java.lang.String newReferralData);

    public java.lang.String listOwnReferrals();

    public java.lang.String saveReferral(java.lang.String referral);
}
