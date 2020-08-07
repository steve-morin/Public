Discrete Event Simulation - Linear Process

Asynchronous Transfer Mode (ATM) Switch

OVERVIEW:
This example runs several simulations on the
model of an ATM switch; using SimKit and the Java programming language.
The switch has two sources(X1, X2), a processor(P), and two destinations 
(Y1, Y2). 

		X1--\   /--Y1
	             \_/
	             (P)
	            /  \
	        X2-/    \--Y2

The following parameters are variable:
* the interarrival time from the source X2
* as is the probability that the cell is sent from X2 to Y1
* the buffer capacity at the output ports
By performing repeated test cases we can determine what buffer capacity will
allow us to maintain a probability of low cell loss less than 5%.

RESULTS/CONCLUSION:
The results of the simulations showed the following facts;

+ the faster the interarrival time, the greater the load is on the output port
+ the larger the probability of a cell going from X2 to Y1, the greater the 
  load on the output port
+ the larger the buffer capacity, the less likely it is that cell loss will occur

Here are some noteworthy results for the Y1 LP;
The interarrival time is fixed for these results, it was obvious that by increasing
the interarrival time the buffer capacity needed would be reduced, as would cell loss.
Note that cell loss is quite high in all cases for a buffer size of one, and that the high priority cell loss
is even hight than low priority cell loss.


| ProbX2Y1 | BufferCapacity | InterArrive | %High Cell Loss | %Low Cell Loss | Mean	| Std. Dev.	|
| ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- |
  0.50     |        1              |       2.74         |    0.064           |   0.035          |      4.6      |   0.7
  0.75     |        1              |       2.74         |    0.11            |   0.085          |      4.89     |   0.85
  0.99     |        1              |       2.74         |    0.21            |   0.16           |      5.1      |   0.9

Note that a buffer size of 3, and a X2Y1prob = 0.75, the probability of low cell loss is less than 5%.
This is a good option if you are trying to efficiently use your memory size, however if the worst case occurs (X2Y1prob = 0.99)
then there is an unacceptable level of cell loss.

ProbX2Y1   |	BufferCapacity     |	InterArrive	|   %High Cell Loss  | %Low Cell Loss	|     Mean	| Std. Dev.	
| ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- |
  0.50     |        3              |       2.74         |    0.006           |   0.002          |      5.7      |   1.7 
  0.75     |        3              |       2.74         |    0.01            |   0.04           |      6.7      |   2.3
  0.99     |        3              |       2.74         |   0.05             |   0.16           |      7.9      |   2.5

Notice that to acertain that cell loss is below 5% for the worst case of when probX2Y1 = 0.99 and the X2InterArrive = 2.74,
the buffer size must be able to hold 65 cells. Notice that each cell spends a substantial amount of time in the system.
ProbX2Y1   |	BufferCapacity     |	InterArrive	|   %High Cell Loss  | %Low Cell Loss	|     Mean	| Std. Dev.	
| ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- |
  0.99     |        65             |       2.74         |   0.0              |   0.5            |      52.8     |   78.7

In summary, the design of the switch(specifically the output buffer size) depends
on how important it is to keep cell loss at a low level. If this is a primary goal,
then a buffer size of 65 cells is needed. However, if it expensive or impractical 
to have a buffer that big, then a buffer size of 3 will maintain a low cell loss when
probX2Y1 is at 0.75 .
