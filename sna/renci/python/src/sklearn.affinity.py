#!/usr/bin/env python
import sys, getopt
import csv
import json
import scipy.sparse
from scipy.sparse import csr_matrix
import numpy as np
from sklearn.cluster import AffinityPropagation
from sklearn import metrics
from sklearn.datasets.samples_generator import make_blobs
from sklearn.preprocessing import StandardScaler

# maintain two versions of python codes
def main(argv):
   if (sys.version_info > (3, 0)):
      # Python 3 code in this block
      inputFile = 'D:/F/UnivSub/UNC/internship/RENCI/DataBridge/cluster_0301/data/test/DataBridge-input-small.csv'
      outputFile = 'D:/F/UnivSub/UNC/internship/RENCI/DataBridge/cluster_0301/data/test/DataBridge-output-small_0329.csv'
      outputFile_network = 'D:/F/UnivSub/UNC/internship/RENCI/DataBridge/cluster_0301/data/test/DataBridge-network-small.json'
      imax = 0
      jmax = 0
      # inputFile = sys.argv[1]
      # outputFile = sys.argv[2]
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
      print('Input file is:', inputFile)
      print('Output file is:', outputFile)
      print('thisDamping is:', str(thisDamping))

      networkdict = dict()
      networkdict['nodes'] = list()
      networkdict['links'] = list()

      with open(inputFile, newline='') as csvfile:
         csvReader = csv.reader(csvfile, delimiter=',', quotechar='|')
         # First line is the number of distinct nodes.
         headerRows = next(csvReader)
         imax = int(headerRows[0])
         jmax = int(headerRows[0])
         print(str(imax) + " " + str(jmax))

         # define the matrix
         simMatrix = np.zeros((imax, jmax), dtype=np.float)
         currentNodeIndex = 0
         # We build a map between the matrix we want to build and the node identifiers
         # as we read in the rows.
		 # and the clusters assigned in all the levels
         thisI = 0
         thisJ = 0
         nodeMap = dict()

         # we also want a list that maps the indices to the node names
         indexList = list()
         for row in csvReader:
            if (row[0] in nodeMap):
               pass
            else:
               nodeMaplist = list()
               nodeMaplist.append(currentNodeIndex)
               nodeMap[row[0]] = nodeMaplist
               indexList.append(row[0])
               currentNodeIndex += 1
            thisI = nodeMap[row[0]][0]

            if (row[1] in nodeMap):
               pass
            else:
               nodeMaplist = list()
               nodeMaplist.append(currentNodeIndex)
               nodeMap[row[1]] = nodeMaplist
               indexList.append(row[1])
               currentNodeIndex += 1
            thisJ = nodeMap[row[1]][0]

            # matrix is symetric
            simMatrix[thisI, thisJ] = float(row[2])
            simMatrix[thisJ, thisI] = float(row[2])

            linkdict = dict()
            linkdict['source'] = thisI
            linkdict['target'] = thisJ
            linkdict['value'] = float(row[2])
            networkdict['links'].append(linkdict)

         for i in range(0, imax):
            # Set all of the diagonals to 1
            simMatrix[i, i] = 1.

	  # do the clustering
      db = AffinityPropagation(affinity='precomputed', damping=thisDamping)
      labels = db.fit_predict(simMatrix)

      # Number of clusters in labels, ignoring noise if present.
      n_clusters_ = len(set(labels)) - (1 if -1 in labels else 0)

      print('Estimated number of clusters: %d' % n_clusters_)

	  # if there are more than one cluster
      if n_clusters_ > 1:
         print(labels, len(labels))

         # save cluster labels to each node in nodeMap
         for i in range(0, len(labels)):
            nodeMapKey = indexList[i]
            nodeMap[nodeMapKey].append(labels[i])

         clusterNodeList0 = list(indexList)
		 # do clustering for the subgroup and update the nodeMap object
         nodeMap = subcluster(labels, clusterNodeList0, nodeMap, simMatrix, thisDamping)

      with open(outputFile, 'w', newline='') as csvoutfile:
         csvWriter = csv.writer(csvoutfile, delimiter=',', quotechar='|')
		 # output all the nodes
         for i in range(0, imax):
            writerowlist = list()
            nodeMapKey = indexList[i]
            writerowlist.append(nodeMapKey)
            writerowlist = writerowlist + nodeMap[nodeMapKey][1:]
            csvWriter.writerow(writerowlist)
            nodedict = dict()
            nodedict['name'] = nodeMapKey
            nodedict['title'] = nodeMapKey
            nodedict['group'] = nodeMap[nodeMapKey][1]
            nodedict['subgroup'] = nodeMap[nodeMapKey][2:]
            nodedict['URL'] = nodeMapKey
            nodedict['description'] = nodeMapKey
            networkdict['nodes'].append(nodedict)
      with open(outputFile_network, 'w') as f:
         json.dump(networkdict, f, sort_keys=True, indent=2, default=str)
   else:
      # Python 2 code in this block
      # inputFile = 'D:/F/UnivSub/UNC/internship/RENCI/DataBridge/cluster_0301/data/test/DataBridge-input-small.csv'
      # outputFile = 'D:/F/UnivSub/UNC/internship/RENCI/DataBridge/cluster_0301/data/test/DataBridge-output-small.csv'
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
      # print 'Input file is:', inputFile
      # print 'Output file is:', outputFile
      # print 'thisDamping is:', str(thisDamping)

      networkdict = dict()
      networkdict['nodes'] = list()
      networkdict['links'] = list()

      with open(inputFile, 'rb') as csvfile:
         csvReader = csv.reader(csvfile, delimiter=',', quotechar='|')
         # First line is the number of distinct nodes.
         headerRows = csvReader.next()
         imax = int(headerRows[0])
         jmax = int(headerRows[0])
         # print str(imax) + " " + str(jmax)

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
               pass
            else:
               nodeMaplist = list()
               nodeMaplist.append(currentNodeIndex)
               nodeMap[row[0]] = nodeMaplist
               indexList.append(row[0])
               currentNodeIndex += 1
            thisI = nodeMap[row[0]][0]

            if (row[1] in nodeMap):
               pass
            else:
               nodeMaplist = list()
               nodeMaplist.append(currentNodeIndex)
               nodeMap[row[1]] = nodeMaplist
               indexList.append(row[1])
               currentNodeIndex += 1
            thisJ = nodeMap[row[1]][0]

            # matrix is symetric
            simMatrix[thisI, thisJ] = float(row[2])
            simMatrix[thisJ, thisI] = float(row[2])

            linkdict = dict()
            linkdict['source'] = thisI
            linkdict['target'] = thisJ
            linkdict['value'] = float(row[2])
            networkdict['links'].append(linkdict)

         for i in range(0, imax):
            # Set all of the diagonals to 1
            simMatrix[i, i] = 1.

	  # do the clustering
      db = AffinityPropagation(affinity='precomputed', damping=thisDamping)
      labels = db.fit_predict(simMatrix)

      # Number of clusters in labels, ignoring noise if present.
      n_clusters_ = len(set(labels)) - (1 if -1 in labels else 0)

      # print 'Estimated number of clusters: %d' % n_clusters_

	  # if there are more than one cluster
      if n_clusters_ > 1:
         # print labels, len(labels)

         # save cluster labels to each node in nodeMap
         for i in range(0, len(labels)):
            nodeMapKey = indexList[i]
            nodeMap[nodeMapKey].append(labels[i])

         clusterNodeList0 = list(indexList)
		 # do clustering for the subgroup and update the nodeMap object
         nodeMap = subcluster(labels, clusterNodeList0, nodeMap, simMatrix, thisDamping)

      with open(outputFile, 'wb') as csvoutfile:
         csvWriter = csv.writer(csvoutfile, delimiter=',', quotechar='|')
         for i in range(0, imax):
            # csvWriter.writerow([indexList[i], labels[i]])
            writerowlist = list()
            nodeMapKey = indexList[i]
            writerowlist.append(nodeMapKey)
            writerowlist = writerowlist + nodeMap[nodeMapKey][1:]
            csvWriter.writerow(writerowlist)
            nodedict = dict()
            nodedict['name'] = nodeMapKey
            nodedict['title'] = nodeMapKey
            nodedict['group'] = nodeMap[nodeMapKey][1]
            nodedict['subgroup'] = nodeMap[nodeMapKey][2:]
            nodedict['URL'] = nodeMapKey
            nodedict['description'] = nodeMapKey
            networkdict['nodes'].append(nodedict)

