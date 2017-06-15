package org.renci.databridge.contrib.similarity.renci;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.renci.databridge.persistence.metadata.*;

/**
 * Basic class for Renci similarity classes. Once we have all of the metadata we call a function
 * to do the actual evaluation.  That function is defined in a class that implements this class.
 */
public abstract class RenciSimMeasure implements SimilarityProcessor {

    /**
     * @param collection1 First CollectionTransferObject
     * @param collection2 Second CollectionTransferObject
     * @param params in this case, used to specify which metadata fields from the two collections to compare.
     * @return the calculated similarity value
     */
    public double compareCollections (CollectionTransferObject collection1,
                                      CollectionTransferObject collection2,
                                      String params) {

        // Get all of the requested metadata fields
        String [] fieldArray = params.split("\\|");
        StringBuilder builder1 = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();

        for (String thisField : fieldArray){
 //          System.out.println("thisField is: " + thisField);
           builder1.append(collection1.getDataByFieldName(thisField));
           builder1.append(" ");
           builder2.append(collection2.getDataByFieldName(thisField));
           builder2.append(" ");
        }

//      System.out.println("metadata1: " + builder1.toString());
//      System.out.println("metadata2: " + builder2.toString());

        // now call the compute method with all of the proper data
        return (computeSimilarity(builder1.toString(), builder2.toString()));
    }

    /**
     * @param metaData1 the selected metadata from the first transfer object
     * @param metaData2 the selected metadata from the second transfer object
     * @return the calculated similarity value
     */
   abstract double computeSimilarity(String metaData1, String metaData2);
}
