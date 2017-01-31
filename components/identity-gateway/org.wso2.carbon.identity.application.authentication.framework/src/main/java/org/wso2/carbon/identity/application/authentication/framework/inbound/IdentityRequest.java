/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.application.authentication.framework.inbound;

import org.wso2.msf4j.Request;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class IdentityRequest implements Serializable {

    private static final long serialVersionUID = -5698789879774366242L;

    protected Map<String, String> headers = new HashMap();
//    protected Map<String, Cookie> cookies = new HashMap();
    protected Map<String, String[]> parameters = new HashMap();
    protected Map<String, Object> attributes = new HashMap();
    protected String contextPath;
    protected String method;
    protected String pathInfo;
    protected String pathTranslated;
    protected String queryString;
    protected String requestURI;
    protected StringBuffer requestURL;
    protected String contentType;

    public Map<String, String> getHeaderMap() {
        return Collections.unmodifiableMap(headers);
    }

    public Enumeration<String> getHeaders(String name) {
        String headerValue = headers.get(name);
        if (headerValue != null) {
            String[] multiValuedHeader = headerValue.split(",");
            return Collections.enumeration(Arrays.asList(multiValuedHeader));
        } else {
            return Collections.emptyEnumeration();
        }
    }

    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(headers.keySet());
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

//    public Map<String, Cookie> getCookieMap() {
//        return Collections.unmodifiableMap(cookies);
//    }
//
//    public Cookie[] getCookies() {
//        Collection<Cookie> cookies = getCookieMap().values();
//        return cookies.toArray(new Cookie[cookies.size()]);
//    }

    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(parameters);
    }

    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

    public String[] getParameterValues(String paramName) {
        return parameters.get(paramName);
    }

    public String getParameter(String paramName) {
        String[] values = parameters.get(paramName);
        if(values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getMethod() {
        return method;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getPathTranslated() {
        return pathTranslated;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public StringBuffer getRequestURL() {
        return requestURL;
    }


    public String getContentType() {
        return contentType;
    }

    protected IdentityRequest(IdentityRequestBuilder builder) throws FrameworkClientException {
        this.headers = builder.headers;
//        this.cookies = builder.cookies;
        this.parameters = builder.parameters;
        this.attributes = builder.attributes;
        this.contextPath = builder.contextPath;
        this.method = builder.method;
        this.pathInfo = builder.pathInfo;
        this.pathTranslated = builder.pathTranslated;
        this.queryString = builder.queryString;
        this.requestURI = builder.requestURI;
        this.requestURL = builder.requestURL;
        this.contentType = builder.contentType;
    }

//    public String getBrowserCookieValue() {
//        String cookieValue = null;
//        Cookie cookie = this.getCookieMap().get(IdentityRequestConstants.BROWSER_COOKIE);
//        if (cookie != null) {
//            cookieValue = cookie.getValue();
//        }
//        return cookieValue;
//    }

    public static class IdentityRequestBuilder {

        protected Request request;
        protected Map<String, String> headers = new HashMap();
//        protected Map<String, Cookie> cookies = new HashMap();
        protected Map<String, String[]> parameters = new HashMap();
        protected Map<String, Object> attributes = new HashMap();
        protected String contextPath;
        protected String method;
        protected String pathInfo;
        protected String pathTranslated;
        protected String queryString;
        protected String requestURI;
        protected StringBuffer requestURL;
        protected String contentType;

        public IdentityRequestBuilder(Request request) {
            this.request = request;
        }

        public IdentityRequestBuilder() {

        }

        public Request getRequest() {
            return request;
        }

        public IdentityRequestBuilder setHeaders(Map<String, String> headers) {
            if (headers == null) {
                throw FrameworkRuntimeException.error("Headers map is null.");
            }
            this.headers = headers;
            return this;
        }

        public IdentityRequestBuilder addHeaders(Map<String, String> headers) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                addHeader(header.getKey(), header.getValue());
            }
            return this;
        }

        public IdentityRequestBuilder addHeader(String name, String value) {
            if (this.headers.containsKey(name)) {
                throw FrameworkRuntimeException.error("Headers map trying to override existing " +
                        "header " + name);
            }
            this.headers.put(name, value);
            return this;
        }

//        public IdentityRequestBuilder setCookies(Map<String, Cookie> cookies) {
//            if (cookies == null) {
//                throw FrameworkRuntimeException.error("Cookies map is null.");
//            }
//            this.cookies = cookies;
//            return this;
//        }
//
//        public IdentityRequestBuilder addCookie(String name, Cookie value) {
//            if (this.cookies.containsKey(name)) {
//                throw FrameworkRuntimeException.error("Cookies map trying to override existing " +
//                        "cookie " + name);
//            }
//            this.cookies.put(name, value);
//            return this;
//        }
//
//        public IdentityRequestBuilder addCookies(Map<String, Cookie> cookies) {
//            for (Map.Entry<String, Cookie> cookie : cookies.entrySet()) {
//                addCookie(cookie.getKey(), cookie.getValue());
//            }
//            return this;
//        }

        public IdentityRequestBuilder setParameters(Map<String, String[]> parameters) {
            if (parameters == null) {
                throw FrameworkRuntimeException.error("Parameters map is null.");
            }
            this.parameters = parameters;
            return this;
        }

        public IdentityRequestBuilder addParameter(String name, String[] values) {
            if (this.parameters.containsKey(name)) {
                throw FrameworkRuntimeException.error("Parameters map trying to override existing " +
                        "key " + name);
            }
            this.parameters.put(name, values);
            return this;
        }

        public IdentityRequestBuilder addParameter(String name, String value) {
            if (this.parameters.containsKey(name)) {
                throw FrameworkRuntimeException.error("Parameters map trying to override existing " +
                        "key " + name);
            }
            this.parameters.put(name, new String[]{value});
            return this;
        }

        public IdentityRequestBuilder addParameters(Map<String, String[]> parameters) {
            for (Map.Entry<String, String[]> parameter : parameters.entrySet()) {
                addParameter(parameter.getKey(), parameter.getValue());
            }
            return this;
        }

        public IdentityRequestBuilder setAttributes(Map<String, Object> attributes) {
            if (attributes == null) {
                throw FrameworkRuntimeException.error("Attributes map is null.");
            }
            this.attributes = attributes;
            return this;
        }

        public IdentityRequestBuilder addAttribute(String name, Object value) {
            if (this.attributes.containsKey(name)) {
                throw FrameworkRuntimeException.error("Attributes map trying to override existing " +
                                                      "key " + name);
            }
            this.attributes.put(name, value);
            return this;
        }

        public IdentityRequestBuilder addAttributes(Map<String, Object> attributes) {
            for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
                addAttribute(attribute.getKey(), attribute.getValue());
            }
            return this;
        }


        public IdentityRequestBuilder setContextPath(String contextPath) {
            this.contextPath = contextPath;
            return this;
        }

        public IdentityRequestBuilder setMethod(String method) {
            this.method = method;
            return this;
        }

        public IdentityRequestBuilder setPathInfo(String pathInfo) {
            this.pathInfo = pathInfo;
            return this;
        }

        public IdentityRequestBuilder setPathTranslated(String pathTranslated) {
            this.pathTranslated = pathTranslated;
            return this;
        }

        public IdentityRequestBuilder setQueryString(String queryString) {
            this.queryString = queryString;
            return this;
        }

        public IdentityRequestBuilder setRequestURI(String requestURI) {
            this.requestURI = requestURI;
            return this;
        }

        public IdentityRequestBuilder setRequestURL(StringBuffer requestURL) {
            this.requestURL = requestURL;
            return this;
        }

        public IdentityRequestBuilder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public IdentityRequest build() throws FrameworkClientException {
            return new IdentityRequest(this);
        }

    }

    public static class IdentityRequestConstants {
        public static final String BROWSER_COOKIE = "SIOWTOSW";
    }
}
