APPTS: An Adaptive Penalty based Parallel Tabu Search for Constrained Covering Array Generation
==============================================================================

Usage
----
To use APPTS to generate a constrained covering array, execute the following command:

```
java -jar appts.jar <MODEL_NAME> <CONSTRAINT_NAME> <CUTOFFTIME>
```

For example, run
```
java -jar appts.jar demo.model demo.constraints 30
```

Note that we rely on `iopg-ft.jar` to generate the initial CA, so that it should be put into the same directory as `appts.jar`. We also provide the executable files of alternative versions of APPTS, i.e., `DPTS`, `SPTS` and `APTS`, and they can be used as the same way of `appts.jar`.

Source code
----
Please refer to the `appts_source` directory.

Experimental results
----
Please refer to the `results.pdf` file.
