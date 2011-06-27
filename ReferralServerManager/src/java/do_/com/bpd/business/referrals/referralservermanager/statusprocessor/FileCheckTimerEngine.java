/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.statusprocessor;

import javax.ejb.Singleton;
import javax.ejb.LocalBean;

/**
 *
 * @author Administrator
 */
@Singleton
@LocalBean
public class FileCheckTimerEngine {
    public FileCheckTimerEngine() {
        System.out.println("TIME CHECK ENGINE LOADED");
    }
}
