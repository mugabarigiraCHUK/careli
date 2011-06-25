/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.statusprocessor;

import java.io.File;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Administrator
 */
@MessageDriven(mappedName = "jms/newCcStatusFilesFound", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "StatusFilesListener"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "StatusFilesListener")
})
public class StatusFilesListener implements MessageListener {

    public StatusFilesListener() {
    }

    @Override
    public void onMessage(Message message) {
        String[] receivedFiles = {"C:\\Users\\Administrator\\Desktop\\samples\\cs923829.csv"};
        StatusFileProcessor sfp = new StatusFileProcessor(receivedFiles);
    }
}
