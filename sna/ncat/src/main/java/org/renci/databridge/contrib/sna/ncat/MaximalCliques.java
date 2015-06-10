/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.sna.ncat;

import org.renci.databridge.persistence.network.*;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.renci.databridge.contrib.sna.ncat.algo.BronKerboschCliqueFinder;
import org.renci.databridge.contrib.sna.ncat.graph.MyLink;
import org.renci.databridge.contrib.sna.ncat.graph.MyNode;
import org.renci.databridge.contrib.sna.ncat.graph.SurveyGraph;

/**
 *
 * @author Nerketur
 */
public class MaximalCliques implements NetworkProcessor {
    private static UndirectedSparseGraph<MyNode, MyLink> graph;
    private static final String[] emp = new String[] {};

    private HashMap<String, String[]> run(ArrayList<String> toNodes, double[][] data) {
// arr,data, createNodeHash(keywords), threshold
//	public ModifiedGraph(String[] verts, double[][] data, HashMap<String, Collection<String>> attrs, double thresh) {
        
        // create a simple graph for the demo
//        SurveyGraph sg = new SurveyGraph(verts, data, attrs);
        SurveyGraph sg = new SurveyGraph(toNodes.toArray(emp), data);
        graph = sg.getGraph();
        
        //transform(graph, thresh); // threshold of .5
        BronKerboschCliqueFinder bkf = new BronKerboschCliqueFinder(graph);
        Collection<Set<MyNode>> nodeList = bkf.getAllMaximalCliques();
        ArrayList<HashSet<MyNode>> clusters = mergeCliques(graph, nodeList);

        HashMap<String, String[]> ret = new HashMap<>();
        //so here we have clusters of nodes, known by the strings.
        for (HashSet<MyNode> cluster : clusters) {
            ArrayList<String> nodes = new ArrayList<>();
            for (MyNode node : cluster) {
                nodes.add(node.label());
            }
            ret.put(Integer.toString(cluster.hashCode()), nodes.toArray(emp));
            
        }
        return ret;
    }

    

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
                //mat.addEleDir(node1, node1, 1.0, i, j);
                mat.addEleUndir(node1, node1, 1.0, i, j);
            else
                //regular node
                //mat.addEleDir(node1, node2, sim, i, j);
                mat.addEleUndir(node1, node2, sim, i, j);
        }
        double[][] data = mat.toDouble();
        
        HashMap<String, String[]> ret = run(mat.toNodes(), data);
        return ret;
    }
    
    private ArrayList<HashSet<MyNode>> mergeCliques(UndirectedSparseGraph<MyNode, MyLink> graph, Collection<Set<MyNode>> data) {
        //Before we merge them, we remove the nodes that are members of more than one clique.
        ArrayList<HashSet<MyNode>> newCliques = new ArrayList<>();
        HashSet[] cliques = data.toArray(new HashSet[0]);
        //1.)  for Ci = C1 to Cm-1 in Cliques
        for (int i = 0; i < cliques.length-1; i++) {
        //2.)     for Cj = Ci+1 to Cm in Cliques
            for (int j = i+1; j < cliques.length; j++) {
        //3.)         if sum(Ci, S(u, v)) >= sum(Cj, S(u, v))
                if (i == j)
                    continue;
                if (similarity(graph, cliques[i]) >= similarity(graph, cliques[j])) {
        //4.)             Cj <- Cj - Ci
                    cliques[j].removeAll(cliques[i]);
        //5.)         else
                } else {
        //6.)             Ci <- Ci - Cj
                    cliques[i].removeAll(cliques[j]);
        //7.)         end if
                }
        //8.)     end for
            }
        //9.)     if |Ci| > 1
            if (cliques[i].size() > 1) {
        //10.)        NewCliques <- Ci
                newCliques.add(cliques[i]);
        //11.)    end if
            }
        //12.) end for
        }
        if (cliques[cliques.length-1].size() > 1)
            newCliques.add(cliques[cliques.length-1]);
        
        return newCliques;
    }
    
    private double similarity(UndirectedSparseGraph<MyNode, MyLink> g, Set<MyNode> Ci) {
        MyNode[] CiArr = Ci.toArray(new MyNode[0]);
        double sim = 0.0;
        for (int i = 0; i < CiArr.length-1; i++) {
            for (int j = i+1; j < CiArr.length; j++) {
                try {
                    sim += g.findEdge(CiArr[i], CiArr[j]).getWeight();
                } catch (Exception e) {
                    System.out.println("Uh-oh.  Couldn't find the edge from " + CiArr[i] + " to " + CiArr[j] + "!");
                }
            }
        }
        //System.out.println("Similarity of the clique is " + sim);
        return sim;
    }
}
