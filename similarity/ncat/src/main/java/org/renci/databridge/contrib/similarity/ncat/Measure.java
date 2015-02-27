package org.renci.databridge.contrib.similarity.ncat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.renci.databridge.persistence.metadata.*;



//Note that data here is a simple Collection of the Data size, followed by data elements

//Each data element will have number of attributes, (d), followed by the attribute list

/**
 * An abstract class that allows for easy implementation of various similarity measurements.
 * @author Nerketur
 */
public abstract class Measure implements SimilarityProcessor {
    /**
     * data the original collection of data
     */
    protected Collection<Collection<Collection<String>>> data;
    /**
     * fullData the collection of all attributes from all categories
     */
    protected Collection<String> fullData;
    protected Collection<String> values;
    protected ArrayList<String> fullValues;
    private ArrayList<HashSet<String>> attrs;

    /**
     * This initializes the static variables of the Measure class
     * @param data data to use in initializing the measure
     */
    public Measure(Collection<Collection<Collection<String>>> data) {
        this.data = data; // original data
        this.fullData = new ArrayList<>(); // all data from all attributes
        Iterator<Collection<Collection<String>>> itSur = data.iterator();
        while (itSur.hasNext()) {
            Iterator<Collection<String>> itAttr = itSur.next().iterator();
            while (itAttr.hasNext()) {
                fullData.addAll(itAttr.next());
            }
        }
    }

    /**
     * This computes the similarity matrix for every survey compared with every other survey, including itself.
     * @return the similarity matrix for the surveys in <code>data</code>
     */
    public double[][] compute() {
        double[][] ans = new double[data.size()][data.size()];
        Iterator<Collection<Collection<String>>> itX = data.iterator();
        for (int i = 0; itX.hasNext(); i++) {
            Collection<Collection<String>> surveyX = itX.next();
            Iterator<Collection<Collection<String>>> itY = data.iterator();
            for (int j = 0; itY.hasNext(); j++) {
                Collection<Collection<String>> surveyY = itY.next();
                ans[i][j] = Math.round(computeSim(surveyX, surveyY) * 100000)/100000.0;
                //put answer in matrix
            } //end for
        } //end for
        return ans;
    }

    private Collection<String> parseSumDscr(CollectionTransferObject collection) {
        //survey1.add(collection1.);
        //sumDscr assumes "key: value" for doing things the same way as the code
        Iterator<String> it = collection.getExtra().keySet().iterator();
        Collection<String> sumDscr = new ArrayList<>();
        while (it.hasNext()) {
            String key = it.next();
            String keyL = key;
            while (keyL.charAt(keyL.length()-1) <= '9' && keyL.charAt(keyL.length()-1) >= '0')
                keyL = keyL.substring(0, keyL.length()-1);
            sumDscr.add(keyL + ": " + collection.getExtra().get(key));
        }
        return sumDscr;
    }
    
