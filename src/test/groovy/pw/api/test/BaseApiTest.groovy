package pw.api.test

import spock.lang.Specification
import spock.lang.*

import javax.xml.ws.http.HTTPException

import api.test.helpers.BidHelper
import api.test.helpers.EventHelper
import api.test.helpers.UserHelper
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.time.TimeCategory
import pw.api.test.utils.Templater

abstract class BaseApiTest extends Specification {
	
	@Shared static validChars = ['�', '$', '!', '&', '()', '@', '?', ',', '.', '+', '=', '/', ':', '#']
	@Shared static invalidChars = ['`', '^', '*', '_', '{', '}', '[', ']', '~', ';', '<', '>', '|']
	def tenSecondsAgo = use(TimeCategory) { new Date() - 10.seconds }
	static printRequest = true
	static printResponse = true
	
	static baseUrl = "http://localhost:8080/api/"
	static requestHeaders = ['Accept': 'application/json', 'Content-Type':'application/json']
	
	//String authToken
	@Shared static authToken
	@Shared static adminUserToken
	@Shared static individualUserToken
	@Shared static individualUserToken2
	@Shared static bandUserToken
	@Shared static bandUserToken2
	
	//Helpers
	@Shared static BidHelper bidHelper
	@Shared static EventHelper eventHelper
	@Shared static UserHelper userHelper
	
	def setupSpec() {
		bidHelper = new BidHelper(this)
		eventHelper = new EventHelper(this)
		userHelper = new UserHelper(this)
		
		adminUserToken = userHelper.getUserToken('alunAdmin', 'pass1234')
		individualUserToken = userHelper.getUserToken('davo123', 'pass1234')
		individualUserToken2 = userHelper.getUserToken('therealjesus', 'pass1234')
		bandUserToken = userHelper.getUserToken('dazla3', 'pass1234')
		bandUserToken2 = userHelper.getUserToken('slimjim', 'pass1234')
	}
	
	def get(String url) {
		doRequest('GET', url, null)
	}
	
	def post(String url, String templateName, Map params) {
		doRequestWithTemplating('POST', url, templateName, params)
	}
	
	def post(String url, String templateName, Map params, token) {
		authToken = token
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
			setRequestProperty('AuthToken', authToken)
			requestHeaders.each { header ->
				setRequestProperty(header.key, header.value)
			}
			
			//Set request body
			def requestJson
			if (requestContent) {
				try {
					requestJson = JsonOutput.prettyPrint(requestContent)
				} catch (groovy.json.JsonException e) {
					throw new Error("The following json request content could not be parsed:\n $requestContent")
				}
				outputStream.withWriter { request ->
					request << requestJson
				}
			}
			
			//Print out request details
			if (printRequest) {
				println "$method - $url:\nToken:${getRequestProperty('AuthToken')}\n$requestJson"
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
