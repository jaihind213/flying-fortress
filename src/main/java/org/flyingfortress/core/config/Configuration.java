package org.flyingfortress.core.config;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

/**
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 4:40 PM
 */
public class Configuration {
    @JsonProperty
    protected String name;
    @JsonProperty
    protected String description;
    @JsonProperty
    protected Map<String,Object> properties;

    public Configuration() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
