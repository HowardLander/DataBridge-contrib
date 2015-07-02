#!/usr/bin/env python
import sys, getopt
import csv
import scipy.sparse
from scipy.sparse import csr_matrix
import numpy as np
from sklearn.cluster import AffinityPropagation
from sklearn import metrics
from sklearn.datasets.samples_generator import make_blobs
from sklearn.preprocessing import StandardScaler

def main(argv):
   inputFile = ''
   outputFile = ''
   imax = 0
   jmax = 0
   inputFile = sys.argv[1]
   outputFile = sys.argv[2]
   print 'Input file is "', inputFile
   print 'Output file is "', outputFile

   with open(inputFile, 'rb') as csvfile:
      csvReader = csv.reader(csvfile, delimiter=',',quotechar='|')
      csvReader.next()
      for row in csvReader:
         if int(row[0]) > imax:
            imax = int(row[0])
         if int(row[1]) > jmax:
            jmax = int(row[1])
      print str(imax) + " "  + str(jmax)
            
   # define the matrix         
   simMatrix = np.zeros((imax + 1, jmax + 1), dtype=np.float)
   with open(inputFile, 'rb') as csvfile:
      csvReader = csv.reader(csvfile, delimiter=',',quotechar='|')
      for row in csvReader:
         simMatrix[int(row[0]), int(row[1])] = float(row[2])
         simMatrix[int(row[1]), int(row[0])] = float(row[2])

      for i in range(0,imax):
         # Set all of the diagonals to 1
         simMatrix[i,i] = 1.

   db = AffinityPropagation(affinity='precomputed',damping=.99)
   labels = db.fit_predict(simMatrix)

   # Number of clusters in labels, ignoring noise if present.
   n_clusters_ = len(set(labels)) - (1 if -1 in labels else 0)

   #print 'Estimated number of clusters: %d' % n_clusters_ 
   print labels, len(labels)

   with open(outputFile, 'wb') as csvoutfile:
      csvWriter = csv.writer(csvoutfile, delimiter=',',quotechar='|')
      for i in range(0, imax + 1):
         csvWriter.writerow([i, labels[i]])


if __name__ == "__main__":
   main(sys.argv[1:])

   
