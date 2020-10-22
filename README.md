# DigiAlpha
Optimized 32-Bit Full Adder, CEC-SAT Verifier &amp; 2-SAT Solver

This is a SUTD 2020 ISTD Term 4 2D Project (executed in one week), comprised of these courses:
- 50.001 Introduction to Information Systems & Programming (`cec-verifier`)
- 50.002 Computation Structures (`optimized-adder`)
- 50.004 Introduction to Algorithms (`sat-solver`)

Group 11 Team Members:
- [Velusamy Sathiakumar Ragul Balaji](https://github.com/ragulbalaji) (Team Leader)
- [James Raphael Tiovalen](https://github.com/jamestiotio)
- [Anirudh Shrinivason](https://github.com/Anirudh181001)
- [Jia Shuyi](https://github.com/shuyijia)
- [Gerald Hoo Yong Wei](https://github.com/geraldhyw)
- [Shoham Chakraborty](https://github.com/shohamc1)



## CEC Verifier

For the CEC Verifier, we implemented a DPLL algorithm as the default algorithm with multi-threading capabilities [UNCONFIRMED]. Further improvements could be obtained by implementing CDCL as the default algorithm to fallback to, and topping up with an add-on of using Kosaraju's algorithm for 2-SAT Problems.

More details are outlined in our 50.001 report document.



## Optimized Adder

>  NOTE: JSim's timing analysis report might be slightly incorrect since it seems that there is a constant offset of about 1 nanosecond for the checkoff file to start executing and about 0.2 nanoseconds for the timing analysis program to terminate.

Our goal is to achieve a 32-bit adder with minimum area-delay product, with the delay being under 3 nanoseconds. (Power usage, length of wire tracks, "neatness" of the components' arrangement, fan-out depth, logic levels, number of cells, etc are not taken into consideration.)

For the optimized 32-bit adder, we implemented a hybrid Han-Carlson adder, which is a hybridization of the Brent-Kung adder (top & bottom layers) and the Kogge-Stone adder (middle layer). Other flavours such as the Ladner-Fischer, Ling, Knowles and Sklansky adders are slower for 32-bits (in this **very specific case**). We used faster components that utilize inverting logic to shorten the preprocessing delay and double inverters are used as a replacement for buffers. Efficient AndOrInverter gates are also used to reduce delay.

Better adder designs that utilize the optimized spanning tree mechanism (such as the Lynch-Swartzlander adder or the Kantabutra-style adder) require the usage of Manchester Carry Chains (MCCs) for them to be sufficiently better (instead of just a CLA tree with ripple-carry full adders at the end). While MCCs are faster and require less area, implementing them requires us to directly access and manipulate the arrangement of PFETs and NFETs (as well as transmission gates), which are **seemingly** not allowed since they are not part of the `stdcell.jsim` library (although they are part of JSim's built-in sub-circuits, except for transmission gates) and it would be considered as a change in technology instead of arrangement. However, the quaternary logic could serve as an inspiration even for traditional CMOS-based adders. There are also modified versions of the aforementioned adder flavours which might be better.

Other alternatives could also be explored in the future, such as combining carry-skip or carry-select into the adder, implementing HK signals for Radix-8 Booth Encoding (better than Radix-2 or Radix-4) or employing the help of an evolutionary machine learning algorithm to find the best placement of black boxes, grey boxes and buffers to generate a unique flavour of the parallel prefix carry-lookahead adder.

For the CNF file generator, we convert our best circuit (the Han-Carlson adder) into a BC file, which in turn is converted into a CNF file ready for submission by using an online tool.

The even bits and odd bits are not treated equally in our selected adder architecture. Also, the more significant bits travel longer distances than the less significant bits. As such, with these factors in our consideration, we arbitrarily choose Bit 7 and Bit 24 as our test bits.



## SAT Solver

For the deterministic 2-SAT Solver, we implemented Kosaraju's algorithm to achieve polynomial time asymptotics.

For the randomized SAT Solver, we implemented the Relaxed Papadimitriou's Random Walk algorithm [UNCONFIRMED].

A detailed explanation of the implementation details, as well as a comparison between the two algorithms are outlined in our 50.004 report document.