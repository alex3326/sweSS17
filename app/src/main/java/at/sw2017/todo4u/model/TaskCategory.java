package at.sw2017.todo4u.model;

public class TaskCategory extends BaseModel {
    private String name;

    public TaskCategory() {
    }

    public TaskCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TaskCategory{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
