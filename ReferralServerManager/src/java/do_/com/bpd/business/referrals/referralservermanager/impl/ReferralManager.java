/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.impl;

import com.google.gson.Gson;
import do_.bpd.hr.bank.referrals.api.InvalidReferral;
import do_.bpd.hr.bank.referrals.api.NonExistentReferral;
import do_.com.bpd.business.referrals.persistence.entities.*;
import do_.bpd.hr.bank.referrals.api.ReferralExistsAndActive;
import do_.com.bpd.business.referrals.persistence.controllers.EntityChangeJpaController;
import do_.com.bpd.business.referrals.persistence.controllers.ReferralJpaController;
import do_.com.bpd.business.referrals.persistence.exceptions.NonexistentEntityException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Administrator
 */
public class ReferralManager implements IReferralManager {

    public static final String NEW_STATUS = "NEW";
    public static final String DELETED_STATUS = "DELETED";
    public static final String CAPTURED_STATUS = "CAPTURED";
    public static final String ISSUED_STATUS = "ISSUED";
    public static final String ACTIVATED_STATUS = "ACTIVATED";
    public static final String USED_STATUS = "USED";
    public static final String CLOSED_STATUS = "CLOSED";
    public static final String CANCELLED_STATUS = "CANCELLED";
    private static ReferralManager instance = null;
    private static String localLoggedUser = "";
    private ReferralJpaController rc;
    private Gson gson;

    public static ReferralManager getDefault(String loggedUser) {
        if (instance == null || (loggedUser == null ? localLoggedUser != null : !loggedUser.equals(localLoggedUser))) {
            localLoggedUser = loggedUser;
            instance = new ReferralManager();
        }
        return instance;
    }

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private ReferralJpaController getReferralController() {
        return new ReferralJpaController();
//        if (rc == null) {
//            rc = new ReferralJpaController();
//        }
//        return rc;
    }

    @Override
    public boolean cancelReferral(int referralId) {
        throw new UnsupportedOperationException(java.util.ResourceBundle.getBundle("do_/com/bpd/business/referrals/referralservermanager/bundle").getString("NOT SUPPORTED YET."));
    }

    //<editor-fold defaultstate="collapsed" desc="Create referral methods">
    @Override
    public Referral createReferral(Referral referral) {
        getReferralController().create(referral);
        return null;
    }

    /**
     * Creates a referral and returns a json encode version of the Referral type
     * @param referral
     * @return 
     */
    @Override
    public String createReferral(String referral) {

        boolean isValid = true;
        String msg = "";

        Gson gson = getGson();
        Referral rf = gson.fromJson(referral, Referral.class);

        //Validate required info
        isValid = requiredInfoComplete(rf);

        //Validate if referrer is suspended
        isValid = referrerIsNotSuspended(rf.getReferrerId());

        //Validate if client is already in active referral
        isValid = clientIsReadyToRefer(rf);

        //Validate if source ip is forbidden
        isValid = ipIsInRange(rf.getSourceIp());

        //Validate if date is in the future
        isValid = isWithinDateRange(rf);

        if (!isValid) {
            return "ERROR";
        }

        //Arbitrarilly set the logged user
        rf.setLoggedUser(localLoggedUser);
        getReferralController().create(rf);
        Referral newReferral = getReferralController().findReferral(rf.getId());
        String jsonReferral = gson.toJson(newReferral);
        return jsonReferral;
    }

    @Override
    @Deprecated
    public Referral createReferral(Map<String, Object> options) throws InvalidReferral, ReferralExistsAndActive, Exception {
        ReferralJpaController rjc = getReferralController();
        Map<String, String> ecc;
        rjc.create(parseToReferral(options));
        return null;
    }
    //</editor-fold>

    @Override
    public boolean deleteReferral(Referral referral, String comment) {
        return deleteReferral(referral.getId(), comment);
    }

