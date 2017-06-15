package org.renci.databridge.contrib.similarity.ncat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.renci.databridge.persistence.metadata.*;



//Note that data here is a simple Collection of the Data size, followed by data elements

//Each data element will have number of attributes, (d), followed by the attribute list

/**
 * An class that allows for easy implementation of various similarity measurements.
 * @author Nerketur
 */
public class Measure implements SimilarityProcessor {
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
//    public Measure(Collection<Collection<Collection<String>>> data) {
//        this.data = data; // original data
//        this.fullData = new ArrayList<>(); // all data from all attributes
//        Iterator<Collection<Collection<String>>> itSur = data.iterator();
//        while (itSur.hasNext()) {
//            Iterator<Collection<String>> itAttr = itSur.next().iterator();
//            while (itAttr.hasNext()) {
//                fullData.addAll(itAttr.next());
//            }
//        }
//    }

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

    /**
     * Grab the data from a user defined field. Remove any trailing numbers from
     * the key. Note the collection will have exactly one item, but this
     * conforms to the previous interface.
     * 
     * @param collection the collection transfer object
     * @param fieldName the fieldName for which to return data
     * @return 
     */
    private Collection<String> parseSumDscr(CollectionTransferObject collection, String fieldName) {
        Collection<String> sumDscr = new ArrayList<>();
        String key = fieldName;
        String keyL = key;
        while (keyL.charAt(keyL.length()-1) <= '9' && keyL.charAt(keyL.length()-1) >= '0') {
            keyL = keyL.substring(0, keyL.length()-1);
        }
        System.out.println("adding metatdata for key: " + key + " " + keyL);
        sumDscr.add(keyL + ": " + collection.getDataByFieldName(fieldName));
        System.out.println("data: " + Arrays.toString(sumDscr.toArray()));
        return sumDscr;
    }
    
    /**
     * This is way one.  convert the object into our way of dealing with things.
     * Way two will be completed later
     * 
     * @param collection the collection transfer object
     * @return 
     */
    private Collection<String> parseSumDscr(CollectionTransferObject collection) {
        //survey1.add(collection1.);
        //sumDscr assumes "key: value" for doing things the same way as the code
        //TODO: make it so we don't have to convert
        Iterator<String> it = collection.getExtra().keySet().iterator();
        Collection<String> sumDscr = new ArrayList<>();
        while (it.hasNext()) {
            String key = it.next();
            String keyL = key;
            while (keyL.charAt(keyL.length()-1) <= '9' && keyL.charAt(keyL.length()-1) >= '0')
                keyL = keyL.substring(0, keyL.length()-1);
            System.out.println("adding metatdata for key: " + key + " " + keyL);
            sumDscr.add(keyL + ": " + collection.getExtra().get(key));
        }
        return sumDscr;
    }
    
    protected void fillData(Collection<Collection<String>> survey1, Collection<Collection<String>> survey2) {
        data = new ArrayList<>();
        data.add(survey1);
        data.add(survey2);
        this.fullData = new ArrayList<>(); // all data from all attributes (could fill as they come in as well)
        Iterator<Collection<Collection<String>>> itSur = data.iterator();
        while (itSur.hasNext()) {
            Iterator<Collection<String>> itAttr = itSur.next().iterator();
            while (itAttr.hasNext()) {
                fullData.addAll(itAttr.next());
            }
        }
    }
    
    /**
     * @param collection1 survey 1
     * @param collection2 survey 2
     * @param params in this case, used to specify which metadata fields to compare.
     * @return 
     */
    @Override
    public double compareCollections (CollectionTransferObject collection1,
                                      CollectionTransferObject collection2,
                                      String params) {
        
        //required format
        //list of list of attributes
        Collection<Collection<String>> survey1 = new ArrayList<>();
        Collection<Collection<String>> survey2 = new ArrayList<>();
        System.out.println("params are: " + params);

        String [] fieldArray = params.split("\\|");
        for (String thisField : fieldArray){
           System.out.println("thisField is: " + thisField);
           survey1.add(parseSumDscr(collection1, thisField));
           survey2.add(parseSumDscr(collection2, thisField));
        }
        // Following original code replaced by loop above 
        // survey1.add(collection1.getKeywords());
        // survey1.add(parseSumDscr(collection1));
        // survey2.add(collection2.getKeywords());
        // survey2.add(parseSumDscr(collection2));
        fillData(survey1, survey2); // This is not needed for all measures, but IS needed for the more complex ones.
        //TODO: only have it fill data if needed by the measurement.
        // bit of a complicated task, 

        double val = this.computeSim(survey1, survey2);
        if (Double.isNaN(val)) {
          val = 0;
        }
        return val;

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
        ArrayList<ArrayList<String>> fullAttrs = new ArrayList<>();
        HashSet<String> comb;
        ArrayList<String> fullComb; // 
        Iterator<Collection<String>> itX = X.iterator();
        Iterator<Collection<String>> itY = Y.iterator();
        while (itX.hasNext()) {
            Collection<String> valX = itX.next();
            Collection<String> valY = itY.next();
            comb = new HashSet<>();
            comb.addAll(valX);
            comb.addAll(valY);
            attrs.add(comb);
            fullComb = new ArrayList<>();
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
            ans += computeSimK(Xk, Yk) * weight(Xk, Yk);
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
                ans[i][j] = computeWordSim(xk, yk, s);
                //assert(ans[i][j] <= 1);
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
    
    protected double in(Object dummy) {
        throw new UnsupportedOperationException("Please override this method with your own implementation");
    }
    
    protected double notIn(Object dummy) {
        throw new UnsupportedOperationException("Please override this method with your own implementation");
    }

    //protected abstract double mostMatch(int xLen, int yLen);

    /**
     * This is used to find the weight for each group of attributes.  by default it returns 1/(totAttributes)
     * This value is important because we add them together to get the final result, which we expect to be
     * between 0 and 1.  Override this if the weight is incorrect for a particular algorithm.
     * @param Xk attribute list for survey 1
     * @param Yk attribute list for survey 2
     * @return the weight for each attribute list.  In this default case, returns 1/(number of attribute lists)
     */
    protected double weight(Collection<String> Xk, Collection<String> Yk) {
        return 1.0 / attrs.size();
    }

    protected double computeWordSim(String xk, String yk, ArrayList<String> s) {
        if (xk.equalsIgnoreCase(yk))
            return in(s);
        else
            return notIn(s);
    }
}
