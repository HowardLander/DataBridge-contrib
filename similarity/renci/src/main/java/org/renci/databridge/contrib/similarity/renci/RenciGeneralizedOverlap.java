package org.renci.databridge.contrib.similarity.renci;
import org.renci.databridge.contrib.similarity.util.*;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.lang.StringBuilder;

import static com.google.common.base.Predicates.in;
import static org.simmetrics.builders.StringMetricBuilder.with;


import org.simmetrics.StringMetric;
import org.simmetrics.StringDistance;
import org.simmetrics.metrics.GeneralizedOverlapCoefficient;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;


/**
 * Implements the Levenshtein similarity relying on the simmetrics library (https://github.com/Simmetrics)
 */
public class RenciGeneralizedOverlap extends RenciSimMeasure {

    /**
     * @param metaData1 the selected metadata from the first transfer object
     * @param metaData2 the selected metadata from the second transfer object
     * @return the calculated similarity value
     */
   double computeSimilarity(String metaData1, String metaData2) {

      //Set<String> stopWordsSet = SimilarityUtils.getStopWords();
      // Set up for the calculation
             //   .filter(Predicates.not(in(commonWords)))
      StringMetric metric = with(new GeneralizedOverlapCoefficient<String>())
                .simplify(Simplifiers.toLowerCase())
                .tokenize(Tokenizers.whitespace())
                .build();

      return metric.compare(metaData1, metaData2);
   }
}
