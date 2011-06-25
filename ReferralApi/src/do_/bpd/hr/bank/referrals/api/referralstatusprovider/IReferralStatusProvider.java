/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.bpd.hr.bank.referrals.api.referralstatusprovider;

import do_.bpd.hr.bank.referrals.api.ReferralType;

/**
 *
 * @author Administrator
 */
public interface IReferralStatusProvider {
    public String getReferralStatus(String trackNumber, ReferralType type);
}
