import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import Tree1.TreeNode;

public class Tree {

	public static class Node{
		Node left;
		Node right;
		Node parent;
		int data;

		public Node(int data){
			this.data = data;
		}
	}

	public static Node createBST(int[] arr){
		//without parents
		//return createBST(arr,0,arr.length-1);

		//with parents
		return createBSTParents(arr,0,arr.length-1, null);
	}
	public static Node createBST(int[] arr, int start,int end){

		if(start > end){
			return null;
		}

		int mid =  (start + end)/2;

		Node root = new Node(arr[mid]);

		root.left = createBST(arr, start, mid-1);
		root.right = createBST(arr, mid+1, end);

		return root;
	}

	public static Node createBSTParents(int[] arr, int start,int end ,Node parent){

		if(start > end){
			return null;
		}
		
		int mid = (start + end)/2;
		
		Node root = new Node(arr[mid]);
		
		root.parent = parent;
		
		root.left = createBSTParents(arr,start,mid-1,root);
		root.right = createBSTParents(arr,mid+1,end,root);
		
		return root;

	}

	public static void inOrder(Node root){
		if(root == null){
			return;
		}
		inOrder(root.left);
		System.out.println(root.data);


		inOrder(root.right);
	}

	public static Node success(Node root){
		if ( root == null ) return null;

		//there is right child so we will take is left most child
		if(root.right != null){
			return leftMostChild(root.right);
		}else{

			Node child = root;
			Node parent = root.parent;

			while(parent != null && parent.left != child){
				child = parent;
				parent = parent.parent;
			}
			return parent;
		}

	}

	private static Node leftMostChild(Node root) {
		if (root == null) return null;

		while(root.left != null){
			root = root.left;
		}
		return root;

	}

	public static Node lcaBST(Node root, int v1, int v2){

		if((root.data >= v1 && root.data <= v2)||(root.data <= v1 && root.data >= v2)){
			return root;
		}

		if(root.data > v1){
			return lcaBST(root.left,v1,v2);
		}else{
			return lcaBST(root.right,v1,v2);
		}
	}

	public static Node lcaBinaryNotOrdered(Node root, int v1, int v2){
		if(!cover(root,v1) || !cover(root,v2)){
			return null;
		}
		return lcaNotOrderedHelper(root,v1,v2);
	}
	private static Node lcaNotOrderedHelper(Node root, int v1, int v2) {
		if(root == null) return null;
		if(root.data == v1 || root.data == v2) return root;

		boolean v1Left = cover(root.left,v1);
		boolean v2Left = cover(root.left,v2);

		if(v1Left != v2Left) {
			return root;
		}else{
			Node child = v1Left ? root.left : root.right;
			return lcaNotOrderedHelper(child, v1,v2);

		}
	}
	//check if value in tree
	private static boolean cover(Node root, int v1) {

		if(root == null) return false;
		if(root.data == v1) return true;

		return cover(root.left,v1)||cover(root.right,v1);
	}

	public static int countPathsWithSum(Node root, int target){

		if(root == null) return 0;

		int pathFromRoot = countPathsFromNode(root,target,0);

		int pathLeft = countPathsWithSum(root.left,target);
		int pathRight = countPathsWithSum(root.right,target);

		return pathFromRoot + pathLeft + pathRight;


	}

	public static int countPathsFromNode( Node root, int targetSum, int currentSum){

		if(root == null) return 0;

		currentSum = currentSum + root.data;

		int totalPaths = 0;

		if (currentSum == targetSum){
			totalPaths++;
		}
		totalPaths = totalPaths + countPathsFromNode(root.left, targetSum, currentSum);
		totalPaths = totalPaths + countPathsFromNode(root.right, targetSum, currentSum);

		return totalPaths;
	}

	public static boolean isRoute(Node root, Node start, Node end){
		return isRoute(start,end, new HashSet<>());
	}

	public static boolean isRoute(Node start, Node end, Set<Node> visited){
		if(start.data == end.data) return true;

		visited.add(start);

		for(Node temp: getAdj(start)){
			if(!visited.contains(temp)){
				visited.add(temp);
				isRoute(temp,end,visited);
			}
		}
		return false;
	}


	public static void createDepthList(Node root,ArrayList<LinkedList<Node>> lists, int level){
		if (root == null) return;

		LinkedList<Node> list = null;

		if(lists.size() == level){
			list = new LinkedList<Node>();
			lists.add(list);
		}else{
			list = lists.get(level);
		}
		list.add(root);
		createDepthList(root.left,lists,level + 1);
		createDepthList(root.right, lists, level + 1);
	}



	public static ArrayList<LinkedList<Node>> createDepthList(Node root){
		ArrayList<LinkedList<Node>> lists = new ArrayList<LinkedList<Node>>();
		createDepthList(root,lists,0);

		return lists;
	}


	public static ArrayList<LinkedList<Node>> createDepthList1(Node root){
		ArrayList<LinkedList<Node>> lists = new ArrayList<LinkedList<Node>>();
		LinkedList<Node> current = new LinkedList<>();
		if (root != null){
			current.add(root);

		}
		while(current.size() > 0){
			lists.add(current);
			LinkedList<Node> parents = current;
			current = new LinkedList<Node>();

			for(Node parent: parents){
				if(parent.left != null) current.add(parent.left);
				if(parent.right != null) current.add(parent.right);
			}
		}
		return lists;

	}
	public static List<Node> getAdj(Node n){
		List<Node> adj = new LinkedList<>();
		if(n.left != null) adj.add(n.left);
		if(n.right != null) adj.add(n.right);

		return adj;
	}

	public ArrayList<ArrayList<Integer>> pathSum(Node root, int sum) {

		ArrayList<ArrayList<Integer>> result =  new ArrayList<ArrayList<Integer>>();

		ArrayList<Integer> list =  new ArrayList<Integer>();
		
		
		pathSum1(root,result,list,0,sum);
		return result;
	}

	public void pathSum1(Node root, ArrayList<ArrayList<Integer>> lists ,ArrayList<Integer> list,int current, int sum) {

		
		if (root == null) return;
		
		list.add(root.data);
		
		if(root.left == null && root.right == null){
			current += root.data;
			if(current == sum){
				lists.add(list);
			}
			list.remove(list.size()-1);
			return;
		}
		
		current = current + root.data;
		pathSum1(root.left,lists,list,current,sum);
		pathSum1(root.right,lists,list,current,sum);
	}











		public static void main(String[] args) {

			int[] arr = {1,3,9,5,4,7,8,10,11,13,15,16,22,14,33,31};

			Arrays.sort(arr);

			Node root = createBST(arr);

			ArrayList<LinkedList<Node>> ans = createDepthList1(root); 

			for(LinkedList<Node> list : ans){
				System.out.println("first  : " + list.size());
				for ( Node n : list){
					System.out.println(n.data);
				}
			}




			System.out.println("root " + root.data);

			inOrder(root);

			Node successor = success(root.left.right);

			Node lca = lcaBinaryNotOrdered(root,3,4);

			int numPaths = countPathsWithSum(root, 9);

			System.out.println("num of paths : " + numPaths);
			System.out.println("lca " + lca.data);


			Node v1 = new Node(4);
			Node v2 = new Node(5);

		}

	}
