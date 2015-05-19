/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.sna.ncat.graph;

/**
 *
 * @author Nerketur
 */
public class MyLink {
    private double weight;
    private int id;
    //private final UndirectedSparseGraph graph;
    private static int edgeCount = 0;
    private double number = 1;

    public MyLink(double weight) {
        //this.graph = graph;
        this.id = edgeCount++; // This is defined in the outer class.
        this.weight = weight;
    }

    public MyLink(double weight, double number) {
        this(weight);
        this.number = number;
    }

    @Override
    public String toString() {
        // Always good for debugging
        return "E" + id;
    }

    /**
     * @return the label wanted.
     */
    public String label() {
        return String.format("%1.5f", getNormalizedWeight());
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(double val) {
        weight = val;
    }

    public double getNum() {
        return number;
    }

    public double getNormalizedWeight() {
        return weight/number;
    }
    
}
