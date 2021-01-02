
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

public class CountCommitsPerDate  implements AnalyzerPlugin {

	private final Configuration configuration; 
	private String[] args;
	private Result result;
    
    public CountCommitsPerDate(Configuration generalConfiguration, String[] args) {
        this.configuration = generalConfiguration;
        this.args = args;
    }
    
    Result processLog(List<Commit> gitLog){
    	
        var result = new Result();
        for (var commit : gitLog) { 
           	if(args.length == 0) {
            	var nb = result.commitsPerDate.getOrDefault(commit.author, 0); //
            	result.commitsPerDate.put(commit.author, nb + 1); 	
        	}else if(args.length == 2) {
            	if(dateCompare(args[0], args[1])) {
                	var nb = result.commitsPerDate.getOrDefault(commit.author, 0); //
                	result.commitsPerDate.put(commit.author, nb + 1); 	
            	}
        	}
        }
        return result; 
    }

    @Override 
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }
    
    @Override
    public Result getResult() {
        if(result == null) run(); 
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerDate = new HashMap<>(); 
      
        Map<String, Integer> getCommitsPerWeek() {
            return commitsPerDate;
        }

        @Override
        public String getResultAsString() {
            return commitsPerDate.toString();
        }
        @Override //Pas encore achevé #WilliamBenakli
        public Map<String, Integer> getPluginInfoByArray() { 	
        	return commitsPerDate;
        }

        @Override //redefinition de la methode getResultAsHtmlDiv() 
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            //contient un message htlm
            for (var item : commitsPerDate.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
    
    //permet de comparer une date
    public static boolean dateCompare(String date, String dateCompare) {
    	   SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");;
    	    try {
				return (format.parse(date).compareTo(format.parse(dateCompare)) <= 0);
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
    }
    
    public static String traitementDatePerCommits(String date) {
    	return date.replace("-", "/");
    }
}