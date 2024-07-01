package tree;

import lombok.ToString;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * TreeNode로 {@link java.util.stream.Stream}에서 사용하기 위한 {@link Collector}를 제공 클래스이다.
 *
 * <pre>
 *     {@code
 *         List<Department> departments = findAllDepartments();
 *         TreeNode<Department> treeNode = departments.stream().collect(
 *              TreeCollectors
 *                  .id(Department::getDepartmentId)
 *                  .parentId(Department::getParentDepartmentId)
 *                  .root(i -> i.getParentDepartmentId() == null)
 *                  .toTreeNode()
*           );
 *     }
 * </pre>
 *
 * {@link Builder#root(Predicate)} {@link Builder#rootPredicate} 로,
 * root 노드가 여러 개가 나오는 경우에 {@link Builder#toTreeNodeList()}는 여러 {@code TreeNode}를 구성하여 {@code List<TreeNode<T>>}로 제공하고,
 * {@link Builder#toTreeNode()}의 경우는 한개의 최 상위의 Root노드를 임의로 하나 더 만들어 하위 노드로 등록하여 {@code TreeNode<T>}로 제공한다.
 *
 */
public class TreeCollectors {

    /**
     * Id를 가리키는 Function
     * @param id {@link TreeNode<T>}애서 T에서 id 가리키는 function
     * @return {@link Builder}
     * @param <T> TreeNode에 등록할 타입
     * @param <PK> id의 타입
     */
    public static <T,PK> Builder<T,PK> id(Function<? super T, ? extends PK> id) {
        return new Builder<>(id);
    }

    public static class Builder<T, PK> {
        private final Function<? super T,? extends PK> id;
        private Function<? super T,? extends PK> parentId;

        private final Predicate<T> defaultRootPredicate = t -> parentId.apply(t) == null;
        private Predicate<? super T> rootPredicate = defaultRootPredicate;

        public Builder(Function<? super T, ? extends PK> id) {
            this.id = Objects.requireNonNull(id);
        }

        /**
         * parentId를 가리키는 Function
         *
         * @param parentId {@link TreeNode<T>}애서 T에서 parentId 가리키는 function
         * @return {@link Builder}
         */
        public Builder<T, PK> parentId(Function<? super T, ? extends PK> parentId) {
            this.parentId = Objects.requireNonNull(parentId);
            return this;
        }

        /**
         * Root 노드의 조건, 등록하지 않은 경우엔 parentId가 null인 것을 루트노드 조건으로 한다.
         *
         * @param rootPredicate 루트 노드의 조건
         * @return {@link Builder}
         */
        public Builder<T, PK> root(Predicate<? super T> rootPredicate) {
            this.rootPredicate = Objects.requireNonNull(rootPredicate);
            return this;
        }

        private void nullCheck() {
            Objects.requireNonNull(id);
            Objects.requireNonNull(parentId);
            Objects.requireNonNull(rootPredicate);
        }

        public Collector<T, ?, TreeNode<T>> toTreeNode() {
            nullCheck();
            return Collector.of(() -> new TreeNodeAccumulator(), TreeNodeAccumulator::add, (left, right) -> {
                left.merge(right);
                return left;
            }, TreeNodeAccumulator::toTreeNode);
        }

        public Collector<T, ?, List<TreeNode<T>>> toTreeNodeList() {
            nullCheck();
            return Collector.of(() -> new TreeNodeAccumulator(), TreeNodeAccumulator::add, (left, right) -> {
                left.merge(right);
                return left;
            }, TreeNodeAccumulator::toTreeNodeList);
        }

        /**
         * {@link Collector}에서 Type Parameter A인 {@link Collector#accumulator()} 에 해당하는 클래스.
         * reduce 작업에서 쓰이는 변경 가능한 (mutable accumulation). 구현 세부 정보로 숨겼습니다.
         */
        @ToString
        private class TreeNodeAccumulator {
            private final Set<T> roots = new LinkedHashSet<>();
            private final Map<PK, List<T>> parentsIdMap = new HashMap<>();

            public void add(T node) {
                if(rootPredicate.test(node)) {
                    roots.add(node);
                } else {
                    PK p = parentId.apply(node);
                    parentsIdMap.computeIfAbsent(p, k -> new ArrayList<>()).add(node);
                }
            }

            public void merge(TreeNodeAccumulator nodes) {
                this.roots.addAll(nodes.roots);
                nodes.parentsIdMap.forEach((key, value) -> {
                    List<T> result = this.parentsIdMap.get(key) == null ? new ArrayList<>(): new ArrayList<>(this.parentsIdMap.get(key));
                    result.addAll(value);
                    this.parentsIdMap.put(key, result);
                });
            }


            private List<TreeNode<T>> toTreeNodeList() {
                List<TreeNode<T>> result = new ArrayList<>();
                for (T root : roots) {
                    MutableTreeNode<T> rootNode = MutableTreeNode.create(root);
                    addChild(rootNode);
                    result.add(rootNode);
                }
                return result;
            }

            private TreeNode<T> toTreeNode() {
                List<TreeNode<T>> nodes = toTreeNodeList();
                if(nodes.isEmpty()) {
                    return TreeNode.empty();
                }
                if(nodes.size() == 1) {
                    return nodes.get(0);
                }
                MutableTreeNode<T> root = MutableTreeNode.empty();
                for (TreeNode<T> node : nodes) {
                    root.add(node);
                }
                return root;
            }

            private void addChild(MutableTreeNode<T> root) {
                PK pk = id.apply(root.value());
                List<T> children = parentsIdMap.remove(pk);
                if (children != null) {
                    for(T child : children) {
                        MutableTreeNode<T> node = MutableTreeNode.create(child);
                        root.add(node);
                        addChild(node);
                    }
                }
            }
        }
    }


}
