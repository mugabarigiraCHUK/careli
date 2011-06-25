/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import do_.com.bpd.business.referrals.persistence.entities.Referral;
import do_.com.bpd.business.referrals.persistence.controllers.ReferralJpaController;
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
public class NewEmptyJUnitTest {

    public NewEmptyJUnitTest() {
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
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    @Test
    public void hello() {
        ReferralJpaController cnt = new ReferralJpaController();//
        Referral ref = new Referral();
        ref.setLoggedUser("logged");
        ref.setSourceIp("10SOMEIP");
        ref.setCustomerName("tango");
        cnt.create(ref);
    }
}
