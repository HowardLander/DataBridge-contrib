/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.sna.ncat.graph;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JButton;

/**
 *
 * @author Nerketur
 */
public class SurveyGraph {
    
    int edgeCount = 0;
    private final UndirectedSparseGraph<MyNode, MyLink> g;
    private final MyNode[] nodes;
    
    public SurveyGraph(String[] verts, double[][] surveyData) {
        this(verts, surveyData, null);
    }
    public SurveyGraph(String[] verts, double[][] surveyData, HashMap<String, Collection<String>> attrs) {
        //We create the graph here
        g = new UndirectedSparseGraph<>();
        nodes = new MyNode[surveyData.length];
        for (int i = 0; i < surveyData.length; i++) {
            if (attrs != null)
                nodes[i] = new MyNode(verts[i], attrs.get(verts[i]));
            else
                nodes[i] = new MyNode(verts[i]);
        }
        for (int i = 0; i < surveyData.length; i++)
            for (int j = i+1; j < surveyData[i].length; j++)
                if (surveyData[i][j] > 0 && i != j)
                    g.addEdge(new MyLink(surveyData[i][j]), nodes[i], nodes[j]);
    }

    public UndirectedSparseGraph<MyNode, MyLink> getGraph() {
        return g;
    }

    public JButton getButton() {
        JButton b = new JButton("Hi");
        b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                g.removeVertex(nodes[0]);
                
            }
        });
        return b;
    }
}
