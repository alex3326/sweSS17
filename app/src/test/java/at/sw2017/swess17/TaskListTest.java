package at.sw2017.swess17;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.sw2017.swess17.application.Task;
import at.sw2017.swess17.application.TaskList;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TaskListTest {

    @Test
    public void taskCreation_isCorrect() throws Exception {
        Date date = new Date();
        Task task = new Task("Task1", "Only a task", date);

        assertEquals(task.getName(),"Task1");
        assertEquals(task.getText_description(),"Only a task");
        assertEquals(task.getDate(),date);
    }

    public void taskList_isCorrect() throws Exception {
        Date date = new Date();
        Task task1 = new Task("Task1", "Only a task", date);
        Task task2 = new Task("Task2", "Only a task", date);
        Task task3 = new Task("Task3", "Only a task", date);

        TaskList<Task> taskList = new TaskList<Task>();
        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);

        assertEquals(taskList.size(),3);
    }
}