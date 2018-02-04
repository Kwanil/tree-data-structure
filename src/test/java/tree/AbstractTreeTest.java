package tree;

public abstract class AbstractTreeTest {

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
    TreeNode<String> createTreeNode() {
        TreeNode<String> rootNode = new TreeNode<>("root");
        TreeNode<String> sub1Node = new TreeNode<>(rootNode,"sub1");
        TreeNode<String> sub1_1Node = new TreeNode<>(sub1Node,"sub1-1");
        new TreeNode<>(sub1_1Node,"sub1-1-1");
        new TreeNode<>(sub1_1Node,"sub1-1-2");
        TreeNode<String> sub1_2Node = new TreeNode<>(sub1Node,"sub1-2");
        new TreeNode<>(sub1_2Node,"sub1-2-1");
        new TreeNode<>(sub1Node,"sub1-3");
        TreeNode<String> sub2Node = new TreeNode<>(rootNode,"sub2");
        new TreeNode<>(sub2Node,"sub2-1");
        TreeNode<String> sub2_2Node = new TreeNode<>(sub2Node,"sub2-2");
        new TreeNode<>(sub2_2Node,"sub2-2-1");
        new TreeNode<>(sub2_2Node,"sub2-2-2");
        TreeNode<String> sub3Node = new TreeNode<>(rootNode,"sub3");

        System.out.println(this.getClass() + " create TreeNode<String> below :");
        System.out.println(rootNode);

        return rootNode;
    }
}
