package com.example.house_rentalapplication;

public class YourModelClass {
    private String id;
    private String name;
    private int age;
    // Add other fields as needed

    // Empty constructor (required by Firestore)
    public YourModelClass() {
    }

    public YourModelClass(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        // Initialize other fields as needed
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
