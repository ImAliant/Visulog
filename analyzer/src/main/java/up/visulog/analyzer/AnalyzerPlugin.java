package up.visulog.analyzer; // this file belongs to the package Analyzer

import java.util.Map;

// This class is an interface that groups related methods with empty bodies that are going to be implemented in other classes


public interface AnalyzerPlugin {
    interface Result {
        String getResultAsString();
        String getResultAsHtmlDiv();
        Map<String, Integer> getPluginInfoByArray();
    }
    /* fonctions in the interface Result that are going to be implemented and used in other classes
     * (in CountCommitPerAuthorPluging.java for example) on Result objects or object that implements the class Result
    */

    /**
     * run this analyzer plugin
     */
    void run();  //method that is going to be implemented in classes that implement the class AnalyzerPlugin

    /**
     *
     * @return the result of this analysis. Runs the analysis first if not already done.
     */
    Result getResult();  //method that is going to be implemented in classes that implement the class AnalyzerPlugin on Result objects

}
