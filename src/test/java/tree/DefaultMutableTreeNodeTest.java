package tree;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMutableTreeNodeTest {

    @Test
    void treeNode() {
        MutableTreeNode<String> root = MutableTreeNode.create("root");
        MutableTreeNode<String> child1 = MutableTreeNode.create("child1");
        MutableTreeNode<String> child2 = MutableTreeNode.create("child2");
        MutableTreeNode<String> child3 = MutableTreeNode.create("child3");
        root.add(child1);
        root.add(child2);
        root.add(child3);

        MutableTreeNode<String> child1_1 = MutableTreeNode.create("child1_1");
        child1.add(child1_1);
        MutableTreeNode<String> child1_2 = MutableTreeNode.create("child1_2");
        child1.add(child1_2);
        MutableTreeNode<String> child2_1 = MutableTreeNode.create("child2_1");
        child2.add(child2_1);

        assertEquals(root, TreeNodes.getRoot(child3));
        assertEquals(Arrays.asList(child3, child1_1, child1_2, child2_1).size(), TreeNodes.getAllLeaves(root).size());
        assertEquals(Arrays.asList(root, child2, child2_1), TreeNodes.pathFromRoot(child2_1));

        assertEquals(child2_1, TreeNodes.searchFirst(root, "child2_1").orElseThrow());
        assertNull(TreeNodes.searchFirst(root, "notChild").orElse(null));

        assertEquals(Arrays.asList(child1_1, child1_2, child2_1, child3), TreeNodes.searchAll(root, TreeVisitOption.depthFirst(), TreeNode::isLeaf));
        assertEquals(Arrays.asList(child3, child1_1, child1_2, child2_1), TreeNodes.searchAll(root, TreeVisitOption.breadthFirst(), TreeNode::isLeaf));

    }


    @Test
    void equalsAndHashCode() {
        TreeNode<Department> node1 = departmentChart1();
        TreeNode<Department> node2 = departmentChart2();

        assertEquals(node1, node2);
    }

    private TreeNode<Department> departmentChart1() {
        List<Department> departments = getDepartments();
        TreeNode<Department> treeNode = departments.stream().collect(TreeCollectors.id(Department::getDepartmentCode).parentId(Department::getUpperDepartmentCode).root(i -> i.getUpperDepartmentCode() == null).toTreeNode());
        System.out.println(TreeNodes.toString(treeNode));
        return treeNode;
    }

    private TreeNode<Department> departmentChart2() {
        List<Department> departments = getDepartments();
        TreeNode<Department> treeNode = departments.stream().parallel().collect(TreeCollectors.id(Department::getDepartmentCode).parentId(Department::getUpperDepartmentCode).toTreeNode());
        System.out.println(TreeNodes.toString(treeNode));
        return treeNode;
    }

    private List<Department> getDepartments() {
        Department d1 = new Department();
        d1.setDepartmentCode("CORP");
        d1.setDepartmentName("Corporation");

        Department d2 = new Department();
        d2.setDepartmentCode("CENTER");
        d2.setDepartmentName("Center");
        d2.setUpperDepartmentCode("CORP");

        Department d3 = new Department();
        d3.setDepartmentCode("TEAM1");
        d3.setDepartmentName("Team1");
        d3.setUpperDepartmentCode("CENTER");

        Department d4 = new Department();
        d4.setDepartmentCode("TEAM2");
        d4.setDepartmentName("Team2");
        d4.setUpperDepartmentCode("CENTER");

        Department d5 = new Department();
        d5.setDepartmentCode("HR");
        d5.setDepartmentName("HumanResource");
        d5.setUpperDepartmentCode("CORP");

        return List.of(d1, d2, d3, d4, d5);
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    static class Department {
        private String departmentCode;
        private String departmentName;
        private String upperDepartmentCode;
    }
}
