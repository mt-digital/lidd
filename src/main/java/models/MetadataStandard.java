package models;

public class MetadataStandard {

    String standardName = "None";
    
    String specificationUrl = "https://google.com";

    public MetadataStandard() {};

    public MetadataStandard(String snm, String spUrl)
    {
        this.standardName = snm;
        this.specificationUrl = spUrl;
    }

    public boolean equals(MetadataStandard mds)
    {
        return (mds.standardName == this.standardName &&
                mds.specificationUrl == this.specificationUrl);
    }
}
