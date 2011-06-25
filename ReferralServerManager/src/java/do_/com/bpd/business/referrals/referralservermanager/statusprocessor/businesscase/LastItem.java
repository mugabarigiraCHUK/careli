/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.statusprocessor.businesscase;

import do_.com.bpd.business.referrals.referralservermanager.api.ILastItem;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Samuel Pichardo
 */
public class LastItem implements ILastItem {

    @Override
    public String getLastItem(Map<String, Date> items) {
        String key = null;
        long date = 0;
        if (items != null && !items.isEmpty()) {
            Object[] f = items.keySet().toArray();

            for (int i = 0; i < f.length; i++) {
                long time = items.get(f[i].toString()).getTime();

                if (time > date) {
                    date = time;
                    key = f[i].toString();
                }
            }
        }
        return key;
    }
}
