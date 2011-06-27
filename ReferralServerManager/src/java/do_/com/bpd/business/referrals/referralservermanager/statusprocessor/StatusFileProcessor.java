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
    public static final String SYSTEM_USER_ROLE = "SYSTEM";
    private static final Logger logger = Logger.getLogger(StatusFileProcessor.class.getName());
    public static int attempts = 0;

    public StatusFileProcessor(final String[] receivedFiles) {
        Runnable r = new Runnable() {

            String[] receivedFilesNames = receivedFiles;
            File[] receivedFilesFO = null;
            int iCounter = 0;
            private List<String> expectedMeaninglessStatus = new ArrayList();

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
                    } else if (status.equals(ReferralManager.CANCELLED_STATUS)) {
                        //Nothing is done when the status is closed                
                        processCancelledReferral(referral);
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
                    //The new referral has being captured whatsoever
                    referral.setStatus(ReferralManager.CAPTURED_STATUS);
                    Referral saveReferral = ReferralManager.getDefault(SYSTEM_USER_ROLE).saveReferral(referral, null);
                } else {
                    //The referral product form has not been captured
                    //Nothing to do but wait
                }
            }

            /**
             * Processes captured referrals
             */
            private void processCapturedReferral(Referral referral) throws Exception {
                BusinessCaseAbstractClass referralCase = getReferralLastBusinessCase(referral);
                String businessCaseStatus = referralCase.getBusinessCaseStatus();
                if (businessCaseStatus.equals("CAPTURED")) {
                    //It is still in capture state
                    //Nothing to do by now but wait
                } else if (businessCaseStatus.equals("APPROVED")) {
                    //Product has been approved and is ready to be issued
                    referral.setStatus(ReferralManager.ISSUED_STATUS);
                    ReferralManager.getDefault(SYSTEM_USER_ROLE).saveReferral(referral, null);
                } else if (businessCaseStatus.equals("REJECTED")) {
                    //The request have been rejected. Let's cancel it
                    referral.setStatus(ReferralManager.CANCELLED_STATUS);
                    ReferralManager.getDefault(SYSTEM_USER_ROLE).saveReferral(referral, null);
                } else {
                    //There is an unexpected status
                    if (!isAnExpectedMeaninglessBusinessCaseStatus(businessCaseStatus)) {
                        processUnknownBusinessCaseStatus(referral, referralCase);
                    } else {
                        //Do nothing. Everything is fine
                    }
                }
            }

            private void processDeletedReferral(Referral referral) {
                //This should never happen
                logger.log(Level.WARNING, "A deleted referral arrived at StatusFileProcessor. This should not be happening. The referral id is : " + referral.getId());
            }

            private void processIssuedProductReferral(Referral referral) throws Exception {
                BusinessCaseAbstractClass referralCase = getReferralLastBusinessCase(referral);
                String businessCaseStatus = referralCase.getBusinessCaseStatus();
                if (businessCaseStatus.equals("ISSUED")) {
                    //The referral case is still in issued status
                    //Nothing to do by now
                } else if (businessCaseStatus.equals("ACTIVE")) {
                    //The product has been activated thus delivered.
                    referral.setStatus(ReferralManager.ACTIVATED_STATUS);
                    ReferralManager.getDefault(SYSTEM_USER_ROLE).saveReferral(referral, null);
                } else if (businessCaseStatus.equals("USED")) {
                    //Just in case the product have been used, let's skip to closed
                    referral.setStatus(ReferralManager.CLOSED_STATUS);
                    ReferralManager.getDefault(SYSTEM_USER_ROLE).saveReferral(referral, null);
                } else {
                    //There is an unexpected status
                    if (!isAnExpectedMeaninglessBusinessCaseStatus(businessCaseStatus)) {
                        processUnknownBusinessCaseStatus(referral, referralCase);
                    } else {
                        //Do nothing. Everything is fine
                    }
                }
            }

            /**
             * Processes referrals when the product has been activated
             */
            private void processActivatedProductReferral(Referral referral) throws Exception {
                BusinessCaseAbstractClass referralCase = getReferralLastBusinessCase(referral);
                String businessCaseStatus = referralCase.getBusinessCaseStatus();
                if (businessCaseStatus.equals("ACTIVE")) {
                    //The referral is still in active status. Meaning it haven't been used just yet
                    //Nothing to do by now but wait
                } else if (businessCaseStatus.equals("USED")) {
                    //The product has been used so the referral is closed whatsoever
                    referral.setStatus(ReferralManager.CLOSED_STATUS);
                    ReferralManager.getDefault(SYSTEM_USER_ROLE).saveReferral(referral, null);
                } else {
                    //There is an unexpected status
                    if (!isAnExpectedMeaninglessBusinessCaseStatus(businessCaseStatus)) {
                        processUnknownBusinessCaseStatus(referral, referralCase);
                    } else {
                        //Do nothing. Everything is fine
                    }
                }
            }

            /**
             * Processes referrals after the product have been used at least once
             */
            private void processUsedProductReferral(Referral referral) throws Exception {
                BusinessCaseAbstractClass referralCase = getReferralLastBusinessCase(referral);
                String businessCaseStatus = referralCase.getBusinessCaseStatus();
                if (businessCaseStatus.equals("USED")) {
                    //The referral case is still in used status. It should have been already promoted to closed
                    if (referral.getStatus().equals(ReferralManager.CLOSED_STATUS)) {
                        referral.setStatus(ReferralManager.CLOSED_STATUS);
                        ReferralManager.getDefault(SYSTEM_USER_ROLE).saveReferral(referral, null);
                    } else {
                        //Nothing else to do. This is closed.
                    }
                } else {
                    //There is an unexpected status
                    if (!isAnExpectedMeaninglessBusinessCaseStatus(businessCaseStatus)) {
                        processUnknownBusinessCaseStatus(referral, referralCase);
                    } else {
                        //Do nothing
                    }
                }
            }

            /**
             * Process referrals that have been closed
             */
            private void processClosedReferral(Referral referral) {
                //Nothing should be done when a referral is closed except to change the state if not changed
                //This should never be executed. But just in case...
                logger.log(Level.WARNING, "A closed referral is being processed. This should not be happening");
            }

            /**
             * Processes a referral that comes with an unexpected status
             */
            private void processUnknownStatusReferral(Referral referral) throws Exception {
                String unknownStatusStr = (referral.getStatus().isEmpty()) ? "[EMPTY]" : referral.getStatus();
                logger.log(Level.WARNING, "Unknown referral status is being processed. The status is " + unknownStatusStr + ", the referral id is ", String.valueOf(referral.getId()));
                //TODO: tellAdmin("Unknown referral status is being processed for referral with id: " + referral.getId());
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

            /**
             * Returns true if the status is expected although meaningless
             */
            private boolean isAnExpectedMeaninglessBusinessCaseStatus(String businessCaseStatus) {
                //TODO: Fill up the meaningless cases status
                return expectedMeaninglessStatus.contains(businessCaseStatus);
            }

            /**
             * Prompts a user for solution to this case
             */
            private void processUnknownBusinessCaseStatus(Referral referral, BusinessCaseAbstractClass referralCase) {
                //TODO: Implement
            }

            private void processCancelledReferral(Referral referral) {
                logger.log(Level.WARNING, "Cancelled referral processed. This should not happen");
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
        List<Referral> activeReferrals = ReferralManager.getDefault(SYSTEM_USER_ROLE).getActiveReferrals();
        return activeReferrals;
    }
}
