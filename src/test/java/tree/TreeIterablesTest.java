package tree;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TreeIterablesTest extends AbstractTreeTest{
	TreeNode<String> treeNode = createTreeNode();

	@Test
	public void testToParent() {
		assertIterable(TreeIterables.toParent(treeNode), "root");
		assertIterable(TreeIterables.toParent(treeNode.childAt(0)), "sub1", "root");
		assertIterable(TreeIterables.toParent(treeNode.childAt(1).childAt(0)), "sub2-1","sub2","root");
	}
	
	@Test
	public void testBreadthFirst() {
		assertIterable(TreeIterables.breadthFirst(treeNode.childAt(2)), "sub3");
		assertIterable(TreeIterables.breadthFirst(treeNode.childAt(1)), "sub2", "sub2-1", "sub2-2","sub2-2-1", "sub2-2-2");
		assertIterable(TreeIterables.breadthFirst(treeNode), "root","sub1","sub2","sub3","sub1-1","sub1-2","sub1-3","sub2-1","sub2-2","sub1-1-1","sub1-1-2","sub1-2-1","sub2-2-1","sub2-2-2");
	}
	
	@Test
	public void testDeepFirst() {
		assertIterable(TreeIterables.depthFirst(treeNode.childAt(2)), "sub3");
		assertIterable(TreeIterables.depthFirst(treeNode.childAt(1)), "sub2", "sub2-1", "sub2-2", "sub2-2-1","sub2-2-2");
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


}
