import java.util.ArrayList;
import java.util.Scanner;

public class TodoListApp {
    private ArrayList<String> todoList = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public void displayList() {
        if (todoList.isEmpty()) {
            System.out.println("\nYour to-do list is empty.");
        } else {
            printHeader("To-Do List");
            for (int i = 0; i < todoList.size(); i++) {
                System.out.println(" [ ] " + todoList.get(i));
            }
            printFooter();
        }
    }

    public void addTask(String task) {
        todoList.add(task);
        System.out.println("\nTask added: " + task);
    }

    public void removeTask(int taskIndex) {
        if (isValidIndex(taskIndex)) {
            String removedTask = todoList.remove(taskIndex);
            System.out.println("\nTask removed: " + removedTask);
        } else {
            System.out.println("\nInvalid task index.");
        }
    }

    private boolean isValidIndex(int taskIndex) {
        return taskIndex >= 0 && taskIndex < todoList.size();
    }

    public void run() {
        int choice;

        do {
            clearScreen();
            printHeader("To-Do List");
            System.out.println(" [1] Display To-Do List");
            System.out.println(" [2] Add Task");
            System.out.println(" [3] Remove Task");
            System.out.println(" [4] Quit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayList();
                    break;
                case 2:
                    System.out.print("\nEnter the task to add: ");
                    String newTask = scanner.nextLine();
                    addTask(newTask);
                    break;
                case 3:
                    System.out.print("\nEnter the task index to remove: ");
                    int taskIndex = scanner.nextInt();
                    removeTask(taskIndex - 1);
                    break;
                case 4:
                    System.out.println("\nGoodbye!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }

            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        } while (choice != 4);

        scanner.close();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void printHeader(String title) {
        System.out.println("\n**********");
        System.out.println(" " + title);
        System.out.println("**********");
    }

    private void printFooter() {
        System.out.println("**********");
    }

    public static void main(String[] args) {
        TodoListApp app = new TodoListApp();
        app.run();
    }
}
