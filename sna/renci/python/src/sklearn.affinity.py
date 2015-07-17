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
   if (len(sys.argv) < 4):
      # pick a default value.
      thisDamping = .92
   else: 
      # The third argument contains parameters in the format of key1:value1|key2:value2. In this
      # case we are only expecting one: "damping"
      paramList = sys.argv[3].split("|")
      for thisParam in paramList:
         # first and only parameter should be damping
         paramSplit = thisParam.split(":")
         if (paramSplit[0] == "damping"):
             thisDamping = float(paramSplit[1])
   print 'Input file is:', inputFile
   print 'Output file is:', outputFile
   print 'thisDamping is:', str(thisDamping)

            
   with open(inputFile, 'rb') as csvfile:
      csvReader = csv.reader(csvfile, delimiter=',',quotechar='|')
      # First line is the number of distinct nodes.
      headerRows = csvReader.next()
      imax = int(headerRows[0])
      jmax = int(headerRows[0])
      print str(imax) + " "  + str(jmax)

      # define the matrix         
      simMatrix = np.zeros((imax, jmax), dtype=np.float)
      currentNodeIndex = 0
      # We build a map between the matrix we want to build and the node identifiers
      # as we read in the rows.
      thisI = 0
      thisJ = 0
      nodeMap = dict()

      # we also want a list that maps the indices to the node names
      indexList = list()
      for row in csvReader:
         if (row[0] in nodeMap):
            thisI = nodeMap[row[0]]
         else:
            nodeMap[row[0]] = currentNodeIndex
            indexList.append(row[0])
            currentNodeIndex += 1

         if (row[1] in nodeMap):
            thisJ = nodeMap[row[1]]
         else:
            nodeMap[row[1]] = currentNodeIndex
            indexList.append(row[1])
            currentNodeIndex += 1

         # matrix is symetric
         simMatrix[thisI, thisJ] = float(row[2])
         simMatrix[thisJ, thisI] = float(row[2])

      for i in range(0,imax):
         # Set all of the diagonals to 1
         simMatrix[i,i] = 1.

   db = AffinityPropagation(affinity='precomputed',damping=thisDamping)
   labels = db.fit_predict(simMatrix)

   # Number of clusters in labels, ignoring noise if present.
   n_clusters_ = len(set(labels)) - (1 if -1 in labels else 0)

   #print 'Estimated number of clusters: %d' % n_clusters_ 
   print labels, len(labels)

   with open(outputFile, 'wb') as csvoutfile:
      csvWriter = csv.writer(csvoutfile, delimiter=',',quotechar='|')
      for i in range(0, imax):
         csvWriter.writerow([indexList[i], labels[i]])


if __name__ == "__main__":
   main(sys.argv[1:])

   
