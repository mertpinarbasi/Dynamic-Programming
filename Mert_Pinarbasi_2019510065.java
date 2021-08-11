import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Mert_Pinarbasi_2019510065 {

	public static int DP(LCRS.Node root, Map<LCRS.Node, Integer> table, ArrayList<LCRS.Node> selectedTableDP) {

		// Dynamic programming is about make a smart recursion algorithm by using
		// a table to store previous actions to prevent make unnecessary recursions.
		// By using a table , there is no need to calculate repeated sup-problem.

		// Since our problem can be splitted into sub-problems , divide-and-conquer
		// tecnhique was very suitable.Also, the problem is an optimization problem.
		// Hence, we can split the problem into parts to find the optimal results.

		// I have used "maximum independent set" problem's template to solve DP
		// approach.
		// MIS(maximum independent set) is related to graph data structure.
		// Since tree DS is an non-cyclic graph I have decided to use it.
		// I have implemented MIS problem to Left-Child Right-Siblings tree
		// to solve the problem.Also I have transformed MIS to "maximum independent
		// weighted set"
		// by changing "include +1" to " include ability" .

		// Also , at the end of the decision operations ,there is a if control statement
		// to update selectedNode list to store selected nodes in order to print them .

		// The details of implementation and MIS is covered on the progress report
		// document.

		// If root is null it means that tree is null , so the code will be stopped.
		if (root == null) {
			return 0;
		}

		// Since the algorithm has a recursive pattern ,
		// when the new recursion is started the node will be checked if it's inside the
		// table ?
		// If the node inside the table there is no need to calculate the node's score
		// again.
		// Hence , we can prevent from repetitive recursion instantly.
		if (table.get(root) != null) {
			return table.get(root);
		}
		// Exclude variable is used to store exclude option in case of the current node
		// is not selected.
		// If current node is not selected , the gain of this node's children's ability
		// will be calculcated by using DP and stored
		// stored in the exclude variable.
		int exclude = 0;
		LCRS.Node tmp = root;
		LCRS.Node child = root.child;
		// If current node is not selected then we need to calculate the gain of
		// children's total ability
		if (child != null) {
			while (child != null) {
				// children's total ability is calculated by using DP for each node and their
				// return will be stored
				// in the exclude variable as mentioned above.
				exclude = DP(child, table, selectedTableDP) + exclude;
				child = child.next;
			}
		}
		// In the second phase of the algorithm,
		// include option will be calculated to make best decision.
		// If the current node is included, current node's data will be added to include
		// variable firstly.

		// Since , we can not pick the children of the node in case of select current
		// node , the gain of the grandchildren's will be calculated by using DP
		// recursion.
		// The final result will be stored in the include variable after calculation is
		// completed to make a decision between include and exclude option.

		child = tmp.child;
		int include = root.data;
		if (root.child != null) {
			while (child != null) {
				LCRS.Node grandchild = child.child;
				// As mentioned above , after "the root.data" added the include variable,
				// the gain of the grandchildren's total ability will be stored in the include
				// variable.
				// by using while loop below.
				while (grandchild != null) {

					include = DP(grandchild, table, selectedTableDP) + include;
					grandchild = grandchild.next;

				}
				child = child.next;
			}
		}
		// The control statment below is unrelated to DP calculation.
		// It's used for only update the selectedNode list to store them in a list.
		// Hence, the selected node list will be updated after each iteration.

		// If "include">"exclude" it means that
		// a new node is selected and the selected table needs to be updated .
		// The new's node's children will be deleted if they are inside the table.
		// And new node's grand children will be added , then grandchildren's child will
		// be removed
		// to guarantee that table is showing correct.
		if (include > exclude) {
			selectedTableDP.add(root);
			child = root.child;
			while (child != null) {
				if (selectedTableDP.contains(child)) {
					selectedTableDP.remove(child);
					LCRS.Node grandchild = child.child;
					while (grandchild != null) {
						if (!selectedTableDP.contains(grandchild)) {
							selectedTableDP.add(grandchild);
							LCRS.Node grandschild = grandchild.child;
							while (grandschild != null) {
								if (selectedTableDP.contains(grandschild)) {
									selectedTableDP.remove(grandschild);

								}
								grandschild = grandschild.next;
							}
						}
						grandchild = grandchild.next;
					}
				}

				child = child.next;

			}
		}
		// "include" and "exclude" option will be compared to make best decision.
		// The biggest one will be stored the table , to use it in the possible future
		// recursions.
		table.put(root, Integer.max(exclude, include));
		return table.get(root);
	}

	public static int Greedy(LCRS.Node root, ArrayList<LCRS.Node> selectedTableGreedy) {

		// banned arrayList is created for banned children of the node which is elected
		// Hence , these kind of children whill not be added to selectedTableGreedy
		ArrayList<LCRS.Node> banned = new ArrayList<LCRS.Node>();
		// list is used for traverse tree easily for greedy method .
		ArrayList<LCRS.Node> list = new ArrayList<LCRS.Node>();
		// temp is used for store the current node's children nodes to make decision
		// after .
		ArrayList<LCRS.Node> temp = new ArrayList<LCRS.Node>();
		// LCRS tree will copy to list by using listGreedy.
		LCRS.listGreedy(list, root);
		// total variable is used to store total ability score.
		int total = 0;

		// All nodes of the tree will be traversed by using for-each loop
		// Since greedy method is focused to make best choice for the current moment,
		// Every node will be compared by own ability with it's children total ability
		// to make a decision
		// If node's own ability is bigger than the children the node should be added to
		// selectedList if only
		// it's not in the banned list.
		// That operation will be applied all nodes of the tree to maximize result.
		// However, if the node inside the banned list , it will not be checked
		// by using the if structure at the beginning.
		// Banned means that it's parent node is selected so it can not be selected to
		// list.
		// Morevover, there is a control statement for the root node.
		// Because root node might be skipped at the first iteration.
		// Hence there is a control statement for the root node to maximize ability
		// score.

		// The LCRS tree's all nodes will be traversed by using for-each loop.
		for (LCRS.Node node : list) {
			LCRS.Node child = node.child;
			// exclude variable to store , current node's children's total ability.
			int exclude = 0;
			// Before to make a decision , every node is going to be checked if it's banned
			// if it's not banned , that means that it's parent is not selected
			// so , it's safe to select that node.
			if (!banned.contains(node)) {
				temp.clear();
				while (child != null) {
					// Current node's children's total ability will be stored in exclude variable
					// Moreover, children will be stored in temp array to banned in case of select
					// current node.
					if (!banned.contains(child)) {
						temp.add(child);
						exclude += child.data;
					}

					child = child.next;

				}
				// If node's own data is bigger than the exclude , we should select that node to
				// selectedTable
				// Since we can not pick the node's children anymore , all children will be
				// added banned list.
				if (node.data >= exclude) {
					total += node.data;
					banned.addAll(temp);
					selectedTableGreedy.add(node);

				}

			}

		}
		// After the all tree traversed and total ability calculation is completed ,
		// I have added a control statement for "root" node only to maximize greedy
		// results.
		// Sometimes , at the beginning of the algorithm root and their children might
		// not be selected
		// which is cause downgrade for ability score.
		// To prevent that, the root node will be checked after all procesess are
		// completed.
		// Hence, if root and their children is not selected root node will be added to
		// selected list
		// to maximize greedy results.
		if (!selectedTableGreedy.contains(root)) {
			LCRS.Node rootChild = root.child;
			while (rootChild != null) {
				if (selectedTableGreedy.contains(rootChild)) {
					break;
				}
				rootChild = rootChild.next;
			}
			selectedTableGreedy.add(root);
			total += root.data;
		}

		return total;

	}

	// Basic for-each method to print Left-Child Right-Siblings tree to check the
	// file operations is completed
	// successfully.
	// printTable can be used to seek LCRS tree before to check is tree is the
	// correct form .
	public static void printTable(Map<LCRS.Node, Integer> dpTable) {
		for (Entry<LCRS.Node, Integer> entry : dpTable.entrySet()) {
			LCRS.Node key = entry.getKey();
			Integer value = entry.getValue();

			System.out.println(key.name + ":" + value);
		}
	}

	// printSelectedNodeList is used for print arrayLists for selectedNodes both for
	// DP and Greedy algorithm operations.
	public static void printSelectedNodeList(ArrayList<LCRS.Node> selectedTable) {
		for (LCRS.Node node : selectedTable) {
			System.out.println(node.name + ":" + node.data);
		}
	}

	// Left-Child Right-Siblings tree "LCRS" is used as a main data structure
	// to store data after file operations.
	public static class LCRS {
		// Node class is the vertex class for the tree.
		public static class Node {

			int data; // to store ability values.
			String name; // to store lions names
			Node next, child; // next is used for siblings child is used for left-child.

			public Node(String name, int data) {
				this.name = name;
				this.data = data;
				next = child = null;
			}

			// toString method is modified to print data.s
			@Override
			public String toString() {
				return "" + name + ":" + data + "\n";
			}

		}

		// A sibling will be added at the latest siblings for the node by traversing
		// while loop.
		static public Node addSibling(Node node, String name, int data) {
			if (node == null)
				return null;
			while (node.next != null)
				node = node.next;
			return (node.next = new Node(name, data));
		}

		// A child will be added at the given node.
		static public Node addChild(Node node, String name, int data) {
			if (node == null)
				return null;

			// Check if child is not empty.
			if (node.child != null)
				return (addSibling(node.child, name, data));
			else
				return (node.child = new Node(name, data));
		}

		// Tree will be traversed in depth-first search by using recursion to print
		// elements on tree.
		public static void traverseTree(Node root) {

			if (root == null)
				return;
			while (root != null) {
				System.out.println("\"" + root.name + ":" + root.data + "\"");
				// By using if structure , traverse operation will start at the latest child
				// to ensure that traverse operation is in DFS form.
				if (root.child != null) {

					traverseTree(root.child);

				}

				root = root.next;
			}

		}
		// listGreedy method is used for traverse tree in the Greedy algorithm
		// The tree will be transferred into Greedy method by using this method.

		public static void listGreedy(ArrayList<LCRS.Node> list, LCRS.Node root) {

			if (root == null)
				return;
			while (root != null) {
				list.add(root);
				if (root.child != null) {

					listGreedy(list, root.child);

				}

				root = root.next;
			}

		}

		// findNode method is used at the file operations while creating the LCRS tree
		// By using findNode method,the node which is parent node in the hierachy table
		// is found to add child or sibling.
		// DFS search will be applied to find a node in the tree .
		static public Node findNode(Node root, String parentName) {
			Node search = null;

			if (root == null)
				return null;

			while (root != null) {
				// the node will be checkhed is it the wanted one for every iteration.
				if (root.name.equalsIgnoreCase(parentName)) {

					search = root;

				}
				// else the algorithm will continue in DFS form , to find the given pareant.
				if (root.child != null) {
					if (search != null) {
						break;
					}
					search = findNode(root.child, parentName);

				}
				// If the search is found the will loop will be broken to return search
				// instantly.
				if (search != null)
					break;
				root = root.next;
			}
			return search;

		}
	}

	public static void main(String[] args) {

		File hunting_abilities = new File("hunting_abilities.txt");
		File lions_hierarchy = new File("lions_hierarchy.txt");

		Scanner scanHunt;
		Scanner scanLions;
		// try-catch structure to prevent file operation errors.
		try {
			scanHunt = new Scanner(hunting_abilities);
			scanLions = new Scanner(lions_hierarchy);

			LCRS.Node root = null;
			// dpTable variable to store left-child right-sibling tree
			Map<LCRS.Node, Integer> dpTable = new HashMap<LCRS.Node, Integer>();
			// abilityTable is created for store name:ability table after file reading
			// operation.
			Hashtable<String, Integer> abilityTable = new Hashtable<String, Integer>();
			// dpTable variable to store selected nodes in DP approach.
			ArrayList<LCRS.Node> selectedTableDP = new ArrayList<LCRS.Node>();
			// selectedTableGreedy variable to store selected nodes in DP approach.
			ArrayList<LCRS.Node> selectedTableGreedy = new ArrayList<LCRS.Node>();

			int scanHuntCounter = 0;
			// scanHunt is used file reading operation for the ability.txt
			// The nodes will be created and stored in a hashtable with names and abilities.
			// First file reading operation is passed because they were include tags not
			// name and ability.
			while (scanHunt.hasNext()) {
				String word = scanHunt.nextLine();
				String[] splitWord = word.split("	");
				if (scanHuntCounter != 0)
					abilityTable.put(splitWord[0], Integer.valueOf(splitWord[1]));

				scanHuntCounter++;
			}

			int scanLionsCounter = 0;
			// Tree will be created while hieararchy table is reading by using while loop
			// below.
			while (scanLions.hasNext()) {
				String word = scanLions.nextLine();
				String[] splitWord = word.split("	");
				String firstNode = splitWord[0];
				String secondNode = splitWord[1];
				String relationship = splitWord[2].toLowerCase();
				// First line will not be readed since it contains tags.
				if (scanLionsCounter >= 1)

				{
					String parentNode = firstNode;
					String subNode = secondNode;
					int abilityVal = abilityTable.get(subNode);
					// Root will be declared in the first line.
					if (scanLionsCounter == 1) {

						root = new LCRS.Node(parentNode, abilityTable.get(parentNode));
						LCRS.addChild(LCRS.findNode(root, parentNode), subNode, abilityVal);

					} else {
						// File reading operations will be continue and tree will be created.
						// By using findNode method , the node who is written in the file will be found
						// by traversing tree

						if (relationship.equalsIgnoreCase("left-child")) {
							LCRS.addChild(LCRS.findNode(root, parentNode), subNode, abilityVal);
						} else if (relationship.equalsIgnoreCase("right-sibling")) {
							LCRS.addSibling(LCRS.findNode(root, parentNode), subNode, abilityVal);
						}
					}
				}
				// Counter for counting lines to file management.
				scanLionsCounter++;
			}

			scanLions.close();
			scanHunt.close();

			// The LCRS tree can be seen by using LCRS.traverseTree(root) method before the
			// DP and Greedy operations.

			// LCRS.traverseTree(root);

			printTable(dpTable);

			// DP method will print the maximum number of the optimal ability.
			System.out.println("\nMaximum ability DP:" + DP(root, dpTable, selectedTableDP) + "\n");
			System.out.println("Selected Nodes:");
			System.out.println("-------------------------------------------");
			// printSelectedNodeList for used to print selected nodes for the DP method.
			printSelectedNodeList(selectedTableDP);
			System.out.println("-------------------------------------------");
			System.out.println("The total length of the selected nodes:" + selectedTableDP.size());
			System.out.println("-------------------------------------------");
			System.out.println("///////////////////////////////////////////");
			System.out.println("-------------------------------------------");
			// Greedy method will print the maximum number of optimal abilitiy.
			System.out.println("Maximum ability Greedy: " + Greedy(root, selectedTableGreedy) + "\n");
			System.out.println("Selected Nodes:");
			System.out.println("-------------------------------------------");
			// printSelectedNodeList for used to print selected nodes for the Greedy method.
			printSelectedNodeList(selectedTableGreedy);
			System.out.println("-------------------------------------------");
			System.out.println("The total length of the selected nodes:" + selectedTableGreedy.size());
		}
		// An error will be given in case of file exception error.
		catch (FileNotFoundException e) {
			System.out.println("File  not found.\nPlease check the file path");
			System.out.println(e.getMessage());
			e.printStackTrace();

		}
	}

}
