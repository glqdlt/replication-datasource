package com.glqdlt.ex.replicationjdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDatasource extends AbstractRoutingDataSource {

    private DataSourceType SLAVE_ROUND_ROBIN = DataSourceType.SLAVE_1;

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        if (transactionActive) {
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            if (readOnly) {
                return route();
            }
        }
        return DataSourceType.MASTER;
    }

    private synchronized DataSourceType route() {
        if (SLAVE_ROUND_ROBIN == DataSourceType.SLAVE_1) {
            this.SLAVE_ROUND_ROBIN = DataSourceType.SLAVE_2;
        } else {
            this.SLAVE_ROUND_ROBIN = DataSourceType.SLAVE_1;
        }
        return this.SLAVE_ROUND_ROBIN;
    }
}
