package up.visulog.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.analyzer.CountRemoveLinePerAutor.Result;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

public class CountModifyLinePerAuthors implements AnalyzerPlugin {
		
		private final Configuration configuration; 
	    private Result result;
	    
	    public CountModifyLinePerAuthors(Configuration generalConfiguration) {// constructeur de la classe (en public)
	    
	        this.configuration = generalConfiguration;// initialisation de l'attribut configuration;
	    }

	    static Result processLog(List<Commit> gitLog) { // methode static processLog qui prend comme argument une liste de type commit que l'on nome gitLog
	        var result = new Result(); 
	        for (var commit : gitLog) {  
	            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0); //
	            result.commitsPerAuthor.put(commit.author, nb + commit.nbAllModifiction); 
	        }
	        return result; //retourn le nombre de commit emis par chaqu'un(je crois)
	    }

	    @Override 
	    public void run() {
	        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));// redefinition de l'attribut de result
	    }

	    @Override
	    public Result getResult() {
	        if (result == null) run(); 
	        return result;
	    }

	    static class Result implements AnalyzerPlugin.Result { 
	        private final Map<String, Integer> commitsPerAuthor = new HashMap<>(); 
	      
	        Map<String, Integer> getCommitsPerAuthor() {
	            return commitsPerAuthor;
	        }

	        @Override //redefinition de la methode getResultAsString (pour chaque personne qui dépose son commit)
	        public String getResultAsString() {
	            return commitsPerAuthor.toString();//retourn maintenant l'auteur du commit avec la methode toString
	        }

	        @Override //redefinition de la methode getResultAsHtmlDiv() 
	        public String getResultAsHtmlDiv() {
	            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
	            //contient un message htlm
	            for (var item : commitsPerAuthor.entrySet()) {
	                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
	            }
	            html.append("</ul></div>");
	            return html.toString();
	        }
	        
	        @Override //Pas encore achev� #WilliamBenakli
	        public Map<String, Integer> getPluginInfoByArray() {
	        	 return commitsPerAuthor;
	        }

	    }
	    
}
