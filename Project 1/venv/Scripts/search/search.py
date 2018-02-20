# search.py
# ---------
# Licensing Information:  You are free to use or extend these projects for 
# educational purposes provided that (1) you do not distribute or publish 
# solutions, (2) you retain this notice, and (3) you provide clear 
# attribution to UC Berkeley, including a link to 
# http://inst.eecs.berkeley.edu/~cs188/pacman/pacman.html
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero 
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and 
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]


def depthFirstSearch(problem):
    """
    Search the deepest nodes in the search tree first.

    Your search algorithm needs to return a list of actions that reaches the
    goal. Make sure to implement a graph search algorithm.

    To get started, you might want to try some of these simple commands to
    understand the search problem that is being passed in:

    print "Start:", problem.getStartState()
    print "Is the start a goal?", problem.isGoalState(problem.getStartState())
    print "Start's successors:", problem.getSuccessors(problem.getStartState())
    """
    "*** YOUR CODE HERE ***"

    # print "Start:", problem.getStartState()
    # print "Is the start a goal?", problem.isGoalState(problem.getStartState())
    # print "Start's successors:", problem.getSuccessors(problem.getStartState())

    def make_init_node(init_state):
        return init_state, 'None', 0, 'None'

    def state(n):
        # print n[0]
        return n[0]

    def action(n):
        # print n[1]
        return n[1]

    def step_cost(n):
        # print n[2]
        return n[2]

    def backtrack(gs, k):
        actions = []
        current_backtracking_info = k[gs]
        while True:
            if current_backtracking_info == ('None', 'None'):
                break
            else:
                act = current_backtracking_info[0]
                st = current_backtracking_info[1]
                actions.append(act)
                current_backtracking_info = k[st]

        # CHECK CODE
        # print actions

        actions_reversed = []
        for i in reversed(actions):
            actions_reversed.append(i)

        # CHECK CODE
        # print actions_reversed

        return actions_reversed

    # Create a list for storing visited nodes (closed), a queue for nodes just added by the search (fringe),
    # And a hash table for the backtrack function
    closed = []
    fringe = util.Stack()
    known = dict()

    # Init the starting node and add it to the hashtable and the known nodes
    start_node = make_init_node(problem.getStartState())
    fringe.push(start_node)
    known[state(start_node)] = ('None', 'None')

    # Repeat until fringe is empty
    while fringe:

        # Remove the next item from the fringe
        node = fringe.pop()

        # If that object's state is the goal state, perform backtracking
        if problem.isGoalState(state(node)):
            return backtrack(state(node), known)

        # Generate the children of the current node
        successors = problem.getSuccessors(state(node))

        # Mark current node as visited
        closed.append(node)

        # For each child node of the current node, add to fridge and known nodes (along with
        # backtracking information)
        for successor in successors:
            if state(successor) not in known:
                fringe.push(successor)
                known[state(successor)] = (action(successor), state(node))

    # raise Exception("Depth-First Search failed to find a valid path to the goal.")

    # util.raiseNotDefined()


def breadthFirstSearch(problem):
    """Search the shallowest nodes in the search tree first."""
    "*** YOUR CODE HERE ***"

    def make_init_node(init_state):
        return init_state, 'None', 0, 'None'

    def state(n):
        # print n[0]
        return n[0]

    def action(n):
        # print n[1]
        return n[1]

    def step_cost(n):
        # print n[2]
        return n[2]

    def backtrack(gs, k):
        actions = []
        current_backtracking_info = k[gs]
        while True:
            if current_backtracking_info == ('None', 'None'):
                break
            else:
                act = current_backtracking_info[0]
                st = current_backtracking_info[1]
                actions.append(act)
                current_backtracking_info = k[st]

        # CHECK CODE
        # print actions

        actions_reversed = []
        for i in reversed(actions):
            actions_reversed.append(i)

        # CHECK CODE
        # print actions_reversed

        return actions_reversed

    # Create a list for storing visited nodes (closed), a queue for nodes just added by the search (fringe),
    # And a hash table for the backtrack function
    closed = []
    fringe = util.Queue()
    known = dict()

    # Init the starting node and add it to the hash table and the known nodes
    start_node = make_init_node(problem.getStartState())
    fringe.push(start_node)
    known[state(start_node)] = ('None', 'None')

    # Repeat until fringe is empty
    while fringe:

        # Remove the next item from the fringe
        node = fringe.pop()

        # If that object's state is the goal state, perform backtracking
        if problem.isGoalState(state(node)):
            return backtrack(state(node), known)

        # Generate the children of the current node
        successors = problem.getSuccessors(state(node))

        # Mark current node as visited
        closed.append(node)

        # For each child node of the current node, add to fridge and known nodes (along with
        # backtracking information)
        for successor in successors:
            if state(successor) not in known:
                fringe.push(successor)
                known[state(successor)] = (action(successor), state(node))

    # raise Exception("Breadth-First Search failed to find a valid path to the goal.")

    # util.raiseNotDefined()

