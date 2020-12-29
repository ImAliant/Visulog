package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Commit {
	
	    public final String id;
	    public final String date;
	    public final String author;
	    public final String description;
	    public final String mergedFrom;
	    public final int nbLineAdd;
	    public final int nbLineRemove;
	    public final int nbAllModifiction;

    
    /*Commentaire: William Benakli
     * 
     * Creation d'un Object commit comportant plusieurs information en final
     * Etant en final ces infos vont être définis qu'une seule fois, ici lors du constructeur
     * 
     * @id, pour permettre d'identifier chaque Commit par son id
     * @date, pour savoir quand la personne à déposer son commit
     * @auteur, pour determiner qui est le createur du commit
     * @description, pour mettre une description au commit
     * @mergedFrom, pour savoir si le commit est merger depuis une autre branche
     * 
     *      
     */
    
    //Constructeur assez basique vu en JAVA
    public Commit(String id, String author, String date, String description, String mergedFrom,int nbLineAdd, int nbLineRemove, int nbAllModifiction) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.description = description;
        this.mergedFrom = mergedFrom;
        this.nbLineAdd = nbLineAdd;
        this.nbLineRemove = nbLineRemove;
        this.nbAllModifiction = nbAllModifiction;
    }
    
    
    /*Commentaire: William Benakli
     * 
     * Fonction : parseLogFromCommand | Recupere et sépare depuis une commande
     * Parametre: Path gitPath | Path est un chemin d'entrée donc gitPath est un chemin expl: "/document/git..."
     * Retourne: List<Commit> | Elle retourne une list de commit présent dans le chemin
     * 
     * 
     */
    // TODO: factor this out (similar code will have to be used for all git commands)
    public static List<Commit> parseLogFromCommand(Path gitPath) {
    	
    	//On recupere en fonction du chemin d'acces 
        ProcessBuilder builder = new ProcessBuilder("git", "log", "--numstat").directory(gitPath.toFile());
        //Process
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException("Error running \"git log\".", e);
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return parseLog(reader);
    }

    public static List<Commit> parseLog(BufferedReader reader) {
        var result = new ArrayList<Commit>();
        Optional<Commit> commit = parseCommit(reader);
        while (commit.isPresent()) {
            result.add(commit.get());
            commit = parseCommit(reader);
        }
        return result;
    }

    public static Optional<Commit> parseCommit(BufferedReader input) {
        try {

            var line = input.readLine();
            if (line == null) return Optional.empty(); // if no line can be read, we are done reading the buffer
            var idChunks = line.split(" ");

            int[] modification = new int[3];
        	while(!idChunks[0].equals("commit")) {
	        	if(idChunks[0].split("\t")[0].length() != 0 && !idChunks[0].split("\t")[0].equals("-")){
	        		modification[0] = modification[0]+ Integer.valueOf(idChunks[0].split("\t")[0]);      
	        		modification[1] = modification[1] + Integer.valueOf(idChunks[0].split("\t")[1]);            	
	        	}
	            line = input.readLine();
	            if (line == null) return Optional.empty(); // if no line can be read, we are done reading the buffer	
	            idChunks = line.split(" ");
        	}
        	
        	if (!idChunks[0].equals("commit")) parseError();
        	var builder = new CommitBuilder(idChunks[1]);
            line = input.readLine();
            
            while (!line.isEmpty()) {
                var colonPos = line.indexOf(":");
                var fieldName = line.substring(0, colonPos);
                var fieldContent = line.substring(colonPos + 1).trim();
                switch (fieldName) {
                    case "Author":
                        builder.setAuthor(fieldContent);
                        break;
                    case "Merge":
                        builder.setMergedFrom(fieldContent);
                        break;
                    case "Date":
                        builder.setDate(fieldContent);
                        break;
                    default: // TODO: warn the user that some field was ignored
                }
                System.out.println("add " + modification[0] +" remove " + modification[1]);
	            builder.setNbLineAdd(modification[0]);
	            builder.setNbLineRemove(modification[1]);
	            builder.setNbLineModify(modification[0] + modification[1]);
                line = input.readLine(); //prepare next iteration
                if (line == null) parseError(); // end of stream is not supposed to happen now (commit data incomplete)
            }

            // now read the commit message per se
            var description = input
                    .lines() // get a stream of lines to work with
                    .takeWhile(currentLine -> !currentLine.isEmpty()) // take all lines until the first empty one (commits are separated by empty lines). Remark: commit messages are indented with spaces, so any blank line in the message contains at least a couple of spaces.
                    .map(String::trim) // remove indentation
                    .reduce("", (accumulator, currentLine) -> accumulator + currentLine); // concatenate everything
            builder.setDescription(description);
            return Optional.of(builder.createCommit());
        } catch (IOException e) {
            parseError();
        }
        return Optional.empty(); // this is supposed to be unreachable, as parseError should never return
    }


    
    // Helper function for generating parsing exceptions. This function *always* quits on an exception. It *never* returns.
    private static void parseError() {
        throw new RuntimeException("Wrong commit format.");
    }
    
    public boolean isMergeCommit() {
        return mergedFrom != null;
    }
    
    
    
    
    /*Commentaire: William Benakli
     * 
     * 
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     *
     *ToString() permet de recuper les informations liées au commit: id, date, author....
     *
     */
    @Override //On met ovveride puisque la fonction toString d'un object existe déjà, alors on la surcharge vu en Amphi
    public String toString() {
        return "Commit{" +
                "id='" + id + '\'' +
                (mergedFrom != null ? ("mergedFrom...='" + mergedFrom + '\'') : "") + //TODO: find out if this is the only optional field
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
