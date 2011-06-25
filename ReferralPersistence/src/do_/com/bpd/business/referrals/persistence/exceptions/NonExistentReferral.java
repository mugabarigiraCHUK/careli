/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.persistence.exceptions;

/**
 *
 * @author Administrator
 */
@Deprecated
public class NonExistentReferral extends Exception {

    public NonExistentReferral(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistentReferral(String message) {
        super(message);
    }
}
