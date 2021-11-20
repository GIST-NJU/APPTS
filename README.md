An Adaptive Penalty based Parallel Tabu Search for Constrained Covering Array Generation
==============================================================================

Source code
----
Please refer to the directories `appts_source`.

Experimental results
----
Please refer to the file `results.pdf`.


Executable JAR File
----
To generate a constrained covering array by APPTS, use the following command:

java -jar appts.jar  <MODEL_NAME><CONSTRAINT_NAME><CUTOFFTIME>
For example: java -jar appts.jar demo.model demo.constraints 30

Noted: (1)iopg-ft.jar is used to generate a initial CA and it must be in the same directory as appts.jar.
(2) we also provide the .jar files for the alternative versions of APPTS, i.e., DPTS,SPTS and APTS, and these files are used  in the same way as appts.jar.