package pw.api.test

import spock.lang.Specification
import spock.lang.*
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import pw.api.test.utils.Templater

class BaseApiTest extends Specification {
	
	def printRequest = true
	def printResponse = true
	
	def localHostName = {
		def address = new InetAddress()
		def localhost = address.localHost
		localhost.hostName
	}
	def baseUrl = "http://$localHostName:8080/"
	def requestHeaders = [Accept: 'application/json', 'Content-Type':'application/json']
	
	def post(String url, String templateName, Map params) {
		//Do templating
		def requestContent = Templater.use(templateName, params)
		
		def responseJson
		HttpURLConnection connection = new URL("$baseUrl$url").openConnection()
		connection.with {
			doOutput = true
			requestMethod = 'POST'
			
			//Set request headers
			requestHeaders.each { header ->
				setRequestProperty(header.key, header.value)
			}
			
			//Set request body
			def requestJson = JsonOutput.prettyPrint(requestContent)
			if (printRequest) {
				println requestJson
			}
			outputStream.withWriter { request ->
				request << requestJson
			}
			
			//Execute request and get response
			def responseContent = (responseCode == 200) ? content.text : errorStream.text
			responseJson = new JsonSlurper().parseText(responseContent)
			if (printResponse) {
				println JsonOutput.prettyPrint(responseContent)
			}
		}
		responseJson
	}
	
	def get(String url) {
		def responseText = "$baseUrl$url".toURL().getText(requestProperties: requestHeaders)
		def json = new JsonSlurper().parseText(responseText)
		if (printRequest) {
			println JsonOutput.prettyPrint(responseText)
		}
		json
	}
}
