package tree;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TreesTest extends AbstractTreeTest{
	TreeNode<String> treeNode = createTreeNode();

	
	@Test
	public void contents() throws Exception {
		assertEquals(Trees.contents(Arrays.asList(treeNode)), Arrays.asList("root"));
		assertEquals(Trees.contents(treeNode.children()), Arrays.asList("sub1", "sub2", "sub3"));
	}
	
	
	@Test
	public void search() throws Exception {
		TreeNode<String> sub1_1 = Trees.searchFirst(treeNode, "sub1-1").get();
		assertEquals(sub1_1.get(), "sub1-1");
		assertEquals(sub1_1.children().size(), 2);
		
		TreeNode<String> root = Trees.searchFirst(treeNode, "root").get();
		assertEquals(treeNode, root);
		
		TreeNode<String> sub2_1 = Trees.searchFirst(treeNode, TreeSearchOption.depthFirst(), o->o.get().equals("sub2-1")).get();
		assertEquals(sub2_1.get(),"sub2-1");

		TreeNode<String> sub2_5 = Trees.searchFirst(treeNode, TreeSearchOption.depthFirst(), o->o.get().equals("sub2-5")).orElse(null);
		assertTrue(sub2_5 == null);
		
		
		List<TreeNode<String>> sub1s = Trees.searchAll(treeNode, o->o.get().startsWith("sub1-1"));
		assertEquals(3, sub1s.size());
		assertTrue(sub1s.contains(Trees.searchFirst(treeNode, "sub1-1").get()));
		assertTrue(sub1s.contains(Trees.searchFirst(treeNode, "sub1-1-1").get()));
		assertTrue(sub1s.contains(Trees.searchFirst(treeNode, "sub1-1-2").get()));
		
		
		List<TreeNode<String>> sub2s = Trees.searchAll(treeNode, TreeSearchOption.depthFirst(), o->o.get().startsWith("sub2-2"));
		assertEquals(3, sub1s.size());
		assertTrue(sub2s.contains(Trees.searchFirst(treeNode, "sub2-2").get()));
		assertTrue(sub2s.contains(Trees.searchFirst(treeNode, "sub2-2-1").get()));
		assertTrue(sub2s.contains(Trees.searchFirst(treeNode, "sub2-2-2").get()));
	}
	
	
	@Test
	public void pathFromRoot() throws Exception {
		TreeNode<String> sub1_1_2Node = Trees.searchFirst(treeNode, "sub1-1-2").get();
		List<TreeNode<String>> parents = Trees.pathFromRoot(sub1_1_2Node);
		List<String> parentContents = Trees.contents(parents);
		assertEquals("root", parentContents.get(0));
		assertEquals("sub1", parentContents.get(1));
		assertEquals("sub1-1", parentContents.get(2));
		assertEquals("sub1-1-2", parentContents.get(3));
	}
	
	@Test
	public void root() throws Exception {
		TreeNode<String> sub1_1_2Node = Trees.searchFirst(treeNode, "sub1-1-2").get();
		TreeNode<String> root = Trees.root(sub1_1_2Node).get();
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
	

}
