package tree;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TreeStreamsTest extends AbstractTreeTest{
	TreeNode<String> treeNode = createTreeNode();

	@Test
	public void testToParent() {
		TreeStreams.breadthFirst(treeNode);
		assertStream(TreeStreams.toParent(treeNode), "root");
		assertStream(TreeStreams.toParent(treeNode.childAt(0)), "sub1", "root");
		assertStream(TreeStreams.toParent(treeNode.childAt(1).childAt(0)), "sub2-1","sub2","root");
	}
	
	@Test
	public void testBreadthFirst() {
		assertStream(TreeStreams.breadthFirst(treeNode.childAt(2)), "sub3");
		assertStream(TreeStreams.breadthFirst(treeNode.childAt(1)), "sub2", "sub2-1", "sub2-2","sub2-2-1", "sub2-2-2");
		assertStream(TreeStreams.breadthFirst(treeNode), "root","sub1","sub2","sub3","sub1-1","sub1-2","sub1-3","sub2-1","sub2-2","sub1-1-1","sub1-1-2","sub1-2-1","sub2-2-1","sub2-2-2");
	}
	
	@Test
	public void testDeepFirst() {
		assertStream(TreeStreams.depthFirst(treeNode.childAt(2)), "sub3");
		assertStream(TreeStreams.depthFirst(treeNode.childAt(1)), "sub2", "sub2-1", "sub2-2", "sub2-2-1","sub2-2-2");
		assertStream(TreeStreams.depthFirst(treeNode), "root","sub1","sub1-1","sub1-1-1","sub1-1-2","sub1-2","sub1-2-1","sub1-3","sub2","sub2-1","sub2-2","sub2-2-1","sub2-2-2","sub3");
	}
	
	
	private <T> void assertStream(Stream<TreeNode<T>> stream, @SuppressWarnings("unchecked") T...contents) {
		List<T> list = Arrays.asList(contents);
		Iterator<TreeNode<T>> iterator = stream.iterator();
		
		int index = 0;
		while (iterator.hasNext()) {
			T expected = iterator.next().get();
			T actual = list.get(index++);
			assertEquals(expected, actual);
		}
		
	}

}
