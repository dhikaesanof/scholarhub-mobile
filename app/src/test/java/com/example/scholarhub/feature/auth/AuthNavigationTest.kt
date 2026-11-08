package com.example.scholarhub.feature.auth

import com.example.scholarhub.navigation.AppDestination
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthNavigationTest {

    @Test
    fun authRole_hasCorrectLabels() {
        assertEquals("Mahasiswa", AuthRole.Mahasiswa.label)
        assertEquals("Mentor", AuthRole.Mentor.label)
    }

    @Test
    fun appDestination_defaultsAndValues() {
        val login = AppDestination.Login
        assertTrue(login is AppDestination)

        val regMahasiswa = AppDestination.Register(AuthRole.Mahasiswa)
        assertEquals(AuthRole.Mahasiswa, regMahasiswa.initialRole)

        val regMentor = AppDestination.Register(AuthRole.Mentor)
        assertEquals(AuthRole.Mentor, regMentor.initialRole)

        val detail = AppDestination.ScholarshipDetail("SH28")
        assertEquals("SH28", detail.scholarshipId)

        val mentor = AppDestination.ProfileMentor
        assertTrue(mentor is AppDestination)
    }
}
