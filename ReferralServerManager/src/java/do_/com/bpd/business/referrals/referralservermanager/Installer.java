/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager;

import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;

/**
 *
 * @author Administrator
 */
@Singleton
@LocalBean
public class Installer {

    public Installer() {
        Logger.getLogger(Installer.class.getName()).info("Installing ReferralServerManager");
    }

}
