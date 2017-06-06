package at.sw2017.todo4u.model;

public class Setting extends BaseModel {
    private String key;
    private int value = 0;

    public Setting() {
    }

    public Setting(String key) {
        this.key = key;
    }

    public Setting(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + getId() +
                ", key='" + key + '\'' +
                ", value=" + value +
                '}';
    }

    public static String KEY_CATEGORY_COLOR_OPTION = "Category-Color-Option";
}
