import copy
import random

def Randomised_Algo(formula, variable_list):
    assignment = copy.deepcopy(formula)
    Assignment = {}
    for each in variable_list:
        if each not in Assignment:
            Assignment[each] = False

    for i in range(100*len(variable_list)**2):
        current_assignment = set_assignment(formula, Assignment)
        false_clauses = clause_check(current_assignment)

        if len(false_clauses) != 0:
            chosen_clause = random.choice(false_clauses)
            chosen_var_ind = random.randint(0,1)
            chosen_var = formula[chosen_clause][chosen_var_ind]
            Assignment[abs(int(chosen_var))] = not Assignment[abs(int(chosen_var))]

        else:
            return True
    return False

def set_assignment(formula, assignment):
    # formula: original formula
    # assignment: dictionary of assigned booleans
    temp = copy.deepcopy(formula)
    for i in range(len(formula)):
        for j in range(2):
            var = temp[i][j]
            idx = int(formula[i][j])
            if var[0] == '-':
                idx = idx * -1
                temp[i][j] = not assignment[idx]
            else:
                temp[i][j] = assignment[idx]
    return temp
        

def clause_check(assignment):
    false_clauses = []
    for idx, val in enumerate(assignment):
        if not (val[0] or val[1]):
            false_clauses.append(idx)
            
    return false_clauses