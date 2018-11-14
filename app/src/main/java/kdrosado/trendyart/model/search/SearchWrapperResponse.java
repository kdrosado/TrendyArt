package kdrosado.trendyart.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import kdrosado.trendyart.model.Links;

public class SearchWrapperResponse {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    @SerializedName("offset")
    @Expose
    private Integer offset;

    @SerializedName("q")
    @Expose
    private String q;

    @SerializedName("_links")
    @Expose
    private Links links;

    @SerializedName("_embedded")
    @Expose
    private EmbeddedResults embedded;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public EmbeddedResults getEmbedded() {
        return embedded;
    }

    public void setEmbedded(EmbeddedResults embedded) {
        this.embedded = embedded;
    }
}
