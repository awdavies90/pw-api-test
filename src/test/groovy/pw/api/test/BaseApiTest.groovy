package pw.api.test

import spock.lang.Specification
import spock.lang.*

import javax.xml.ws.http.HTTPException

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import pw.api.test.utils.Templater

class BaseApiTest extends Specification {
	
	def printRequest = true
	def printResponse = true
	
	static baseUrl = "http://localhost:8080/"
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
			def responseContent = (responseCode == 200) ? content?.text : errorStream?.text
			if (responseContent) {
				responseJson = new JsonSlurper().parseText(responseContent)
				if (printResponse) {
					println JsonOutput.prettyPrint(responseContent)
				}
			} else {
				throw new Exception("The request to $baseUrl$url failed with a response code of $responseCode")
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
	
	def delete(String url) {
		def responseJson
		HttpURLConnection connection = new URL("$baseUrl$url").openConnection()
		connection.with {
			requestMethod = 'DELETE'
			
			//Set request headers
			requestHeaders.each { header ->
				setRequestProperty(header.key, header.value)
			}
			
			//Execute request and get response
			def responseContent = (responseCode == 200) ? content?.text : errorStream?.text
			responseJson = new JsonSlurper().parseText(responseContent)
			if (printResponse) {
				println JsonOutput.prettyPrint(responseContent)
			}
		}
		responseJson
	}
}
