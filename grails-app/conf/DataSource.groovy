hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            //            url = "jdbc:h2:mem:devDb;MVCC=TRUE"
			driverClassName = "org.postgresql.Driver"
			username = "postgres"
			password = "postgres"
			dbCreate = "create" // one of 'create', 'create-drop', 'update', 'validate', ''
			url = "jdbc:postgresql://localhost:5432/intertigre"
        }
    }
    test {
        dataSource {
//            dbCreate = "create-drop"
//            url = "jdbc:h2:mem:testDb;MVCC=TRUE"
			driverClassName = "org.postgresql.Driver"
			username = "postgres"
			password = "postgres"
			dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
			url = "jdbc:postgresql://localhost:5432/intertigretest"
        }
		hibernate {
			flush.mode = 'AUTO'
		}
    }
    production {
        dataSource {
            dbCreate = "create"
	        driverClassName = "org.postgresql.Driver"
	        dialect = org.hibernate.dialect.PostgreSQLDialect
	    
	        uri = new URI(System.env.DATABASE_URL?:"postgres://test:test@localhost/test")
	
	        url = "jdbc:postgresql://"+uri.host+uri.path
	        username = uri.userInfo.split(":")[0]
	        password = uri.userInfo.split(":")[1]
        }
    }
}