def uniformCostSearch(problem):
    """Search the node of least total cost first."""
    "*** YOUR CODE HERE ***"

    def make_init_node(init_state):
        return init_state, 'None', 0, 'None'

    def state(n):
        # print n[0]
        return n[0]

    def action(n):
        # print n[1]
        return n[1]

    def step_cost(n):
        # print n[2]
        return n[2]

    def backtrack(gs, k):
        actions = []
        current_backtracking_info = k[gs]
        while True:
            if current_backtracking_info == ('None', 'None'):
                break
            else:
                act = current_backtracking_info[0]
                st = current_backtracking_info[1]
                actions.append(act)
                current_backtracking_info = k[st]

        # CHECK CODE
        # print actions

        actions_reversed = []
        for i in reversed(actions):
            actions_reversed.append(i)

        # CHECK CODE
        # print actions_reversed

        return actions_reversed

    # Create a list for storing visited nodes (closed), a queue for nodes just added by the search (fringe),
    # And a hash table for the backtrack function
    closed = []
    fringe = util.PriorityQueue()
    known = dict()

    # Init the starting node and add it to the hash table and the known nodes
    start_node = make_init_node(problem.getStartState())
    fringe.push(start_node, 0)
    known[state(start_node)] = ('None', 'None')

    # Repeat until fringe is empty
    while fringe:

        # Remove the next item from the fringe
        node = fringe.pop()

        # If that object's state is the goal state, perform backtracking
        if problem.isGoalState(state(node)):
            return backtrack(state(node), known)

        # Generate the children of the current node
        successors = problem.getSuccessors(state(node))

        # Mark current node as visited
        closed.append(node)

        # For each child node of the current node, add to fridge and known nodes (along with
        # backtracking information)
        for successor in successors:
            if state(successor) not in known:
                fringe.push(successor, step_cost(successor))
                known[state(successor)] = (action(successor), state(node))

    # raise Exception("Uniform Cost Search failed to find a valid path to the goal.")

    # util.raiseNotDefined()

def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

def aStarSearch(problem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""
    "*** YOUR CODE HERE ***"

    def make_init_node(init_state):
        return init_state, 'None', 0, 'None'

    def state(n):
        # print n[0]
        return n[0]

    def action(n):
        # print n[1]
        return n[1]

    def step_cost(n):
        # print n[2]
        return n[2]

    def backtrack(gs, k):
        actions = []
        current_backtracking_info = k[gs]
        while True:
            if current_backtracking_info == ('None', 'None'):
                break
            else:
                act = current_backtracking_info[0]
                st = current_backtracking_info[1]
                actions.append(act)
                current_backtracking_info = k[st]

        # CHECK CODE
        # print actions

        actions_reversed = []
        for i in reversed(actions):
            actions_reversed.append(i)

        # CHECK CODE
        # print actions_reversed

        return actions_reversed

    def f(item):
        heuristic(item, problem) + step_cost(item)
        return item ;

        # Create a list for storing visited nodes (closed), a queue for nodes just added by the search (fringe),
        # And a hash table for the backtrack function

    closed = []
    fringe = util.PriorityQueueWithFunction(f)
    known = dict()

    current_total_cost = 0

    # Init the starting node and add it to the hash table and the known nodes
    start_node = make_init_node(problem.getStartState())
    fringe.push(start_node)
    known[state(start_node)] = ('None', 'None')

    # Repeat until fringe is empty
    while fringe:

        # Remove the next item from the fringe
        node = fringe.pop()

        # If that object's state is the goal state, perform backtracking
        if problem.isGoalState(state(node)):
            return backtrack(state(node), known)

        # Generate the children of the current node
        successors = problem.getSuccessors(state(node))

        # Mark current node as visited
        closed.append(node)

        # For each child node of the current node, add to fridge and known nodes (along with
        # backtracking information)
        for successor in successors:
            if state(successor) not in known:
                fringe.push(successor)
                known[state(successor)] = (action(successor), state(node))

    # raise Exception("Uniform Cost Search failed to find a valid path to the goal.")

    # util.raiseNotDefined()


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
