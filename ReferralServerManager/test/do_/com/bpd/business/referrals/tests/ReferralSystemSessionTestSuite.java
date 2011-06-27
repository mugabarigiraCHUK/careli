/*
 * Tests:
 * Referral cruds
 * Case lifecycle simmulations
 * Administrator status change
 * NotifyAdmin of any event
 * View own referral list
 * View someone else's referral list
 * View everybody's referral list
 * View referral detail
 * Change referral status
 * Include referrer
 * Exclude referrer
 * Throw events via jms
 */
package do_.com.bpd.business.referrals.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Administrator
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ReferralManagerTest.class})
public class ReferralSystemSessionTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
