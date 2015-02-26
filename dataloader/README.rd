Invocation using maven:

mvn exec:java -Dexec.mainClass=org.renci.databridge.contrib.dataloader.DataLoader -Dexec.args="./DataBridge.conf org.renci.databridge.contrib.formatter.codebook.CodeBookMetadataFormatterImpl test_ingest_2 file:///path/to/file"

mvn exec:java -Dexec.mainClass=org.renci.databridge.contrib.dataloader.DataLoader -Dexec.args="./DataBridge.conf org.renci.databridge.contrib.formatter.oaipmh.OaipmhMetadataFormatterImpl test_ingest_4 file:///path/to/file"

NB: Hangs after it sends message and must be killed. 

Example formatters:

org.renci.databridge.contrib.formatter.oaipmh.OaipmhMetadataFormatterImpl
org.renci.databridge.contrib.formatter.codebook.CodeBookMetadataFormatterImpl

