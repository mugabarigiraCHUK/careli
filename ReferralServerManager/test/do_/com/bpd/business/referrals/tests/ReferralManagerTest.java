/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.tests;

import do_.bpd.hr.bank.referrals.api.NonExistentReferral;
import java.lang.reflect.Type;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Date;
import do_.com.bpd.business.referrals.persistence.entities.Referral;
import do_.com.bpd.business.referrals.referralservermanager.facade.ReferralSystemSessionRemote;
import do_.com.bpd.business.referrals.referralservermanager.impl.IdGen;
import do_.com.bpd.business.referrals.referralservermanager.impl.ReferralManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class ReferralManagerTest {

    public static int lastReferralFound = 0;
    ReferralSystemSessionRemote referralSystemSession = lookupReferralSystemSessionRemote();

    public ReferralManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void loginTest() {
        referralSystemSession.loggin("u15377");
    }

    @Test
    public void createReferralTest() {

        Gson gs = new Gson();


        //System.out.println("THE GSON : " + toJson);
        // Referral fromJson = gs.fromJson(toJson, Referral.class);

        //System.out.println("FROM JSON " + fromJson.getCustomerName());

        referralSystemSession.loggin("u15377");

        String[] customerNames = {"Alfredo Alou", "Polivio Diaz", "Juan de La Cruz", "Pango Panda", "Sato Gato", "Foo Bar", "Tanya Roberts", "Vilma Picapiedras", "Felipe Tudor", "Eso Ajo"};
        String[] customerNtlIds = {"23423423", "090980983", "1230957", "248202357", "23847922", "95023974", "345093405", "26923849", "23487923", "2347299"};

        String statusStr = "";
        String referrerId = "";

        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                statusStr = ReferralManager.DELETED_STATUS;
            } else if (i == 6) {
                statusStr = ReferralManager.ISSUED_STATUS;
            } else if (i == 9) {
                statusStr = ReferralManager.CLOSED_STATUS;
            } else {
                statusStr = ReferralManager.NEW_STATUS;
            }

            if (i == 4 || i == 8) {
                referrerId = "u10000";
            } else {
                referrerId = "u15377";
            }

            Referral rf = new Referral();
            rf.setLoggedUser("Null");
            rf.setCustomerName(customerNames[i]);
            rf.setCustomerFullAddress("Some address");
            rf.setCustomerNtlId(customerNtlIds[i]);
            rf.setPhones("809-544-6555, 829-333-4444");
            rf.setReferralCreationDate(new Date());
            rf.setReferralDate(new Date());
            rf.setReferrerId(referrerId);
            rf.setSourceIp("10.10.10." + i);
            rf.setStatus(statusStr);
            rf.setTrackId(IdGen.generateId());
            String toJson = gs.toJson(rf);
            String createReferral = referralSystemSession.createReferral(toJson);
            //System.out.println("NEW REFERRAL = " + createReferral);
        }
    }

    @Test
    public void getAllReferrals() {
        String allReferrals = referralSystemSession.getAllReferrals(null);
        Type collectionType = new TypeToken<List<Referral>>() {
        }.getType();
        List fromJson = getGson().fromJson(allReferrals, collectionType);
        assertTrue(fromJson.size() > 9);
//        for (Object object : fromJson) {
//            System.out.println(((Referral) object).getCustomerName());
//        }
    }

    @Test
    public void getReferral() {
        String referral = "null";
        for (int i = 1340; i < 100000; i++) {
            try {
                referral = referralSystemSession.getReferral(Integer.toString(i));
                if (!referral.equals("null") && !referral.equals("")) {
                    lastReferralFound = i;
                    break;
                }
            } catch (NonExistentReferral ex) {
                Logger.getLogger(ReferralManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //System.out.println("REFERRAL FOUND " + lastReferralFound + " " +  referral);
        assertFalse(referral.equals("null"));
    }

    @Test
    public void deleteReferral() {
        try {
            String referral = "null";
            int refToDelete = 0;
            for (int i = 200; i < 100000; i++) {
                try {
                    referral = referralSystemSession.getReferral(Integer.toString(i));
                    if (!referral.equals("null") && !referral.equals("")) {
                        //System.out.println("FOUND REFERRAL AT " + i + ". CONTENT: " + referral);
                        refToDelete = i;
                        break;
                    }
                } catch (NonExistentReferral ex) {
                    Logger.getLogger(ReferralManagerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String deleteReferral = referralSystemSession.deleteReferral(String.valueOf(refToDelete), "Some comments");
            String deletedReferral = referralSystemSession.getReferral(String.valueOf(refToDelete));
            Gson gson = getGson();
            Referral fromJson = gson.fromJson(deletedReferral, Referral.class);
            assertEquals(fromJson.getStatus(), "deleted");
        } catch (NonExistentReferral ex) {
            Logger.getLogger(ReferralManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void getEmployeeReferrals() {
        String fakeEmployeeReferrals = referralSystemSession.listEmployeeReferrals("u10000");
        Type theType = new TypeToken<List<Referral>>() {
        }.getType();
        List<Referral> fromJson = getGson().fromJson(fakeEmployeeReferrals, theType);
        assertFalse(fromJson.size() > 0);

        String realEmployeeReferrals = referralSystemSession.listEmployeeReferrals("u15377");
        List<Referral> refs = getGson().fromJson(realEmployeeReferrals, theType);

        assertTrue(refs.size() > 0);

    }

    @Test
    public void saveReferral() {
        String referral = "null";
        for (int i = 200; i < 100000; i++) {
            try {
                referral = referralSystemSession.getReferral(Integer.toString(i));
                if (!referral.equals("null") && !referral.equals("")) {
                    lastReferralFound = i;
                    break;
                }
            } catch (NonExistentReferral ex) {
                Logger.getLogger(ReferralManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Referral r = getGson().fromJson(referral, Referral.class);
        r.setStatus("APPROVED");
        String saveReferral = referralSystemSession.saveReferral(getGson().toJson(r));
        System.out.println(saveReferral);
    }

    private Gson getGson() {
        return new Gson();
    }

    private ReferralSystemSessionRemote lookupReferralSystemSessionRemote() {
        try {
            Context c = new InitialContext();
            return (ReferralSystemSessionRemote) c.lookup("java:global/ReferralServer/ReferralServerManager/ReferralSystemSession!do_.com.bpd.business.referrals.referralservermanager.facade.ReferralSystemSessionRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
