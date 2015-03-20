package org.renci.databridge.contrib.example.messagesend;

import java.io.*;
import java.util.*;
import org.renci.databridge.util.*;
import org.renci.databridge.message.*;

public class InsertDVNMetadata {

   public static void main(String [] args) {

      AMQPComms ac = new AMQPComms (args[0]);
      // className (get from shoffner), nameSpace (your nameSpace) file)
      String headers = InsertMetadataJavaURIMetadataDB.getSendHeaders(args[1], args[2], args[3]);
      AMQPMessage theMessage = new AMQPMessage ();
      ac.publishMessage(theMessage, headers, true);

   }
}
