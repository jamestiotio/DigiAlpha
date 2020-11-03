import os
from randomised.randomisedAlgo import *

if __name__ == "__main__":
    NO_OF_VARIABLES = 0

    filename = os.path.abspath('test1.cnf')
    output_list = []
    variables_list = []

    with open(filename) as f:
        data = f.readlines()
        
        for line in data:
            if line[0] == 'c' or line == '\n':
                pass
            elif line[0] == 'p':
                line = line.split(' ')
                NO_OF_VARIABLES = line[2]
            else:
                output_list.append(line.split('\n')[0][:-1].split())
                line = line.split(' ')
                
                for var in line[:-1]:
                    if var != '0' or var != '\n':
                        if not abs(int(var)) in variables_list:
                            variables_list.append(abs(int(var)))


# print(output_list) #  list of outputs
# print(variables_list) # list of variables
# print(NO_OF_VARIABLES) # number of variables

print(Randomised_Algo(output_list, variables_list))





# def check(assignment):
#     # assignment: dictionary of assignments to all variables
#     truth = []
#     k = True
#     for each in assignment:
#         truth.append(each[0] or each[1])
        
#     for i in truth:
#         k = k and i
    
#     return k