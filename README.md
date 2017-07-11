##Tree

\-`jdk8`

\-`no dependency`

Java로 일반적인 계층적 구조인 Tree 자료구조를 작성하였다.

Tree생성자(TreeGenerator)를 통해 데이터목록`List<Data>`을 계층적인 모델로 변경이 가능하게 구현을 하였으며, 데이터베이스에 계층적인 구조를 관리하는 일반적인 그래프 알고리즘 2개를 지원한다. 1. [Adjancency List Model](https://en.wikipedia.org/wiki/Adjacency_list) 2. [Nested Set Model](https://en.wikipedia.org/wiki/Nested_set_model)

####1.Adjacency list Model 예시 -key와 parentKey 조합으로 구성된다.

```
@Getter
class Category implements AdjacentNode<Integer> {
	private int parentKey;
	private int key;
	private String name;

	public Category(int parentKey, int key, String name) {
		this.parentKey = parentKey;
		this.key = key;
		this.name = name;
	}
}

// DB List
public List<Category> selectCategories(){
  return Arrays.asList(
		new Category(0, 1, "root"),
		new Category(3, 6, "sub2-1"),
		new Category(2, 4, "sub1-1"),
		new Category(2, 5, "sub1-2"),
		new Category(1, 2, "sub1"),
		new Category(1, 3, "sub2"),
		new Category(1, 7, "sub3")
	);
}

TreeGenerator generator = new RecursiveTreeGenerator();
List<Category> categories = selectCategories();
Category root = categories.get(0);
TreeNode<Category> treeNode = generator.create(categories, root);

<결과>
> root
>> sub1
>>> sub1-1
>>> sub1-2
>> sub2
>>> sub2-1
>> sub3

assertThat(treeNode.get().getName(), is("root"));

assertThat(treeNode.getChildAt(0).getName(), is("sub1"));
assertThat(treeNode.getChildAt(1).getName(), is("sub2"));
assertThat(treeNode.getChildAt(2).getName(), is("sub3"));

assertThat(treeNode.getSubTreeAt(0).getChildAt(0).getName(), is("sub1-1"));
assertThat(treeNode.getSubTreeAt(0).getChildAt(1).getName(), is("sub1-2"));
assertThat(treeNode.getSubTreeAt(1).getChildAt(0).getName(), is("sub2-1"));
```

####2.Nested Set Model 예시 -left key와 right key 조합으로 구성된다.

```
@Getter
class Category implements NestedSetNode {
	private long left;
	private long right;
	private String name;

	public Category(long left, long right, String name) {
		this.left = left;
		this.right = right;
		this.name = name;
	}
}

// DB List
public List<Category> selectCategories(){
  return Arrays.asList(
    new Category(0, 13, "root"),
    new Category(1, 6, "sub1"),
    new Category(2, 3, "sub1-1"),
    new Category(4, 5, "sub1-2"),
    new Category(7, 10, "sub2"),
    new Category(8, 9, "sub2-1"),
    new Category(11, 12, "sub3")
	);
}

TreeGenerator generator = new RecursiveTreeGenerator();
List<Category> categories = selectCategories();
Category root = categories.get(0);
TreeNode<Category> treeNode = generator.create(categories, root);

<결과>
> root
>> sub1
>>> sub1-1
>>> sub1-2
>> sub2
>>> sub2-1
>> sub3

assertThat(treeNode.get().getName(), is("root"));

assertThat(treeNode.getChildAt(0).getName(), is("sub1"));
assertThat(treeNode.getChildAt(1).getName(), is("sub2"));
assertThat(treeNode.getChildAt(2).getName(), is("sub3"));

assertThat(treeNode.getSubTreeAt(0).getChildAt(0).getName(), is("sub1-1"));
assertThat(treeNode.getSubTreeAt(0).getChildAt(1).getName(), is("sub1-2"));
assertThat(treeNode.getSubTreeAt(1).getChildAt(0).getName(), is("sub2-1"));
```

### TreeNode를 탐색하는 Utility제공

-	어떠한 TreeNode 한 지점(A)을 탐색하는 로직 제공.
-	탐색알고리즘은 깊이우선탐색(`SearchOption.depthFirst()`), 너비우선탐색(`SearchOption.breadthFirst()`) 두가지를 제공한다.
-	단일 노드탐색 `Trees.searchFirst(root, A지점)`
-	다중 노드탐색 `Trees.searchAll(root, 찾고자하는 predicate)`

```
/**
 * TreeNode Template
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
 */
TreeNode<String> node = Trees.searchFirst(treeNode, "sub1-1");
assertEquals(node.get(), "sub1-1");

List<TreeNode<String>> sub1s = Trees.searchAll(treeNode, o->o.get().startsWith("sub1-1"));
assertEquals(3, sub1s.size());
//sub1-1, sub1-1-1, sub1-1-2
```

-	어느 한지점(A)의 노드를 가지고있을때, root에서부터 경로를 얻어올수 있다. `Trees.pathFromRoot(A지점)`
-	어느 한지점(A)에서 root 노드를 찾아갈수 있다. `Trees.root(A지점)`
-	어느 한지점(A)에서 모든 자식노드(leaves)를 얻어올수있다, `Tree.leaves(A지점)`

```
@Test
public void pathFromRoot() throws Exception {
  TreeNode<String> node = Trees.searchFirst(treeNode, "sub1-1-2");
  List<TreeNode<String>> parents = Trees.pathFromRoot(node);
  List<String> parentContents = Trees.contents(parents);
  assertEquals("root", parentContents.get(0));
  assertEquals("sub1", parentContents.get(1));
  assertEquals("sub1-1", parentContents.get(2));
  assertEquals("sub1-1-2", parentContents.get(3));
}

@Test
public void root() throws Exception {
  TreeNode<String> node = Trees.searchFirst(treeNode, "sub1-1-2");
  TreeNode<String> root = Trees.root(node);
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
```
