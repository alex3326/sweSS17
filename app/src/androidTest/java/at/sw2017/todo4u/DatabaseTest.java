package at.sw2017.todo4u;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.sw2017.todo4u.database.TaskCategoriesDataSource;
import at.sw2017.todo4u.database.TasksDataSource;
import at.sw2017.todo4u.database.Todo4uContract;
import at.sw2017.todo4u.database.Todo4uDbHelper;
import at.sw2017.todo4u.model.Task;
import at.sw2017.todo4u.model.TaskCategory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class DatabaseTest {
    private Context instrumentationCtx;

    @Before
    public void setup() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
        TestHelper.clearDatabase();
    }

    @After
    public void tearDown() {
        TestHelper.clearDatabase();
    }

    @Test
    public void simpleInsertAndReadNoNull() {
        String categoryName = "My Test Category";
        String taskTitle = "My First TEST TASK";
        String taskDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et tempus magna. Fusce dui odio, cursus quis nisi ac, luctus iaculis nulla. In mollis eget nibh et mattis. Morbi quis sapien quam. Nullam a ullamcorper magna. Integer pellentesque ipsum facilisis eros hendrerit posuere sed in ante. Donec sodales massa non neque vulputate, sagittis ullamcorper eros vehicula.";
        Calendar dueCal = Calendar.getInstance();
        dueCal.set(2017, 5, 10, 9, 30);
        Calendar remindCal = Calendar.getInstance();
        remindCal.set(2017, 5, 9, 14, 45);
        Date creationDate = new Date();

        TaskCategory cat = new TaskCategory(categoryName);
        Task task = new Task();
        task.setCategory(cat);
        task.setTitle(taskTitle);
        task.setDescription(taskDescription);
        task.setDueDate(dueCal.getTime());
        task.setReminderDate(remindCal.getTime());
        task.setCreationDate(creationDate);
        task.setProgress(52);

        TaskCategoriesDataSource tcDs = new TaskCategoriesDataSource(instrumentationCtx);
        tcDs.open();
        assertTrue(tcDs.insertOrUpdate(cat));
        tcDs.close();


        TasksDataSource tDs = new TasksDataSource(instrumentationCtx);
        tDs.open();
        assertTrue(tDs.insertOrUpdate(task));

        Task dbTask = tDs.getById(task.getId());
        assertNotNull(dbTask);

        assertEquals(task.getId(), dbTask.getId());
        assertEquals(cat.getId(), dbTask.getCategory().getId());
        assertEquals(categoryName, dbTask.getCategory().getName());
        assertEquals(taskTitle, dbTask.getTitle());
        assertEquals(taskDescription, dbTask.getDescription());
        assertEquals(dueCal.getTime(), dbTask.getDueDate());
        assertEquals(remindCal.getTime(), dbTask.getReminderDate());
        assertEquals(creationDate, dbTask.getCreationDate());
        assertEquals(remindCal.getTime(), dbTask.getReminderDate());
        assertEquals(52, dbTask.getProgress());

        List<Task> tasksInCategory = tDs.getTasksInCategory(cat);

        assertNotNull(tasksInCategory);
        assertEquals(1, tasksInCategory.size());
        assertEquals(task.getId(), tasksInCategory.get(0).getId());

        tDs.close();
    }

    @Test
    public void simpleInsertAndReadManyNull() {
        String categoryName = "My Null Category";
        String taskTitle = "My Null TASK";
        String taskDescription = "Lorem ipsum dolor sit NULLPOINTER.";

        TaskCategory cat = new TaskCategory(categoryName);
        Task task = new Task();
        task.setCategory(cat);
        task.setTitle(taskTitle);
        task.setDescription(taskDescription);

        TaskCategoriesDataSource tcDs = new TaskCategoriesDataSource(instrumentationCtx);
        tcDs.open();
        assertTrue(tcDs.insertOrUpdate(cat));
        tcDs.close();


        TasksDataSource tDs = new TasksDataSource(instrumentationCtx);
        tDs.open();
        assertTrue(tDs.insertOrUpdate(task));

        Task dbTask = tDs.getById(task.getId());
        assertNotNull(dbTask);

        assertEquals(task.getId(), dbTask.getId());
        assertEquals(cat.getId(), dbTask.getCategory().getId());
        assertEquals(categoryName, dbTask.getCategory().getName());
        assertEquals(taskTitle, dbTask.getTitle());
        assertEquals(taskDescription, dbTask.getDescription());
        assertNull(dbTask.getDueDate());
        assertNull(dbTask.getReminderDate());
        assertNull(dbTask.getCreationDate());
        assertEquals(0, dbTask.getProgress());

        List<Task> tasksInCategory = tDs.getTasksInCategory(cat);

        assertNotNull(tasksInCategory);
        assertEquals(1, tasksInCategory.size());
        assertEquals(task.getId(), tasksInCategory.get(0).getId());

        tDs.close();
    }

    @Test
    public void simpleInsertAndReadDescriptionAndCategoryNull() {
        String taskTitle = "My Null Null TASK";

        Task task = new Task();
        task.setTitle(taskTitle);

        TasksDataSource tDs = new TasksDataSource(instrumentationCtx);
        tDs.open();
        assertTrue(tDs.insertOrUpdate(task));

        Task dbTask = tDs.getById(task.getId());
        assertNotNull(dbTask);

        assertEquals(task.getId(), dbTask.getId());
        assertNull(dbTask.getCategory());
        assertEquals(taskTitle, dbTask.getTitle());
        assertNull(dbTask.getDescription());
        assertNull(dbTask.getDueDate());
        assertNull(dbTask.getReminderDate());
        assertNull(dbTask.getCreationDate());
        assertEquals(0, dbTask.getProgress());

        tDs.close();
    }

    @Test
    public void openCategoryDSBeforeReading() {
        String categoryName = "My Null Category";
        String taskTitle = "My Null TASK";
        String taskDescription = "Lorem ipsum dolor sit NULLPOINTER.";

        TaskCategory cat = new TaskCategory(categoryName);
        Task task = new Task();
        task.setCategory(cat);
        task.setTitle(taskTitle);
        task.setDescription(taskDescription);

        TaskCategoriesDataSource tcDs = new TaskCategoriesDataSource(instrumentationCtx);
        tcDs.open();
        assertTrue(tcDs.insertOrUpdate(cat));


        TasksDataSource tDs = new TasksDataSource(instrumentationCtx);
        tDs.open();
        assertTrue(tDs.insertOrUpdate(task));

        Task dbTask = tDs.getById(task.getId());
        assertNotNull(dbTask);

        assertEquals(task.getId(), dbTask.getId());
        assertEquals(cat.getId(), dbTask.getCategory().getId());
        assertEquals(categoryName, dbTask.getCategory().getName());
        assertEquals(taskTitle, dbTask.getTitle());
        assertEquals(taskDescription, dbTask.getDescription());
        assertNull(dbTask.getDueDate());
        assertNull(dbTask.getReminderDate());
        assertNull(dbTask.getCreationDate());
        assertEquals(0, dbTask.getProgress());

        List<Task> tasksInCategory = tDs.getTasksInCategory(cat);

        assertNotNull(tasksInCategory);
        assertEquals(1, tasksInCategory.size());
        assertEquals(task.getId(), tasksInCategory.get(0).getId());

        tDs.close();
        tcDs.close();
    }

    @Test
    public void insertUpdateDelete() {
        String categoryName = "My Test Category 2";
        String taskTitle = "My First TEST TASK";
        String taskDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et tempus magna. Fusce dui odio, cursus quis nisi ac, luctus iaculis nulla. In mollis eget nibh et mattis. Morbi quis sapien quam. Nullam a ullamcorper magna. Integer pellentesque ipsum facilisis eros hendrerit posuere sed in ante. Donec sodales massa non neque vulputate, sagittis ullamcorper eros vehicula.";
        Calendar dueCal = Calendar.getInstance();
        dueCal.set(2017, 5, 10, 9, 30);
        Calendar remindCal = Calendar.getInstance();
        remindCal.set(2017, 5, 9, 14, 45);
        Date creationDate = new Date();

        TaskCategory cat = new TaskCategory(categoryName);
        Task task = new Task();
        task.setCategory(cat);
        task.setTitle(taskTitle);
        task.setDescription(taskDescription);

        TaskCategoriesDataSource tcDs = new TaskCategoriesDataSource(instrumentationCtx);
        tcDs.open();
        assertTrue(tcDs.insertOrUpdate(cat));
        tcDs.close();


        TasksDataSource tDs = new TasksDataSource(instrumentationCtx);
        tDs.open();
        assertTrue(tDs.insertOrUpdate(task));

        Task dbTask = tDs.getById(task.getId());
        assertNotNull(dbTask);

        assertEquals(task.getId(), dbTask.getId());
        assertEquals(cat.getId(), dbTask.getCategory().getId());
        assertEquals(categoryName, dbTask.getCategory().getName());
        assertEquals(taskTitle, dbTask.getTitle());
        assertEquals(taskDescription, dbTask.getDescription());
        assertNull(dbTask.getDueDate());
        assertNull(dbTask.getReminderDate());
        assertNull(dbTask.getCreationDate());
        assertEquals(0, dbTask.getProgress());

        String taskDescription2 = "The new description.";
        String taskTitle2 = "The new title.";

        dbTask.setDescription(taskDescription2);
        dbTask.setTitle(taskTitle2);
        dbTask.setDueDate(dueCal.getTime());
        dbTask.setReminderDate(remindCal.getTime());
        dbTask.setCreationDate(creationDate);
        dbTask.setProgress(100);

        tDs.insertOrUpdate(dbTask);

        dbTask = tDs.getById(task.getId());
        assertNotNull(dbTask);

        assertEquals(task.getId(), dbTask.getId());
        assertEquals(cat.getId(), dbTask.getCategory().getId());
        assertEquals(categoryName, dbTask.getCategory().getName());
        assertEquals(taskTitle2, dbTask.getTitle());
        assertEquals(taskDescription2, dbTask.getDescription());
        assertEquals(dueCal.getTime(), dbTask.getDueDate());
        assertEquals(remindCal.getTime(), dbTask.getReminderDate());
        assertEquals(creationDate, dbTask.getCreationDate());
        assertEquals(remindCal.getTime(), dbTask.getReminderDate());
        assertTrue(dbTask.isFinished());

        List<Task> tasksInCategory = tDs.getTasksInCategory(cat);

        assertNotNull(tasksInCategory);
        assertEquals(1, tasksInCategory.size());
        assertEquals(task.getId(), tasksInCategory.get(0).getId());

        assertTrue(tDs.delete(dbTask));
        assertFalse(tDs.delete(task.getId()));

        tDs.close();
    }

    @Test
    public void getAllTasks() {
        Todo4uDbHelper helper = new Todo4uDbHelper(instrumentationCtx);
        helper.onDowngrade(helper.getWritableDatabase(), 1, 0);
        helper.close();

        TasksDataSource tDs = new TasksDataSource(instrumentationCtx);
        tDs.open();
        assertEquals(0, tDs.getAll().size());
        tDs.close();


        TaskCategory tc = new TaskCategory("My Cat");
        TaskCategoriesDataSource tcDs = new TaskCategoriesDataSource(instrumentationCtx);
        assertFalse(tcDs.isDatabaseOpen());
        assertFalse(tcDs.isDatabaseWritable());
        tcDs.open();
        assertTrue(tcDs.isDatabaseOpen());
        assertTrue(tcDs.isDatabaseWritable());
        assertTrue(tcDs.insertOrUpdate(tc));
        assertNotEquals(0, tc.getId());
        tcDs.close();

        Task t1 = new Task("T1");
        t1.setCategory(tc);

        Task t2 = new Task("T2");
        t2.setCategory(tc);

        tDs.open();
        assertTrue(tDs.isDatabaseWritable());

        assertTrue(tDs.insertOrUpdate(t1));
        assertTrue(tDs.insertOrUpdate(t2));

        List<Task> allTasks = tDs.getAll();
        List<Task> categoryNullTasks = tDs.getTasksInCategory(null);
        tDs.close();

        assertNotNull(allTasks);
        assertEquals(2, allTasks.size());
        assertEquals(t1, allTasks.get(0));
        assertEquals(t2, allTasks.get(1));

        assertNotNull(categoryNullTasks);
        assertEquals(2, categoryNullTasks.size());
        assertEquals(t1, categoryNullTasks.get(0));
        assertEquals(t2, categoryNullTasks.get(1));

    }
}
