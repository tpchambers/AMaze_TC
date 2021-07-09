package edu.wm.cs.cs301.amazebytheochambers.generation;
import java.util.ArrayList;

/**				--	Main Psuedocode for Kruskal's Algorithm --
 * First, we think of the maze as a group of disjoint spanning trees. 
 *At first, each maze cell will be its own tree.
 *To begin the algorithm we randomly select a position on the maze.
 *This position selected is the edge, or current wall of the floorplan between the current tree and its neighbor tree.
 *Once we have the current position, we check to see if it is a possible candidate to break down.
 * It will be a possible candidate if the current wall can be torn down, and if the current wall is not on the boarder.
 * If the wall is a possible candidate, and the two trees (single cells/trees at first) are different, then we proceed with the algorithm.
 * We then union the sets of these two disjoint trees, such that one tree contains the other, so the cells are in the same set.
 * We continue to repeat this until all of the maze trees are under one tree and the algorithm finishes when there are no wall potential edges left to select from.
 */
public class MazeBuilderKruskal extends MazeBuilder implements Runnable {
	public int tree_x; /** x location of tree */
	public int neighbortree_x;/**x location of neighbor tree */
	public  int tree_y; /**y location of tree */
	public int neighbortree_y; /** y location of neighbor tree */
	public Tree[][] spanning_tree; /** tree data structure representing overall spanning tree */

//we are using the Kruskal Algorithm
	public MazeBuilderKruskal() {
		super();
		System.out.println("MazeBuilderKruskal uses Kruskal's algorithm to generate maze.");
	}
	/**Here is a simple data structure to represent a tree, which can contain an integer data type.
	 * We use this tree to design the overall data structure representing the maze.
	 */
	final class Tree {
		private int cell_data;
		//default
		Tree () {
		}
		/**We can access the cell's data point, in this case an integer
		 *
		 * @param cell_data
		 */
		Tree (int cell_data) {
			this.cell_data= cell_data;
		}
		/**Call to access data point of tree.
		 *
		 * @return
		 */
		public int get_data() {
			return this.cell_data;
		}
		/**
		 * We check if the current tree contains the other tree.
		 * @param cur_tree
		 * @return
		 */
		public boolean contains_tree (Tree cur_tree) {
			if (this == cur_tree) {
				return true;
			} else {return false;}
		}
	}

	/**We begin by declaring an ArrayList containing data of Wallboard type.
	 * The wallboard type is equivalent to a graph edge.
	 *We must iterate over every single possible position in the current floorplan.
	 * For Prim's algorithm, it is fine to add a new edge to the array list after every neighbor.
	 * For Kruskal's algorithm,however, we must create multiple spanning trees at once from random edges.
	 * To do this, we have to make sure our initial list of edges contains all possible edges of the floorplan.
	 * After the arraylist is populated, we call construct_tree, to create our data structure representing a large spanning tree.
	 * Inside the spanning tree, we create a tree with a unique number assigned to it.
	 * Thus, each tree inside the spanning tree is belonging to its own set.
	 * Calling spanning_tree[0][0], will return a tree in the corresponding position of the floorplan, and calling tree.get_data() will return its unique number.
	 * We then create integer variables to hold the current positions returned by the randomly chosen edge.
	 * This will allow us to start a tree randomly through each iteration, until all trees are under the same set.
	 * We call check_possible to make sure that the neighboring trees are within the boundaries of the floorplan.
	 * If so, we proceed into the main loop, where we check if the neighbor tree equals the current tree.
	 * If not already in the same set, we create two new trees to represent the current and neighbor trees at these positions.
	 * We then iterate over the entire array of possible floorplan positions, and if at any of these values in our corresponding spanning tree are equal to the current tree,we update the spanning tree's current tree to equal the neighbor tree.
	 * Thus, the current_tree and neighbor tree are now in the same tree set
	 * We then get the neighbor of the current edge, and mark this tree position on the floorplan as visited, repeating until we have no more candidates left.
	 */
	protected void generatePathways() {
		final ArrayList<Wallboard> edges = new ArrayList<Wallboard>();
		for (int w = 0; w < this.width; w++) { // same function as used as prims, but we want to generate all of the possible edges we can add to the array list
			for (int h = 0; h< this.height; h++) {
				for (CardinalDirection cd : CardinalDirection.values()) { //checking possible direction values
					Wallboard wallboard = new Wallboard(w, h, cd);
					if (floorplan.canTearDown(wallboard)) {// 
						edges.add(wallboard);
					}
				}
			}
		}

		Tree[][] spanning_tree = construct_tree();
		//repeat until edges are empty and our sets are in a single set 
		do  {
			Wallboard cur_edge= extractWallboardFrompotential_wallboardsetRandomly(edges);
			int tree_x = cur_edge.getX(); //x location of tree 
			int neighbortree_x = cur_edge.getNeighborX(); // x location of neighbor tree
			int tree_y = cur_edge.getY(); // y location of tree
			int neighbortree_y= cur_edge.getNeighborY(); // y location of neighbor tree
			// if possible & current tree does not contain neighbor tree
			if(check_possible(neighbortree_x,neighbortree_y) && !(spanning_tree[tree_x][tree_y].contains_tree(spanning_tree[neighbortree_x][neighbortree_y]))) {
				floorplan.deleteWallboard(cur_edge); //delete current edge
				Tree current_tree = spanning_tree[tree_x][tree_y]; //initialize new current_tree from tree locations above
				Tree neighbor_tree = spanning_tree[neighbortree_x][neighbortree_y]; //same thing
				//loop over entire spanning tree and check where the spanning tree contains the current tree
				for (int w =0;w<this.width;w++) {
					for (int h=0;h<this.height;h++) {
						if (spanning_tree[w][h].contains_tree(current_tree)) {
							spanning_tree[w][h] = neighbor_tree; //need to update exact location of current tree to neighbor tree, very important
							//update x and y positions to neighbors and mark as visited
							int x = cur_edge.getNeighborX();
							int y = cur_edge.getNeighborY();
							floorplan.setCellAsVisited(x, y); //mark as visited
						}
					}
				}
			}

		} while (!edges.isEmpty());
		//use to check if all data points were updated to be equal
		//for (int i=0; i< width; i++) {
		//	for (int j=0;j<height;j++) {
		//	System.out.println(spanning_tree[i][j].get_data());
		//}
		//}
	}
	/**Constructing tree of data type tree.
	 * Made so each tree is represented as a tree containing a unique identifier .
	 * If we call spanning_tree[0][0], we retrieve a tree with the data identifier 0 at this location.
	 * Identifier created in increasing order.
	 */
	private Tree[][] construct_tree () {
		Tree [][]spanning_tree = new Tree[this.width][this.height];
		int k = 0;
		for (int w=0; w<this.width;w++) {
			for (int h=0;h<this.height;h++) {
				spanning_tree[w][h] = new Tree(k);
				k ++;
			}
		}
		return spanning_tree;
	}

	/**Simple function ensuring that we are in bounds.		 * 
	 */
	private boolean check_possible(int x, int y) {
		if (x>=0 && x < this.width && y >= 0 && y < this.height) { // can't be at entrance or out of bounds
			return true;
		}
		else {return false;}
	}
	/**
	 * From Prim Algorithm, we simply remove a random candidate from the current Wallboard list */
	private Wallboard extractWallboardFrompotential_wallboardsetRandomly(final ArrayList<Wallboard> edges) {
		return edges.remove(random.nextIntWithinInterval(0, edges.size()-1));
	}
}






