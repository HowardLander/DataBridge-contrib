/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.sna.ncat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Mainly created to make it far easier to manage Arrays from Graphs.
 * @author Nerketur
 */
public class SparseMat {

    HashMap<String, HashMap<String, Double>> graph;
    HashSet<String> nodes;
    private final HashMap<Integer, String> idx2node;
    //private final HashMap<String, Integer> node2idx;

    public SparseMat() {
        this.graph = new HashMap<>();
        this.idx2node = new HashMap<>();
        //this.node2idx = new HashMap<>();
        this.nodes = new HashSet<>();
    }
    
    void addEleUndir(String node1, String node2, double val) {
        //Undirected is directed both ways
        addEleDir(node1, node2, val, -1, -1);
        addEleDir(node2, node1, val, -1, -1);
    }
    void addEleDir(String node1, String node2, double val) {
        addEleDir(node1, node2, val, -1, -1);
    }

    void addEleDir(String node1, String node2, double val, int i, int j) {
        nodes.add(node1);
        nodes.add(node2);
        if (i >= 0 || j >= 0) {
            idx2node.put(i, node1);
            idx2node.put(j, node2);
            //node2idx.put(node1, i);
            //node2idx.put(node2, j);
        }
        HashMap<String, Double> hm;
        if (!graph.containsKey(node1))
            hm = new HashMap<>();
        else
            hm = graph.get(node1);
        hm.put(node2, val);
        graph.put(node1, hm);
    }
    
    void print() {
        //needs to be square, only includes available nodes
        //Prints in table format
        ArrayList<String> ordNodes = toNodes();
        for (String node : ordNodes) {
            System.out.print("\t" + node);
        }
        System.out.println();
        for (String nodeR : ordNodes)
            print(nodeR);
        System.out.println();
    }

    void print(String nodeR) {
        System.out.print(nodeR);
        for (String nodeC : toNodes())
            System.out.print("\t" + getVal(nodeR, nodeC));
        System.out.println();
    }

    HashSet<String> getNodes() {
        return nodes; // All nodes
    }

    HashSet<String> getNodes(String get) {
        HashSet<String> ret = new HashSet<>();
        if (graph.containsKey(get))
            ret.addAll(graph.get(get).keySet());
        return ret; // Only nodes in the requested row.
    }

    double[][] toDouble() {
        ArrayList<String> ordNodes = toNodes(); // must be sorted to ensure the same matrix every time
        double[][] ret = new double[nodes.size()][nodes.size()];
        for (int row = 0; row < ret.length; row++)
            for (int col = 0; col < ret.length; col++)
                ret[row][col] = getVal(ordNodes.get(row), ordNodes.get(col));
        return ret; // full double[][] matrix
    }
    ArrayList<String> toNodes() {
        //MUST be ordered, so that this will match toDouble()
        ArrayList<String> ordNodes = new ArrayList<>();
        ArrayList<String> extraNodes = new ArrayList<>();
        for (int i = 0; i > nodes.size(); i++)
            if (idx2node.containsKey(i))
                ordNodes.add(idx2node.get(i));
        extraNodes.addAll(nodes);
        extraNodes.removeAll(ordNodes); //ensure we have all known nodes
        Collections.sort(extraNodes);   // sort the non-indexed ones
        ordNodes.addAll(extraNodes);    // add to the end of the indexed ones.
        return ordNodes;
    }

    private double getVal(String nodeR, String nodeC) {
        double res = 0.0;
        if (graph.containsKey(nodeR))
            if (graph.get(nodeR).containsKey(nodeC))
                res = graph.get(nodeR).get(nodeC);
        return res;
    }
}
