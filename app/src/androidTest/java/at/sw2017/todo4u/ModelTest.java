package at.sw2017.todo4u;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import at.sw2017.todo4u.model.Task;
import at.sw2017.todo4u.model.TaskCategory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ModelTest {
    @Test
    public void taskGetter() {
        String title = "Mein Task 42";
        Task t = new Task(title);
        assertEquals(title, t.getTitle());
        assertNull(t.getDescription());
        assertNull(t.getDueDate());
        assertNull(t.getDueDateAsNumber());
        assertNull(t.getCreationDate());
        assertNull(t.getCreationDateAsNumber());
        assertNull(t.getReminderDate());
        assertNull(t.getReminderDateAsNumber());
        assertNull(t.getCategory());
        assertNull(t.getState());
        assertEquals(0, t.getId());
        assertEquals("Task{id=0, title='Mein Task 42', description='null', dueDate=null, creationDate=null, reminderDate=null, category=null, state=null}", t.toString());
    }

    @Test
    public void taskEquals() {
        Task t1 = new Task("T");
        t1.setId(9);

        Task t2 = new Task("X");
        t2.setId(9);

        Task t3 = new Task("T");
        t3.setId(3);

        assertTrue(t1.equals(t1));
        assertTrue(t1.equals(t2));
        assertTrue(t2.equals(t2));
        assertTrue(t2.equals(t1));
        assertFalse(t1.equals(new Object()));
        assertFalse(t1.equals(null));
        assertFalse(t1.equals(t3));
        assertFalse(t3.equals(t1));
    }

    @Test
    public void taskHashcode() {
        Task t1 = new Task("T");
        assertEquals(0, t1.hashCode());
        t1.setId(12);
        assertEquals(12, t1.hashCode());

    }

    @Test
    public void taskState() {
        Task t1 = new Task("T");
        t1.setState(0);
        assertEquals(Task.State.OPEN, t1.getState());
        t1.setState(1);
        assertEquals(Task.State.FINISHED, t1.getState());
        t1.setState(2);
        assertEquals(Task.State.IN_PROGRESS, t1.getState());
        t1.setState(Task.State.OPEN);
        assertEquals(0, t1.getStateId());
        t1.setState(Task.State.FINISHED);
        assertEquals(1, t1.getStateId());
        t1.setState(Task.State.IN_PROGRESS);
        assertEquals(2, t1.getStateId());

        assertEquals(Task.State.OPEN, Task.State.valueOf("OPEN"));
        assertEquals(Task.State.FINISHED, Task.State.valueOf("FINISHED"));
        assertEquals(Task.State.IN_PROGRESS, Task.State.valueOf("IN_PROGRESS"));
    }

    @Test
    public void taskCategory() {
        String name = "My Task Category";
        String name2 = "My New Task Category";
        TaskCategory tc = new TaskCategory(name);
        assertEquals(name, tc.getName());
        tc.setName(name2);
        assertEquals(name2, tc.getName());
        assertEquals("TaskCategory{id='0', name='My New Task Category'}", tc.toString());

    }

}
