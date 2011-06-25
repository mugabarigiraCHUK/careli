/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.bpd.hr.bank.referrals.api;

/**
 *
 * @author Administrator
 */
public class NonExistentReferral extends Exception {

    public NonExistentReferral(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistentReferral(String message) {
        super(message);
    }
}
