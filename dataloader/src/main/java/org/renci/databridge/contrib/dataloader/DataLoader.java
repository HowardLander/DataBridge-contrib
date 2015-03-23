package org.renci.databridge.contrib.dataloader;

import java.io.*;
import java.util.*;
import org.renci.databridge.util.AMQPComms;
import org.renci.databridge.util.AMQPMessage;
import org.renci.databridge.message.InsertMetadataJavaURIMetadataDB;

/**
 * Data loader that sends a message to Databridge Ingest Engine to load a document into a namespace from a URL using a formatter.
 * 
 */
public class DataLoader {

  public static void main (String [] args) {

    for (int i = 0; i < args.length; i ++) {
      System.out.println ("arg " + i + ": " + args [i]);
    }

    if (args.length != 4) {
      throw new RuntimeException ("Usage: DataLoader <abs_path_to_AMQPComms_props_file, <FQCN_of_formatter_impl> <name_space>, <inputURI>");
    }

    AMQPComms ac = new AMQPComms (args [0]);
    String className = args [1];
    String nameSpace = args [2];
    String inputURI = args [3];

    String headers = InsertMetadataJavaURIMetadataDB.getSendHeaders (className, nameSpace, inputURI);
    System.out.println ("headers are: " + headers);
    AMQPMessage theMessage = new AMQPMessage ();
    ac.publishMessage (theMessage, headers, true);
    ac.shutdownConnection ();

  }
}
