#Tree

\-`jdk8`

\-`no dependency`

Java로 일반적인 계층적 구조인 Tree 자료구조를 작성하였다.

- Java8의 stream을 통한 TreeNode의 접근등을 할수있도록 하였다. 
- TreeNode.stream에서 검색 방식은 3가지로 너비우선, 깊이우선, 부모방향이다.
- 트리노드로 구성되지 않은 List구조를 TreeNode로 간편하게 변경하는 Collector를 제공한다.
(`TreeNode<Category> root = categories.stream().collect(toTreeNode());`)
- 트리노드객체로 구성하지 않더라도, Pojo객체 내부에 mutable한 children setter가 있다면, 객체자체를 Tree로 구성할수 있다
(`Category root = categories.stream().collect(toTree());`)


#### Adjacency list Model 구조를 TreeNode로 간편 변경가능하도록한다.

```
@Getter
class Category {
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

List<Category> categories = selectCategories();


TreeNode<Category> root = categories.stream().collect(toTreeNode());

public Collector<Category,?,TreeNode<Category>> toTreeNode() {
return TreeNodeCollector<Category,Integer>.treeNode()
                                .id(Category::getId)    //ID는??
                                .parentId(Category::getParentId) //Parent ID
                                .root(i->i.parentId==0) //Root는??
                                .toTree();
}

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
-	탐색알고리즘은 깊이우선탐색(`TreeSearchOption.depthFirst()`), 너비우선탐색(`TreeSearchOption.breadthFirst()`) 제공한다. 또한 현재 노드에서 부모방향(`TreeSearchOption.toParent()`)으로 탐색할수 있다
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
