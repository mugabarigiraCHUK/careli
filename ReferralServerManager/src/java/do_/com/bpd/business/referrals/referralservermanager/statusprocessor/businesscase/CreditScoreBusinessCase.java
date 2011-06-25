/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.statusprocessor.businesscase;

import do_.com.bpd.business.referrals.persistence.entities.Referral;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class CreditScoreBusinessCase extends BusinessCaseAbstractClass {

    public CreditScoreBusinessCase(String[] businessCaseRow) throws Exception {
        super(businessCaseRow);
    }

    @Override
    public String getSourceType() {
        return "CC";
    }

    @Override
    public Map<String, Object> getFields() {
        return this.businessCaseRow;
    }

    @Override
    public boolean matchesReferral(Referral referral) {
        //A credit score business case...
        Map<String, Object> fields = this.getFields();
        String customerId = "";
        if (fields != null) {
            customerId = (String) this.getFields().get("client_id");
        }
        final String customerNtlId = referral.getCustomerNtlId();
        final boolean matching = (customerId == null) ? false : customerId.equals(customerNtlId);
        return matching;
    }

    @Override
    public String[] getCSVColumns() {
        String[] columns = {"id", "date", "client_id", "doc_type", "address", "phone", "status", "prod_num"};
        return columns;
    }

    @Override
    public Map<String, Object> getBusinessCaseRow() {
        return this.businessCaseRow;
    }
}
