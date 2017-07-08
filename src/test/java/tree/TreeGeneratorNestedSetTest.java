package tree;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TreeGeneratorNestedSetTest {
	RecursiveTreeGenerator generator = new RecursiveTreeGenerator();

	@Test
	public void testOneNode() throws Exception {
		// given
		List<Category> categories = Arrays.asList(new Category(0, 1, "root"));

		// when
		TreeNode<Category> treeNode = generator.create(categories, categories.get(0));

		// then
		assertThat(treeNode.get().getName(), is("root"));
		assertThat(treeNode.isRoot(), is(true));
		assertThat(treeNode.isLeaf(), is(true));
	}

	@Test
	public void testRoot() throws Exception {
		// given
		List<Category> categories = Arrays.asList(
				new Category(0, 13, "root"), 
				new Category(1, 6, "sub1"),
				new Category(2, 3, "sub1-1"), 
				new Category(4, 5, "sub1-2"), 
				new Category(7, 10, "sub2"),
				new Category(8, 9, "sub2-1"), 
				new Category(11, 12, "sub3"));

		// when
		TreeNode<Category> treeNode = generator.create(categories, categories.get(0));

		// then
		assertThat(treeNode.get().getName(), is("root"));
		assertThat(treeNode.isRoot(), is(true));
		assertThat(treeNode.isLeaf(), is(false));

		assertThat(treeNode.getChildAt(0).getName(), is("sub1"));
		assertThat(treeNode.getSubTreeAt(0).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(0).isLeaf(), is(false));

		assertThat(treeNode.getSubTreeAt(0).getChildAt(0).getName(), is("sub1-1"));
		assertThat(treeNode.getSubTreeAt(0).getSubTreeAt(0).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(0).getSubTreeAt(0).isLeaf(), is(true));
		assertThat(treeNode.getSubTreeAt(0).getChildAt(1).getName(), is("sub1-2"));
		assertThat(treeNode.getSubTreeAt(0).getSubTreeAt(1).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(0).getSubTreeAt(1).isLeaf(), is(true));

		assertThat(treeNode.getChildAt(1).getName(), is("sub2"));
		assertThat(treeNode.getSubTreeAt(1).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(1).isLeaf(), is(false));

		assertThat(treeNode.getSubTreeAt(1).getChildAt(0).getName(), is("sub2-1"));
		assertThat(treeNode.getSubTreeAt(1).getSubTreeAt(0).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(1).getSubTreeAt(0).isLeaf(), is(true));

		assertThat(treeNode.getChildAt(2).getName(), is("sub3"));
		assertThat(treeNode.getSubTreeAt(2).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(2).isLeaf(), is(true));

	}

	@Test
	public void testRootIsSub1() throws Exception {
		// given
		List<Category> categories = Arrays.asList(
				new Category(0, 13, "root"), 
				new Category(1, 6, "sub1"),
				new Category(2, 3, "sub1-1"), 
				new Category(4, 5, "sub1-2"), 
				new Category(7, 10, "sub2"),
				new Category(8, 9, "sub2-1"), 
				new Category(11, 12, "sub3"));

		// when
		TreeNode<Category> treeNode = generator.create(categories, categories.get(1)); // root is sub1

		// then
		assertThat(treeNode.get().getName(), is("sub1"));

		assertThat(treeNode.getChildAt(0).getName(), is("sub1-1"));
		assertThat(treeNode.getChildAt(1).getName(), is("sub1-2"));
	}

	@Test
	public void testRootIncludedDirtyNodes() throws Exception {
		// given
		List<Category> categories = Arrays.asList(
				new Category(0, 13, "root"), 
				new Category(1, 6, "sub1"),
				new Category(2, 3, "sub1-1"), 
				new Category(4, 5, "sub1-2"), 
				new Category(7, 10, "sub2"),
				new Category(8, 9, "sub2-1"), 
				new Category(11, 12, "sub3"),
				new Category(13, 14, "sub7-1-1"), // dirty
				new Category(15, 16, "sub2-2-1") 
				);

		// when
		TreeNode<Category> treeNode = generator.create(categories, categories.get(0));

		// then
		assertThat(treeNode.get().getName(), is("root"));
		assertThat(treeNode.isRoot(), is(true));
		assertThat(treeNode.isLeaf(), is(false));

		assertThat(treeNode.getChildAt(0).getName(), is("sub1"));
		assertThat(treeNode.getSubTreeAt(0).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(0).isLeaf(), is(false));

		assertThat(treeNode.getSubTreeAt(0).getChildAt(0).getName(), is("sub1-1"));
		assertThat(treeNode.getSubTreeAt(0).getSubTreeAt(0).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(0).getSubTreeAt(0).isLeaf(), is(true));
		assertThat(treeNode.getSubTreeAt(0).getChildAt(1).getName(), is("sub1-2"));
		assertThat(treeNode.getSubTreeAt(0).getSubTreeAt(1).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(0).getSubTreeAt(1).isLeaf(), is(true));

		assertThat(treeNode.getChildAt(1).getName(), is("sub2"));
		assertThat(treeNode.getSubTreeAt(1).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(1).isLeaf(), is(false));

		assertThat(treeNode.getSubTreeAt(1).getChildAt(0).getName(), is("sub2-1"));
		assertThat(treeNode.getSubTreeAt(1).getSubTreeAt(0).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(1).getSubTreeAt(0).isLeaf(), is(true));

		assertThat(treeNode.getChildAt(2).getName(), is("sub3"));
		assertThat(treeNode.getSubTreeAt(2).isRoot(), is(false));
		assertThat(treeNode.getSubTreeAt(2).isLeaf(), is(true));
	}

	static class Category implements NestedSetNode {
		private long left;
		private long right;
		private String name;

		public Category(long left, long right, String name) {
			this.left = left;
			this.right = right;
			this.name = name;
		}

		@Override
		public long getLeft() {
			return left;
		}

		@Override
		public long getRight() {
			return right;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "[left=" + left + ",right=" + right + ",name=" + name + "]";
		}

	}
}
