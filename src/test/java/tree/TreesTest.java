package tree;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import tree.Trees.SearchOption;

public class TreesTest {
	TreeNode<String> treeNode = createTreeNode();

	
	@Test
	public void contents() throws Exception {
		assertEquals(Trees.contents(Arrays.asList(treeNode)), Arrays.asList("root"));
		assertEquals(Trees.contents(treeNode.getSubTrees()), Arrays.asList("sub1", "sub2", "sub3"));
	}
	
	
	@Test
	public void search() throws Exception {
		TreeNode<String> sub1_1 = Trees.searchFirst(treeNode, "sub1-1");
		assertEquals(sub1_1.get(), "sub1-1");
		assertEquals(sub1_1.getChildren().size(), 2);
		
		TreeNode<String> root = Trees.searchFirst(treeNode, "root");
		assertEquals(treeNode, root);
		
		TreeNode<String> sub2_1 = Trees.searchFirst(treeNode, SearchOption.depthFirst(), o->o.get().equals("sub2-1"));
		assertEquals(sub2_1.get(),"sub2-1");

		TreeNode<String> sub2_5 = Trees.searchFirst(treeNode, SearchOption.depthFirst(), o->o.get().equals("sub2-5"));
		assertTrue(sub2_5 == null);
		
		
		List<TreeNode<String>> sub1s = Trees.searchAll(treeNode, o->o.get().startsWith("sub1-1"));
		assertEquals(3, sub1s.size());
		assertTrue(sub1s.contains(Trees.searchFirst(treeNode, "sub1-1")));
		assertTrue(sub1s.contains(Trees.searchFirst(treeNode, "sub1-1-1")));
		assertTrue(sub1s.contains(Trees.searchFirst(treeNode, "sub1-1-2")));
		
		
		List<TreeNode<String>> sub2s = Trees.searchAll(treeNode, SearchOption.depthFirst(), o->o.get().startsWith("sub2-2"));
		assertEquals(3, sub1s.size());
		assertTrue(sub2s.contains(Trees.searchFirst(treeNode, "sub2-2")));
		assertTrue(sub2s.contains(Trees.searchFirst(treeNode, "sub2-2-1")));
		assertTrue(sub2s.contains(Trees.searchFirst(treeNode, "sub2-2-2")));
	}
	
	
	@Test
	public void pathFromRoot() throws Exception {
		TreeNode<String> sub1_1_2Node = Trees.searchFirst(treeNode, "sub1-1-2");
		List<TreeNode<String>> parents = Trees.pathFromRoot(sub1_1_2Node);
		List<String> parentContents = Trees.contents(parents);
		assertEquals("root", parentContents.get(0));
		assertEquals("sub1", parentContents.get(1));
		assertEquals("sub1-1", parentContents.get(2));
		assertEquals("sub1-1-2", parentContents.get(3));
	}
	
	@Test
	public void root() throws Exception {
		TreeNode<String> sub1_1_2Node = Trees.searchFirst(treeNode, "sub1-1-2");
		TreeNode<String> root = Trees.root(sub1_1_2Node);
		assertEquals(root, treeNode);
	}

	@Test
	public void leaves() throws Exception {
		List<TreeNode<String>> leaves = Trees.leaves(treeNode);
		
		List<String> contents = Trees.contents(leaves);
		
		assertTrue(contents.contains("sub1-1-1"));
		assertTrue(contents.contains("sub1-1-2"));
		assertTrue(contents.contains("sub1-2-1"));
		assertTrue(contents.contains("sub1-3"));
		assertTrue(contents.contains("sub2-1"));
		assertTrue(contents.contains("sub2-2-1"));
		assertTrue(contents.contains("sub2-2-2"));
		assertTrue(contents.contains("sub3"));
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
