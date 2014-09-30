package org.renci.databridgecontrib.similarity.mocksimilarity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import org.renci.databridge.util.*;
import org.renci.databridge.persistence.metadata.*;
import com.rabbitmq.client.*;

/**
 *
 * @author Lander
 */
public class MockSimilarity implements RelevanceInterface {

    public double compareCollections (CollectionTransferObject collection1,
                                      CollectionTransferObject collection2) {
        return .75;
    }
}
