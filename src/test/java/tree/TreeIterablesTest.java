package tree;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class TreeIterablesTest {
	TreeNode<String> treeNode = createTreeNode();

	@Test
	public void testToParent() {
		assertIterable(TreeIterables.toParent(treeNode), "root");
		assertIterable(TreeIterables.toParent(treeNode.getSubTreeAt(0)), "sub1", "root");
		assertIterable(TreeIterables.toParent(treeNode.getSubTreeAt(1).getSubTreeAt(0)), "sub2-1","sub2","root");
	}
	
	@Test
	public void testBreadthFirst() {
		assertIterable(TreeIterables.breadthFirst(treeNode.getSubTreeAt(2)), "sub3");
		assertIterable(TreeIterables.breadthFirst(treeNode.getSubTreeAt(1)), "sub2", "sub2-1", "sub2-2","sub2-2-1", "sub2-2-2");
		assertIterable(TreeIterables.breadthFirst(treeNode), "root","sub1","sub2","sub3","sub1-1","sub1-2","sub1-3","sub2-1","sub2-2","sub1-1-1","sub1-1-2","sub1-2-1","sub2-2-1","sub2-2-2");
	}
	
	@Test
	public void testDeepFirst() {
		assertIterable(TreeIterables.depthFirst(treeNode.getSubTreeAt(2)), "sub3");
		assertIterable(TreeIterables.depthFirst(treeNode.getSubTreeAt(1)), "sub2", "sub2-1", "sub2-2", "sub2-2-1","sub2-2-2");
		assertIterable(TreeIterables.depthFirst(treeNode), "root","sub1","sub1-1","sub1-1-1","sub1-1-2","sub1-2","sub1-2-1","sub1-3","sub2","sub2-1","sub2-2","sub2-2-1","sub2-2-2","sub3");
	}
	
	
	private <T> void assertIterable(Iterable<TreeNode<T>> iterable, @SuppressWarnings("unchecked") T...contents) {
		List<T> list = Arrays.asList(contents);
		Iterator<TreeNode<T>> iterator = iterable.iterator();
		
		int index = 0;
		while (iterator.hasNext()) {
			T expected = iterator.next().get();
			T actual = list.get(index++);
			assertEquals(expected, actual);
		}
		
	}
	
	/** 
	 * create TreeNode
	 * 
	 * root
	 * 	 sub1
	 * 	  sub1-1
	 *	   sub1-1-1
	 *	   sub1-1-2
	 *	  sub1-2
	 *	   sub1-2-1
	 *	  sub1-3
	 *	 sub2
	 *	  sub2-1
	 *	  sub2-2
	 *	   sub2-2-1
	 *	   sub2-2-2
	 *	 sub3
	 * @return
	 */
	private TreeNode<String> createTreeNode() {
		TreeNode<String> rootNode = new TreeNode<String>("root");
		TreeNode<String> sub1Node = new TreeNode<String>("sub1");
		TreeNode<String> sub1_1Node = new TreeNode<String>("sub1-1");
		sub1_1Node.addChild("sub1-1-1");
		sub1_1Node.addChild("sub1-1-2");
		TreeNode<String> sub1_2Node = new TreeNode<String>("sub1-2");
		sub1_2Node.addChild("sub1-2-1");
		sub1Node.addSubTree(sub1_1Node);
		sub1Node.addSubTree(sub1_2Node);
		sub1Node.addChild("sub1-3");
		TreeNode<String> sub2Node = new TreeNode<String>("sub2");
		sub2Node.addChild("sub2-1");
		TreeNode<String> sub2_2Node = new TreeNode<String>("sub2-2");
		sub2_2Node.addChild("sub2-2-1");
		sub2_2Node.addChild("sub2-2-2");
		sub2Node.addSubTree(sub2_2Node);
		TreeNode<String> sub3Node = new TreeNode<String>("sub3");
		rootNode.addSubTree(sub1Node);
		rootNode.addSubTree(sub2Node);
		rootNode.addSubTree(sub3Node);
		
		System.out.println(this.getClass() + " create TreeNode<String> below :");
		System.out.println(rootNode);
		
		return rootNode;
	}

}
