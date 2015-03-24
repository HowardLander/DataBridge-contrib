/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.databridgeoaiharvestdataloader;
import java.util.logging.Logger;
import org.renci.databridge.contrib.dataharvester.DataHarvester;
import org.renci.databridge.contrib.dataloader.DataLoader;

/**
 *
 * @author Jonc and Akio
 */

public class OAIHarvestLoader {
    
    static final Logger logger = Logger.getLogger(OAIHarvestLoader.class.getName());
    
    public static void main (String [] args) {

    for (int i = 0; i < args.length; i ++) {
      System.out.println ("arg " + i + ": " + args [i]);
    }

    if (args.length != 2) {
      throw new RuntimeException ("Usage: OAIHarvestLoader <SetSpec> and <outFileName>");
    }
    
    
    String metadataPrefix = args [0];
    String metadataformat = args [1];
    String SetSpec = args [2];
    String setname = args [3];
    String outprefix = args [4];
    String outFileName= args [5];
    String BaseURL = args [6];
    
    // usage sample argument [0] -metadataPrefix [1] ddi [2] -setSpec [3] Harris [4] -out [5] harrismetadata [6] http://arc.irss.unc.edu/dvn/OAIHandler
    
        DataHarvester.main(args);
        
        
    // Read harvested records from file system and send message to databridge via dataloader
        
        String[]newargs= new String[4];
        newargs[0]= "/Users/jonc/NetBeansProjects/dataharvesterworkingdir/DataBridge.conf";
        newargs[1]="org.renci.databridge.contrib.formatter.oaipmh.OaipmhMetadataFormatterImpl";
        newargs[2]="dvntestnamespace";
        newargs[3]="/Users/jonc/NetBeansProjects/dataharvesterworkingdir/"+outFileName;
        
        
      //  sample arguments [0]????ask Mike path to AMQPComms_props_file "/Users/jonc/NetBeansProjects/dataharvesterworkingdir/DataBridge.conf" [1] "org.renci.databridge.contrib.formatter.oaipmh.OaipmhMetadataFormatterImpl", [2]"dvntestnamespace", [3] "/Users/jonc/NetBeansProjects/dataharvesterworkingdir/"+outFileName
    
        DataLoader.main(newargs); 
        
        // would like to add some logging and progress notification of some type here
    }  
}
    
    
