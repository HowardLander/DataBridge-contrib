package org.renci.databridge.contrib.example.messagesend;

import java.io.*;
import java.util.*;
import org.renci.databridge.util.*;
import org.renci.databridge.message.*;

public class MessageSend {

   /***************************************************************************
    * This is an example class to show how to send messages in the DataBridge
    * system.  This file is going to send the Create.JSON.File.NetworkDB.URI
    * message. For that we need some arguments.  Since this is just an example
    * we are going to use positional parameters.  What we need are
    *     propertyFile containing the queue information
    *     nameSpace
    *     similarityId
    *     snaId
    *     outputFile
    *
    *
    **************************************************************************/
   public static void main(String [] args) {

      AMQPComms ac = new AMQPComms (args[0]);
      String headers = CreateJSONFileNetworkDBURI.getSendHeaders(args[1], args[2], args[3], args[4]);
      System.out.println("headers are: " + headers);
      AMQPMessage theMessage = new AMQPMessage ();
      ac.publishMessage(theMessage, headers, true);

   }
}
