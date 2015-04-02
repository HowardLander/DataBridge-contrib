/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.sna.ncat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.renci.databridge.persistence.network.*;
import org.renci.databridge.persistence.metadata.*;

/**
 *
 * @author Nerketur
 */
public class KMeans implements NetworkProcessor {

    /**
     * Run the k-means standard algorithm using specified data and number of clusters.
     * 
     * Note that the initialization uses the Forgy method, which selects the initial centroids from the data set randomly.
     * This implies that the method may give different results each run.
     * 
     * data supplied is expected to be double[][], and a similarity matrix.
     * @param data the data set (usually a similarity matrix) to use. All rows should have the same number of columns!
     * @param clusters the number of clusters to use for the algorithm.
     * @param labels an ArrayList of the labels for each row
     * @return a HashSet of the clusters.
     */
    private static HashSet<Cluster> kMeans(ArrayList<String> labels, double[][] data, int clusters) {
        if (clusters > data.length) {
            clusters = data.length; // more is pointless
            System.err.println("WARNING: specified cluster number is larger than amount of data, using data size instead.");
        }
        //We will use the forgy method of initialization
        KMeans list = new KMeans(data, labels);
        list.makeClusters(clusters);
        //use the standard algorithm:
        return list.runAlg();
    }
    static HashSet<Cluster> kMeans(double[][] data, int clusters) {
        ArrayList<String> labs = new ArrayList<>();
        for (int i = 0; i < data.length; i++)
            labs.add(Integer.toString(i));
        return kMeans(labs, data, clusters);
    }
    
    // centroids
    private double[][] data;
    private HashSet<Cluster> clusters;
    private ArrayList<String> labels;
    
    
    public KMeans() {
        clusters = new HashSet<>();
        labels = new ArrayList<>();
    }
    /**
     * Initializer for the data set.  Only used internally
     * @param data the similarity matrix to use
     */
    private KMeans(double[][] data, ArrayList<String> labels) {
        this.data = data;
        this.labels = labels;
        clusters = new HashSet<>();
    }

    /**
     * Only used internally.  Uses the Forgy method of initializing the clusters.
     * @param numClust number of clusters to create
     * @return new clusters with the centroids equal to different, (randomized) datapoints.
     */
    private HashSet<Cluster> makeClusters(int numClust) {
        List<double[]> list = Arrays.asList(data);
        Collections.shuffle(list);
        for (int i = 0; i < numClust; i++) {
            //newMap.put(newCent, new ArrayList<double[]>());
            clusters.add(new Cluster(list.get(i))); // so we don't change the original data ints
        }
        return clusters;
    }

    /**
     * Only used internally.
     * 
     * Runs the k-means standard algorithm
     * @return the clusters in a HashSet
     */
    private HashSet<Cluster> runAlg() {
        // Assignment step: Assign each observation to the cluster whose mean yields the least within-cluster sum of squares (WCSS)
        //    Basically, nearest euclidian distance
        ArrayList<Cluster> oldClusters = new ArrayList<>();
        ArrayList<Cluster> clusterArr = new ArrayList<>();
        clusterArr.addAll(clusters);
        ArrayList<Cluster> retArr = clusterArr;
        
        
        //HashMap<double[], ArrayList<double[]>> oldClust;
        while (!clusterArr.equals(oldClusters)) {
            oldClusters = clusterArr;
            for (int i = 0; i < data.length; i++) {
                Cluster min = null;
                double mean = Double.MAX_VALUE;
                for (Cluster clust : clusterArr) {
                    //find the closest mean
                    double tmp = eucDist(clust.centroid.toArray(new Double[0]), data[i]);
                    if (tmp <= mean) {
                        mean = tmp;
                        min = clust;
                    }
                }
                // assign
                min.add(data[i], labels.get(i));
            }
            
            // Update step: Calculate the new means to be the centroids of the observations in the new clusters. 
            int numvecs;
            ArrayList<Double> newCent;
            ArrayList<Double[]> datList;
            //For every current entry, we remove it, and add a new one.  So beter to create a new dict.
            //HashMap<double[], ArrayList<double[]>> newMap = new HashMap<>();
            ArrayList<Cluster> newArr = new ArrayList<>();
            
            for (Cluster cluster : clusterArr) {
                datList = cluster.data;
                numvecs = datList.size();
                Double[] tmp = new Double[datList.get(0).length];
                newCent = new ArrayList<>(tmp.length);
                for (int i = 0; i < tmp.length; i++)
                    tmp[i] = 0.0;
                for (Double[] dat : datList) {
                    for (int i = 0; i < dat.length; i++)
                        tmp[i] += dat[i]/numvecs; // will introduce roundoff error, but should be minimal
                }
                newCent.addAll(Arrays.asList(tmp));
                newArr.add(new Cluster(newCent));
                retArr = clusterArr;
            }
            clusterArr = newArr;
        }
        //return (ArrayList<ArrayList<double[]>>)oldClust.values();
        clusters.clear();
        clusters.addAll(retArr);
        return clusters;
    }

    /**
     * Gets the euclidian distance between a pair of points clust and d in n-dimensional space
     * @param clust the first point (centroid)
     * @param d the second point (data element)
     * @return the euclidian distance between clust and d
     */
    private double eucDist(Double[] clust, double[] d) {
        if (clust.length != d.length)
            return Double.MAX_VALUE;
        double tmp = 0, tmp1;
        for (int i = 0; i < clust.length; i++) {
            tmp1 = clust[i] - d[i];
            tmp += tmp1*tmp1;
        }
        assert(tmp >= 0);
        return Math.sqrt(tmp);
    }

    @Override
    public HashMap<String, String[]> processNetwork(Iterator<NetworkDyadTransferObject> theDyads, String params) {
        //We create the similarity matrix here.  Order doesn't really matter.
        SparseMat mat = new SparseMat();
        //We ASSUME that the similarity is undirected.  If this is not true, use the commented version instead.
        NetworkDyadTransferObject ndto;
        while (theDyads.hasNext()) {
            ndto = theDyads.next();
            String node1 = ndto.getNode1DataStoreId();
            String node2 = ndto.getNode2DataStoreId();
            double sim = ndto.getSimilarity();
            //singleton nodes are special.
            if (node2 == null)
                // Singleton node, just add 1 to self symmetry
                // We assume no other nodes use it.
                mat.addEleDir(node1, node1, 1);
            else
                //regular node
                mat.addEleDir(node1, node2, sim);
        }
        double[][] data = mat.toDouble();
        
        //Currently have to change here to get different numbers of clusters
        HashSet<Cluster> kMeans = KMeans.kMeans(mat.toNodes(), data, 3);
        
        HashMap<String, String[]> ret = new HashMap<>();
        int index = 1;
        for (Cluster c : kMeans) {
            // Create node list
            String[] nodeList = c.nodes();
            //ret.put(Integer.toString(c.hashCode()), nodeList);
            System.out.println("index: " + index);
            ret.put(Integer.toString(index), nodeList);
            index++;
        }
        return ret;
    }
    
}
