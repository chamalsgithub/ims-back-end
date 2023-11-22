package lk.ijse.dep11.Ims.api;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.Ims.to.CourseTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin
public class CourseHttpController {

    private final HikariDataSource pool;

    public CourseHttpController() {
        HikariConfig config = new HikariConfig();
        config.setUsername("root");
        config.setPassword("1234");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/dep11_ims");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.addDataSourceProperty("maximumPoolSize",10);
        pool = new HikariDataSource(config);
    }

    @PreDestroy
    public void destroy(){
        pool.close();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    public CourseTO createCourse(@RequestBody @Validated CourseTO course){
        try {
            Connection connection = pool.getConnection();
            PreparedStatement stm = connection.prepareStatement("INSERT INTO courses(name, duration_in_months) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, course.getName());
            stm.setInt(2, course.getDurationInMonths());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int courseId = generatedKeys.getInt(1);
            course.setId(courseId);
            return course;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{courseId}", consumes = "application/json")
    public void updateCourse(@PathVariable int courseId,
            @RequestBody @Validated CourseTO course){
        try(Connection connection = pool.getConnection()){
            PreparedStatement stmExist = connection.prepareStatement("SELECT * FROM courses WHERE id=?");
            stmExist.setInt(1,courseId);
            if(!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }

            PreparedStatement stm = connection.prepareStatement("UPDATE courses SET name = ?, duration_in_months=? WHERE id = ?");
            stm.setString(1,course.getName());
            stm.setInt(2,course.getDurationInMonths());
            stm.setInt(3, courseId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable int courseId){
        try(Connection connection = pool.getConnection()){
            PreparedStatement stmExist = connection.prepareStatement("SELECT * FROM courses WHERE id=?");
            stmExist.setInt(1,courseId);
            if(!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }

            PreparedStatement stm = connection.prepareStatement("DELETE FROM courses WHERE id = ?");
            stm.setInt(1,courseId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @GetMapping(value = "/{courseId}", produces = "application/json")
    public CourseTO getCourseDetails(@PathVariable int courseId){
        try(Connection connection = pool.getConnection()){
            PreparedStatement stmExist = connection.prepareStatement("SELECT * FROM courses WHERE id=?");
            stmExist.setInt(1,courseId);
            ResultSet rst = stmExist.executeQuery();
            if(!rst.next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }
            CourseTO course = new CourseTO();
            course.setId(courseId);
            course.setName(rst.getString("name"));
            course.setDurationInMonths(rst.getInt("duration_in_months"));

            return course;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping(produces = "application/json")
    public List<CourseTO> getAllCourses(){
        try {
            Connection connection = pool.getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM courses ORDER BY id");
            LinkedList<CourseTO> courseList = new LinkedList<>();

            while (rst.next()){
                int id = rst.getInt("id");
                String name = rst.getString("name");
                int durationInMonths = rst.getInt("duration_in_months");
                courseList.add(new CourseTO(id, name, durationInMonths));
            }
            return courseList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
