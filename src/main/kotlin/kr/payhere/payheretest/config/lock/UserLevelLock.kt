package kr.payhere.payheretest.config.lock

import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.function.Supplier
import javax.sql.DataSource

@Component
class UserLevelLock(
    private val dataSource: DataSource
) {
    fun <T> executeWithLock(
        userLockName: String,
        supplier: Supplier<T>,
        timeoutSeconds: Int = 3,
    ): T {
        return try {
             dataSource.connection.use { connection ->
                try {
                    getLock(connection, userLockName, timeoutSeconds)
                    return supplier.get()
                } finally {
                    releaseLock(connection, userLockName)
                }
            }
        } catch (e: SQLException) {
            throw RuntimeException(e.message, e)
        } catch (e: RuntimeException) {
            throw RuntimeException(e.message, e)
        }
    }

    @Throws(SQLException::class)
    private fun getLock(
        connection: Connection,
        userLockName: String,
        timeOutSeconds: Int
    ) {
        connection.prepareStatement(GET_LOCK).use { preparedStatement ->
            preparedStatement.setString(1, userLockName)
            preparedStatement.setInt(2, timeOutSeconds)
            checkResultSet(userLockName, preparedStatement, "GetLock_")
        }
    }

    @Throws(SQLException::class)
    private fun releaseLock(
        connection: Connection,
        userLockName: String
    ) {
        connection.prepareStatement(RELEASE_LOCK).use { preparedStatement ->
            preparedStatement.setString(1, userLockName)
            checkResultSet(userLockName, preparedStatement, "ReleaseLock_")
        }
    }

    @Throws(SQLException::class)
    private fun checkResultSet(
        userLockName: String,
        preparedStatement: PreparedStatement,
        type: String
    ) {
        preparedStatement.executeQuery().use { resultSet ->
            if (!resultSet.next()) {
                throw RuntimeException(EXCEPTION_MESSAGE)
            }
            val result: Int = resultSet.getInt(1)
            if (result != 1) {

                throw RuntimeException(EXCEPTION_MESSAGE)
            }
        }
    }

    companion object {
        private const val GET_LOCK = "SELECT GET_LOCK(?, ?)"
        private const val RELEASE_LOCK = "SELECT RELEASE_LOCK(?)"
        private const val EXCEPTION_MESSAGE = "LOCK 을 수행하는 중에 오류가 발생하였습니다."
    }
}