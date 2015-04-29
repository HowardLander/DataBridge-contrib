/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.similarity.ncat;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import java.util.ArrayList;

/**
 *
 * @author Nerketur
 */
public class Lin extends Measure {

    private static ILexicalDatabase db = new NictWordNet();
        
    @Override
    protected double computeWordSim(String xk, String yk, ArrayList<String> s) {
        if (xk.equals(yk))
            return 1.0;
        edu.cmu.lti.ws4j.impl.Lin m = new edu.cmu.lti.ws4j.impl.Lin(db);        
        return m.calcRelatednessOfWords(xk, yk);
    }
}
