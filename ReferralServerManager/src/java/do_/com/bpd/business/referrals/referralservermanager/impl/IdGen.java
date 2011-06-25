/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.referralservermanager.impl;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author Administrator
 */
public class IdGen {

    private static SecureRandom random = new SecureRandom();

    public static String generateId() {
        return new BigInteger(24, random).toString();
    }
}
