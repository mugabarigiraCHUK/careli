/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.api;

import java.io.File;
import java.util.Map;
import java.io.IOException;
/**
 *
 * @author Samuel Pichardo
 */
public interface ICaseExtractor 
{
    public Map<Integer, String[]> getCase(File file) throws IOException;
}
