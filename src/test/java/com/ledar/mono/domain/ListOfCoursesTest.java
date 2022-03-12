package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ListOfCoursesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ListOfCourses.class);
        ListOfCourses listOfCourses1 = new ListOfCourses();
        listOfCourses1.setId(1L);
        ListOfCourses listOfCourses2 = new ListOfCourses();
        listOfCourses2.setId(listOfCourses1.getId());
        assertThat(listOfCourses1).isEqualTo(listOfCourses2);
        listOfCourses2.setId(2L);
        assertThat(listOfCourses1).isNotEqualTo(listOfCourses2);
        listOfCourses1.setId(null);
        assertThat(listOfCourses1).isNotEqualTo(listOfCourses2);
    }
}
