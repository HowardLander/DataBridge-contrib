/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.dataharvester;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ORG.oclc.oai.harvester2.app.RawWrite;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author asone
 */
public class DataHarvester {

    static final Logger logger = Logger.getLogger(DataHarvester.class.getName());

    public static void main(String[] args) {

        try {
            logger.info("check arguments");
            for (int i = 0; i < args.length; i++) {
                System.out.println("argument " + i + ": " + args[i]);
            }

            if (args.length < 2) {
                logger.severe("two arguments are expected: <SetSpec> and <DDI filename>");
                System.err.println("Usage: java -jar DataHarvester <SetSpec> and <DDI filename>");
                // throw new RuntimeException("Usage: java -jar DataHarvester <SetSpec> and <DDI filename>");
                System.exit(1);
            }

            HashMap options = getOptions(args);
            List rootArgs = (List) options.get("rootArgs");
            logger.log(Level.INFO, "rootArts:size={0}", rootArgs.size());
            for (int i = 0; i < rootArgs.size(); i++) {
                System.out.println(rootArgs.get(i));
            }

            for (Object key : options.keySet()) {
                System.out.println(key + " :: " + options.get(key));
            }

            String baseURL = null;
            if (rootArgs.size() > 0) {
                baseURL = (String) rootArgs.get(0);
                logger.log(Level.INFO, "baseURL={0}", baseURL);
            } else {
                throw new IllegalArgumentException();
            }

            OutputStream out = System.out;
            
            String outFileName = (String) options.get("-out");
            logger.log(Level.INFO, "outFileName={0}", outFileName);
            
            String from = (String) options.get("-from");
            logger.log(Level.INFO, "from={0}", from);
            String until = (String) options.get("-until");
            logger.log(Level.INFO, "until={0}", until);
            String metadataPrefix = (String) options.get("-metadataPrefix");

            if (metadataPrefix == null) {
                metadataPrefix = "oai_dc";
                logger.log(Level.INFO, "metadataPrefix is null: set to the default:{0}",
                        metadataPrefix);
            } else {
                logger.log(Level.INFO, "metadataPrefix={0}", metadataPrefix);
            }
            String resumptionToken = (String) options.get("-resumptionToken");
            logger.log(Level.INFO, "resumptionToken={0}", resumptionToken);

            String setSpec = (String) options.get("-setSpec");
            logger.log(Level.INFO, "setSpec={0}", setSpec);

            if (resumptionToken != null) {
                logger.log(Level.INFO, "resumptionToken is not null");
                if (outFileName != null) {
                    out = new FileOutputStream(outFileName, true);
                    logger.log(Level.INFO, "outFileName={0}", outFileName);
                } else {
                    logger.log(Level.INFO, "outFileName is null");
                }
                RawWrite.run(baseURL, resumptionToken, out);
            } else {
                logger.log(Level.INFO, "resumptionToken is  null");
                if (outFileName != null) {
                    logger.log(Level.INFO, "outFileName={0}", outFileName);
                    out = new FileOutputStream(outFileName);
                } else {
                    logger.log(Level.INFO, "outFileName is null");
                }
                RawWrite.run(baseURL, from, until, metadataPrefix, setSpec, out);
            }

            if (out != System.out) {
                out.close();
            }

        } catch (IllegalArgumentException ex) {
            logger.log(Level.SEVERE, "IllegalArgumentException", ex);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "FileNotFoundException", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException", ex);
        } catch (ParserConfigurationException ex) {
            logger.log(Level.SEVERE, "ParserConfigurationException", ex);
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, "SAXException", ex);
        } catch (TransformerException ex) {
            logger.log(Level.SEVERE, "TransformerException", ex);
        } catch (NoSuchFieldException ex) {
            logger.log(Level.SEVERE, "NoSuchFieldException", ex);
        }
        logger.log(Level.INFO, "existing DataHarvester");
    }

    private static HashMap getOptions(String[] args) {
        HashMap options = new HashMap();
        ArrayList rootArgs = new ArrayList();
        options.put("rootArgs", rootArgs);

        for (int i = 0; i < args.length; ++i) {
            if (args[i].charAt(0) != '-') {
                rootArgs.add(args[i]);
            } else if (i + 1 < args.length) {
                options.put(args[i], args[++i]);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return options;
    }

}
