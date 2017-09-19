/* Woodstox Lite ("wool") XML processor
 *
 * Copyright (c) 2006- Tatu Saloranta, tatu.saloranta@iki.fi
 *
 * Licensed under the License specified in the file LICENSE which is
 * included with the source code.
 * You may not use this file except in compliance with the License.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fasterxml.aalto.impl;

import javax.xml.stream.*;

/**
 * Base class for all {@link XMLStreamException} instances that
 * we use. Sometimes used as is, but usually there should be more
 * specific sub-class that indicates class of exception.
 */
@SuppressWarnings("serial")
public class StreamExceptionBase
    extends XMLStreamException
{
    /**
     * D'oh. Super-class munges and hides the message, have to duplicate here
     */
    final String mMsg;

    public StreamExceptionBase(String msg) {
        super(msg);
        mMsg = msg;
    }

    public StreamExceptionBase(Throwable th) {
        super(th.getMessage(), th);
        mMsg = th.getMessage();

        if (th != null && getCause() == null) {
            initCause(th);
        }
    }

    public StreamExceptionBase(String msg, Location loc) {
        super(msg, loc);
        mMsg = msg;
    }

    public StreamExceptionBase(String msg, Location loc, Throwable th) {
        super(msg, loc, th);
        mMsg = msg;

        if (th != null && getCause() == null) {
            initCause(th);
        }
    }

    /**
     * Method is overridden for two main reasons: first, default method
     * does not display public/system id information, even if it exists, and
     * second, default implementation can not handle nested Location
     * information.
     */
    public String getMessage()
    {
        String locMsg = getLocationDesc();
        /* Better not use super's message if we do have location information,
         * since parent's message contains (part of) Location
         * info; something we can regenerate better...
         */
        if (locMsg == null) {
            return super.getMessage();
        }
        StringBuilder sb = new StringBuilder(mMsg.length() + locMsg.length() + 20);
        sb.append(mMsg);
        sb.append('\n');
        sb.append(" at ");
        sb.append(locMsg);
        return sb.toString();
    }

    public String toString()
    {
        return getClass().getName()+": "+getMessage();
    }

    /*
    ////////////////////////////////////////////////////////
    // Internal methods:
    ////////////////////////////////////////////////////////
     */

    protected String getLocationDesc()
    {
        Location loc = getLocation();
        return (loc == null) ? null : loc.toString();
    }
}
