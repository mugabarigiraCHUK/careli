/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.api;

import java.util.Map;
import java.util.Date;

/**
 *
 * @author Samuel Pichardo
 */
public interface ILastItem {

    /**
     * Takes a map(String, Date) as parameter and returns the key related to the highest date in the collection as string;
     * @param items
     * @return 
     */
    public String getLastItem(Map<String, Date> items);
}
