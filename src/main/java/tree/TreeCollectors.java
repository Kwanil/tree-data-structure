package tree;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class TreeCollectors {
    private TreeCollectors(){
        throw new UnsupportedOperationException("initialize error");
    }

    public static <T,PK> TreeCollectorBuilder.IdFunction<T,PK> tree() {
        return id->parentId->root->childAppender->new TreeCollectorBuilder(id,parentId,root,childAppender);
    }

    public static <T,PK> TreeNodeCollectorBuilder.IdFunction<T,PK> treeNode() {
        return id->parentId->root->new TreeNodeCollectorBuilder(id,parentId,root);
    }

    public static class TreeNodeCollectorBuilder<T, PK> {
        private final Predicate<? super T> rootPredicate;
        private final Function<? super T,? extends PK> id;
        private final Function<? super T,? extends PK> parentId;

        private TreeNodeCollectorBuilder(
                Function<? super T, ? extends PK> id,
                Function<? super T, ? extends PK> parentId,
                Predicate<? super T> rootPredicate) {
            this.rootPredicate = Objects.requireNonNull(rootPredicate);
            this.id = Objects.requireNonNull(id);
            this.parentId = Objects.requireNonNull(parentId);
        }

        public interface RootPredicate<T,PK> {
            TreeNodeCollectorBuilder<T,PK> root(Predicate<? super T> rootPredicate);
        }

        public interface IdFunction<T,PK> {
            ParentIdFunction<T,PK> id(Function<? super T,? extends PK> id);
        }

        public interface ParentIdFunction<T,PK> {
            RootPredicate<T,PK> parentId(Function<? super T,? extends PK> parentId);
        }

        public Collector<T,?,TreeNode<T>> toTree() {
            return Collectors.collectingAndThen(
                    Collectors.partitioningBy(rootPredicate, Collectors.groupingBy(parentId)),
                    new ResultFunction());
        }

        private class ResultFunction implements Function<Map<Boolean, Map<PK, List<T>>>, TreeNode<T>>{

            @Override
            public TreeNode<T> apply(Map<Boolean, Map<PK, List<T>>> map) {
                T root = root(map);
                Map<PK, List<T>> parentIdMap = parentIdMap(map);
                return new ChildrenAppenderFunction(parentIdMap).apply(root);
            }

            private T root(Map<Boolean, Map<PK, List<T>>> map) throws NoSuchElementException {
                Map.Entry<PK, List<T>> rootMap = map.get(true).entrySet().stream().findFirst().get();
                return rootMap.getValue().stream().findFirst().get();
            }

            private Map<PK, List<T>> parentIdMap(Map<Boolean, Map<PK, List<T>>> map) {
                return map.get(false);
            }
        }

        private class ChildrenAppenderFunction implements Function<T,TreeNode<T>> {
            private final Map<PK, List<T>> parentIdMap;

            private ChildrenAppenderFunction(Map<PK, List<T>> parentIdMap) {
                this.parentIdMap = parentIdMap;
            }

            @Override
            public TreeNode<T> apply(T t) {
                TreeNode<T> root = new TreeNode<>(t);
                addChild(root);
                return root;
            }

            private void addChild(TreeNode<T> root) {
                List<T> children = parentIdMap.remove(id.apply(root.get()));
                if (children != null) {
                    for(T child : children) {
                        this.addChild(new TreeNode<>(root, child));
                    }
                }
            }
        }
    }

    public static class TreeCollectorBuilder<T, PK> {
        private final Predicate<? super T> rootPredicate;
        private final Function<? super T,? extends PK> id;
        private final Function<? super T,? extends PK> parentId;
        private final BiConsumer<? super T, List<? super T>> childAppender;

        private TreeCollectorBuilder(
                Function<? super T, ? extends PK> id,
                Function<? super T, ? extends PK> parentId,
                Predicate<? super T> rootPredicate,
                BiConsumer<? super T, List<? super T>> childAppender) {
            this.rootPredicate = Objects.requireNonNull(rootPredicate);
            this.id = Objects.requireNonNull(id);
            this.childAppender = Objects.requireNonNull(childAppender);
            this.parentId = Objects.requireNonNull(parentId);
        }

        public interface RootPredicate<T,PK> {
            ChildrenAppender<T,PK> root(Predicate<? super T> rootPredicate);
        }

        public interface IdFunction<T,PK> {
            ParentIdFunction<T,PK> id(Function<? super T,? extends PK> id);
        }

        public interface ParentIdFunction<T,PK> {
            RootPredicate<T,PK> parentId(Function<? super T,? extends PK> parentId);
        }

        public interface ChildrenAppender<T,PK> {
            TreeCollectorBuilder<T,PK> childrenAppend(BiConsumer<? super T, List<T>> childAppender);
        }

        public Collector<T,?,T> toTree() {
            return Collectors.collectingAndThen(
                    Collectors.partitioningBy(rootPredicate, Collectors.groupingBy(parentId)),
                    new ResultFunction());
        }

        private class ResultFunction implements Function<Map<Boolean, Map<PK, List<T>>>, T>{

            @Override
            public T apply(Map<Boolean, Map<PK, List<T>>> map) {
                T root = root(map);
                Map<PK, List<T>> parentIdMap = parentIdMap(map);
                return new ChildrenAppenderFunction(parentIdMap).apply(root);
            }

            private T root(Map<Boolean, Map<PK, List<T>>> map) throws NoSuchElementException {
                Map.Entry<PK, List<T>> rootMap = map.get(true).entrySet().stream().findFirst().get();
                return rootMap.getValue().stream().findFirst().get();
            }

            private Map<PK, List<T>> parentIdMap(Map<Boolean, Map<PK, List<T>>> map) {
                return map.get(false);
            }
        }

        private class ChildrenAppenderFunction implements Function<T,T> {
            private final Map<PK, List<T>> parentIdMap;

            private ChildrenAppenderFunction(Map<PK, List<T>> parentIdMap) {
                this.parentIdMap = parentIdMap;
            }

            @Override
            public T apply(T t) {
                addChild(t);
                return t;
            }

            private void addChild(T root) {
                List<T> children = parentIdMap.remove(id.apply(root));
                if (children != null) {
                    childAppender.accept(root, children);
                    children.forEach(this::addChild);
                }
            }
        }
    }
}