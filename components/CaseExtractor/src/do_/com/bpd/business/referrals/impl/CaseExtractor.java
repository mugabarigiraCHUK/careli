/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.impl;

import com.csvreader.CsvReader;
import do_.com.bpd.business.referrals.api.ICaseExtractor;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Samuel Pichardo
 */
public class CaseExtractor implements ICaseExtractor
{

    @Override
    public Map<Integer, String[]> getCase(File file) throws IOException
    {
        CsvReader reader = null;
        Map<Integer, String[]> result = new HashMap<Integer, String[]>();
        try
        {
            reader = new CsvReader(file.getPath());
            
            int columns = reader.getColumnCount();
            int rowCount = 0;
            while (reader.readRecord())
            {
                String[] row = new String[columns];
                for (int i = 0; i < columns; i++)
                {
                    row[i] = reader.get(i);
                }
                rowCount++;
                result.put(rowCount, row);
            }
        } catch (IOException e)
        {
            throw e;
        } finally
        {
            if (reader != null)
                reader.close();
        }
        return result;
    }
}