    @Override
    public boolean deleteReferral(int referralId, String comment) {
        authorize("ADMIN,OWNER,SYSTEM");

        System.out.println("REFERRAL ID " + referralId);

        Referral referralFound = getReferralController().findReferral(referralId);
        boolean deleted = false;
        final String deletedStatus = "deleted";

        if (!referralFound.getStatus().equals(deletedStatus)) {
            String previousStatus = referralFound.getStatus();
            referralFound.setStatus(DELETED_STATUS);
            try {
                getReferralController().edit(referralFound);
                deleted = true;
                //Register incident
                EntityChange ec = new EntityChange();
                ec.setChangeDate(new Date());
                ec.setCurrentValue("deleted");
                ec.setPreviousValue(previousStatus);
                ec.setField("status");
                ec.setEntityId(referralId);
                ec.setEntityType("referral");
                ec.setComment(java.util.ResourceBundle.getBundle("do_/com/bpd/business/referrals/referralservermanager/bundle").getString("ENTITY DELETED BY ") + localLoggedUser);
                ec.setId(0);

                EntityChangeJpaController ecjc = new EntityChangeJpaController();
                ecjc.create(ec);
                ecjc = null;

            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ReferralManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ReferralManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Register change
        return deleted;
    }

    @Override
    public List<Referral> getEmployeeReferrals(String empId) {
        Query q = getReferralController().getEntityManager().createNamedQuery("Referral.findByReferrerId");
        Query setParameter = q.setParameter("referrer_id", empId);
        List<Referral> resultList = q.getResultList();
        return resultList;
    }

    @Override
    public boolean isClientReferralActive(String clientNtlId) throws Exception {
        throw new UnsupportedOperationException(java.util.ResourceBundle.getBundle("do_/com/bpd/business/referrals/referralservermanager/bundle").getString("NOT SUPPORTED YET."));
    }

    @Override
    public String normalizeNatlIdNumber(String natlId) {
        throw new UnsupportedOperationException(java.util.ResourceBundle.getBundle("do_/com/bpd/business/referrals/referralservermanager/bundle").getString("NOT SUPPORTED YET."));
    }

    @Override
    public Referral saveReferral(Referral referral, Map<String, Object> options) throws Exception {
        authorize("OWNER,ADMIN,SYSTEM");
        try {
            //VAlidate
            Referral ref = this.getAnyReferral(referral.getId());
            if (ref.getStatus().equals(DELETED_STATUS)) {
                throw new Exception(java.util.ResourceBundle.getBundle("do_/com/bpd/business/referrals/referralservermanager/bundle").getString("CANNOT MODIFY DELETED REFERRAL"));
            }
        } catch (do_.com.bpd.business.referrals.persistence.exceptions.NonExistentReferral ex) {
            Logger.getLogger(ReferralManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            getReferralController().edit(referral);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ReferralManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReferralManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return referral;
    }

    @Override
    public boolean transferReferral(int referralId, String transferTo, String comment) throws Exception {
        throw new UnsupportedOperationException(java.util.ResourceBundle.getBundle("do_/com/bpd/business/referrals/referralservermanager/bundle").getString("NOT SUPPORTED YET."));
    }

    @Override
    public List<Referral> findActiveReferrals() {
        throw new UnsupportedOperationException(java.util.ResourceBundle.getBundle("do_/com/bpd/business/referrals/referralservermanager/bundle").getString("NOT SUPPORTED YET."));
    }

    private Referral parseToReferral(Map<String, Object> options) {
        return null;
    }

    @Override
    public Referral getReferral(int referralId) throws NonExistentReferral {
        ReferralJpaController rjc = getReferralController();
        Referral findReferral = rjc.findReferral(referralId);
        if (null != findReferral) {
            if (findReferral.getStatus().equals(DELETED_STATUS)) {
                throw new NonExistentReferral(java.util.ResourceBundle.getBundle("do_/com/bpd/business/referrals/referralservermanager/bundle").getString("DELETED REFERRAL"));
            }
        }
        return findReferral;
    }

    public Referral getAnyReferral(int referralId) {
        authorize("ADMIN,SYSTEM");
        ReferralJpaController rjc = getReferralController();
        Referral findReferral = rjc.findReferral(referralId);
        return findReferral;
    }

    public String getReferral(String referralId) throws NonExistentReferral {
        Referral referral = getReferral(Integer.parseInt(referralId));
        return getGson().toJson(referral);
    }

    //<editor-fold defaultstate="collapsed" desc="Validations">
    private boolean requiredInfoComplete(Referral rf) {
        return true;
    }
    
    private boolean referrerIsNotSuspended(String referrerId) {
        return true;
    }
    
    private boolean clientIsReadyToRefer(Referral rf) {
        return true;
    }
    
    private boolean ipIsInRange(String sourceIp) {
        return true;
    }
    
    private boolean isWithinDateRange(Referral rf) {
        return true;
    }
    //</editor-fold>

    /**
     * Proxies to the Authorization Service
     * @param roles 
     */
    private void authorize(String roles) {
        return;
    }

    /**
     * Lists all the referrals in the system
     * @return 
     */
    public String getAllReferrals(String filter) {
        List<Referral> findReferralEntities = getReferralController().findReferralEntities();
        String toJson = getGson().toJson(findReferralEntities);
        return toJson;
    }

    /**
     * Lists all the referrals in the system
     * @return 
     */
    public List<Referral> getAllReferrals() {
        List<Referral> findReferralEntities = getReferralController().findReferralEntities();
        return findReferralEntities;
    }

    /**
     * Lists all the active referrals in the system
     * @return 
     */
    public List<Referral> getActiveReferrals() {
        EntityManager em = getReferralController().getEntityManager();
        Query q = em.createQuery("SELECT r FROM Referral r WHERE r.status <> '" + ReferralManager.DELETED_STATUS + "' AND r.status <> '" + ReferralManager.CLOSED_STATUS + "'");
        List<Referral> resultList = q.getResultList();
        return resultList;
    }
}
