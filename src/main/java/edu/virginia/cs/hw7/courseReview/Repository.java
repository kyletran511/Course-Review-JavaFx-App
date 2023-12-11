package edu.virginia.cs.hw7.courseReview;

import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class Repository {

    private static final Repository singleton = new Repository();

    private Session session;

    public static void main (String[] args){
        Repository test = Repository.getInstance();
        System.out.println(test.getStudentByName("kyle"));
    }

    private Repository() {
        session = HibernateUtils.getSessionFactory().openSession();
        initTables();
    }

    public static Repository getInstance() {
        return singleton;
    }

    private void initTables() {

           session.beginTransaction();
           String course_sql = "    create table IF NOT EXISTS COURSES(\n" +
                   "       ID bigint not null,\n" +
                   "        CATALOG_NUMBER integer not null,\n" +
                   "        DEPARTMENT varchar(4) not null,\n" +
                   "        primary key (ID)\n" +
                   "    );";
           session.createSQLQuery(course_sql).executeUpdate();

           String student_sql = "   create table IF NOT EXISTS STUDENTS (\n" +
                   "       ID bigint not null,\n" +
                   "        PASSWORD varchar(128) not null,\n" +
                   "        USERNAME varchar(128) not null UNIQUE,\n" +
                   "        primary key (ID)\n" +
                   "    )";

           session.createSQLQuery(student_sql).executeUpdate();

           String review_sql = "create table IF NOT EXISTS REVIEWS(\n" +
                   "       ID bigint not null,\n" +
                   "        RATING integer not null,\n" +
                   "        REVIEW varchar(255) not null,\n" +
                   "        COURSE_ID bigint,\n" +
                   "        STUDENT_ID bigint,\n" +
                   "        primary key (ID),\n" +
                   "        FOREIGN KEY (COURSE_ID) REFERENCES COURSES(ID),\n" +
                   "        FOREIGN KEY (STUDENT_ID) REFERENCES STUDENTS(ID)\n" +
                   "    )";
           session.createSQLQuery(review_sql).executeUpdate();


           String sequence_sql = "    create table if not exists hibernate_sequence (\n" +
                   "       next_val bigint\n" +
                   "    );";

           session.createSQLQuery(sequence_sql).executeUpdate();

           //Checking if table is empty code inspired from hw6 isElementInTableById method in DatabaseMangerImpl
           boolean is_seq_empty = ((Integer)session.createSQLQuery("select count(*) as count from hibernate_sequence").uniqueResult()) == 0;
           if(is_seq_empty) {
            session.createSQLQuery("insert into hibernate_sequence values (1)").executeUpdate();
           }

           session.getTransaction().commit();
    }

    public void clearTable() {
        session.beginTransaction();
        String dropReview = "delete from Review ";
        String dropCourse = "delete from Course";
        String dropStudent = "delete from Student";

        session.createQuery(dropReview).executeUpdate();
        session.createQuery(dropCourse).executeUpdate();
        session.createQuery(dropStudent).executeUpdate();
        session.getTransaction().commit();
    }

    public void saveStudent(Student s) {
//        if(studentExistsByName(s.getUsername()))
//            throw new IllegalStateException("Trying to save a student already in the table");
        session.beginTransaction();
        System.out.println("Saving");
        session.save(s);
        session.getTransaction().commit();
    }

    public void saveCourse(Course c) {
        session.beginTransaction();
        session.save(c);
        session.flush();
        session.getTransaction().commit();
    }

    public void saveReview(Review newReview) {
        session.beginTransaction();
        newReview.setReviewer(getStudentReference(newReview.getReviewer()));
        newReview.setCourse(getCourseReference(newReview.getCourse()));
        session.save(newReview);
        session.getTransaction().commit();
    }

    public Student getStudentReference(Student s) {
        return session.getReference(Student.class, s.getId());
    }

    public Course getCourseReference(Course c) {
        return session.getReference(Course.class, c.getId());
    }

    public boolean studentExistsByName(String name) {
        String hql = "select 1 from Student where username = :name";
        var query = session.createQuery(hql);
        query.setParameter("name", name);
        return (query.uniqueResult() != null);
    }

    public Student getStudentByName(String name) {
        String hql = "FROM Student where username = :name";
        TypedQuery<Student> query = session.createQuery(hql);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }


    public Course getCourse(String department, int courseNum) {
        //session.beginTransaction();
        String hql = "FROM Course where department = :dep and catalogNumber = :num";
        TypedQuery<Course> query = session.createQuery(hql);
        query.setParameter("dep", department);
        query.setParameter("num", courseNum);
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }

    public Review getReviewByUserAndCourse(Student user, Course course){
        String hql = "FROM Review r WHERE r.reviewer.id = :userID AND r.course.id = :courseID";
        TypedQuery<Review> query = session.createQuery(hql);
        query.setParameter("userID", user.getId());
        query.setParameter("courseID", course.getId());
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }

    public List<Review> getReviewByCourse(Course course){
        String hql = "FROM Review r WHERE r.course.id = :courseID";
        TypedQuery<Review> query = session.createQuery(hql);
        query.setParameter("courseID", course.getId());
        try {
            return query.getResultList();
        } catch (NoResultException ignored) {
            return null;
        }
    }
}
