# Additional Experiments

During the 2D week, I was occupied with the Optimized 32-Bit Full Adder component. Thus, since I have some free time now, I took the liberty of experimenting with other possible implementations of the CEC Verifier which are faster and more optimized. This subfolder contains the results of such experiments.

Please do take note that results might vary between different hardware. In contrast to the relatively more stable timing analysis of JSim for the 50.002 component, Java's timing analysis for 50.001 might have quite a larger range of variance and fluctuations (even after considering the average). As such, a common benchmarking hardware would be required for a fair comparison.

> To avoid a `StackOverflowError`, increase the JVM stack size by adding the argument/flag `-Xss<value>`. 4 MB (replace `<value>` with `4m`) should be enough for our case.
>
> Additionally, to pass the console output (including error messages) to a file for additional debugging and inspection capabilities, this option can be specified: `java <filename/classname> > output.txt 2>&1`. For `jar` files, add the `-jar` flag.

More experiments that take advantage of dynamic programming for the recursive DPLL algorithm, multithreading (using `ExecutorService`, `CountDownLatch`, `ThreadPoolExecutor` or `ForkJoinPool`) and asynchronicity to run atomic operations could also be conducted and tested in the future.

> Hypothesis: synchronized methods attached to specific objects as locks to prevent deadlocks, race conditions or `ConcurrentModificationException` for the sake of correctness of the final result (which is important for our case) might slow down the overall runtime instead. Further tests are necessary to reach a reasonable conclusion.

## Benchmarking

For our original code under the `cec-verifier/src` folder, these are the benchmarking results that I have obtained (which are different from the ones presented in our report):

> Test file: `test_2020.cnf`
>
> Result: not satisfiable
>
> Running time (averaged over multiple runs): 873.9 ms
>
> Specification: ASUS ROG Zephyrus G14 GA401IV | CPU: AMD Ryzen 9 4900HS with Radeon Graphics, 3.0 GHz, 8 Core(s), 16 Logical Processor(s) | GPU: NVIDIA GeForce RTX 2060 with Max-Q Design

As demonstrated, it differs quite significantly from the ones presented in our report (about 2615 ms faster). As such, it seems that the running time is quite hardware-dependent (even though JVM is not supposed to be hardware-dependent, but who knows what is actually going on here).

In comparison, these are the benchmarking results of the better CEC Verifier / SAT Solver under the `cec-verifier/experiments/improved/src` folder:

> Test file: `test_2020.cnf`
>
> Result: not satisfiable
>
> Running time (averaged over multiple runs): 213.4 ms
>
> Specification: ASUS ROG Zephyrus G14 GA401IV | CPU: AMD Ryzen 9 4900HS with Radeon Graphics, 3.0 GHz, 8 Core(s), 16 Logical Processor(s) | GPU: NVIDIA GeForce RTX 2060 with Max-Q Design