package tree;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TreeNodeTest extends AbstractTreeTest{
	
	@Test
	public void testOnlyRootNode() throws Exception {
		TreeNode<String> treeNode = new TreeNode<String>("root");

		assertThat(treeNode.get(), is("root"));
		assertThat(treeNode.isRoot(), is(true));
		assertThat(treeNode.isLeaf(), is(true));
		assertThat(treeNode.parent(), is((TreeNode<String>)null));
	}

    @Test
    public void testOneRootTwoChildren() throws Exception {
        TreeNode<String> root = new TreeNode<>("root");
        TreeNode<String> sub1 = new TreeNode<>(root, "sub1");
        TreeNode<String> sub2 = new TreeNode<>(root, "sub2");

        assertThat(root.children(), is(Arrays.asList(sub1, sub2)));
        assertThat(root.childAt(0), is(sub1));
        assertThat(root.childAt(1), is(sub2));
        assertThat(root.children().indexOf(sub1), is(0));
        assertThat(root.children().indexOf(sub2), is(1));

    }

    @Test
    public void test() throws Exception {

        TreeNode<String> root = new TreeNode<>("root");
        TreeNode<String> sub1 = new TreeNode<>(root, "sub1");
        TreeNode<String> sub2 = new TreeNode<>(root, "sub2");

        assertThat(root.children(), is(Arrays.asList(sub1, sub2)));
        assertThat(root.childAt(0), is(sub1));
        assertThat(root.childAt(1), is(sub2));
    }
}
