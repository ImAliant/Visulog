package up.visulog.analyzer.html;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;

public class PageCreation {	
	
	/**Cette fonction utilise la librairie HTMLFLOW https://htmlflow.org/
	 * Elle permet de generer un site et d'y integrer un graphique
	 * elle utilise la librairie boostrap pour  un meilleur affichage #WilliamBenakli
	 * @param title
	 * @param importscript
	 * @param script
	 * @param page
	 * @return String (correspondant au site en html / js)
	 */
	public static String createPage(String title, String importscript, String script, String page) {
		String boostrap = "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\" integrity=\"sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2\" crossorigin=\"anonymous\">\r\n";
		HtmlView view = StaticHtml.view(v -> v
	            .html()
	                .head().title().text(title + " | WorkTeamStatus (WTS)").__()
	                	.text(importscript)
	                	.text(boostrap).__()   
	                .body()
	                	.nav().attrClass("navbar fixed-top navbar-dark bg-primary")
	                		.a().attrClass("navbar-brand").attrStyle("color: white").text(" WorkTeamStatus (WTS) ").__()
	                		.div().attrClass("navbar-collapse").attrStyle("display: flex; flex-direction: row;")
	                			.ul().attrClass("navbar-nav mr-auto")
	                				.text("")
                				.__()
	                	.__()
	                	.__()
		                .div().attrStyle("display: flex; flex-direction: column")
		                	.p().attrStyle("margin: auto; margin-top: 80px;").text("WorkTeamStatus (WTS) | Premier graphique qui compte le nombre de commit par auteur ").__()
		                	.div().attrStyle("margin: auto; height: 50%; width: 50%;").text(page).__()
		                	.p().attrStyle("margin: auto;").text("Le graphique actuelle est en barre ").__()
		                .__()
	                    .footer().attrClass("py-4 fixed-bottom bg-dark text-white-50")
	                    	.div().attrClass("container text-center")
	                    	.small().text("Copyright WorkTeamStatus (WTS) ").__()
	                    	.__()
                    	.__()
                    .script().text(script).__()
	            .__()); 
		return view.render();       		
	}
		
}