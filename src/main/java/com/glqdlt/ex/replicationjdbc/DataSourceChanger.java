package com.glqdlt.ex.replicationjdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Component
public class DataSourceChanger {
    @Autowired
    @Qualifier("lazyConnectionDataSourceProxy")
    private LazyConnectionDataSourceProxy dataSourceProxy;

    public synchronized void changeDataSource(DataSource target) {

        dataSourceProxy.setTargetDataSource(target);
    }
}
