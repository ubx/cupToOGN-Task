# cupToOGN-Task
Convert Tasks from CUP file format into OGN task file format
``````
java -jar cupToOgnTask.jar -h
usage: cupToOgnTask
 -compare                             check if there are waypoins with the same position
 -cup,--seeyou cup input file <arg>   the cup-file to read
 -ogn <arg>                           the ogn-file (json-encoded) to write, if not specified: sysout