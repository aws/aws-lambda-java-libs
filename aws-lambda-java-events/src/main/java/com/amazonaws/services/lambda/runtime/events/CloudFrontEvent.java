package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a CloudFront event
 */
public class CloudFrontEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -7169297388214516660L;

    private List<Record> records;

    /**
     *  class that represents a header
     */
    public static class Header implements Serializable, Cloneable {

        private static final long serialVersionUID = 7041042740552686996L;

        private String key;

        private String value;

        /**
         * default constructor
         */
        public Header() {}

        /**
         * @return key value of header
         */
        public String getKey() {
            return this.key;
        }

        /**
         * @param key value of header
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @param key value of header
         * @return Header object
         */
        public Header withKey(String key) {
            setKey(key);
            return this;
        }

        /**
         * @return value of header value
         */
        public String getValue() {
            return this.value;
        }

        /**
         * @param value of header value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @param value of header value
         * @return Header object
         */
        public Header withValue(String value) {
            setValue(value);
            return this;
        }

        /**
         * Returns a string representation of this object; useful for testing and debugging.
         *
         * @return A string representation of this object.
         *
         * @see Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getKey() != null)
                sb.append("key: ").append(getKey()).append(",");
            if (getValue() != null)
                sb.append("value: ").append(getValue());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof Header == false)
                return false;
            Header other = (Header) obj;
            if (other.getKey() == null ^ this.getKey() == null)
                return false;
            if (other.getKey() != null && other.getKey().equals(this.getKey()) == false)
                return false;
            if (other.getValue() == null ^ this.getValue() == null)
                return false;
            if (other.getValue() != null && other.getValue().equals(this.getValue()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getKey() == null) ? 0 : getKey().hashCode());
            hashCode = prime * hashCode + ((getValue() == null) ? 0 : getValue().hashCode());
            return hashCode;
        }

        @Override
        public Header clone() {
            try {
                return (Header) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
        
    }

    /**
     * Class that represents the configuration of a CloudFront message
     */
    public static class Config implements Serializable, Cloneable {

        private static final long serialVersionUID = -286083903805870299L;

        private String distributionId;

        /**
         * default constructor
         */
        public Config() {}

        /**
         * @return distribution id of cloud front entity
         */
        public String getDistributionId() {
            return this.distributionId;
        }

        /**
         * @param distributionId distribution id of cloud front entity
         */
        public void setDistributionId(String distributionId) {
            this.distributionId = distributionId;
        }

        /**
         * @param distributionId distribution id of cloud front entity
         * @return Config
         */
        public Config withDistributionId(String distributionId) {
            setDistributionId(distributionId);
            return this;
        }

        /**
         * Returns a string representation of this object; useful for testing and debugging.
         *
         * @return A string representation of this object.
         *
         * @see Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getDistributionId() != null)
                sb.append("distributionId: ").append(getDistributionId());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof Config == false)
                return false;
            Config other = (Config) obj;
            if (other.getDistributionId() == null ^ this.getDistributionId() == null)
                return false;
            if (other.getDistributionId() != null && other.getDistributionId().equals(this.getDistributionId()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getDistributionId() == null) ? 0 : getDistributionId().hashCode());

            return hashCode;
        }

        @Override
        public Config clone() {
            try {
                return (Config) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * class that represents a CLoudFront request
     */
    public static class Request implements Serializable, Cloneable {

        private static final long serialVersionUID = 3245036101075464149L;

        private String uri;

        private String method;

        private String httpVersion;

        private String clientIp;

        private Map<String, List<Header>> headers;

        /**
         * default constructor
         */
        public Request() {}

        /**
         * @return uri of cloud front endpoint
         */
        public String getUri() {
            return this.uri;
        }

        /**
         * @param uri uri of cloud front endpoint
         */
        public void setUri(String uri) {
            this.uri = uri;
        }

        /**
         * @param uri uri of cloud front endpoint
         * @return Request object
         */
        public Request withUri(String uri) {
            setUri(uri);
            return this;
        }

        /**
         * @return method used by cloud front entity
         */
        public String getMethod() {
            return this.method;
        }

        /**
         * @param method method used by cloud front entity
         */
        public void setMethod(String method) {
            this.method = method;
        }

        /**
         * @param method method used by cloud front entity
         * @return Request object
         */
        public Request withMethod(String method) {
            setMethod(method);
            return this;
        }

        /**
         * @return httpVersion http version used by cloud front
         */
        public String getHttpVersion() {
            return this.httpVersion;
        }

        /**
         * @param httpVersion http version used by cloud front
         */
        public void setHttpVersion(String httpVersion) {
            this.httpVersion = httpVersion;
        }

        /**
         * @param httpVersion http version used by cloud front
         * @return Request
         */
        public Request withHttpVersion(String httpVersion) {
            setHttpVersion((httpVersion));
            return this;
        }

        /**
         * @return client ip address
         */
        public String getClientIp() {
            return this.clientIp;
        }

        /**
         * @param clientIp client ip address
         */
        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }

        /**
         * @param clientIp client ip address
         * @return Request object
         */
        public Request withClientIp(String clientIp) {
            setClientIp(clientIp);
            return this;
        }

        /**
         * @return headers used in the cloud front request
         */
        public Map<String, List<Header>> getHeaders() {
            return this.headers;
        }

        /**
         * @param headers headers used in the cloud front request
         */
        public void setHeaders(Map<String, List<Header>> headers) {
            this.headers = headers;
        }

        /**
         * @param headers used in the cloud front request
         * @return Response object
         */
        public Request withHeaders(Map<String, List<Header>> headers) {
            setHeaders(headers);
            return this;
        }

        /**
         * Returns a string representation of this object; useful for testing and debugging.
         *
         * @return A string representation of this object.
         *
         * @see Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getUri() != null)
                sb.append("uri: ").append(getUri()).append(",");
            if (getMethod() != null)
                sb.append("method: ").append(getMethod()).append(",");
            if (getHttpVersion() != null)
                sb.append("httpVersion: ").append(getHttpVersion()).append(",");
            if (getClientIp() != null)
                sb.append("clientIp: ").append(getClientIp()).append(",");
            if (getHeaders() != null)
                sb.append("headers: ").append(getHeaders().toString());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof Request == false)
                return false;
            Request other = (Request) obj;
            if (other.getUri() == null ^ this.getUri() == null)
                return false;
            if (other.getUri() != null && other.getUri().equals(this.getUri()) == false)
                return false;
            if (other.getMethod() == null ^ this.getMethod() == null)
                return false;
            if (other.getMethod() != null && other.getMethod().equals(this.getMethod()) == false)
                return false;
            if (other.getHttpVersion() == null ^ this.getHttpVersion() == null)
                return false;
            if (other.getHttpVersion() != null && other.getHttpVersion().equals(this.getHttpVersion()) == false)
                return false;
            if (other.getClientIp() == null ^ this.getClientIp() == null)
                return false;
            if (other.getClientIp() != null && other.getClientIp().equals(this.getClientIp()) == false)
                return false;
            if (other.getHeaders() == null ^ this.getHeaders() == null)
                return false;
            if (other.getHeaders() != null && other.getHeaders().equals(this.getHeaders()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getUri() == null) ? 0 : getUri().hashCode());
            hashCode = prime * hashCode + ((getMethod() == null) ? 0 : getMethod().hashCode());
            hashCode = prime * hashCode + ((getHttpVersion() == null) ? 0 : getHttpVersion().hashCode());
            hashCode = prime * hashCode + ((getClientIp() == null) ? 0 : getClientIp().hashCode());
            hashCode = prime * hashCode + ((getHeaders() == null) ? 0 : getHeaders().hashCode());
            return hashCode;
        }

        @Override
        public Request clone() {
            try {
                return (Request) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    /**
     * class that represents a Response object
     */
    public static class Response implements Serializable, Cloneable {

        private static final long serialVersionUID = -3711565862079053710L;

        private String status;

        private String statusDescription;

        private String httpVersion;

        private Map<String, List<Header>> headers;

        /**
         * default constructor
         */
        public Response() {}

        /**
         * @return status code returned by cloud front
         */
        public String getStatus() {
            return this.status;
        }

        /**
         * @param status status code returned by cloud front
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * @param status status code returned by cloud front
         * @return Response
         */
        public Response withStatus(String status) {
            setStatus(status);
            return this;
        }

        /**
         * @return status description returned by cloud front
         */
        public String getStatusDescription() {
            return this.statusDescription;
        }

        /**
         * @param statusDescription status description returned by cloud front
         */
        public void setStatusDescription(String statusDescription) {
            this.statusDescription = statusDescription;
        }

        /**
         * @param statusDescription status description returned by cloud front
         * @return Response
         */
        public Response withStatusDescription(String statusDescription) {
            setStatusDescription(statusDescription);
            return this;
        }

        /**
         * @return http version used by cloud front
         */
        public String getHttpVersion() {
            return this.httpVersion;
        }

        /**
         * @param httpVersion http version used by cloud front
         */
        public void setHttpVersion(String httpVersion) {
            this.httpVersion = httpVersion;
        }

        /**
         * @param httpVersion http version used by cloud front
         * @return Response object
         */
        public Response withHttpVersion(String httpVersion) {
            setHttpVersion(httpVersion);
            return this;
        }

        /**
         * @return headers included with the Cloud front response
         */
        public Map<String, List<Header>> getHeaders() {
            return this.headers;
        }

        /**
         * @param headers headers included with the Cloud front response
         */
        public void setHeaders(Map<String, List<Header>> headers) {
            this.headers = headers;
        }

        /**
         * @param headers headers included with the Cloud front response
         * @return Response object
         */
        public Response withHeaders(Map<String, List<Header>> headers) {
            setHeaders(headers);
            return this;
        }

        /**
         * Returns a string representation of this object; useful for testing and debugging.
         *
         * @return A string representation of this object.
         *
         * @see Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getStatus() != null)
                sb.append("status: ").append(getStatus()).append(",");
            if (getStatusDescription() != null)
                sb.append("statusDescription: ").append(getStatusDescription()).append(",");
            if (getHttpVersion() != null)
                sb.append("httpVersion: ").append(getHttpVersion()).append(",");
            if (getHeaders() != null)
                sb.append("headers: ").append(getHeaders().toString());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof Response == false)
                return false;
            Response other = (Response) obj;
            if (other.getStatus() == null ^ this.getStatus() == null)
                return false;
            if (other.getStatus() != null && other.getStatus().equals(this.getStatus()) == false)
                return false;
            if (other.getStatusDescription() == null ^ this.getStatusDescription() == null)
                return false;
            if (other.getStatusDescription() != null && other.getStatusDescription().equals(this.getStatusDescription()) == false)
                return false;
            if (other.getHttpVersion() == null ^ this.getHttpVersion() == null)
                return false;
            if (other.getHttpVersion() != null && other.getHttpVersion().equals(this.getHttpVersion()) == false)
                return false;
            if (other.getHeaders() == null ^ this.getHeaders() == null)
                return false;
            if (other.getHeaders() != null && other.getHeaders().equals(this.getHeaders()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getStatus() == null) ? 0 : getStatus().hashCode());
            hashCode = prime * hashCode + ((getStatusDescription() == null) ? 0 : getStatusDescription().hashCode());
            hashCode = prime * hashCode + ((getHttpVersion() == null) ? 0 : getHttpVersion().hashCode());
            hashCode = prime * hashCode + ((getHeaders() == null) ? 0 : getHeaders().hashCode());
            return hashCode;
        }

        @Override
        public Response clone() {
            try {
                return (Response) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * class that represents the CloudFront body within a record
     */
    public static class CF implements Serializable, Cloneable {

        private static final long serialVersionUID = -5940167419180448832L;

        private Config config;

        private Request request;

        private Response response;

        /**
         * default constructor
         */
        public CF() {}

        /**
         * @return configuration object used by cloud front
         */
        public Config getConfig() {
            return this.config;
        }

        /**
         * @param config configuration object used by cloud front
         */
        public void setConfig(Config config) {
            this.config = config;
        }

        /**
         * @param config configuration object used by cloud front
         * @return CF object
         */
        public CF withConfig(Config config) {
            setConfig(config);
            return this;
        }

        /**
         * @return Request object
         */
        public Request getRequest() {
            return this.request;
        }

        /**
         * @param request Request object used by cloud front
         */
        public void setRequest(Request request) {
            this.request = request;
        }

        /**
         * @param request Request object used by cloud front
         * @return CF
         */
        public CF withRequest(Request request) {
            setRequest(request);
            return this;
        }

        /**
         * @return Response object used by cloud front
         */
        public Response getResponse() {
            return this.response;
        }

        /**
         * @param response Response object used by cloud front
         */
        public void setResponse(Response response) {
            this.response = response;
        }

        /**
         * @param response Response object used by cloud front
         * @return CF
         */
        public CF withResponse(Response response) {
            setResponse(response);
            return this;
        }

        /**
         * Returns a string representation of this object; useful for testing and debugging.
         *
         * @return A string representation of this object.
         *
         * @see Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getConfig() != null)
                sb.append("config: ").append(getConfig().toString()).append(",");
            if (getRequest() != null)
                sb.append("request: ").append(getRequest().toString()).append(",");
            if (getResponse() != null)
                sb.append("response: ").append(getResponse().toString());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof CF == false)
                return false;
            CF other = (CF) obj;
            if (other.getConfig() == null ^ this.getConfig() == null)
                return false;
            if (other.getConfig() != null && other.getConfig().equals(this.getConfig()) == false)
                return false;
            if (other.getRequest() == null ^ this.getRequest() == null)
                return false;
            if (other.getRequest() != null && other.getRequest().equals(this.getRequest()) == false)
                return false;
            if (other.getResponse() == null ^ this.getResponse() == null)
                return false;
            if (other.getResponse() != null && other.getResponse().equals(this.getResponse()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getConfig() == null) ? 0 : getConfig().hashCode());
            hashCode = prime * hashCode + ((getRequest() == null) ? 0 : getRequest().hashCode());
            hashCode = prime * hashCode + ((getResponse() == null) ? 0 : getResponse().hashCode());
            return hashCode;
        }

        @Override
        public CF clone() {
            try {
                return (CF) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * Class that represents a record in a CLoudFront event
     */
    public static class Record implements Serializable, Cloneable {

        private static final long serialVersionUID = -6114551370798889850L;

        private CF cf;

        /**
         * default constructor
         */
        public Record() {}

        /**
         * @return CF object that contains message from cloud front
         */
        public CF getCf() {
            return this.cf;
        }

        /**
         * @param cf CF object that contains message from cloud front
         */
        public void setCf(CF cf) {
            this.cf = cf;
        }

        /**
         * @param cf CF object that contains message from cloud front
         * @return Record object
         */
        public Record withCf(CF cf) {
            setCf(cf);
            return this;
        }

        /**
         * Returns a string representation of this object; useful for testing and debugging.
         *
         * @return A string representation of this object.
         *
         * @see Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getCf() != null)
                sb.append("cf: ").append(getCf().toString());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof Record == false)
                return false;
            Record other = (Record) obj;
            if (other.getCf() == null ^ this.getCf() == null)
                return false;
            if (other.getCf() != null && other.getCf().equals(this.getCf()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getCf() == null) ? 0 : getCf().hashCode());
            return hashCode;
        }

        @Override
        public Record clone() {
            try {
                return (Record) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * default constructor
     */
    public CloudFrontEvent() {}

    /**
     * @return list of records in cloud front event
     */
    public List<Record> getRecords() {
        return this.records;
    }

    /**
     * @param records list of records in cloud front event
     */
    public void setRecords(List<Record> records) {
        this.records = records;
    }

    /**
     * @param records list of records in cloud front event
     * @return CloudFrontEvent object
     */
    public CloudFrontEvent withRecords(List<Record> records) {
        setRecords(records);
        return this;
    }

    /**
     * Returns a string representation of this object; useful for testing and debugging.
     *
     * @return A string representation of this object.
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getRecords() != null)
            sb.append("records: ").append(getRecords().toString());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof CloudFrontEvent == false)
            return false;
        CloudFrontEvent other = (CloudFrontEvent) obj;
        if (other.getRecords() == null ^ this.getRecords() == null)
            return false;
        if (other.getRecords() != null && other.getRecords().equals(this.getRecords()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getRecords() == null) ? 0 : getRecords().hashCode());
        return hashCode;
    }

    @Override
    public CloudFrontEvent clone() {
        try {
            return (CloudFrontEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
}
