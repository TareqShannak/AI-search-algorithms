import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class SearchAlgorithm {
	//First we defined variables that we will use in search goals
	public static ArrayList<City> orderOfExpansion = new ArrayList<City>();
	static ArrayList<City> pathFound = new ArrayList<City>();
	static City Goal = null;
	//To reverse order of cities in the linked list:
	static Stack<City> reversePathFound = new Stack<>();
	
	//							Breadth First Search
	//			We used a Queue to achieve FIFO in checking the cities
	//				hence we search for the goal level by level
	//			if our queue is empty and no goal found, it returns null
	//otherwise, it returns the path from start point to the goal that we found
	public static ArrayList<City> BFS(int startId, ArrayList<Integer> goalsId) {
		clearOldData(resetAll);
		Queue<City> Fringe = new LinkedList<>();
		City startCity = new City(startId, 0, minHeurestic(startId, goalsId), null);
		Fringe.add(startCity);
		while (Fringe.size() > 0) {
			City current = Fringe.poll();
			orderOfExpansion.add(current);
			if (goalsId.contains(current.getId())) {
				Goal = current;
				break;
			}
			for (int i = 0; i < Main.CostFunc[current.getId()].size(); i++) {
				City newCity = new City(Main.CostFunc[current.getId()].get(i).id,
						current.getCostToReach() + Main.CostFunc[current.getId()].get(i).driveRoute,
						minHeurestic(Main.CostFunc[current.getId()].get(i).id, goalsId), current);
				Fringe.add(newCity);
			}
		}
		return reversepathFoundFound(Goal, pathFound);
	}

	//							Depth First Search
	//			We used a Stack to achieve LIFO in checking the cities
	//					hence we Expand deepest unexpanded node
	//			if our stack is empty and no goal found, it returns null
	//otherwise, it returns the path from start point to the goal that we found
	public static ArrayList<City> DFS(int startId, ArrayList<Integer> goalsId) {
		clearOldData(resetAll);
		ArrayList<Integer> visitedIds = new ArrayList<Integer>();
		Stack<City> Fringe = new Stack<>();
		City startCity = new City(startId, 0, minHeurestic(startId, goalsId), null);
		Fringe.push(startCity);
		visitedIds.add(startCity.getId());
		while (Fringe.size() > 0) {
			City current = Fringe.pop();
			orderOfExpansion.add(current);
			if (goalsId.contains(current.getId())) {
				Goal = current;
				break;
			}
			// because it is a stack, we started the loop from the last element:
			for (int i = Main.CostFunc[current.getId()].size() - 1; i >= 0; i--) {
				// this 'IF' Condition to ensure that our algorithm will not stuck in infinite loop
				if (visitedIds.contains(Main.CostFunc[current.getId()].get(i).id)) {
					orderOfExpansion.add(new City(Main.CostFunc[current.getId()].get(i).id,
							current.getCostToReach() + Main.CostFunc[current.getId()].get(i).driveRoute,
							minHeurestic(Main.CostFunc[current.getId()].get(i).id, goalsId), current));
					continue;
				}
				City newCity = new City(Main.CostFunc[current.getId()].get(i).id,
						current.getCostToReach() + Main.CostFunc[current.getId()].get(i).driveRoute,
						minHeurestic(Main.CostFunc[current.getId()].get(i).id, goalsId), current);
				Fringe.push(newCity);
				visitedIds.add(newCity.getId());
			}
		}
		return reversepathFoundFound(Goal, pathFound);
	}

	//						Iterative Deepening Search
	//				We used a recursion method to check the cities
	//				hence we check a level after checking a depth
	//		if our recursion method returns null, then we will increase depth
	//				  and call the method to search for a goal
	public static ArrayList<City> IterativeDeepening(int startId, ArrayList<Integer> goalsId) {
		clearOldData(resetAll);
		Queue<City> Fringe = new LinkedList<>();
		Fringe.add(new City(startId, 0, minHeurestic(startId, goalsId), null));
		City current = Fringe.poll();
		orderOfExpansion.add(current);
		int depth = 0;
		while (true) {
			Goal = DLS(current, depth, goalsId, Fringe);
			if (Goal != null)
				break;
			depth++;
			Fringe.clear();
		}
		return reversepathFoundFound(Goal, pathFound);
	}

	//Recursive Method for IDS
	static City DLS(City current, int depth, ArrayList<Integer> goalsId, Queue<City> Fringe) {
		if (depth == 0)
			for (int i = 0; i < goalsId.size(); i++)
				if (current.getId() == goalsId.get(i))
					return current;
		if (depth > 0) {
			for (int i = 0; i < Main.CostFunc[current.getId()].size(); i++) {
				City city = new City(Main.CostFunc[current.getId()].get(i).id,
						current.getCostToReach() + Main.CostFunc[current.getId()].get(i).driveRoute,
						minHeurestic(Main.CostFunc[current.getId()].get(i).id, goalsId), current);
				Fringe.add(city);
				orderOfExpansion.add(city);
				City Found = DLS(city, depth - 1, goalsId, Fringe);
				if (Found != null)
					return Found;
			}
		}
		return null;
	}

	//								Greedy Search
	//				We used a Priority Queue to checking the cities
	//			that sorts the elements automatically after each change
	//	by their evaluation function to take the least F(n) = H(n) in the queue
	public static ArrayList<City> Greedy(int startId, ArrayList<Integer> goalsId) {
		clearOldData(resetAll);
		PriorityQueue<City> Fringe = new PriorityQueue<>();
		City startCity = new City(startId, 0, minHeurestic(startId, goalsId), null);
		Fringe.add(startCity);
		while (Fringe.size() > 0) {
			City current = Fringe.poll();
			orderOfExpansion.add(current);
			if (goalsId.contains(current.getId())) {
				Goal = current;
				break;
			}
			for (int i = 0; i < Main.CostFunc[current.getId()].size(); i++) {
				City newCity = new City(Main.CostFunc[current.getId()].get(i).id,
						current.getCostToReach() + Main.CostFunc[current.getId()].get(i).driveRoute,
						minHeurestic(Main.CostFunc[current.getId()].get(i).id, goalsId), current);
				Fringe.add(newCity);
			}
		}
		return reversepathFoundFound(Goal, pathFound);
	}

	//									A_Star Search
	//					We used a Priority Queue to checking the cities
	//				that sorts the elements automatically after each change
	//	by their evaluation function to take the least F(n) = G(n) + H(n) in the queue
	//There is an extra IF Condition that we used when we searched Optimal for all goals
	public static ArrayList<City> AStar(int startId, ArrayList<Integer> goalsId) {
		clearOldData(resetAll);
		PriorityQueue<City> Fringe = new PriorityQueue<>();
		City startCity = new City(startId, 0, minHeurestic(startId, goalsId), null);
		Fringe.add(startCity);
		while (Fringe.size() > 0) {
			City current = Fringe.poll();
			if (resetAll)
				orderOfExpansion.add(current);
			else
				resetAll = true;
			if (goalsId.contains(current.getId())) {
				Goal = current;
				break;
			}
			for (int i = 0; i < Main.CostFunc[current.getId()].size(); i++) {
				City newCity = new City(Main.CostFunc[current.getId()].get(i).id,
						current.getCostToReach() + Main.CostFunc[current.getId()].get(i).driveRoute,
						current.getCostToReach() + Main.CostFunc[current.getId()].get(i).driveRoute
								+ minHeurestic(Main.CostFunc[current.getId()].get(i).id, goalsId),
						current);
				Fringe.add(newCity);
			}
		}
		return reversepathFoundFound(Goal, pathFound);
	}

	//							Uniform Cost Search
	//				We used a Priority Queue to checking the cities
	//			that sorts the elements automatically after each change
	//	by their evaluation function to take the least F(n) = G(n) in the queue
	public static ArrayList<City> uniformCost(int startId, ArrayList<Integer> goalsId) {
		clearOldData(resetAll);
		PriorityQueue<City> Fringe = new PriorityQueue<>();
		City startCity = new City(startId, 0, 0, null);
		Fringe.add(startCity);
		while (Fringe.size() > 0) {
			City current = Fringe.poll();
			orderOfExpansion.add(current);
			if (goalsId.contains(current.getId())) {
				Goal = current;
				break;
			}
			for (int i = 0; i < Main.CostFunc[current.getId()].size(); i++) {
				City newCity = new City(Main.CostFunc[current.getId()].get(i).id,
						current.getCostToReach() + Main.CostFunc[current.getId()].get(i).driveRoute,
						current.getCostToReach() + Main.CostFunc[current.getId()].get(i).driveRoute, current);
				Fringe.add(newCity);
			}
		}
		return reversepathFoundFound(Goal, pathFound);
	}

	//							Optimal 1 For All Goals Search
	//	We used A_Star Algorithm to find the path of the nearest goal then we add the path
	//to the whole path and we called A_Star method again to search for the second goal when 
	//the start point is the first goal found, we repeat these steps until we found all goals
	public static ArrayList<City> Optimal1AllGoals(int startId, ArrayList<Integer> goalsId) {
		clearOldData(resetAll);
		ArrayList<City> overallpathFound = new ArrayList<City>();
		Queue<City> temp = new LinkedList<>();
		ArrayList<Integer> goalsIdTemp = new ArrayList<Integer>();
		goalsIdTemp.addAll(goalsId);
		int currentId = startId;
		int currentCost = 0;
		while (goalsIdTemp.size() != 0) {
			temp.clear();
			resetAll = false;
			temp.addAll(AStar(currentId, goalsIdTemp));
			if (!overallpathFound.isEmpty()) {
				temp.remove();
				currentCost = overallpathFound.get(overallpathFound.size() - 1).getCostToReach();
			}
			while (!temp.isEmpty()) {
				temp.peek().setCostToReach(temp.peek().getCostToReach() + currentCost);
				temp.peek().setevaluationFunc(temp.peek().getevaluationFunc() + currentCost);
				currentId = temp.peek().getId();
				overallpathFound.add(temp.remove());
			}
			goalsIdTemp.remove(goalsIdTemp.indexOf(currentId));
		}
		return overallpathFound;
	}

	//				method to reverse the path that we found
	//		to make the first city in the arraylist is the start point
	static ArrayList<City> reversepathFoundFound(City Goal, ArrayList<City> pathFound) {
		while (Goal != null) {
			reversePathFound.push(Goal);
			Goal = Goal.nextTo;
		}
		while (reversePathFound.size() > 0)
			pathFound.add(reversePathFound.pop());
		return pathFound;
	}

	//				method returns the minimum heurestic value 
	//				  for the nearest goal from chosen city
	static int minHeurestic(int cityId, ArrayList<Integer> goalsId) {
		int min = Main.HeuresticFunc[goalsId.get(0)].get(cityId);
		for (int i = 1; i < goalsId.size(); i++)
			if (Main.HeuresticFunc[goalsId.get(i)].get(cityId) < min)
				min = Main.HeuresticFunc[goalsId.get(i)].get(cityId);
		return min;
	}

	static Boolean resetAll = true;

	static void clearOldData(Boolean resetAll) {
		if (resetAll)
			orderOfExpansion.clear();
		pathFound.clear();
		Goal = null;
	}
}
