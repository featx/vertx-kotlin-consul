package org.featx.cusp.user.transmit.repository

import io.vertx.sqlclient.Pool

/**
 * @author excepts
 */
interface TransactionConnectionHold {
    val connectionPool: Pool
}