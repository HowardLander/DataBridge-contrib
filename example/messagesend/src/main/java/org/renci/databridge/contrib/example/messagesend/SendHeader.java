package org.renci.databridge.contrib.example.messagesend;

import java.io.*;
import java.util.*;
import org.renci.databridge.util.*;
import org.renci.databridge.message.*;

public class SendHeader {

   /***************************************************************************
    * This is an example class to show how to send messages in the DataBridge
    * system.  This file is going to send the Create.JSON.File.NetworkDB.URI
    * message. For that we need some arguments.  Since this is just an example
    * we are going to use positional parameters.  What we need are
    *     propertyFile containing the queue information
    *     header we want to send
    *
    *
    **************************************************************************/
   public static void main(String [] args) {

      System.out.println("Initializing with isConsumer set to false");
      AMQPComms ac = new AMQPComms (args[0], false);
      String headers = args[1];
      System.out.println("headers are: " + headers);
      AMQPMessage theMessage = new AMQPMessage ();
      System.out.println("about to publish");
      ac.publishMessage(theMessage, headers, true);
      System.out.println("after the publish");
      ac.shutdownConnection();

   }
}
