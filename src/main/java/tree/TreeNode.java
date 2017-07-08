package tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class TreeNode<T> {
	private TreeNode<T> parent;
	private T contents;
	private List<TreeNode<T>> subTrees;
	
	final static int rootDepth = 1;
	
	public TreeNode(T contents) {
		this(null, contents);
	}

	private TreeNode(TreeNode<T> parent, T contents) {
		Objects.requireNonNull(contents, "contents must not be null");
		this.parent = parent;
		this.contents = contents;
		this.subTrees = new ArrayList<TreeNode<T>>();
	}
	
	private TreeNode(TreeNode<T> parent, TreeNode<T> thisNode) {
		this(parent, thisNode.get());
		thisNode.parent = parent;
		addSubTrees(thisNode.getSubTrees());
	}

	public TreeNode<T> getParent() {
		return parent;
	}

	public T get() {
		return contents;
	}

	public T getChildAt(int index) {
		return this.subTrees.get(index).get();
	}
	
	public List<T> getChildren() {
		return Collections.unmodifiableList(Trees.contents(this.subTrees));
	}

	public List<TreeNode<T>> getSubTrees() {
		return Collections.unmodifiableList(subTrees);
	}
	
	public TreeNode<T> getSubTreeAt(int index) {
		return subTrees.get(index);
	}

	public boolean addSubTree(TreeNode<T> subTree) {
		Objects.requireNonNull(subTree);
		return subTrees.add(new TreeNode<>(this, subTree));
	}
	
	public boolean addSubTrees(Collection<? extends TreeNode<T>> subTrees) {
		Objects.requireNonNull(subTrees);
		return this.subTrees.addAll(subTrees.stream().map(node->new TreeNode<>(this, node)).collect(Collectors.toList()));
	}

	public boolean addChild(T child) {
		Objects.requireNonNull(child);
		return this.subTrees.add(new TreeNode<T>(this, child));
	}

	public boolean addChildren(Collection<? extends T> children) {
		Objects.requireNonNull(children);
		return this.subTrees.addAll(children.stream().map(child->new TreeNode<T>(this,child)).collect(Collectors.toList()));
	}
	
	public int depth() {
		if(isRoot()) {
			return rootDepth;
		}
		return getParent().depth() + 1;
	}
	
	public boolean remove() {
		Objects.requireNonNull(parent);
		boolean remove = parent.subTrees.remove(this);
		parent = null;
		return remove;
	}
	
	public boolean removeSubTrees(Predicate<? super TreeNode<T>> filter) {
		return subTrees.removeIf(filter);
	}
	
	public String toString() {
		String space = " ";
		StringBuilder sb = new StringBuilder();
		for(int i=rootDepth; i<depth(); i++) {
			sb.append(space);
		}
		sb.append(contents.toString());
		sb.append("\n");
		for(TreeNode<T> treeNode : this.subTrees) {
			sb.append(treeNode.toString());
		}
		return sb.toString();
	}
	
	public boolean isLeaf() {
		List<TreeNode<T>> children = getSubTrees();
		return children == null || children.isEmpty();
	}
	
	public boolean isRoot() {
		TreeNode<T> parent = getParent();
		return parent == null;
	}
	
}
