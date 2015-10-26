package models;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface NormalizedMetadataRepository 
    extends MongoRepository<NormalizedMetadata, String> 
{
    public List<NormalizedMetadata> findByTitleLike(String title);

    public List<NormalizedMetadata> findByTitle(String title);
}
