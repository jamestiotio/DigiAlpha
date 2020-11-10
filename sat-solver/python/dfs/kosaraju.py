# This class stores a representation of a directed graph using an adjacency list
class Graph:
    def __init__(self, vertices):
        self.V = vertices  # This stores the number of vertices
        self.G = dict()  # This uses a dictionary to store the graph
        self.temp_scc = []  # This is used to store the list of vertices connected to a specific vertex in one strongly-connected component
        self.final_list_of_scc = []  # Store vertices as separate strongly-connected components in topological order

    # Function to add an edge to the graph
    def add_edge(self, before, after):
        if before in self.G:
            self.G[before].append(after)

        else:
            self.G[before] = [after]

    # Function to perform depth-first search on the graph
    def dfs(self, vertex, visited):
        # Base case: mark the current vertex as visited
        visited[vertex] = True
        self.temp_scc.append(vertex)

        # Ensure that the associated vertex key has a non-null list as its value in the dictionary
        if self.G.get(vertex, None):
            # Recursive case: mark all of the vertices adjacent to the specified vertex as visited
            for neighbour in self.G[vertex]:
                if not visited[neighbour]:
                    self.dfs(neighbour, visited)

    # Function to perform depth-first search on the graph and add to the main stack
    def dfs_stack(self, vertex, visited, main_stack):
        # Base case: mark the current vertex as visited
        visited[vertex] = True

        # Another way to ensure that the associated vertex key has a non-null list as its value in the dictionary
        for neighbour in self.G.get(vertex, []):
            if not visited[neighbour]:
                # Recursive case: mark all of the vertices adjacent to the specified vertex as visited
                self.dfs_stack(neighbour, visited, main_stack)

        # Add the current vertex to the main stack
        main_stack.append(vertex)

    # Function to get the transpose/reverse of the graph (this is unique for Kosaraju's algorithm)
    def transpose(self):
        # Add all of the vertices to the reverse graph
        reversed_graph = Graph(self.V)

        # Add the reverse of each directed edge to the reverse graph
        for vertex in self.G:
            for neighbour in self.G[vertex]:
                reversed_graph.add_edge(neighbour, vertex)

        return reversed_graph

    # Function to return all of the strongly-connected components of the graph
    def return_all_scc(self):
        main_stack = []
        visited = [False] * self.V  # Initialize all of the values with False, since we have not visited any nodes yet

        # Add vertices to the main stack in reverse topological order (according to their finishing times)
        for vertex in range(0, self.V):
            if not visited[vertex]:
                self.dfs_stack(vertex, visited, main_stack)

        g_transposed = self.transpose()  # Create the transpose of the graph

        visited = [False] * self.V  # Mark all the vertices as not visited again to prepare for our second round of DFS

        # Process all the vertices in the order defined by the main stack
        while main_stack:
            vertex = main_stack.pop()

            if not visited[vertex]:
                g_transposed.dfs(vertex, visited)
                self.final_list_of_scc.append(g_transposed.temp_scc)
                g_transposed.temp_scc = []

        return self.final_list_of_scc


class Kosaraju:
    def __init__(self, vertices, clauses):
        self.graph = Graph(int(vertices) * 2)
        self.vertices_list = []
        self.clauses_list = []
        self.largest_scc = []
        self.transformed_list = []
        self.map = dict()
        self.value = 0

        # Build list of vertices
        for vertex in range(1, int(vertices) + 1):
            self.vertices_list.append(vertex)
            self.vertices_list.append(-vertex)

        # Build map
        for vertex in self.vertices_list:
            self.map[vertex] = self.value
            self.value += 1

        self.reverse_map = {v:k for k,v in self.map.items()}  # Build reverse map

        # Build implication graph by drawing directed edges using the clauses
        for clause in clauses:
            # Ensure that it is a 2SAT problem
            if len(clause) != 2:
                continue

            self.graph.add_edge(self.map[-int(clause[0])], self.map[int(clause[1])])  # Add edge ¬A -> B for A ∨ B
            self.graph.add_edge(self.map[-int(clause[1])], self.map[int(clause[0])])  # Add edge ¬B -> A for A ∨ B

        self.list_of_scc = self.graph.return_all_scc()  # Find all strongly-connected components

        for list_scc in self.list_of_scc:
            temp = [self.reverse_map[i] for i in list_scc]
            self.transformed_list.append(temp)

    def satisfiable(self):
        # Check for every SCC: if both xi and -xi exists in the same SCC, then it is unsatisfiable
        for scc in self.transformed_list:
            for literal in scc:
                if -literal in scc:
                    return False

        self.largest_scc = sorted(sorted(self.transformed_list, key = lambda j: len(j))[0], key = lambda k: max(k, -k))
        return True

    def kosaraju_solve(self):
        if self.satisfiable():
            output = ""
            for element in self.largest_scc:
                if element < 0:
                    output += "1"
                elif element > 0:
                    output += "0"
                
                output += " "
            
            return "SATISFIABLE" + "\n" + output
        
        else:
            return "UNSATISFIABLE"