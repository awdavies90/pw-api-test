package pw.api.test

import spock.lang.Specification
import spock.lang.*

import javax.xml.ws.http.HTTPException

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.time.TimeCategory
import pw.api.test.bid.BidHelper
import pw.api.test.utils.Templater

class BaseApiTest extends Specification {
	
	@Shared static validChars = ['£', '$', '!', '&', '()', '@', '?', ',', '.', '+', '=', '/', ':', '#']
	@Shared static invalidChars = ['`', '^', '*', '_', '{', '}', '[', ']', '~', ';', '<', '>', '|']
	def tenSecondsAgo = use(TimeCategory) { new Date() - 10.seconds }
	def printRequest = true
	def printResponse = true
	
	//Helpers
	BidHelper bidHelper = new BidHelper(this)
	
	static baseUrl = "http://localhost:8080/api/"
	def requestHeaders = [Accept: 'application/json', 'Content-Type':'application/json']
	
	def get(String url) {
		doRequest('GET', url, null)
//		def responseText = "$baseUrl$url".toURL().getText(requestProperties: requestHeaders)
//		def json = new JsonSlurper().parseText(responseText)
//		if (printRequest) {
//			println JsonOutput.prettyPrint(responseText)
//		}
//		json
	}
	
	def post(String url, String templateName, Map params) {
		doRequestWithTemplating('POST', url, templateName, params)
	}
	
	def post(String url, String requestContent) {
		doRequest('POST', url, requestContent)
	}
	
	def put(String url) {
		doRequest('PUT', url, null)
	}
	
	def put(String url, String requestContent) {
		doRequest('PUT', url, requestContent)
	}
	
	def put(String url, String templateName, Map params) {
		doRequestWithTemplating('PUT', url, templateName, params)
	}
	
	def delete(String url) {
		doRequest('DELETE', url, null)
	}
	
	def doRequestWithTemplating(String method, String url, String templateName, Map params) {
		def requestContent = Templater.use(templateName, params)
		doRequest(method, url, requestContent)
	}
	
	def doRequest(String method, String url, String requestContent) {
		def responseJson
		HttpURLConnection connection = new URL("$baseUrl$url").openConnection()
		connection.with {
			doOutput = true
			requestMethod = method
			
			//Set request headers
			requestHeaders.each { header ->
				setRequestProperty(header.key, header.value)
			}
			
			//Set request body
			if (requestContent) {
				def requestJson = JsonOutput.prettyPrint(requestContent)
				if (printRequest) {
					println requestJson
				}
				outputStream.withWriter { request ->
					request << requestJson
				}
			}
			
			//Execute request and get response
			def responseContent = (responseCode == 200) ? content?.text : errorStream?.text
			if (responseContent) {
				try {
					responseJson = new JsonSlurper().parseText(responseContent)
				} catch (JsonException) {
					responseJson = responseContent
				}
				if (printResponse) {
					println JsonOutput.prettyPrint(responseContent)
				}
			} else {
				throw new Exception("The $method request to $baseUrl$url failed with a response code of $responseCode")
			}
		}
		responseJson
	}
}
