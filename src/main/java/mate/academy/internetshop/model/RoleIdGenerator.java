package mate.academy.internetshop.model;

public class RoleIdGenerator {
    private static Long roleId = 0L;

    public static Long generateRoleId() {
        return roleId++;
    }
}
