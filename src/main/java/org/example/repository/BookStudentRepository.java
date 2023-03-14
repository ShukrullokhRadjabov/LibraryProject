package org.example.repository;

import org.example.dto.Book;
import org.example.dto.Student;
import org.example.dto.StudentBook;
import org.example.enums.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

@Repository
public class BookStudentRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int takeBook(Integer bookId, Student student, int date) {
        studentBookCount(student.getBook_count()+1, student.getId());
        LocalDateTime returnedDate = now().plusDays(date);
        String sql = "insert into student_book(student_id, book_id, created_date, status, returned_date, duration) values('%s', '%s', '%s', '%s', '%s', '%s')";
        sql = String.format(sql, student.getId(), bookId, now(), String.valueOf(BookStatus.TAKEN),returnedDate, date);
        int n = jdbcTemplate.update(sql);
        return n;
    }
    public void studentBookCount(Integer bookCount, Integer id){
        String sql = "update student set book_count = '"+bookCount+"' where id = '"+id+"'";
        jdbcTemplate.update(sql);
    }

    public List<StudentBook> takenBook(Integer id) {
        String sql = "select * from student_book where student_id = '"+id+"'";
        List<StudentBook> studentBookList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StudentBook.class));
        return studentBookList;
    }
}