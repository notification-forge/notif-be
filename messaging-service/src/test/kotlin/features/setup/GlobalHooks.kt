package features.setup

import org.springframework.orm.jpa.vendor.Database
import javax.sql.DataSource

object GlobalHooks {

    private var initDb = false

    fun initializeDatabase(database: Database, ds: DataSource) {
        // do nothing for now
    }
}