package com.wasp.gateway.enquiry.balance.kafka.interfaces

import org.xnio.channels.MessageChannel

/**
 * Created by aodunlami on 5/19/17.
 */
interface LogsChannel {

    //@Output
    MessageChannel output()
}