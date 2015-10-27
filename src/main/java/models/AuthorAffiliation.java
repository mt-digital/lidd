package models;

// TODO use affiliation in the MongoDB
public class AuthorAffiliation
{
    public final String author;
    public final String affiliation;

    public AuthorAffiliation(String author, String affiliation)
    {
        this.author = author;
        this.affiliation = affiliation;
    }
}
