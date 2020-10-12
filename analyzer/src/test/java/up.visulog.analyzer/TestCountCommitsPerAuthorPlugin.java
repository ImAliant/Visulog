package up.visulog.analyzer;    //The class belongs to the up.visulog.analyzer package. La classe appartient au package up.visulog.analyzer.
//import des classes qui vont etre utilisées:
// import the classes that will be used:
import org.junit.Test; 
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.CommitBuilder;

import java.util.ArrayList; //La classe ArrayList implémente un tableau d'objets qui peut grandir ou rétrécir à la demande.

import static org.junit.Assert.assertEquals;

public class TestCountCommitsPerAuthorPlugin { //déclaration de classe public
    /* Let's check whether the number of authors is preserved and that the sum of the commits of each author is equal to the total number of commits */
    /* Vérifions si le nombre d'auteurs est conservé et que la somme des commits de chaque auteur est égale au nombre total de commits */
    @Test
    public void checkCommitSum() {
        var log = new ArrayList<Commit>(); // creation d'une variable log. Creation of a log variable.

	String[] authors = {"foo", "bar", "baz"}; // creation d'un tableau qui a pour valeur les auteurs.

	var entries = 20; //intialisation à 20 de la variable entries.    Initialization to 20 of the variable entries

	for (int i = 0; i < entries; i++) {//boucle qui iténiaire j'usqu'à entries donc 20. loop which iténiaire i up to entries so 20.

	    log.add(new CommitBuilder("").setAuthor(authors[i % 3]).createCommit()); // à chaque itération enregistrement  at each recording iteration
	    //ajout à log commits une creation de commit
	    var res = CountCommitsPerAuthorPlugin.processLog(log);// Creation d'une variable res     Creation of a res variable.
	    
	    assertEquals(authors.length, res.getCommitsPerAuthor().size());// vérifie que le nombre d'auteurs est égale au commits émis

	    var sum = res.getCommitsPerAuthor().values() // Creation d'une variable sum qui à les valeurs de chaque auteur    Creation of a sum variable.

		.stream().reduce(0, Integer::sum);

	    assertEquals(entries, sum.longValue());// vérification  de l'égalitée 
    }
}
