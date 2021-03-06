/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.dsdl.eidea.soap.cxf2.feature;

import com.dsdl.eidea.soap.cxf2.interceptor.LoggingInInterceptor;
import com.dsdl.eidea.soap.cxf2.interceptor.LoggingOutInterceptor;
import org.apache.cxf.Bus;
import org.apache.cxf.annotations.Logging;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

/**
 * This class is used to control message-on-the-wire logging. 
 * By attaching this feature to an endpoint, you
 * can specify logging. If this feature is present, an endpoint will log input
 * and output of ordinary and log messages.
 * <pre>
 * <![CDATA[
    <jaxws:endpoint ...>
      <jaxws:features>
       <bean class="org.apache.cxf.feature.LoggingFeature"/>
      </jaxws:features>
    </jaxws:endpoint>
  ]]>
  </pre>
 */
@NoJSR250Annotations
public class DBLoggingFeature extends LoggingFeature {
    private static final int DEFAULT_LIMIT = 64 * 1024*1024;
    private static final LoggingInInterceptor IN = new LoggingInInterceptor(DEFAULT_LIMIT);
    private static final LoggingOutInterceptor OUT = new LoggingOutInterceptor(DEFAULT_LIMIT);
    
    
    String inLocation;
    String outLocation;
    boolean prettyLogging;
    
    int limit = DEFAULT_LIMIT;

    public DBLoggingFeature() {
        
    }
    public DBLoggingFeature(int lim) {
        limit = lim;
    }
    public DBLoggingFeature(String in, String out) {
        inLocation = in;
        outLocation = out;
    }
    public DBLoggingFeature(String in, String out, int lim) {
        inLocation = in;
        outLocation = out;
        limit = lim;
    }

    public DBLoggingFeature(String in, String out, int lim, boolean p) {
        inLocation = in;
        outLocation = out;
        limit = lim;
        prettyLogging = p;
    }

    public DBLoggingFeature(Logging annotation) {
        inLocation = annotation.inLocation();
        outLocation = annotation.outLocation();
        limit = annotation.limit();
        prettyLogging = annotation.pretty();
    }

    @Override
    protected void initializeProvider(InterceptorProvider provider, Bus bus) {
        if (limit == DEFAULT_LIMIT && inLocation == null 
            && outLocation == null && !prettyLogging) {
            provider.getInInterceptors().add(IN);
            provider.getInFaultInterceptors().add(IN);
            provider.getOutInterceptors().add(OUT);
            provider.getOutFaultInterceptors().add(OUT);
        } else {
            LoggingInInterceptor in = new LoggingInInterceptor(limit);
            in.setOutputLocation(inLocation);
            in.setPrettyLogging(prettyLogging);
            LoggingOutInterceptor out = new LoggingOutInterceptor(limit);
            out.setOutputLocation(outLocation);
            out.setPrettyLogging(prettyLogging);
            
            provider.getInInterceptors().add(in);
            provider.getInFaultInterceptors().add(in);
            provider.getOutInterceptors().add(out);
            provider.getOutFaultInterceptors().add(out);
        }
    }

    /**
     * This function has no effect at this time.
     * @param lim
     */
    public void setLimit(int lim) {
        limit = lim;
    }
    
    /**
     * Retrieve the value set with {@link #setLimit(int)}.
     * @return
     */
    public int getLimit() {
        return limit;
    }
    
    /**
     * @return
     */
    public boolean isPrettyLogging() {
        return prettyLogging;
    }
    /**
     * Turn pretty logging of XML content on/off
     * @param prettyLogging
     */
    public void setPrettyLogging(boolean prettyLogging) {
        this.prettyLogging = prettyLogging;
    }    
}
