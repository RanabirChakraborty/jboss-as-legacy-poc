/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.test;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.common.LocalCalculatorClient;
import org.jboss.common.RemoteCalculator;

/**
 * @author baranowb
 * 
 */
@Stateless(name = "test")
@Local(LocalCalculatorClient.class)
public class CaclulatorClientBean implements LocalCalculatorClient {
    
    @Resource(lookup = "java:global/client-context")
    private Context ctx;
    public String doCallTheBean(String address, String prefix, boolean secured, boolean statefull) throws NamingException {
        final StringBuilder resultBuilder = new StringBuilder();
        final String name = getName(prefix, secured, statefull);
        try {
            RemoteCalculator rc = (RemoteCalculator) ctx.lookup(name);
            resultBuilder.append("*************************************").append("<br>\n");
            int result = rc.add(1, 1);
            resultBuilder.append(result).append("<br>\n");
            result = rc.current();
            resultBuilder.append("Current: " + result).append("<br>\n");
            result = rc.add(1, 12);
            resultBuilder.append(result).append("<br>\n");
            result = rc.current();
            resultBuilder.append("Current: " + result).append("<br>\n");
            result = rc.add(2, 8);
            resultBuilder.append(result).append("<br>\n");
            result = rc.current();
            resultBuilder.append("Current: " + result).append("<br>\n");
            result = rc.subtract(0, 12);
            resultBuilder.append(result).append("<br>\n");
            result = rc.current();
            resultBuilder.append("Current: " + result).append("<br>\n");
            resultBuilder.append("*************************************<br>");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return resultBuilder.toString();
    }

    /**
     * @param secured
     * @param stateful
     * @return
     */
    private String getName(final String prefix, final boolean secured, final boolean stateful) {
        String name;
        if (secured) {
            if (stateful) {
                name = NAME_STATEFUL_SECURED;
            } else {
                name = NAME_STATELESS_SECURED;
            }
        } else {
            if (stateful) {
                name = NAME_STATEFUL;
            } else {
                name = NAME_STATELESS;
            }
        }

        if (prefix != null) {
            name = prefix + "/" + name;
        }
        return name;
    }
}
