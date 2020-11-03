from collections import defaultdict

class Graph:
    def __init__(self, vertices):
        self.V = vertices
        self.edges = []

    def add_edge(self, bef, after):
        self.edges.append((bef, after))
    
    def add_vertex(self, v):
        self.vertices.append(v)

class SCC:
    def __init__(self):
        self.graph = Graph()
        self.construct_implication_graph(self.graph)
        self.scc_list = []

    def construct_implication_graph(self, graph, variable_list, output_list):
        for literal in variable_list:
            graph.add_vertex(literal)
            graph.add_vertex(-literal)
        
        for clause in output_list:
            graph.add_edge(clause[0], clause[1])

    def dfs(self):
        pass