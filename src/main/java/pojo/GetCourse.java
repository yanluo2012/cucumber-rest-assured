package pojo;

import java.util.List;
import java.util.Map;

public class GetCourse {
    public String instructor;
    public String url;
    public String services;
    public String expertise;
    public Map<String, List<Course>> courses;
    public String linkedIn;

    public static class Course {
        public String courseTitle;
        public String price;
    }
}
