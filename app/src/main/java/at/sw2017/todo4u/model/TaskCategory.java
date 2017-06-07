package at.sw2017.todo4u.model;

import android.util.SparseArray;

public class TaskCategory extends BaseModel {


    private String name;
    private CategoryColor color = CategoryColor.NONE;

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

    public CategoryColor getColor() {
        return color;
    }

    public void setColor(int colorId) {
        this.color = colorMap.get(colorId);
    }

    public int getColorId() {
        return color.colorId;
    }

    public void setColor(CategoryColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "TaskCategory{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", color=" + color +
                '}';
    }

    private static final SparseArray<CategoryColor> colorMap = new SparseArray<>();

    static {
        for (CategoryColor type : CategoryColor.values()) {
            colorMap.put(type.colorId, type);
        }
    }

    public enum CategoryColor {
        NONE(0), RED(1), GREEN(2), YELLOW(3), BLUE(4), CYAN(5);

        private final int colorId;

        CategoryColor(int colorId) {
            this.colorId = colorId;
        }

        public int getColorId() {
            return colorId;
        }
    }
}
