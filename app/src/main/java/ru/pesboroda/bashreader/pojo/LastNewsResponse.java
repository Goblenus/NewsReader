package ru.pesboroda.bashreader.pojo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "news",
        "page"
})
public class LastNewsResponse {

    @JsonProperty("status")
    private String status;
    @JsonProperty("news")
    private List<News> news = null;
    @JsonProperty("page")
    private Integer page;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public LastNewsResponse withStatus(String status) {
        this.status = status;
        return this;
    }

    @JsonProperty("news")
    public List<News> getNews() {
        return news;
    }

    @JsonProperty("news")
    public void setNews(List<News> news) {
        this.news = news;
    }

    public LastNewsResponse withNews(List<News> news) {
        this.news = news;
        return this;
    }

    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    public LastNewsResponse withPage(Integer page) {
        this.page = page;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public LastNewsResponse withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
