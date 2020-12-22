package up.visulog.analyzer;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountLineAddPerAuthor implements AnalyzerPlugin {
	private final Configuration configuration; 
    private Result result;
    
    public CountLineAddPerAuthor(Configuration generalConfiguration) {
        
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) { 
        var result = new Result(); 
        for (var commit : gitLog) {  
            var nb = result.LineAdd.getOrDefault(commit.author, 0); 
            result.LineAdd.put(commit.author, nb + commit.nbLineAdd); 
        }
       
        return result; 
    }

    @Override 
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }

    @Override
    public Result getResult() {
        if (result == null) run(); 
        return result;
    }

    static class Result implements AnalyzerPlugin.Result { 
        private final Map<String, Integer> LineAdd = new HashMap<>(); 
      
        Map<String, Integer> getLineAdd() {
            return LineAdd;
        }

        @Override 
        public String getResultAsString() {
            return LineAdd.toString();
        }
        
        @Override 
        public Map<String, Integer> getPluginInfoByArray() { 	
        	return LineAdd;
        }

        @Override //redefinition de la methode getResultAsHtmlDiv() 
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div> Line add : <ul>");
            //contient un message htlm
            for (var item : LineAdd.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    
}
}
