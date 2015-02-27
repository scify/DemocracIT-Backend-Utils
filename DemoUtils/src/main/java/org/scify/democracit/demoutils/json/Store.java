/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.json;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author George K.<gkiom@scify.org>
 */
public class Store {

    public static final String FILE_DELIMITER = System.getProperty("file.separator");

    public static void storeFullObjectToFile(Object oObj, String sFullFileName) {
        File fFile;
        BufferedWriter bw = null;
        try {
            fFile = new File(sFullFileName);
            bw = new BufferedWriter(new FileWriter(fFile, false));
            bw.write(new Gson().toJson(oObj, oObj.getClass()));
        } catch (IOException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Error: {0}", ex.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ex) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

}
