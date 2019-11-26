package dev.iamfoodie.rxjava.data;

import java.util.ArrayList;
import java.util.List;

import dev.iamfoodie.rxjava.models.Task;

public class DataSource {

    public static List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        tasks.add(new Task("Take out the trash", 3, true));
        tasks.add(new Task("Complete the notes app", 2, false));
        tasks.add(new Task("Start reading for final exams...", 5, false));
        tasks.add(new Task("Take custom views course on PluralSight", 3, false));
        tasks.add(new Task("Charge your phone", 1, true));

        return tasks;
    }

}
