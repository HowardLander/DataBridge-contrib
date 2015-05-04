package org.renci.databridge.contrib.sna.mocksna;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import org.renci.databridge.persistence.metadata.*;
import org.renci.databridge.persistence.network.*;
import org.la4j.*;

/**
 *
 * @author Lander
 */
public class MockSNA implements NetworkProcessor {

    public HashMap<String, String[]> processNetwork (Iterator<NetworkDyadTransferObject> theDyads, 
                                                     String params) {

        HashMap<String, Boolean> nodeList = new HashMap<String, Boolean>();
        org.la4j.matrix.sparse.CRSMatrix theMatrix = new org.la4j.matrix.sparse.CRSMatrix();

        while (theDyads.hasNext()) {
            NetworkDyadTransferObject thisDyad = theDyads.next();

            if (false == nodeList.containsKey(thisDyad.getNode1DataStoreId())) {
                nodeList.put(thisDyad.getNode1DataStoreId(), new Boolean("true"));
            }

            if (false == nodeList.containsKey(thisDyad.getNode2DataStoreId())) {
                nodeList.put(thisDyad.getNode2DataStoreId(), new Boolean("true"));
            }

            if (thisDyad.getNode1DataStoreId() != null && thisDyad.getNode2DataStoreId() != null) {
              theMatrix.set(thisDyad.getI(), thisDyad.getJ(), thisDyad.getSimilarity());
            }
        }    

        System.out.println("\tMatrix: ");
        System.out.println(theMatrix.toString());

        HashMap<String, String[]> returnMap = new HashMap<String, String[]>();
        int clusterCounter =  0;

        Iterator it = nodeList.entrySet().iterator();
        while (it.hasNext()) {
           Map.Entry entry = (Map.Entry) it.next();
           String entries[] = new String[2];
           entries[0] = (String) entry.getKey();
           entry = (Map.Entry) it.next();
           entries[1] = (String) entry.getKey();
           String clusterString = Integer.toString(clusterCounter);
           returnMap.put(clusterString, entries);
           clusterCounter ++; 
        }

        return returnMap;
    }
}