    /**
     * This is way one.  convert the object into our way of dealing with things.
     * @param collection1 survey 1
     * @param collection2 survey 2
     * @return 
     */
    public double compareCollections (CollectionTransferObject collection1,
                                      CollectionTransferObject collection2) {
        
        //required format
        //list of list of attributes
        Collection<Collection<String>> survey1 = new ArrayList<>();
        Collection<Collection<String>> survey2 = new ArrayList<>();
        survey1.add(collection1.getKeywords());
        survey1.add(parseSumDscr(collection1));
        survey2.add(collection2.getKeywords());
        survey2.add(parseSumDscr(collection2));
        return this.computeSim(survey1, survey2);
    }
    
    
    /**
     * This method computes the per-attribute similarity of surveys <code>X</code> and <code>Y</code>
     * @param X first survey in comparison.  Note that this is really just lists of attribute values.
     * @param Y second survey in comparison.  Note same as above.
     * @return the per-attribute similarity, normalized to be between 0 and 1, of survey <code>X</code> and Survey <code>Y</code>
     */
    protected double computeSim(Collection<Collection<String>> X, Collection<Collection<String>> Y) {
        double ans = 0.0;
//        double[][] mat = new double[X.size()][Y.size()];
//        for (int i = 0; i < mat.length; i++) {
//            for (int j = 0; j < mat[i].length; j++) {
//                mat[i][j] = 0;
//            }
//        }
        attrs = new ArrayList<>(); // hashset
        ArrayList<ArrayList<String>> fullAttrs = new ArrayList<ArrayList<String>>();
        HashSet<String> comb;
        ArrayList<String> fullComb; // 
        Iterator<Collection<String>> itX = X.iterator();
        Iterator<Collection<String>> itY = Y.iterator();
        while (itX.hasNext()) {
            Collection<String> valX = itX.next();
            Collection<String> valY = itY.next();
            comb = new HashSet<String>();
            comb.addAll(valX);
            comb.addAll(valY);
            attrs.add(comb);
            fullComb = new ArrayList<String>();
            fullComb.addAll(valX);
            fullComb.addAll(valY);
            fullAttrs.add(fullComb);
        }
        Iterator<HashSet<String>> itA = attrs.iterator();
        Iterator<ArrayList<String>> itF = fullAttrs.iterator();
        itX = X.iterator();
        itY = Y.iterator();
        while (itA.hasNext()) {
            values = itA.next();
            fullValues = itF.next();
            Collection<String> Xk = itX.next();
            Collection<String> Yk = itY.next();
            ans += computeSimK(Xk, Yk);
        }
        return ans;
    }

    /**
     * This computes the similarity between each actual attribute group.
     * @param Xk the list of attribute values
     * @param Yk
     * @return 
     */
    protected double computeSimK(Collection<String> Xk, Collection<String> Yk) {
        double[][] ans = new double[Xk.size()][Yk.size()];
        int i = 0;
        //Iterator<String> it = values.iterator();
        Iterator<String> itX = Xk.iterator();
        while (itX.hasNext()) {
            String xk = itX.next();
            Iterator<String> itY = Yk.iterator();
            int j = 0;
            while (itY.hasNext()) {
                String yk = itY.next();
                ArrayList<String> s = new ArrayList<>();
                s.add(xk);
                s.add(yk);
                //Collection[] arr = new Collection[] {Xk, Yk, s};
                if (xk.equalsIgnoreCase(yk))
                    ans[i][j] = weight(Xk, Yk) * in(s);
                else {
                    ans[i][j] = weight(Xk, Yk) * notIn(s);
                }
                j++;
            }
            i++;
        }
        //double match = mostMatch(Xk.size(), Yk.size());
        //double res = ans / match;
//        if (res > 1) {
//            System.out.println("---------------------------------");
//            System.out.println("Error: Value greater than 1");
//            System.out.println("Value of ans: " + ans);
//            System.out.println("Value of mostMatch: " + match);
//            System.out.println("Lists:");
//            System.out.println("   Xk: " + Xk);
//            System.out.println("   Yk: " + Yk);
//        }
        //Convert into double
        //Store all values into an ArrayList, then sort them.
        ArrayList<Double> tmp = new ArrayList<>();
        for (int k = 0; k < ans.length; k++)
            for (int j = 0; j < ans[k].length; j++)
                tmp.add(ans[k][j]);
        Collections.sort(tmp, Collections.reverseOrder());
        int min = Math.min(Xk.size(), Yk.size());
        double res = 0.0;
        for (int j = 0; j < min; j++) {
            res += tmp.get(j);
        }
        return res/min;
    }
    
    protected abstract double in(Object dummy);
    
    protected abstract double notIn(Object dummy);

    //protected abstract double mostMatch(int xLen, int yLen);

    protected double weight(Collection<String> Xk, Collection<String> Yk) {
        return 1.0 / attrs.size();
    }

}
