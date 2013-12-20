package org.flyingfortress.core.txn.buffer;

import java.io.IOException;

/**
 * Buffer operations. todo: rename interface name.
 * User: vishnuhr
 * Date: 19/12/13
 * Time: 9:53 AM
 */
public interface BufferOperations {
    /**
     * clear the buffer.
     */
    public void clear();

    /**
     * sync with secondary persistent storage.
     * @throws IOException
     */
    public void sync() throws IOException;
}
