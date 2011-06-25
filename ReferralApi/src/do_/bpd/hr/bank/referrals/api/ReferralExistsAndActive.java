/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.bpd.hr.bank.referrals.api;

/**
 *
 * @author Administrator
 */
public class ReferralExistsAndActive extends Exception {

    private String msg = "";

    public ReferralExistsAndActive(String string) {
        super(string);
        msg = string;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
