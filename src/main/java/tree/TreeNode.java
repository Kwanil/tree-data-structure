package tree;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class TreeNode<T> {
	private TreeNode<T> parent;
	private final T contents;
	private final List<TreeNode<T>> children = new ArrayList<>();
	
	final static int rootDepth = 1;
	
	public TreeNode(T contents) {
		this(null, contents);
	}

	public TreeNode(TreeNode<T> parent, T contents) {
		Objects.requireNonNull(contents, "contents must not be null");
		this.parent = parent;
		this.contents = contents;
		if (parent != null) {
			parent.children.add(this);
		}
	}

	public TreeNode<T> parent() {
		return parent;
	}

	public T get() {
		return contents;
	}

	public List<TreeNode<T>> children() {
		return Collections.unmodifiableList(children);
	}

	public TreeNode<T> childAt(int index) {
		return children.get(index);
	}

	public int depth() {
		if(isRoot()) {
			return rootDepth;
		}
		return parent.depth() + 1;
	}
	
	public boolean remove() {
		Objects.requireNonNull(parent);
		boolean remove = parent.children.remove(this);
		parent = null;
		return remove;
	}

	public boolean removeChildren(Predicate<? super TreeNode<T>> filter) {
		return children.removeIf(filter);
	}

	public String toString() {
		String space = " ";
		StringBuilder sb = new StringBuilder();
		for(int i=rootDepth; i<depth(); i++) {
			sb.append(space);
		}
		sb.append(contents.toString());
		sb.append("\n");
		for(TreeNode<T> treeNode : this.children) {
			sb.append(treeNode.toString());
		}
		return sb.toString();
	}

	public Stream<TreeNode<T>> stream(TreeSearchOption<T> treeSearchOption) {
		Objects.requireNonNull(treeSearchOption);
		return treeSearchOption.getFunction().apply(this);
	}
	
	public boolean isLeaf() {
		return this.children.isEmpty();
	}
	
	public boolean isRoot() {
		return parent == null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TreeNode<?> treeNode = (TreeNode<?>) o;

		return contents.equals(treeNode.contents);
	}

	@Override
	public int hashCode() {
		return contents.hashCode();
	}
}
