/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.statusprocessor.businesscase;

import do_.com.bpd.business.referrals.api.ICaseExtractor;
import do_.com.bpd.business.referrals.impl.CaseExtractor;
import do_.com.bpd.business.referrals.persistence.entities.Referral;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class BusinessCaseCollection {

    private static BusinessCaseCollection instance;
    private File[] businessCaseFiles = {};
    public Map<Integer, BusinessCaseAbstractClass> businessCaseCollection = new TreeMap();
    private Map<String, Map<Integer, String[]>> filesCases = new TreeMap();
    private Referral referral;

    public static BusinessCaseCollection getDefault(File[] fs, Referral referral) throws Exception {
        if (null == instance) {
            instance = new BusinessCaseCollection(fs, referral);
        }
        //if (fs != instance.businessCaseFiles) {
            //Re-initializes the collection
            instance = new BusinessCaseCollection(fs, referral);
        //}
        return instance;
    }

    public BusinessCaseCollection(File[] businessCaseFiles, Referral referral) throws Exception {
        this.referral = referral;
        this.businessCaseFiles = businessCaseFiles;
        initialize();
    }

    public BusinessCaseCollection(String[] businessCaseFiles, Referral referral) throws Exception {
        this.referral = referral;
        List<File> fs = new ArrayList();
        for (int i = 0; i < businessCaseFiles.length; i++) {
            String fileName = businessCaseFiles[i];
            File casesFile = new File(fileName);
            if (casesFile.exists()) {
                fs.add(new File(fileName));
            }
        }
        if (fs.size() > 0) {
            File[] filesGot = fs.toArray(new File[fs.size()]);
            this.businessCaseFiles = filesGot;
        }
        initialize();
    }

    private void initialize() throws Exception {
        do_.com.bpd.business.referrals.api.ICaseExtractor ce = new CaseExtractor();
        if (null == System.getProperty("mindFirstRowAsHeader")) {
            System.setProperty("mindFirstRowAsHeader", String.valueOf(true));
        }
        if (!businessCaseCollection.isEmpty()) {
            businessCaseCollection.clear();
        }
        for (File file : businessCaseFiles) {
            //String sourceType = getSourceType(file);
            Map<Integer, String[]> cases = getFileCases(ce, file);
            Set<Entry<Integer, String[]>> businessCaseEntries = cases.entrySet();
            int iCounter = 0;
            boolean mindHeaders = Boolean.parseBoolean(System.getProperty("mindFirstRowAsHeader"));
            for (Entry<Integer, String[]> businessCaseEntry : businessCaseEntries) {
                if (iCounter == 0 && mindHeaders) {
                    //TODO: Validate fields and header correlation
                    iCounter++;
                    continue;
                }
                iCounter++;
                CreditScoreBusinessCase bc;
                try {
                    bc = new CreditScoreBusinessCase((String[]) businessCaseEntry.getValue());
                    if (bc.matchesReferral(this.referral)) {
                        businessCaseCollection.put(mindHeaders ? iCounter - 1 : iCounter, bc);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(BusinessCaseCollection.class.getName()).log(Level.SEVERE, "Business case initialization failed in file " + file.getName(), ex);
                    throw ex;
                }
            }
        }
    }

    /**
     * Extracts the cases in a file.  If it has been extracted before just returns a cached copy of the cases
     * avoiding iterating in the file content again.
     * @param ce
     * @param file
     * @return
     * @throws IOException 
     */
    private Map<Integer, String[]> getFileCases(ICaseExtractor ce, File file) throws IOException {
        final String filePathStr = file.getPath();
        Map<Integer, String[]> get = filesCases.get(filePathStr);
        if (get == null) {
            filesCases.put(filePathStr, ce.getCases(file));
        }
        return filesCases.get(filePathStr);
    }

    public Map<Integer, BusinessCaseAbstractClass> getBusinessCaseCollection() {
        return businessCaseCollection;
    }

    public void setBusinessCaseCollection(Map<Integer, BusinessCaseAbstractClass> businessCaseCollection) {
        this.businessCaseCollection = businessCaseCollection;
    }

}
