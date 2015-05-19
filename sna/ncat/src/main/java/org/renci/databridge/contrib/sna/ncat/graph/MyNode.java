/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.sna.ncat.graph;

import java.util.Collection;

/**
 *
 * @author Nerketur
 */
public class MyNode {
    private String id;
    //private final SurveyGraph graph;
    private Collection<String> attributes;

    // good coding practice would have this as private
    public MyNode(String id, Collection attrs) {//, final SurveyGraph graph) {
        //this.graph = graph;
        this.id = id;
        this.attributes = attrs;
    }
    public MyNode(String id) {//, final SurveyGraph graph) {
        //this.graph = graph;
        this.id = id;
        this.attributes = null;
    }

    public String toString() {
        // Always a good idea for debuging
        return "V" + id;
        // JUNG2 makes good use of these.
    }
    
    public String label() {
        return id;
    }

    public Collection<String> attrs() {
        return attributes;
    }
    
}
