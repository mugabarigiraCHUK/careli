/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.statusprocessor.businesscase;

import do_.com.bpd.business.referrals.persistence.entities.Referral;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Administrator
 */
public abstract class BusinessCaseAbstractClass {

    protected Map<String, Object> businessCaseRow = null;
    //protected String sourceType = "N/A";
    //protected String[] caseFileColumns = {"rec_id", "tango", "client_id", "pingo", "tengo"};
    //protected Map<String, Object> fields = new TreeMap();

    public BusinessCaseAbstractClass(String[] businessCaseRow) throws Exception {
        //this.businessCaseRow = businessCaseRow;
        this.businessCaseRow = buildBusinessCaseRow(businessCaseRow);
        //this.sourceType = sourceType;
    }

    /**
     * Returns the source type where the case is contained
     * @return 
     */
    public abstract String getSourceType();
    
    public abstract String[] getCSVColumns();
    
    public abstract boolean matchesReferral(Referral referral);
    
    public abstract Map<String, Object> getBusinessCaseRow();
    
    /**
     * Returns the fields in the business case
     * @return 
     */
    public abstract Map<String, Object> getFields();

    public Object getField(String fieldName) {
        return this.getBusinessCaseRow().get(fieldName);
    }
    
    public abstract String getBusinessCaseStatus();

    private Map<String, Object> buildBusinessCaseRow(String[] businessCaseRow) throws Exception {
        if(businessCaseRow.length != getCSVColumns().length){
            throw new Exception("Uneven columns and fields number");
        }
        Map<String, Object> m = new TreeMap();
        for (int i = 0; i < getCSVColumns().length; i++) {
            String columnName = getCSVColumns()[i];
            m.put(columnName, businessCaseRow[i]);
        }
        return m;
    }
}
