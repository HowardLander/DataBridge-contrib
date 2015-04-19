/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.similarity.ncat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Nerketur
 */
public class OF extends Measure {
    

    @Override
    protected double in(Object dummy) {
        return 1;
    }

    @Override
    protected double notIn(Object XYk) {
        int N = data.size();
        
        Iterator<String> xy = ((Collection<String>)XYk).iterator();
        
        Collection<String> valX = new ArrayList<>();
        valX.add(xy.next());
        Collection<String> valY = new ArrayList<>();
        valY.add(xy.next());

        Collection<String> tmpX = new ArrayList<>();
        Collection<String> tmpY = new ArrayList<>();
        tmpX.addAll(fullValues);
        tmpY.addAll(fullValues);
        tmpX.retainAll(valX); // Since we're only doing main diagonal.
        tmpY.retainAll(valY); // Since we're only doing main diagonal.
        return 1/(1 + Math.log10(N/tmpX.size()) * Math.log10(N/tmpY.size()));
    }

}
