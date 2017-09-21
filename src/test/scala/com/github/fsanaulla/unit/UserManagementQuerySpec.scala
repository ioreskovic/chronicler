package com.github.fsanaulla.unit

import com.github.fsanaulla.query.UserManagementQuery
import com.github.fsanaulla.utils.TestCredentials
import com.github.fsanaulla.utils.TestHelper._
import com.github.fsanaulla.utils.constants.Privileges
import org.scalatest.{FlatSpecLike, Matchers}

class UserManagementQuerySpec
  extends UserManagementQuery
  with FlatSpecLike
  with Matchers
  with TestCredentials {

  private val testUsername = "TEST_USER_NAME"
  private val testPassword = "TEST_PASSWORD"
  private val testDatabase = "TEST_DATABASE"
  private val testPrivilege = Privileges.ALL

  "create user query" should "generate correct query" in {
    createUserQuery(testUsername, testPassword) shouldEqual queryTesterAuth(s"CREATE USER $testUsername WITH PASSWORD '$testPassword'")

    createUserQuery(testUsername, testPassword)(emptyCredentials) shouldEqual queryTester(s"CREATE USER $testUsername WITH PASSWORD '$testPassword'")
  }

  "create admin user query" should "generate correct query" in {
    createAdminQuery(testUsername, testPassword) shouldEqual queryTesterAuth(s"CREATE USER $testUsername WITH PASSWORD '$testPassword' WITH ALL PRIVILEGES")

    createAdminQuery(testUsername, testPassword)(emptyCredentials) shouldEqual queryTester(s"CREATE USER $testUsername WITH PASSWORD '$testPassword' WITH ALL PRIVILEGES")
  }

  "drop user query" should "generate correct query" in {
    dropUserQuery(testUsername) shouldEqual queryTesterAuth(s"DROP USER $testUsername")

    dropUserQuery(testUsername)(emptyCredentials) shouldEqual queryTester(s"DROP USER $testUsername")
  }

  "show users query" should "generate correct query" in {
    showUsersQuery shouldEqual queryTesterAuth("SHOW USERS")

    showUsersQuery()(emptyCredentials) shouldEqual queryTester("SHOW USERS")
  }

  "show user privileges query" should "generate correct query" in {
    showUserPrivilegesQuery(testUsername) shouldEqual queryTesterAuth(s"SHOW GRANTS FOR $testUsername")

    showUserPrivilegesQuery(testUsername)(emptyCredentials) shouldEqual queryTester(s"SHOW GRANTS FOR $testUsername")
  }

  "make admin query" should "generate correct query" in {
    makeAdminQuery(testUsername) shouldEqual queryTesterAuth(s"GRANT ALL PRIVILEGES TO $testUsername")

    makeAdminQuery(testUsername)(emptyCredentials) shouldEqual queryTester(s"GRANT ALL PRIVILEGES TO $testUsername")
  }

  "disable admin query" should "generate correct query" in {
    disableAdminQuery(testUsername) shouldEqual queryTesterAuth(s"REVOKE ALL PRIVILEGES FROM $testUsername")

    disableAdminQuery(testUsername)(emptyCredentials) shouldEqual queryTester(s"REVOKE ALL PRIVILEGES FROM $testUsername")
  }

  "set user password query" should "generate correct query" in {
    setUserPasswordQuery(testUsername, testPassword) shouldEqual queryTesterAuth(s"SET PASSWORD FOR $testUsername = '$testPassword'")

    setUserPasswordQuery(testUsername, testPassword)(emptyCredentials) shouldEqual queryTester(s"SET PASSWORD FOR $testUsername = '$testPassword'")
  }

  "set privileges query" should "generate correct query" in {
    setPrivilegesQuery(testDatabase, testUsername, testPrivilege) shouldEqual queryTesterAuth(s"GRANT $testPrivilege ON $testDatabase TO $testUsername")

    setPrivilegesQuery(testDatabase, testUsername, testPrivilege)(emptyCredentials) shouldEqual queryTester(s"GRANT $testPrivilege ON $testDatabase TO $testUsername")
  }

  "revoke privileges query" should "generate correct query" in {
    revokePrivilegesQuery(testDatabase, testUsername, testPrivilege) shouldEqual queryTesterAuth(s"REVOKE $testPrivilege ON $testDatabase FROM $testUsername")

    revokePrivilegesQuery(testDatabase, testUsername, testPrivilege)(emptyCredentials) shouldEqual queryTester(s"REVOKE $testPrivilege ON $testDatabase FROM $testUsername")
  }

}