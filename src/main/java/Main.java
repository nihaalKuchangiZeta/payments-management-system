import menu.MenuRouter;
import model.User;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to KEL:Payments Management System");
        UserService userService = new UserService();
        User user = userService.userLoginIn();
        MenuRouter.showMenu(user);
    }
}
