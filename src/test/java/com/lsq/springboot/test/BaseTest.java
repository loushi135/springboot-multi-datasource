

package com.lsq.springboot.test;

import com.lsq.springboot.datasource.starter.MultiRoutingDataSource;
import org.junit.After;
import org.junit.Before;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class BaseTest {

    @Before
    public void beforeTest() {
        MultiRoutingDataSource.getInstance().getGroupDataSourceMap().forEach((s, groupDataSource) -> {
            createTable(groupDataSource.getMaster());
            groupDataSource.getSlaves().forEach(this::createTable);
        });
    }

    private void createTable(DataSource dataSource) {
        try {
            if (dataSource == null) {
                return;
            }
            Connection connection = dataSource.getConnection();
            connection.createStatement().execute("CREATE TABLE `tb_order` (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` varchar(255) DEFAULT NULL,\n" +
                    "  `title` varchar(255) DEFAULT NULL,\n" +
                    "  `user_id` int(11) DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropTable(DataSource dataSource) {
        try {
            if (dataSource == null) {
                return;
            }
            Connection connection = dataSource.getConnection();
            connection.createStatement().execute("drop TABLE `tb_order`");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void afterTest() {
        MultiRoutingDataSource.getInstance().getGroupDataSourceMap().forEach((s, groupDataSource) -> {
            dropTable(groupDataSource.getMaster());
            groupDataSource.getSlaves().forEach(this::dropTable);
        });
    }
}
