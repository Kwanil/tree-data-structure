package tree;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.List;

class TreeNodeTest {

    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    static class Department {
        private String departmentCode;
        private String departmentName;
        private String upperDepartmentCode;
    }

    @Test
    void treeNode() {
        TreeNode<Department> departmentChart = departmentChart();
        System.out.println(departmentChart.isRoot());

    }

    private TreeNode<Department> departmentChart() {
        List<Department> departments = getDepartments();
        TreeNode<Department> treeNode = departments.stream().collect(TreeCollectors.id(Department::getDepartmentCode).parentId(Department::getUpperDepartmentCode).root(i -> i.getUpperDepartmentCode() == null).toTreeNode());
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
}
