/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.scify.democracit.demoutils.general;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class CollectionsCalcs {
    
    /**
     *
     * @param <K>
     * @param <V>
     * @param map
     * @param asc true for ascending sorting
     * @return a linkedHashMap with the values sorted in descending order
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValues(Map<K, V> map, final boolean asc) {
        // add entries in a linkedList
        List<Map.Entry<K, V>> list
                = new LinkedList<>(map.entrySet());
        // sort list
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int i = (o1.getValue()).compareTo(o2.getValue());
                return asc ? i : -i;
            }
        });
        // create a new LinkedHashMap
        Map<K, V> result = new LinkedHashMap<>();
        // update values
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
