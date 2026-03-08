package com.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;
import java.io.FileWriter;

public class Task {
    private File file = new File("todolist.json");

    // method to show all data (Read)
    public void readTasks() throws Exception {

        // if file exists
        if( file.exists() ) {
            // create Jackson instance to serialize JSON -> Object
            ObjectMapper mapper = new ObjectMapper();
            // store JSON tree into JsonNode to check if data exists
            JsonNode root = mapper.readTree(file);
            // check if JSON empty
            if (!root.isEmpty()) {
                List<JSONData> tasks = mapper.readValue(
                        file, new TypeReference<List<JSONData>>() {}
                );
                for (JSONData task : tasks) {
                    System.out.print("Id (" + task.getId() + ") ");
                    System.out.println(task.getDescription());
                    System.out.println("Created at: " + task.getCreatedAt());
                    System.out.println("Updated at: " + task.getUpdatedAt());
                    System.out.println("Status: " + task.getStatus());
                }
            } else {
                System.out.println("file is empty!");
            }
        } else {
            try{
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    // read all tasks todo
    public void readTasks(String status) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        if (!root.isEmpty()) {
            List<JSONData> tasks = mapper.readValue(file, new TypeReference<List<JSONData>>() {});
            for (JSONData task : tasks) {
                if (status.equals(task.getStatus())) {
                    System.out.print("Id (" + task.getId() + ") ");
                    System.out.println(task.getDescription());
                    System.out.println("Created at: " + task.getCreatedAt());
                    System.out.println("Updated at: " + task.getUpdatedAt());
                    System.out.println("Status: " + task.getStatus());
                } else {
                    System.out.println("Data don't exist");
                    break;
                }
            }
        }
    }


    // method to add new task
    public void addTask(String description) throws Exception{
        // create date
        LocalDate date = LocalDate.now();

        // convert date to string
        String dateString = date.toString();


        // if file exists
        if( file.exists() ) {
            // create Jackson instance
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(file);

            if(root.isEmpty()) {
                try(FileWriter fileWriter = new FileWriter("todolist.json")){
                    fileWriter.write("[]");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            // read all JSON's data and store it into List before adding a new data
            List<JSONData> tasks = mapper.readValue(file, new TypeReference<List<JSONData>>() {});

            int id;
            // create id
            if (!tasks.isEmpty()) {
                id = tasks.getLast().getId() + 1;
            } else {
                id = 1;
            }

            // add new task into tasks List
            tasks.add(new JSONData(id, description, "todo", dateString, dateString));

            // serialize it back into JSON and write it back into file JSON with a clean writing
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, tasks);
            System.out.println("successful to add new task");
        } else {
            try{
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // method to delete a task
    public void deleteTask(int id) throws Exception {

        // if file exists
        if(file.exists()) {
            // create instance tool (Jackson) to serialize and deserialize
            ObjectMapper mapper = new ObjectMapper();
            // variable to check if data exists
            ArrayNode tasks = (ArrayNode) mapper.readTree(file);

            if(!tasks.isEmpty()) {
                for (int i = 0; i < tasks.size(); i++) {
                    // create a variable to store Array into single Object
                    JsonNode task = tasks.get(i);
                    // check matches data with user's input
                    if(task.get("id").asInt() == id) {
                        // delete matches data
                        tasks.remove(i);
                        System.out.println("successful delete data with id " + id);
                        break;
                    }
                }
                // serialize Object into JSON and write it into file
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, tasks);
            } else {
                System.err.println("Data don't exist, create new task first!");
            }
        } else {
            System.err.println("file don't exist, create new task first!");
        }
    }

    // method to update JSON
    public void updateTask(int updateId, int updateStatus) throws Exception {

        // create instance tool (Jackson) to serialize and deserialize
        ObjectMapper mapper = new ObjectMapper();

        // ArrayNode to manipulate JSON data
        List<JSONData> tasksMapper = mapper.readValue(file, new TypeReference<List<JSONData>>() {});

        ArrayNode tasks = (ArrayNode) mapper.readTree(file);

        if(!tasks.isEmpty()) {
            for(int i = 0; i < tasks.size(); i++ ) {
                ObjectNode task = (ObjectNode) tasks.get(i);
                if (task.get("id").asInt() == updateId) {
                    switch (updateStatus) {
                        case 1:
                            task.put("status", "todo");
                            break;
                        case 2:
                            task.put("status", "in progress");
                            break;
                        case 3:
                            task.put("status", "done");
                            break;
                        default:
                            System.out.println("False!");
                            break;
                    }
                }
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, tasks);
            System.out.println("success update.");
        } else {
            System.out.println("File is empty!");
        }
    }
}

// POJOs Class to store JSON from
class JSONData {
    // class to hold JSON's values
    private int id;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;

    public JSONData () {}
    public JSONData(int id, String description, String status, String createdAt, String updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return this.id;
    }
    public void setId(int id) { this.id = id; }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() {
        return this.updatedAt;
    }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }


}

