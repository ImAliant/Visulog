package up.visulog.analyzer;


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
	private Result result;
	private static String date;
    
    public CountCommitsPerDate(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog){
        var result = new Result(); 
        for (var commit : gitLog) { 
        	//Pour l'instant il y'a pas de date à comparer mais ça viendra par la suite
        	if(dateCompare(commit.date, date)) {
            	var nb = result.commitsPerDate.getOrDefault(commit.date, 0); //
            	result.commitsPerDate.put(commit.date, nb + 1); 	
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
        public ArrayList<String> getPluginInfoByArray() {
        	ArrayList<String> typename= new ArrayList<String>();
        	for (var item : commitsPerDate.entrySet()) typename.add(item.getKey()+"="+String.valueOf(item.getValue()));   	
        	return typename;
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
    	   SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");;
    	    try {
				return (format.parse(date).compareTo(format.parse(dateCompare)) <= 0);
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
    }
    
}