/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.bpd.hr.bank.referrals.api.ccadapter;

/**
 *
 * @author Administrator
 */
public interface ICreditCardSystemAdapter {

    /**
     * Returns the credit card status based on the creditScoring code passed
     * @param creditScoringCode
     * @return 
     */
    public String getCreditCardStatus(String creditScoringCode);
}
