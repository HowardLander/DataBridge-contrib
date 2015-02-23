package org.renci.databridge.contrib.example.messagesend;

import java.io.*;
import java.util.*;
import org.renci.databridge.util.AMQPComms;
import org.renci.databridge.util.AMQPMessage;
import org.renci.databridge.message.InsertMetadataJavaURIMetadataDB;

/**
 * Data loader that loads Codebook documents from a URL.
 * 
 */
public class CodebookDataLoader {

  public static void main(String [] args) {

    if (args.length != 2) {
      throw new RuntimeException ("Usage: IngestEngine <abs_path_to_AMQPComms_props_filei, <inputURI>");
    }

    AMQPComms ac = new AMQPComms (args [0]);

    // "org.renci.databridge.contrib.formatter.oaipmh.OaipmhMetadataFormatterImpl";
    String className = "org.renci.databridge.contrib.formatter.codebook.CodeBookMetadataFormatterImpl";
    String methodName = ""; // not sent because not needed
    String nameSpace = "test_ingest";
    String inputURI = args [1];

    String headers = InsertMetadataJavaURIMetadataDB.getSendHeaders (className, methodName, nameSpace, inputURI);
    System.out.println ("headers are: " + headers);
    AMQPMessage theMessage = new AMQPMessage ();
    ac.publishMessage (theMessage, headers, true);

  }
}
