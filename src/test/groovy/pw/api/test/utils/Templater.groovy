package pw.api.test.utils

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Template
import com.github.jknack.handlebars.io.ClassPathTemplateLoader

class Templater {

	static String use(String templateName, Map params) {
		def returnValue
		if (templateName && params) {
			def loader = new ClassPathTemplateLoader();
			loader.setPrefix("/templates");
			loader.setSuffix(".txt");
			def handlebars = new Handlebars(loader);
			Template template = handlebars.compile(templateName);
			def output = template.apply(params)
			returnValue = output
		}
		returnValue
	}
}
