module CourseReview.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.naming;
    requires java.sql;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires net.bytebuddy;
    requires org.hibernate.orm.core;

    exports edu.virginia.cs.hw7.courseReview;
    exports edu.virginia.cs.hw7.gui;

    opens edu.virginia.cs.hw7.gui to javafx.fxml;
    opens edu.virginia.cs.hw7.courseReview to org.hibernate.orm.core;
}