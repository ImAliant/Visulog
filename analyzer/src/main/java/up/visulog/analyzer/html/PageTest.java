package up.visulog.analyzer.html;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;

public class PageTest {
	
	public static String first_page() {
		HtmlView view = StaticHtml.view(v -> v
	            .html()
	                .head().text("Importation").__()
	                .body()
	                .text("On entre du text ici")
                    .script().text("On entre du script ici")
	            .__()); 
		return view.render();       		
	}
}

