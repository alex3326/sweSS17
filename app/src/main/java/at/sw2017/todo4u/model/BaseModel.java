package at.sw2017.todo4u.model;

public abstract class BaseModel {
    private long id = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseModel baseModel = (BaseModel) o;

        return id == baseModel.id;

    }

    @Override
    public final int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

}
