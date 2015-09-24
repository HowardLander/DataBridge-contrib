#!/bin/bash
# downloads and sends ingest message for each file in file list argument
#
# PRE: classpath for dataloader jars and deps must be in a file called classpath.txt located in the same dir as this script

if [ $# -ne 5 ]
  then
    echo "Usage: download_list.sh <list_of_urls_to_download_one_per_line_with_#_as_comment> /path/to/conf <target_namespace_for_insertion> /path/to/destination/dir/for/file/download <FQCN-for-formatter-instance>"
    exit 
  fi

amqpconffile=$2
namespace=$3
absdldir=$4
formatterclassname=$5
classpath=`cat classpath.txt`
javabase="-Djava.util.logging.config.file=logging.properties -cp $classpath org.renci.databridge.contrib.dataloader.DataLoader $amqpconffile $formatterclassname $namespace"
echo $javabase
# count url lines to get last url line
# loop will count also to identify last url st event flag can be set
lasturlline=`grep -w -v '^#*' -c $1` # count only non-commented, nonempty lines
echo lasturlline: $lasturlline

cururlline=1
while read url
do
  if [[ $url != \#* ]] && [ ! -z "$url" ] ;
  then
    javastring=""
    wget --no-check-certificate --no-clobber --directory-prefix=$absdldir $url
    file="${url##*/}"
    if [ $cururlline -eq $lasturlline ]
      then
        javastring=$javabase$" true file://"
        echo "cururlline -eq lasturlline at $cururlline"
      else
        javastring=$javabase$" false file://" 
      fi
    java $javastring$absdldir/$file
    ((cururlline++))
  fi
done < "$1"
