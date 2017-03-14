#!/usr/bin/env python
import sys, getopt
import csv
from igraph import *
import louvain

def main(argv):
   inputFile = ''
   outputFile = ''
   imax = 0
   jmax = 0
   theGraph = Graph()
   inputFile = sys.argv[1]
   outputFile = sys.argv[2]
   print 'argv[1] is:', sys.argv[1]
   print 'argv[2] is:', sys.argv[2]

            
   with open(inputFile, 'rb') as csvfile:
      csvReader = csv.reader(csvfile, delimiter=',',quotechar='|')
      # First line is the number of distinct nodes.
      headerRows = csvReader.next()
      nNodes = int(headerRows[0])
      theGraph.add_vertices(nNodes)
      print 'nNodes: ', nNodes

      currentNodeIndex = 0
      # We build a map between the matrix we want to build and the node identifiers
      # as we read in the rows.
      thisI = 0
      thisJ = 0
      thisEdge = 0
      nodeMap = dict()

      # we also want a list that maps the indices to the node names
      indexList = list()
      # add this point, each row is an edge in the graph
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

         # add this edge
         theGraph.add_edges([(thisI,thisJ)])
         theGraph.es[thisEdge]["weight"] = float(row[2])
         thisEdge += 1


   part = louvain.find_partition(theGraph, method = 'Modularity', weight = 'weight')

   with open(outputFile, 'wb') as csvoutfile:
     csvWriter = csv.writer(csvoutfile, delimiter=',',quotechar='|')
     for i in range(0, nNodes):
        csvWriter.writerow([indexList[i], part.membership[i]])


if __name__ == "__main__":
   main(sys.argv[1:])

   
