/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.bpd.hr.bank.referrals.api;

/**
 *
 * @author Administrator
 */
public enum ReferralStatus {
    /**
     * The referral is active and in motion
     */
    ACTIVE,
    /**
     * The referral is created but not active
     */
    CREATED,
    /**
     * The referral is closed and no further action can be taken on it unless
     * it is re-activated
     */
    CLOSED,
    /**
     * Is set to standby by an administrator
     */
    STAND_BY,
    /**
     * Cancelled by the owner or the administrator
     */
    CANCELLED
}
