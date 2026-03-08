package com.company;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception{
        String repeat;
        do {
            // Object of Scanner package
            Scanner scannerObj = new Scanner(System.in);


            // show menus to user
            System.out.println("=== To do list ===");
            System.out.println("1. Show all tasks");
            System.out.println("2. Add new tasks");
            System.out.println("3. Update tasks");
            System.out.println("4. Delete tasks");
            System.out.println("5. Show all tasks that are to do");
            System.out.println("6. Show all tasks that are in progress");
            System.out.println("7. Show all tasks that are done");
            System.out.print("Chose your action: ");
            // take user input
            int userChoice = scannerObj.nextInt();

            // functional object declare
            Task task = new Task();

            // checker for delete and update json
            ObjectMapper mapper = new ObjectMapper();
            JsonNode test = mapper.readTree(new File("todolist.json"));

            switch (userChoice) {
                case 1:
                    task.readTasks();
                    break;
                case 2:
                    Scanner scannerDescription = new Scanner(System.in);
                    System.out.print("Description: ");
                    String description = scannerDescription.nextLine();
                    task.addTask(description);
                    break;
                case 3:
                    task.readTasks();

                    if(!test.isEmpty()) {
                        Scanner scannerUpdate = new Scanner(System.in);
                        Scanner scannerStatus = new Scanner(System.in);
                        System.out.print("Update task with id: ");
                        int updateId = scannerUpdate.nextInt();

                        System.out.println("1. todo");
                        System.out.println("2. in progress");
                        System.out.println("3. done");
                        System.out.print("Change status: ");
                        int updateStatus = scannerStatus.nextInt();

                        task.updateTask(updateId, updateStatus);
                    }

                    break;
                case 4:
                    task.readTasks();
                    if (!test.isEmpty()) {
                        Scanner scannerDelete = new Scanner(System.in);
                        System.out.println("Delete task with id: ");
                        int delete = scannerDelete.nextInt();
                        task.deleteTask(delete);
                    }
                    break;
                case 5:
                    task.readTasks("todo");
                    break;
                case 6:
                    task.readTasks("in progress");
                    break;
                case 7:
                    task.readTasks("done");
                    break;
                default:
                    System.out.print("wrong input, type 1 - 7");
                    break;
            }

            System.out.print("Do you want to do it again? (y/n) ");
            Scanner repeatScanner = new Scanner(System.in);
            repeat = repeatScanner.nextLine();
        } while (repeat.equals("y"));
    }
}
