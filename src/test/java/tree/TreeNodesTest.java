package tree;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeNodesTest {
    TreeNode<String> treeNode = createTreeNode();


    @Test
    public void contents() {
        assertEquals(TreeNodes.values(Collections.singletonList(treeNode)), List.of("root"));
        assertEquals(TreeNodes.values(treeNode.children()), Arrays.asList("sub1", "sub2", "sub3"));
    }


    @Test
    public void search() {
        TreeNode<String> sub1_1 = TreeNodes.searchFirst(treeNode, "sub1-1").orElseThrow();
        assertEquals(sub1_1.value(), "sub1-1");
        assertEquals(sub1_1.children().size(), 2);

        TreeNode<String> root = TreeNodes.searchFirst(treeNode, "root").orElseThrow();
        assertEquals(treeNode, root);

        TreeNode<String> sub2_1 = TreeNodes.searchFirst(treeNode, TreeVisitOption.depthFirst(), o->o.value().equals("sub2-1")).orElseThrow();
        assertEquals(sub2_1.value(),"sub2-1");

        TreeNode<String> sub2_5 = TreeNodes.searchFirst(treeNode, TreeVisitOption.depthFirst(), o->o.value().equals("sub2-5")).orElse(null);
        assertNull(sub2_5);


        List<TreeNode<String>> sub1s = TreeNodes.searchAll(treeNode, o->o.value().startsWith("sub1-1"));
        assertEquals(3, sub1s.size());
        assertTrue(sub1s.contains(TreeNodes.searchFirst(treeNode, "sub1-1").orElseThrow()));
        assertTrue(sub1s.contains(TreeNodes.searchFirst(treeNode, "sub1-1-1").orElseThrow()));
        assertTrue(sub1s.contains(TreeNodes.searchFirst(treeNode, "sub1-1-2").orElseThrow()));


        List<TreeNode<String>> sub2s = TreeNodes.searchAll(treeNode, TreeVisitOption.depthFirst(), o->o.value().startsWith("sub2-2"));
        assertEquals(3, sub1s.size());
        assertTrue(sub2s.contains(TreeNodes.searchFirst(treeNode, "sub2-2").orElseThrow()));
        assertTrue(sub2s.contains(TreeNodes.searchFirst(treeNode, "sub2-2-1").orElseThrow()));
        assertTrue(sub2s.contains(TreeNodes.searchFirst(treeNode, "sub2-2-2").orElseThrow()));
    }


    @Test
    public void pathFromRoot() {
        TreeNode<String> sub1_1_2Node = TreeNodes.searchFirst(treeNode, "sub1-1-2").orElseThrow();
        List<TreeNode<String>> parents = TreeNodes.pathFromRoot(sub1_1_2Node);
        List<String> parentContents = TreeNodes.values(parents);
        assertEquals("root", parentContents.get(0));
        assertEquals("sub1", parentContents.get(1));
        assertEquals("sub1-1", parentContents.get(2));
        assertEquals("sub1-1-2", parentContents.get(3));
    }

    @Test
    public void root() {
        TreeNode<String> sub1_1_2Node = TreeNodes.searchFirst(treeNode, "sub1-1-2").orElseThrow();
        TreeNode<String> root = TreeNodes.getRoot(sub1_1_2Node);
        assertEquals(root, treeNode);
    }

    @Test
    public void leaves() {
        List<TreeNode<String>> leaves = TreeNodes.getAllLeaves(treeNode);

        List<String> contents = TreeNodes.values(leaves);

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
