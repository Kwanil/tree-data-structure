package tree;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class TreeNodeTest {
	
	@Test
	public void testOnlyRootNode() throws Exception {
		TreeNode<String> treeNode = new TreeNode<String>("root");

		assertThat(treeNode.get(), is("root"));
		assertThat(treeNode.isRoot(), is(true));
		assertThat(treeNode.isLeaf(), is(true));
		assertThat(treeNode.getParent(), is((TreeNode<String>)null));
	}
	
	@Test
	public void testAddChild() throws Exception {
		TreeNode<String> root = new TreeNode<String>("root");
		root.addChild("sub1");
		root.addChild("sub2");

		assertThat(root.get(), is("root"));
		assertThat(root.isRoot(), is(true));
		assertThat(root.isLeaf(), is(false));
		assertThat(root.getParent(), is((TreeNode<String>)null));
		
		assertThat(root.getSubTreeAt(0).getParent().get(),is("root"));
		assertThat(root.getSubTreeAt(1).getParent().get(),is("root"));
		assertThat(root.getChildAt(0),is("sub1"));
		assertThat(root.getSubTreeAt(0).isLeaf(),is(true));
		assertThat(root.getSubTreeAt(1).get(),is("sub2"));
		assertThat(root.getSubTreeAt(1).isLeaf(),is(true));

		assertThat(root.getChildren().size(),is(root.getSubTrees().size()));
	}
	
	@Test
	public void testAddSubTree() throws Exception {
		TreeNode<String> root = new TreeNode<String>("root");
		root.addSubTree(new TreeNode<String>("sub1"));
		root.addSubTree(new TreeNode<String>("sub2"));
		
		
		assertThat(root.get(), is("root"));
		assertThat(root.isRoot(), is(true));
		assertThat(root.isLeaf(), is(false));
		assertThat(root.getParent(), is((TreeNode<String>)null));
		
		assertThat(root.getSubTreeAt(0).getParent().get(),is("root"));
		assertThat(root.getSubTreeAt(1).getParent().get(),is("root"));
		assertThat(root.getChildAt(0),is("sub1"));
		assertThat(root.getSubTreeAt(0).isLeaf(),is(true));
		assertThat(root.getSubTreeAt(1).get(),is("sub2"));
		assertThat(root.getSubTreeAt(1).isLeaf(),is(true));
		
		assertThat(root.getChildren().size(),is(root.getSubTrees().size()));
	}
	
	@Test
	public void testToString() throws Exception {
		TreeNode<String> root = new TreeNode<String>("root");
		TreeNode<String> sub1 = new TreeNode<String>("sub1");
		sub1.addChild("sub1-1");
		sub1.addChild("sub1-2");
		TreeNode<String> sub2 = new TreeNode<String>("sub2");
		TreeNode<String> sub3 = new TreeNode<String>("sub3");
		sub2.addChild("sub2-1");
		root.addSubTree(sub1);
		root.addSubTree(sub2);
		root.addSubTree(sub3);
		
		
		String expected = "root\n sub1\n  sub1-1\n  sub1-2\n sub2\n  sub2-1\n sub3\n";
		assertEquals(expected, root.toString());
		assertEquals(expected, sub1.getParent().toString());
	}
	
	@Test
	public void testDepth() throws Exception {
		TreeNode<String> root = new TreeNode<String>("root");
		root.addChild("sub1");
		root.addChild("sub2");
		
		assertEquals(2, root.getSubTreeAt(0).depth());
	}
	
	@Test
	public void testRemove() throws Exception {
		TreeNode<String> root = new TreeNode<String>("root");
		root.addChild("sub1");
		root.addChild("sub2");
		
		assertEquals(true, root.getSubTreeAt(0).remove());
		assertEquals(true, root.getSubTreeAt(0).remove());
		assertEquals(true, root.getSubTrees().isEmpty());
	}
	
	@Test
	public void testRemoveSubTree() throws Exception {
		TreeNode<String> root = new TreeNode<String>("root");
		root.addChild("sub1");
		root.addChild("sub2");
		
		assertEquals(true, root.removeSubTrees(o->o.depth() == 2));
		assertEquals(true, root.getSubTrees().isEmpty());
	}
	
}
