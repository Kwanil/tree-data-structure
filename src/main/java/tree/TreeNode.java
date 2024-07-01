package tree;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 객체를 Tree형태로 제공.
 * {@link TreeNode}는 불변성을 위한 것이며, 가변 객체는 {@link MutableTreeNode}를 사용 한다.
 * {@link DefaultMutableTreeNode}가 기본 구현체 이다.
 * 재구현시엔 상속받아서 {@link #parent}, {@link #value},  {@link #children} 메소드를 재 구현하면 된다.
 *
 * @param <T> Tree 형태로 관리하기 위한 Object
 */
public interface TreeNode<T> {

    TreeNode<T> parent();

    T value();

    Collection<? extends TreeNode<T>> children();

    default TreeNode<T> childAt(int index) {
        if (children() == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        return List.copyOf(children()).get(index);
    }

    /**
     * 현재 노드의 자식 노드 개수
     * @return 현재 노드의 자식 노드 개수
     */
    default int childCount() {
        if (children() == null) {
            return 0;
        }
        return children().size();
    }

    /**
     * 현재 노드의 형제 노드 개수, 없으면 자기 자신 1개
     * @return 현재 노드의 형제 노드 개수, 없으면 자기 자신 1개
     */
    default int siblingCount() {
        TreeNode<T> myParent = parent();

        if (myParent == null) {
            return 1;
        }
        return myParent.childCount();
    }

    /**
     * 최상위 노드 여부
     * @return 루트면 true
     */
    default boolean isRoot(){
        return parent() == null;
    }

    /**
     * 최하위 노드 여부
     * @return 최하위면 true
     */
    default boolean isLeaf() {
        return childCount() == 0;
    }

    /**
     * 현재 노드가 최상위 노드에서 부터 깊이
     * @return
     */
    default int depth() {
        int levels = 0;
        TreeNode<T> ancestor = this;
        while((ancestor = ancestor.parent()) != null){
            levels++;
        }
        return levels;
    }

    default boolean isChild(TreeNode<T> node) {
        if (node == null || childCount() == 0) {
            return false;
        }
        return node.parent() == this;
    }

    default boolean isSibling(TreeNode<T> anotherNode) {
        if (anotherNode == null) {
            return false;
        }
        if (anotherNode == this) {
            return true;
        }
        TreeNode<T> myParent = parent();
        boolean result = (myParent != null && myParent == anotherNode.parent());

        if (result && !parent().isChild(anotherNode)) {
            throw new NoSuchElementException("sibling has different parent");
        }
        return result;
    }

    default int indexOf(TreeNode<T> node) {
        Objects.requireNonNull(node, "argument is null");
        if (!isChild(node)) {
            return -1;
        }
        return List.copyOf(children()).indexOf(node);        // linear search
    }

    /**
     * 이 노드의 자식 배열에 있는 자식을 반환합니다.
     * 자식이 마지막 자식인 경우 null을 반환합니다.
     * 이 메서드는 이 노드의 자식을 선형으로 검색하여 자식을 찾고 n은 자식의 수를 나타내는 O(n)입니다.
     *
     * @param           aChild 이후 자식을 찾을 자식 노드
     * @see             #children
     * @exception       IllegalArgumentException 이 노드의 자식이 아닐 경우
     * @return  자식 바로 뒤에 오는 이 노드의 자식
     */
    default TreeNode<T> getChildAfter(TreeNode<T> aChild) {
        Objects.requireNonNull(aChild, "argument is null");

        int index = indexOf(aChild);           // linear search

        if (index == -1) {
            throw new NoSuchElementException("node is not a child");
        }

        if (index < childCount() - 1) {
            return childAt(index + 1);
        }

        return null;
    }

    /**
     * 이 노드의 자식 배열에서 자식 바로 앞에 오는 자식을 반환합니다. 자식이 첫 번째 자식인 경우 null을 반환합니다.
     * 이 메서드는 이 노드의 자식을 선형으로 검색하여 자식을 검색하고 O(n)(여기서 n은 자식의 수)입니다.
     *
     * @param           aChild 이전 자식을 찾을 자식 노드
     * @exception       IllegalArgumentException 이 노드의 자식이 아닐 경우
     * @return  자식 바로 앞에 있는 이 노드의 자식
     */
    default TreeNode<T> getChildBefore(TreeNode<T> aChild) {
        Objects.requireNonNull(aChild, "argument is null");

        int index = indexOf(aChild);           // linear search

        if (index == -1) {
            throw new NoSuchElementException("node is not a child.");
        }

        if (index > 0) {
            return childAt(index - 1);
        } else {
            return null;
        }
    }

    /**
     * 이 노드의 첫 번째 자식을 반환 합니다. 이 노드에 자식이 없으면, NoSuchElementException.
     *
     * @return  이 노드의 첫번짜 자식 노드
     * @exception  NoSuchElementException  없을 경우
     */
    default TreeNode<T> firstChild() {
        if (childCount() == 0) {
            throw new NoSuchElementException("node has no children");
        }
        return childAt(0);
    }

    /**
     * 이 노드의 마지막 자식을 반환합니다. 이 노드에 자식이 없으면, NoSuchElementException.
     *
     * @return 이 노드의 가장 마지막 자식
     * @exception NoSuchElementException  없을 경우
     */
    default TreeNode<T> lastChild() {
        if (childCount() == 0) {
            throw new NoSuchElementException("node has no children");
        }
        return childAt(childCount()-1);
    }

    default TreeNode<T> firstLeaf() {
        TreeNode<T> node = this;
        while (!node.isLeaf()) {
            node = node.firstChild();
        }
        return node;
    }

    default TreeNode<T> lastLeaf() {
        TreeNode<T> node = this;
        while (!node.isLeaf()) {
            node = node.lastChild();
        }
        return node;
    }

    /**
     * 다음 형제 노드 찾기
     * @return 다음 형제 노드 없으면 NoSuchElementException
     */
    default TreeNode<T> nextSibling() {
        TreeNode<T> result;

        TreeNode<T> myParent = parent();

        if (myParent == null) {
            result = null;
        } else {
            result = myParent.getChildAfter(this);      // linear search
        }

        if (result != null && !isSibling(result)) {
            throw new NoSuchElementException("node is not a sibling");
        }

        return result;
    }

    /**
     * 이전 형제 노드 찾기
     * @return 이전 형제 노드 없으면 NoSuchElementException
     */
    default TreeNode<T> previousSibling() {
        TreeNode<T> result;

        TreeNode<T> myParent = parent();

        if (myParent == null) {
            result = null;
        } else {
            result = myParent.getChildBefore(this);      // linear search
        }

        if (result != null && !isSibling(result)) {
            throw new NoSuchElementException("node is not a sibling");
        }
        return null;
    }

    /**
     * 해당 노드가 조상 인지 판단
     */
    default boolean isAncestor(TreeNode<T> anotherNode) {
        if (anotherNode == null) {
            return false;
        }

        TreeNode<T> ancestor = this;
        do {
            if (ancestor == anotherNode) {
                return true;
            }
        } while((ancestor = ancestor.parent()) != null);

        return false;
    }

    /**
     * 해당 노드가 후손 인지 판단
     */
    default boolean isDescendant(TreeNode<T> anotherNode) {
        if (anotherNode == null)
            return false;

        return anotherNode.isAncestor(this);
    }

    default TreeNode<T> nextLeaf() {
        TreeNode<T> myParent = parent();
        if (myParent == null) {
            return null;
        }

        TreeNode<T> nextSibling = nextSibling(); // linear search
        if (nextSibling != null) {
            return nextSibling.firstLeaf();
        }

        return myParent.nextLeaf();  // tail recursion
    }

    default TreeNode<T> previousLeaf() {
        TreeNode<T> myParent = parent();
        if (myParent == null){
            return null;
        }

        TreeNode<T> previousSibling = previousSibling(); // linear search
        if (previousSibling != null){
            return previousSibling.lastLeaf();
        }

        return myParent.previousLeaf();              // tail recursion
    }

    default TreeNode<T> root() {
        TreeNode<T> ancestor = this;
        TreeNode<T> previous;

        do {
            previous = ancestor;
            ancestor = ancestor.parent();
        } while (ancestor != null);

        return previous;
    }

    static <T> TreeNode<T> create(T value) {
        return new DefaultMutableTreeNode<>(value);
    }

    static <T> TreeNode<T> empty() {
        return DefaultMutableTreeNode.empty();
    }
}
