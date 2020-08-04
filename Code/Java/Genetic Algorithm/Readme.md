Project Description

Genetic algorithms simulate the evolutionary process in a computational model 
in order to solve a problem. Scientists have observed heredity and natural 
selection as the driving forces in the evolutionary process. These concepts 
are the focus of problem solving with genetic algorithms. By creating 
"generations" of solutions and simulating evolution, an optimal solution can 
be reached. Typically, genetic algorithms are used to solve complex resource 
allocation optimization problems. Numerous commercial applications have been 
found for genetic algorithms and research has continued to expand the possible 
uses and tools for this type of evolutionary programming. Genetic algorithms 
draw on several key concepts from the biological model.

Chromosomes:      Hold a list of elements know as genes.

Genes:            Traits or characteristics of an instance of problem solution.

Crossover:        This models reproduction by combining the genes of parent 
chromosomes into offspring chromosomes. Each new generation produces a 
different set of chromosomes based on heredity.

Mutation:         This is the random introduction of different genes or traits. 
Its purpose is to maintain or reintroduce divergence into a converging 
population. This is useful for avoiding local maxima and introducing new 
genetic material.

Fitness Evaluation: Chromosomes are assessed according to a fitness function. 
Only the "fittest" survive to the next generation. This is the key part of the 
genetic algorithm and is the most difficult to construct, as it effectively 
reduces the search space.

A simplified genetic algorithm is as follows:
1) Create an initial population.
2) Evaluate each chromosome in that population according to the fitness 
function.
3) Create new generation by applying crossover and mutation to parent 
chromosomes.
4) Evaluate new chromosomes and insert them into the population.
5)  If solution has been reached then stop, if not go to step 3.
