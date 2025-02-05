package io.dev.jobprep.util;

public class ContainerConstants {

    public static final String TEST_USER = "testUser";
    public static final String TEST_PASSWORD = "testPassword";

    public static final String MYSQL_SRV = "jobprep_test_mysql";
    public static final String MONGO_SRV_PRI = "jobprep_test_mongo";
    public static final String MONGO_SRV_SEC_01 = "jobprep_test_mongo_01";
    public static final String MONGO_SRV_SEC_02 = "jobprep_test_mongo_02";
    public static final String REDIS_SRV = "jobprep_test_redis";

    public static final String MYSQL_TEST_DB = "jobprep-mysql-test";
    public static final String MONGO_TEST_DB = "jobprep-mongo-test";

    public static final Integer MYSQL_PORT = 3306;
    public static final Integer MONGO_PORT_PRI = 27017;
    public static final Integer MONGO_PORT_SEC_01 = 27018;
    public static final Integer MONGO_PORT_SEC_02 = 27019;
    public static final Integer REDIS_PORT = 6379;

    public static final String REDIS_HOST = "localhost";

    private ContainerConstants() {
        throw new UnsupportedOperationException("Cannot instantiate this interface!");
    }
}
