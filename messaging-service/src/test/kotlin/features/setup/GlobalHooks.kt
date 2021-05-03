//package features.setup
//
//import org.springframework.core.io.FileUrlResource
//import org.springframework.jdbc.datasource.init.ScriptUtils.executeSqlScript
//import org.springframework.orm.jpa.vendor.Database
//import javax.sql.DataSource
//
//object GlobalHooks {
//
//    private var initDb = false
//
//    fun initializeDatabase(database: Database, ds: DataSource) {
//        if (!initDb) {
//
//            val stmt = ds.connection.createStatement()
//            stmt.execute("drop database messagetests")
//            stmt.execute("create database messagetests")
//            stmt.close()
//
//            executeSqlScript(ds.connection, FileUrlResource("./init/10_create-table.sql"))
//            executeSqlScript(ds.connection, FileUrlResource("./init/20_init.sql"))
//
//            initDb = true
//        }
//    }
//}