# function to do clustering for the subgroup
def subcluster(labels, clusterNodeList0, nodeMap, simMatrix, thisDamping):
   # go over all the subgroups
   for i in range(0, max(labels) + 1):
      labelIndex = 0
	  # build the list of the node identifiers in the subgroup
      clusterNodeList = list()
      for node in labels:
         if (node == i):
            nodeMapKey = clusterNodeList0[labelIndex]
            clusterNodeList.append(nodeMapKey)
            # nodeMap[nodeMapKey].append(node)
         labelIndex += 1

	  # if the number of nodes in the cluster is over the threshold
      if (len(clusterNodeList) > 5):
         imax_i = len(clusterNodeList)
         jmax_i = len(clusterNodeList)
		 # build new simMatrix_i and find similarity value in the simMatrix
         simMatrix_i = np.zeros((imax_i, jmax_i), dtype=np.float)
         for ii in range(0, imax_i):
            indexii = nodeMap[clusterNodeList[ii]][0]
            for jj in range(0, jmax_i):
               indexjj = nodeMap[clusterNodeList[jj]][0]
               simMatrix_i[ii, jj] = simMatrix[indexii, indexjj]
		 # do the clustering
         db = AffinityPropagation(affinity='precomputed', damping=thisDamping)
         labels_i = db.fit_predict(simMatrix_i)

		 # if there are more than one cluster, yes do the recursion again
         n_clusters_ = len(set(labels_i)) - (1 if -1 in labels_i else 0)
         print('Estimated number of clusters: %d' % n_clusters_)

         if n_clusters_ > 1:
            # update nodeMap by new labels
            for ii in range(0, len(labels_i)):
               nodeMapKey = clusterNodeList[ii]
               nodeMap[nodeMapKey].append(labels_i[ii])

            nodeMap = subcluster(labels_i, clusterNodeList, nodeMap, simMatrix, thisDamping)

   return nodeMap


if __name__ == "__main__":
   main(sys.argv[1:])
