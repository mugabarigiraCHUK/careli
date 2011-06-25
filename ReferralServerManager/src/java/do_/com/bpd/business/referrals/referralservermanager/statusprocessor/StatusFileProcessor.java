/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.statusprocessor;

import do_.com.bpd.business.referrals.persistence.entities.Referral;
import do_.com.bpd.business.referrals.referralservermanager.api.ILastItem;
import do_.com.bpd.business.referrals.referralservermanager.impl.ReferralManager;
import do_.com.bpd.business.referrals.referralservermanager.statusprocessor.businesscase.BusinessCaseAbstractClass;
import do_.com.bpd.business.referrals.referralservermanager.statusprocessor.businesscase.BusinessCaseCollection;
import do_.com.bpd.business.referrals.referralservermanager.statusprocessor.businesscase.LastItem;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class StatusFileProcessor {

    public static final String PROCESSING_FILE_THREAD_NAME = "STATUS_FILE_PROCESSOR";
    public static String PROCESSING_STATUS_PROP = "do_.com.bpd.business.referrals.referralservermanager.statusprocessor.processingStatusProp";
    public static final String READY = "ready";
    public static final String BUSY = "busy";
    private static final Logger logger = Logger.getLogger(StatusFileProcessor.class.getName());
    public static int attempts = 0;

    public StatusFileProcessor(final String[] receivedFiles) {
        Runnable r = new Runnable() {

            String[] receivedFilesNames = receivedFiles;
            File[] receivedFilesFO = null;
            int iCounter = 0;

            public void run() {
                try {
                    loadReceivedFiles();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(StatusFileProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }

                System.setProperty(PROCESSING_STATUS_PROP, StatusFileProcessor.BUSY);
                List<Referral> activeReferrals = loadActiveReferrals();
                try {
                    processByStatus(activeReferrals);
                } catch (Exception ex) {
                    Logger.getLogger(StatusFileProcessor.class.getName()).log(Level.SEVERE, "Could not process some or all referrals by status", ex);
                }
                logger.info("Finished processing files");
                //Finished processing files
                System.setProperty(PROCESSING_STATUS_PROP, StatusFileProcessor.READY);
            }

            private void processByStatus(List<Referral> activeReferrals) throws Exception {
                //Here the system decides what to do under these status
                for (Referral referral : activeReferrals) {

                    String status = referral.getStatus();

                    if (status.equals(ReferralManager.NEW_STATUS)) {
                        //Check in CreditScoring file seeking if there is a case with this client ntlId
                        //If true switch to next status
                        processNewReferral(referral);
                    } else if (status.equals(ReferralManager.CAPTURED_STATUS)) {
                        //Check in CS if the case is set to issued
                        //If true switch to next status
                        processCapturedReferral(referral);
                    } else if (status.equals(ReferralManager.DELETED_STATUS)) {
                        //Does nothing and should not run here
                        //If true switch to next status
                        processDeletedReferral(referral);
                    } else if (status.equals(ReferralManager.ISSUED_STATUS)) {
                        //Checks in VP/FDR in order to find a case with this client ntlid or the credit card number
                        //If true switch to next status
                        processIssuedProductReferral(referral);
                    } else if (status.equals(ReferralManager.ACTIVATED_STATUS)) {
                        //Checks in VP/FDR in order to find if the case is in used status
                        //If true switch to next status
                        processActivatedProductReferral(referral);
                    } else if (status.equals(ReferralManager.USED_STATUS)) {
                        //Just changes the status of the referral. Next stop is closed status
                        //If true switch to next status
                        processUsedProductReferral(referral);
                    } else if (status.equals(ReferralManager.CLOSED_STATUS)) {
                        //Nothing is done when the status is closed                
                        processClosedReferral(referral);
                    } else {
                        //Nothing is done when the status is closed                
                        processUnknownStatusReferral(referral);
                    }
                    //Allow other processes to thread
                    Thread.yield();
                }
            }

            /**
             * Processes referral marked as NEW
             */
            private void processNewReferral(Referral referral) throws Exception {
                BusinessCaseAbstractClass referralCase = getReferralLastBusinessCase(referral);
                if (null != referralCase) {
                    //The new referral has being captured
                    referral.setStatus(ReferralManager.CAPTURED_STATUS);
                    Referral saveReferral = ReferralManager.getDefault("LOGGED USER").saveReferral(referral, null);
                }
            }

            private void processCapturedReferral(Referral referral) throws Exception {
                BusinessCaseAbstractClass referralCase = getReferralLastBusinessCase(referral);
            }

            private void processDeletedReferral(Referral referral) {
                //This should never happen
            }

            private void processIssuedProductReferral(Referral referral) throws Exception {
                BusinessCaseAbstractClass referralCase = getReferralLastBusinessCase(referral);
            }

            /**
             * Processes referrals when the product has been activated
             */
            private void processActivatedProductReferral(Referral referral) throws Exception {
                BusinessCaseAbstractClass referralCase = getReferralLastBusinessCase(referral);
            }

            /**
             * Processes referrals after the product have been used at least once
             */
            private void processUsedProductReferral(Referral referral) throws Exception {
                BusinessCaseAbstractClass referralCase = getReferralLastBusinessCase(referral);
            }

            /**
             * Process referrals that have been closed
             */
            private void processClosedReferral(Referral referral) {
                //Nothing should be done when a referral is closed except to change the state if not changed
            }

            /**
             * Processes a referral that comes with an unexpected status
             */
            private void processUnknownStatusReferral(Referral referral) throws Exception {
                BusinessCaseAbstractClass referralCase = getReferralLastBusinessCase(referral);

                //Attempt to change the status

                //Perform discrete operations for this circumstances
            }

            /**
             * Attempts to find the last business case associated with this referral in business files
             * by the user identification or product number
             */
            private BusinessCaseAbstractClass getReferralLastBusinessCase(Referral referral) throws Exception {
                BusinessCaseCollection bcc = BusinessCaseCollection.getDefault(receivedFilesFO, referral);
                Map<Integer, BusinessCaseAbstractClass> businessCaseCollection = bcc.getBusinessCaseCollection();
                BusinessCaseAbstractClass bc = null;
                if (businessCaseCollection.size() > 0) {
                    bc = getLastBusinessCase(businessCaseCollection);
                }
                return bc;
            }

            private void loadReceivedFiles() throws FileNotFoundException {
                List<File> filesInSystem = new ArrayList();
                for (int i = 0; i < receivedFilesNames.length; i++) {
                    String fileName = receivedFilesNames[i];
                    filesInSystem.add(new File(fileName));
                }
                for (File file : filesInSystem) {
                    if (!file.exists()) {
                        throw new FileNotFoundException("File " + file.getName() + " was not found");
                    }
                }
                receivedFilesFO = filesInSystem.toArray(new File[filesInSystem.size()]);
            }

            private BusinessCaseAbstractClass getLastBusinessCase(Map<Integer, BusinessCaseAbstractClass> businessCaseCollection) throws ParseException {
                Set<Entry<Integer, BusinessCaseAbstractClass>> bcEntries = businessCaseCollection.entrySet();
                Map<String, Date> sorter = new TreeMap();
                //TODO: Set the date format as a configuration entry
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for (Entry<Integer, BusinessCaseAbstractClass> businessCaseEntry : bcEntries) {
                    //businessCase.get
                    Map<String, Object> businessCaseRow = businessCaseEntry.getValue().getBusinessCaseRow();
                    //Gets the date
                    Date businessEntryDate = sdf.parse(((String) businessCaseRow.get("date")));
                    String businessEntryId = (String) businessCaseRow.get("id");
                    sorter.put(businessEntryId, businessEntryDate);
                }
                ILastItem li = new LastItem();
                String lastItemId = li.getLastItem(sorter);
                BusinessCaseAbstractClass businessCaseFound = null;

                if (!lastItemId.isEmpty()) {
                    for (Entry<Integer, BusinessCaseAbstractClass> entry : bcEntries) {
                        if (entry.getValue().getField("id").equals(lastItemId)) {
                            businessCaseFound = entry.getValue();
                        }
                    }
                }
                return businessCaseFound;
            }
        };
        String processingStatus = System.getProperty(StatusFileProcessor.PROCESSING_STATUS_PROP, "");

        if (processingStatus.isEmpty() || processingStatus.equals(StatusFileProcessor.READY)) {
            StatusFileProcessor.attempts = 0;
            Thread t = new Thread(r);
            t.setName(PROCESSING_FILE_THREAD_NAME);
            t.setPriority(Thread.MIN_PRIORITY);
            t.setDaemon(false);
            t.start();
        } else {
            logger.warning("Could not process files because in no ready state");
            StatusFileProcessor.attempts++;
        }
    }

    /**
     * Loads the active referrals those that are not deleted or other disabling status
     * Referrals with status deleted and closed are not loaded here
     * @return 
     */
    private List<Referral> loadActiveReferrals() {
        List<Referral> activeReferrals = ReferralManager.getDefault("SYSTEM").getActiveReferrals();
        return activeReferrals;
    }
}
