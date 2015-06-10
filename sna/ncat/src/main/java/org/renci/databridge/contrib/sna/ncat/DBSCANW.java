/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.sna.ncat;

import org.renci.databridge.persistence.network.*;
import weka.clusterers.DBSCAN;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Nerketur
 */
public class DBSCANW implements NetworkProcessor {

    @Override
    public HashMap<String, String[]> processNetwork(Iterator<NetworkDyadTransferObject> theDyads, String params) {
        //We create the similarity matrix here.  Order doesn't really matter.
        SparseMat mat = new SparseMat();
        //We ASSUME that the similarity is directed.  If it is not, use cpmmeted version
        NetworkDyadTransferObject ndto;
        while (theDyads.hasNext()) {
            ndto = theDyads.next();
            String node1 = ndto.getNode1DataStoreId();
            String node2 = ndto.getNode2DataStoreId();
            int i = ndto.getI();
            int j = ndto.getJ();
            double sim = ndto.getSimilarity();
            //singleton nodes are special.
            if (node2 == null)
                // Singleton node, just add 1 to self symmetry
                // We assume no other nodes use it.
                
                //technically adding a single directed path accomplishes the same
                //task, but for ease of understanding, we stay like this.
                //mat.addEleDir(node1, node1, 1.0, i, j);
                mat.addEleUndir(node1, node1, 1.0, i, j);
                    //Technically we should use i for both, simply because it
                    //should be the same for both.  the reason we don't is,
                    //again, for ease of understanding
                    //
                    //Note: if i and j are NOT the same, unexpected results will occur.
            else
                //regular node
                //mat.addEleDir(node1, node2, sim, i, j);
                mat.addEleUndir(node1, node2, sim, i, j);
        }
        double[][] data = mat.toDouble();
        
        HashMap<String, String[]> ret;
        try {
            ret = run(mat.toNodes(), data);
        } catch (Exception ex) {
            ret = new HashMap<>();
            System.err.println(ex.getMessage());
            System.err.println("Unable to create DBSCAN clusterer!");
            System.err.println("Returning empty cluster list...");
        }
        return ret;
    }

    private HashMap<String, String[]> run(ArrayList<String> toNodes, double[][] data) throws Exception {
        HashMap<String, String[]> ret = new HashMap<>();
        
        DBSCAN clusterer = new DBSCAN();
        FastVector attrs = new FastVector();
        for (String node : toNodes) {
            attrs.addElement(new Attribute(node));
        }
        Instances datascheme = new Instances("dataset", attrs, toNodes.size());
        for (double[] row : data) {
            datascheme.add(new Instance(1.0, row));
        }
        clusterer.buildClusterer(datascheme);
        
        //workaround, instances will be returned in the order given.
        HashMap<Integer, ArrayList<String>> tmp = new HashMap<>();
        ArrayList<String> tmp2;
        for (String node : toNodes) {
            int id = clusterer.clusterInstance(null); // will return values in added order
            if (tmp.containsKey(id))
                tmp2 = tmp.get(id);
            else
                tmp2 = new ArrayList<>();
            tmp2.add(node);
            tmp.put(id, tmp2);
        }
        for (int id : tmp.keySet()) {
            ret.put(Integer.toString(id), tmp.get(id).toArray(new String[] {}));
        }
        return ret;
    }
}
