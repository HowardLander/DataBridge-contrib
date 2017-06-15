package org.renci.databridge.contrib.similarity.renci;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.renci.databridge.persistence.metadata.*;
import org.la4j.*;
import org.la4j.matrix.*;
import org.renci.databridge.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Class the compares two similarity matrices. First we subtract the matrices, then we perform a 
 * Eculidean norm on the resultant difference matrix
 */
public class RenciNetworkFile implements SimilarityProcessor {

    private Logger logger = Logger.getLogger ("org.renci.databridge.contrib.similarity.renci");
    /**
     * @param collection1 First CollectionTransferObject
     * @param collection2 Second CollectionTransferObject
     * @param params in this case, used to specify which metadata fields from the two collections to compare.
     * @return the calculated similarity value
     */
    public double compareCollections (CollectionTransferObject collection1,
                                      CollectionTransferObject collection2,
                                      String params) {
        double similarity = 0.;
        
        try {
            SimilarityFile readData1 = new SimilarityFile();
            SimilarityFile readData2 = new SimilarityFile();
            readData1.readFromDisk(collection1.getURL());
            readData2.readFromDisk(collection2.getURL());
            org.la4j.matrix.sparse.CRSMatrix matrix1 = readData1.getSimilarityMatrix();
            org.la4j.matrix.sparse.CRSMatrix matrix2 = readData2.getSimilarityMatrix();
            org.la4j.Matrix differenceMatrix = matrix1.subtract(matrix2);
            similarity = differenceMatrix.euclideanNorm();
        } catch (Exception e) {
            this.logger.log (Level.SEVERE, "Can't read data from file", e);
        }
        return (similarity);
    }

}
