package up.visulog.gitrawdata;

public class CommitBuilder {
    private final String id;
    private String author;
    private String date;
    private String description;
    private String mergedFrom;
    // ajout test bjm:
    private int nbLineAdd;

    //cette fonction initialise commitbuilder avec id ;
    public CommitBuilder(String id) {
        this.id = id;
    }

    //CETTE FONCTION sert a modifier le nom de l'auteur et return tout l'element de commit
    //Builder avec l'auteur modifié
    public CommitBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    //CETTE FONCTION sert a modifier la date et return tout l'element de commit
    //Builder avec l'auteur modifié
    public CommitBuilder setDate(String date) {
        this.date = date;
        return this;
    }

    //CETTE FONCTION sert a modifier la description et return tout l'element de commit
    //Builder avec l'auteur modifié
    public CommitBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    //CETTE FONCTION sert a modifier mergedFrom et return tout l'element de commit
    //Builder avec mergedFrom modifié
    public CommitBuilder setMergedFrom(String mergedFrom) {
        this.mergedFrom = mergedFrom;
        return this;
    }
    
    //test bjm
    //CETTE FONCTION sert a modifier nbLineAdd et return tout l'element de commit
    //Builder avec nbLineAdd modifié
    public CommitBuilder setNbLineAdd(int lineAdd) {
        this.nbLineAdd = lineAdd;
        return this;
    }
    //Cette fonction crée une commit avec les valeur de commitbuilder
    public Commit createCommit() {
        return new Commit(id, author, date, description, mergedFrom,nbLineAdd);
    }
}