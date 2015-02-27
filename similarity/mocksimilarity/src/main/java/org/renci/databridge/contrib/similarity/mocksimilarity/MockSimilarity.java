package org.renci.databridge.contrib.similarity.mocksimilarity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import org.renci.databridge.persistence.metadata.*;
import com.rabbitmq.client.*;

/**
 *
 * @author Lander
 */
public class MockSimilarity implements SimilarityProcessor {

    public double compareCollections (CollectionTransferObject collection1,
                                      CollectionTransferObject collection2) {
        
        int version1 = collection1.getVersion();
        int version2 = collection2.getVersion();
        return (version1 + version2)/10.;
    }
}
