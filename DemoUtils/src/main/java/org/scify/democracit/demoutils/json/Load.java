/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.scify.democracit.demoutils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class Load {
    
    private static final String TAG = Load.class.getName();
    private static final Gson json = new GsonBuilder().disableHtmlEscaping().create();
    
    /**
     * Returns an instance of the Object relevant to the JSON string
     * 
     * @param <T>
     * @param jsonstring the String in json format to be converted.
     * @param classOfT the template the object corresponds to.
     * @return instance of object corresponding to the JSON String.
     * @throws JsonSyntaxException
     * @throws JsonIOException
     */
    public static <T> T unjsonize(String jsonstring, Class<T> classOfT) 
            throws JsonSyntaxException, JsonIOException {
        return json.fromJson(jsonstring, classOfT);
    }
    
    /**
     * 
     * @param <T> 
     * @param sPathToFile
     * @param encoding
     * @param classOfT
     * @return 
     */
    public static <T> T fromFile(String sPathToFile, String encoding, Class<T> classOfT) {
        File fFile = new File(sPathToFile);
        FileInputStream fstream = null;
        if (fFile.canRead()) {
            try {
                fstream = new FileInputStream(fFile);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName(encoding)));
                return json.fromJson(br, classOfT);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TAG).log(Level.SEVERE, ex.getMessage(), ex);
                return null;
            } finally {
                try {
                    if (fstream != null) {
                        fstream.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            Logger.getLogger(TAG).log(Level.WARNING, "Could not read from file {0}", sPathToFile);
            return null;
        }
    }
    
    /**
     * load a <b>Collection</b> of T from string properly
     * @param <T>
     * @param sFullString
     * @param classOfT
     * @return 
     */
    public static <T> T fromString(String sFullString, Class<T> classOfT) {
        Collection<T> cRes = new ArrayList<>();
        try {
            JsonElement jsonElem = new JsonParser().parse(sFullString);
            JsonArray array = jsonElem.getAsJsonArray();
            for (JsonElement eachModelStructAsStr : array) {
                Gson gson = new Gson();
                T eachObj = gson.fromJson(eachModelStructAsStr, classOfT);
                cRes.add(eachObj);
            }
        } catch (JsonSyntaxException ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
        return (T) cRes;
    }    
}
