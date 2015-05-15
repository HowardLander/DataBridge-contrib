DataLoader invocation using maven is as follows.

Arguments:

1. Configuration file containing exchange, queue, and other comms properties.
2. Formatter name. Examples:
   org.renci.databridge.contrib.formatter.oaipmh.OaipmhMetadataFormatterImpl
   org.renci.databridge.contrib.formatter.codebook.CodeBookMetadataFormatterImpl
3. Namespace to ingest into.
4. Whether a Processed.Metadata.To.MetadataDB message should be published by the Ingest Engine
5. URL of file to be loaded.

Example:

cd to databridge-contrib/dataloader/

mvn clean compile exec:java -Dexec.mainClass=org.renci.databridge.contrib.dataloader.DataLoader -Dexec.args="./DataBridge.conf org.renci.databridge.contrib.formatter.codebook.CodeBookMetadataFormatterImpl test_ingest false file:///path/to/file"

