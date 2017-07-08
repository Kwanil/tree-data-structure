package tree;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class TreeGeneratorAdjacentTest {
	RecursiveTreeGenerator generator = new RecursiveTreeGenerator();
	
	@Test
	public void testOneNode() throws Exception {
		//given
		List<Category> categories = Arrays.asList(new Category(0,1,"root"));
		
		//when
		TreeNode<Category> treeNode = generator.create(categories, categories.get(0));
		
		//then
		assertThat(treeNode.get().getName(), is("root"));
		assertThat(treeNode.isRoot(), is(true));
		assertThat(treeNode.isLeaf(), is(true));
	}
	
	@Test
	public void testRoot() throws Exception {
		//given
		List<Category> categories = Arrays.asList(
				new Category(0, 1, "root"),
				new Category(3, 6, "sub2-1"),
				new Category(2, 4, "sub1-1"),
				new Category(2, 5, "sub1-2"),
				new Category(1, 2, "sub1"),
				new Category(1, 3, "sub2"),
				new Category(1, 7, "sub3")
		);
		
		//when
		TreeNode<Category> treeNode = generator.create(categories, categories.get(0));
		
		//then
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
		//given
		List<Category> categories = Arrays.asList(
				new Category(0, 1, "root"),
				new Category(3, 6, "sub2-1"),
				new Category(2, 4, "sub1-1"),
				new Category(2, 5, "sub1-2"),
				new Category(1, 2, "sub1"),
				new Category(1, 3, "sub2"),
				new Category(1, 7, "sub3")
		);
		
		//when
		TreeNode<Category> treeNode = generator.create(categories, categories.get(4)); //root is sub1
		
		//then
		assertThat(treeNode.get().getName(), is("sub1"));
		
		assertThat(treeNode.getChildAt(0).getName(), is("sub1-1"));
		assertThat(treeNode.getChildAt(1).getName(), is("sub1-2"));
	}
	
	@Test
	public void testRootIncludedDirtyNodes() throws Exception {
		//given
		List<Category> categories = Arrays.asList(
				new Category(0, 1, "root"),
				new Category(3, 6, "sub2-1"),
				new Category(2, 4, "sub1-1"),
				new Category(2, 5, "sub1-2"),
				new Category(1, 2, "sub1"),
				new Category(1, 3, "sub2"),
				new Category(1, 7, "sub3"),
				new Category(11, 12, "sub5-1"),		// dirty node
				new Category(13, 14, "sub7-1-1"),	// dirty node
				new Category(15, 16, "sub2-2-1")	// dirty node
		);
		
		//when
		TreeNode<Category> treeNode = generator.create(categories, categories.get(0));
		
		//then
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
	
	
	static class Category implements AdjacentNode<Integer> {
		private int parentKey;
		private int key;
		private String name;
		
		public Category(int parentKey, int key, String name) {
			this.parentKey = parentKey;
			this.key = key;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Integer getKey() {
			return key;
		}

		public Integer getParentKey() {
			return parentKey;
		}
		
		public String toString() {
			return "[key="+key+",parentKey="+parentKey+",name="+name+"]";
		}
	}
}
