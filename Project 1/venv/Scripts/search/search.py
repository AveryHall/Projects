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

    print "Start:", problem.getStartState()
    print "Is the start a goal?", problem.isGoalState(problem.getStartState())
    print "Start's successors:", problem.getSuccessors(problem.getStartState())

    from game import Directions

    # def dfs():  # DFS
    #
    #     max_value = 0
    #     max_weight = 0
    #     items_selected = []
    #
    #     init_node = Node()
    #
    #     stack = [init_node]
    #
    #     while stack:
    #
    #         node = stack.pop()
    #
    #         if node.value > max_value and node.weight <= Capacity:
    #             max_value = node.value
    #             max_weight = node.weight
    #             items_selected = node.items
    #
    #         neighbors = expand_neighbors(node)
    #         for j in neighbors:
    #             stack.append(j)
    #
    #         # CHECK CODE
    #         # for test in stack:
    #         #     print(test.items)
    #         # print("")
    #
    #     return items_selected, max_value, max_weight

    def make_init_node(init_state):
        return init_state, None, 0

    def state(n):
        # print n[0]
        return n[0]

    def action(n):
        # print n[1]
        return n[1]

    def step_cost(n):
        # print n[3]
        return n[3]

    def backtrack(goal, closed_list):
        print "Closed list here", closed_list

        current_node = goal
        actions = [action(goal)]
        current_action = action(goal)

        while state(current_node) != problem.getStartState:

            print "current action", current_action

            if current_action == 'North':
                print "In here"
                x, y = state(current_node)
                parent_state = (x, y-1)
            if current_action == 'South':
                x, y = state(current_node)
                parent_state = (x, y + 1)
            if current_action == 'East':
                x, y = state(current_node)
                parent_state = (x - 1, y)
            if current_action == 'West':
                print "In here!"
                x, y = state(current_node)
                parent_state = (x + 1, y)
                print parent_state

            print "parent state", parent_state

            for i in closed:
                if state(i) == parent_state:
                    print "Again"
                    current_node = i
                    print current_node
                    current_action = action(i)
                    if state(i) != problem.getStartState:
                        actions.append(current_action)
                    break

            print("Actions: ", actions.reverse())

            return actions.reverse()

    # def backtrack(s, m):
    #     actions = []
    #
    #     while True:
    #         previous_moves = m[s];
    #         if len(previous_moves) == 2:
    #             s = state(previous_moves)
    #             a = action(previous_moves)
    #             actions.append(a)
    #         else:
    #             break
    #
    #     return actions.reverse()

    import collections

    # def insert_all(successors, f):
    #     for successor in successors:
    #         f.push(successor)
    #     return

    closed = []
    path_to_goal = []
    meta = dict()
    PrevStates = []
    fringe = util.Stack()
    fringe.push(make_init_node(problem.getStartState()))
    while fringe:
        node = fringe.pop()
        print(node)
        if problem.isGoalState(state(node)):
            closed.append(node)
            return backtrack(node, closed)
        if node not in closed:
            closed.append(node)
            print state(node)
            for successor in problem.getSuccessors(state(node)):
                fringe.push(successor)

    # raise Exception("Depth-First Search failed to find a valid path to the goal.")

    # util.raiseNotDefined()

def breadthFirstSearch(problem):
    """Search the shallowest nodes in the search tree first."""
    "*** YOUR CODE HERE ***"
    util.raiseNotDefined()

def uniformCostSearch(problem):
    """Search the node of least total cost first."""
    "*** YOUR CODE HERE ***"
    util.raiseNotDefined()

def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

def aStarSearch(problem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""
    "*** YOUR CODE HERE ***"
    util.raiseNotDefined()


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
