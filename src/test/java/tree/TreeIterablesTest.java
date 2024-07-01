package tree;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeIterablesTest {
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


    @SafeVarargs
    private <T> void assertIterable(Iterable<TreeNode<T>> iterable, T...contents) {
        List<T> list = Arrays.asList(contents);
        Iterator<TreeNode<T>> iterator = iterable.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            T expected = iterator.next().value();
            T actual = list.get(index++);
            assertEquals(expected, actual);
        }

    }


    /**
     * create TreeNode
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
     */
    TreeNode<String> createTreeNode() {
        MutableTreeNode<String> rootNode = MutableTreeNode.create("root");
        MutableTreeNode<String> sub1Node = MutableTreeNode.create("sub1");
        rootNode.add(sub1Node);
        MutableTreeNode<String> sub1_1Node = MutableTreeNode.create("sub1-1");
        sub1Node.add(sub1_1Node);
        MutableTreeNode<String> sub1_1_1Node = MutableTreeNode.create("sub1-1-1");
        MutableTreeNode<String> sub1_1_2Node = MutableTreeNode.create("sub1-1-2");
        sub1_1Node.add(sub1_1_1Node);
        sub1_1Node.add(sub1_1_2Node);
        MutableTreeNode<String> sub1_2Node = MutableTreeNode.create("sub1-2");
        sub1Node.add(sub1_2Node);
        MutableTreeNode<String> sub1_2_1Node = MutableTreeNode.create("sub1-2-1");
        sub1_2Node.add(sub1_2_1Node);
        MutableTreeNode<String> sub1_3Node = MutableTreeNode.create("sub1-3");
        sub1Node.add(sub1_3Node);
        MutableTreeNode<String> sub2Node = MutableTreeNode.create("sub2");
        rootNode.add(sub2Node);
        MutableTreeNode<String> sub2_1Node = MutableTreeNode.create("sub2-1");
        sub2Node.add(sub2_1Node);
        MutableTreeNode<String> sub2_2Node = MutableTreeNode.create("sub2-2");
        sub2Node.add(sub2_2Node);
        MutableTreeNode<String> sub2_2_1Node = MutableTreeNode.create("sub2-2-1");
        sub2_2Node.add(sub2_2_1Node);
        MutableTreeNode<String> sub2_2_2Node = MutableTreeNode.create("sub2-2-2");
        sub2_2Node.add(sub2_2_2Node);
        MutableTreeNode<String> sub3Node = MutableTreeNode.create("sub3");
        rootNode.add(sub3Node);
        System.out.println(this.getClass() + " create TreeNode<String> depthFirst below :");
        TreeStreams.depthFirst(rootNode).forEach(i-> System.out.println(" ".repeat(Math.max(0, i.depth())) + i));
        System.out.println(this.getClass() + " create TreeNode<String> breadthFirst below :");
        TreeStreams.breadthFirst(rootNode).forEach(i-> System.out.println(" ".repeat(Math.max(0, i.depth())) + i));
        return rootNode;
    }
}
