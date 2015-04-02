/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.sna.ncat;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Nerketur
 */
public class Cluster {
    //Cluster has centroid and data in cluster
    public ArrayList<Double> centroid = new ArrayList<>();
    public ArrayList<Double[]> data = new ArrayList<>();
    private ArrayList<String> nodes = new ArrayList<>();

    public Cluster(double[] get, double[][] data, ArrayList<String> nodes) {
        for (double ele : get) {
            centroid.add(ele);
        }
        for (double[] row : data) {
            ArrayList<Double> dat = new ArrayList<>();
            for (double col : row) {
                dat.add(col);
            }
            this.data.add(dat.toArray(new Double[0]));
        }
    }

    public Cluster(ArrayList<Double> cent, ArrayList<Double[]> data, ArrayList<String> nodes) {
        centroid = cent;
        this.data = data;
        this.nodes = nodes;
    }
    
    public Cluster(double[] get) {
        for (double ele : get)
            centroid.add(ele);
    }
    public Cluster(ArrayList<Double> cent) {
        centroid = cent;
    }

    void add(double[] x, String node) {
        Double[] tmp = new Double[x.length];
        for (int i = 0; i < x.length; i++) {
            tmp[i] = x[i];
        }
        data.add(tmp);  //Add data
        nodes.add(node);//Add label of data
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Cluster) {
            Cluster c = (Cluster) o;
            for (int i = 0; i < c.centroid.size(); i++) {
                if (!this.centroid.get(i).equals(c.centroid.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        //since we say Clusters are equal if the centroids are equal:
        int hash = 7;
        for (Double val : this.centroid) {
            hash = 47 * hash + Objects.hashCode(val);
        }
        return hash;
    }

    String[] nodes() {
        return this.nodes.toArray(new String[0]);
    }
    
}
