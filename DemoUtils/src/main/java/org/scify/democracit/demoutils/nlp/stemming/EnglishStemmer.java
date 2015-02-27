/*
 * 
 */

package org.scify.democracit.demoutils.nlp.stemming;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * @author ggianna
 */
public class EnglishStemmer implements IStemmer {
    SnowballStemmer stemmer;
    
    public EnglishStemmer() {
        this.stemmer = new englishStemmer();
    }

    @Override
    public String stem(String sToStem) {
      stemmer.setCurrent(sToStem);
      stemmer.stem();
      return stemmer.getCurrent();          
    }
    
}
